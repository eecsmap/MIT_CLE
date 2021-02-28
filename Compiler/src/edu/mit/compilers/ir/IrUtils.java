package edu.mit.compilers.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.grammar.*;

public class IrUtils {
    private static final ST importST = new ST();
    private static final ST globalST = new ST();
    private static final Map<String, ArrayList<String>> methodMap = new HashMap<>();

    // parse an AST to IRTree with the help of Symbol Tree
    public static void irParse(AST t) {
        // treat import Symbol Table as special one
        t = importDecl(t, importST);
        t = fieldDecl(t, globalST);
        t = methodDecl(t, globalST);
    }

    // return the next AST to parse
    private static AST importDecl(AST t, ST importST) {
        for (; AstUtils.isImport(t); t = t.getNextSibling()) {
            // parse single import statement
            MethodDesc desc = new MethodDesc(Defs.DESC_METHOD, t.getText());
            importST.push(desc);
        }
        return t;
    }

    private static AST fieldDecl(AST t, ST st) {
        for (; t != null && AstUtils.isType(t); t = t.getNextSibling()) {
            // parse single import statement
            String type = null;
            switch (t.getType()) {
                case DecafScannerTokenTypes.TK_bool:
                    type = Defs.DESC_TYPE_BOOL;
                    break;
                case DecafScannerTokenTypes.TK_int:
                    type = Defs.DESC_TYPE_INT;
                    break;
            }
            AST c = t.getFirstChild();
            for (; c != null; c = c.getNextSibling()) {
                AST cc = c.getFirstChild();
                if (cc != null) {
                    // cc is null -> is array
                    st.push(new ArrayDesc(Defs.ARRAY_PREFIX + type, c.getText(), cc.getText()));
                    continue;
                }
                // cc is null -> it's single Variable
                st.push(new VarDesc(type, c.getText()));
            }
        }
        return t;
    }

    private static AST methodDecl(AST t, ST globalST) {
        for (; t != null && AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST c = t.getFirstChild();
            globalST.push(new MethodDesc(c.getText(), t.getText()));
            ST paramST = new ST(globalST);
            ST localST = new ST(paramST, c.getText());
            c = c.getNextSibling();
            // parse parameters
            ArrayList<String> params = new ArrayList<>();
            for (; c != null && AstUtils.isID(c); c = c.getNextSibling()) {
                paramST.push(new VarDesc(c.getFirstChild().getText(), c.getText()));
                params.add(c.getFirstChild().getText());
            }
            parseBlock(c, localST);
        }
        return t;
    }

    // only =
    private static void parseSimpleAssign(AST t, ST st) {
        String op = "=";
        AST c = t.getFirstChild();
        String lID = c.getText();
        String lType = st.getType(lID);
        if (lType == null) {
            Er.errNotDefined(c, c.getText());
            return;
        }
        c = c.getNextSibling();
        String rType = parseExpr(c, st);
        if (!lType.equals(rType)) {
            Er.errType(c, lType, rType);
        }
    }

    // +=, -=, =
    private static void parseMoreAssign(AST t, ST st) {
        String op = t.getText();
        AST c = t.getFirstChild();
        String lID = c.getText();
        String lType = st.getType(lID);
        if (lType == null) {
            Er.errNotDefined(c, c.getText());
            return;
        }
        c = c.getNextSibling();
        String rType = parseExpr(c, st);
        if (!lType.equals(rType)) {
            Er.errType(c, lType, rType);
        }
    }

    private static void parseIncre(AST t, ST st) {
        String op = t.getText();
        AST c = t.getFirstChild();
        String lID = c.getText();
        String lType = st.getType(lID);
        if (lType == null) {
            Er.errNotDefined(c, lID);
            return;
        }
        if (lType.startsWith(Defs.ARRAY_PREFIX)) {
            String type = parseArrayElement(c, st);
            if (type != null && !type.equals(Defs.DESC_TYPE_INT)) {
                Er.errType(c, Defs.DESC_TYPE_INT, type);
            }
            return;
        }
        if (!lType.equals(Defs.DESC_TYPE_INT)) {
            Er.errType(c, Defs.DESC_TYPE_INT, lType);
        }
        return;
    }

    // return method type
    private static String parseMethodCall(AST t, ST st) {
        Descriptor method = st.getMethod(t.getText());
        if (method == null) {
            method = importST.getMethod(t.getText());
            if (method == null) {
                Er.errNotDefined(t, t.getText());
                return null;
            }
            return Defs.DESC_TYPE_WILDCARD;
        }
        
        AST c = t.getFirstChild();
        ArrayList<String> params = methodMap.get(method.getText());
        if (params == null) {
            Er.errNotDefined(c, method.getText());
            return Defs.DESC_TYPE_WILDCARD;
        }
        int i = 0;
        for (; c != null; c = c.getNextSibling(), i++) {
            String cType = parseExpr(c, st);
            if (params.get(i) != cType) {
                Er.errType(c, params.get(i), cType);
            }
        }

        return method.getType().substring(Defs.DESC_METHOD.length());
    }

    private static String parseArrayElement(AST t, ST st) {
        Descriptor desc = st.getArray(t.getText());
        if (desc == null) {
            Er.errNotDefined(t, t.getText());
            return null;
        }
        String type = desc.getType();
        AST c = t.getFirstChild();
        String subType = parseExpr(c, st);
        if (subType == null) {
            return type.substring(Defs.ARRAY_PREFIX.length());
        }
        if (!subType.equals(Defs.DESC_TYPE_INT) && !subType.equals(Defs.DESC_TYPE_WILDCARD)) {
            Er.errArrayIndexNotInt(t, Defs.DESC_TYPE_INT, subType);
            return type.substring(Defs.ARRAY_PREFIX.length());
        }
        if (c.getType() == DecafScannerTokenTypes.INTLITERAL && desc.findVar(c.getText()) == null) {
            Er.errArrayOutbound(t, desc.getText(), c.getText());
        }
        return type.substring(Defs.ARRAY_PREFIX.length());
    }

    private static void parseIf(AST t, ST st) {
        AST c = t.getFirstChild();
        String type = parseExpr(c, st);
        if (type != null && !type.equals(Defs.DESC_TYPE_BOOL)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        c = parseBlock(c.getNextSibling(), st);
        if (c == null) {
            return;
        }
        // else
        parseBlock(c.getFirstChild(), st);
    }

    // doesn't suppeort declaration in for loop
    private static void parseFor(AST t, ST st) {
        st.pushContext(t.getType());
        AST c = t.getFirstChild();
        parseSimpleAssign(c, st);
        c = c.getNextSibling();
        parseMoreAssign(c, st);
        c = c.getNextSibling();
        parseBlock(c, st);
        st.popContext();
    }

    private static void parseWhile(AST t, ST st) {
        st.pushContext(t.getType());
        AST c = t.getFirstChild();
        String type = parseExpr(c, st);
        if (type != null && !type.equals(Defs.DESC_TYPE_BOOL)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        c = c.getNextSibling();
        parseBlock(c, st);
        st.popContext();
    }

    // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    private static String parseExpr(AST t, ST st) {
        if (t == null) {
            return null;
        }
        if (AstUtils.isBinaryOp(t)) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && !lType.equals(rType)) {
                Er.errType(l, lType, rType);
            }
            return lType;
        }
        if (AstUtils.isBinaryBoolOp(t)) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && !lType.equals(Defs.DESC_TYPE_BOOL)) {
                Er.errType(l, Defs.DESC_TYPE_BOOL, lType);
            }
            if (rType != null && !rType.equals(Defs.DESC_TYPE_BOOL)) {
                Er.errType(r, Defs.DESC_TYPE_BOOL, rType);
            }
            return Defs.DESC_TYPE_BOOL;
        }
        switch(t.getType()) {
            case DecafScannerTokenTypes.ID:
                String type = st.getType(t.getText());
                if (type == null) {
                    Er.errNotDefined(t, t.getText());
                    return null;
                }
                // array
                if (type.startsWith(Defs.ARRAY_PREFIX)) {
                    return parseArrayElement(t, st);
                }
                // method
                if (!type.equals(Defs.DESC_METHOD)) {
                    return parseMethodCall(t, st);
                }
                // var
                return type;
            case DecafScannerTokenTypes.INTLITERAL:
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.TK_true:
            case DecafScannerTokenTypes.TK_false:
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.MINUS:
                String subType = parseExpr(t.getFirstChild(), st);
                if (subType != null && !subType.equals(Defs.DESC_TYPE_INT)) {
                    Er.errType(t, Defs.DESC_TYPE_INT, subType);
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.EXCLAM:
                String subType0 = parseExpr(t.getFirstChild(), st);
                if (subType0 != null && !subType0.equals(Defs.DESC_TYPE_BOOL)) {
                    Er.errType(t, Defs.DESC_TYPE_BOOL, subType0); 
                }
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.TK_len:
                AST c = t.getFirstChild();
                String subType1 = st.getType(c.getText());
                if (subType1 == null || !subType1.startsWith(Defs.ARRAY_PREFIX)) {
                    Er.errNotDefined(c, c.getText());
                }
                return Defs.DESC_TYPE_INT;
        }
        return null;
    }

    // if null -> return; if TK_else -> return current AST
    private static AST parseBlock(AST t, ST st) {
        // parse fields
        t = fieldDecl(t, st);
        // parse statements
        for (; t != null; t = t.getNextSibling()) {
            if (t.getType() == DecafScannerTokenTypes.TK_else) {
                return t;
            }
            parseStmt(t, st);
        }
        return null;
    }

    private static AST parseStmt(AST t, ST st) {
        switch (t.getType()) {
            case DecafScannerTokenTypes.ASSIGN:
            case DecafScannerTokenTypes.PLUSASSIGN:
            case DecafScannerTokenTypes.MINUSASSIGN:
                parseMoreAssign(t, st);
                break;
            case DecafScannerTokenTypes.INCRE:
            case DecafScannerTokenTypes.DECRE:
                parseIncre(t, st);
                break;
            case DecafScannerTokenTypes.TK_if:
                parseIf(t, st);
                break;
            case DecafScannerTokenTypes.TK_for:
                parseFor(t, st);
                break;
            case DecafScannerTokenTypes.TK_while:
                parseWhile(t, st);
                break;
            case DecafScannerTokenTypes.TK_break:
                if (!AstUtils.isLoop(st.getContext())) {
                    Er.errBreak(t);
                }
                break;
            case DecafScannerTokenTypes.TK_continue:
                if (!AstUtils.isLoop(st.getContext())) {
                    Er.errContinue(t);
                }
            case DecafScannerTokenTypes.TK_return:
                String returnType = st.getReturnType();
                if (returnType == null) {
                    Er.report(t, "null return");
                    break;
                }
                String thisReturnType = parseExpr(t, st);
                if (thisReturnType == null) {
                    thisReturnType = Defs.DESC_TYPE_VOID;
                }
                if (!thisReturnType.equals(returnType)) {
                    Er.errType(t, returnType, thisReturnType);
                }
                break;
        }
        return null;
    }
}
