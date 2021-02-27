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
            String type = AstUtils.t0.get(t.getType());
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
        ST paramST = new ST(globalST);
        ST localST = new ST(paramST);
        for (; t != null && AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST c = t.getFirstChild();
            globalST.push(new MethodDesc(c.getText(), t.getText()));
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
        String lhsID = c.getText();
        String lhsType = st.getType(lhsID);
        c = c.getNextSibling();
        String rhsType = parseExpr(c, st);
        if (lhsType != rhsType) {
            Er.errType(c, lhsType, rhsType);
        }
    }

    // +=, -=, =
    private static void parseMoreAssign(AST t, ST st) {
        String op = t.getText();
        AST c = t.getFirstChild();
        String lhsID = c.getText();
        String lhsType = st.getType(lhsID);
        c = c.getNextSibling();
        String rhsType = parseExpr(c, st);
        if (lhsType != rhsType) {
            Er.errType(c, lhsType, rhsType);
        }
    }

    private static void parseIncre(AST t, ST st) {
        String op = t.getText();
        AST c = t.getFirstChild();
        String lhsID = c.getText();
        String lhsType = st.getType(lhsID);
        if (lhsType == null) {
            Er.errNotDefined(c, lhsID);
            return;
        }
        if (lhsType.startsWith(Defs.ARRAY_PREFIX)) {
            String type = parseArrayElement(c, st);
            if (type != Defs.DESC_TYPE_INT) {
                Er.errType(c, Defs.DESC_TYPE_INT, type);
            }
            return;
        }
        if (lhsType != Defs.DESC_TYPE_INT) {
            Er.errType(c, Defs.DESC_TYPE_INT, lhsType);
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
        ArrayList<String> params = methodMap.get(method.text);
        int i = 0;
        for (; c != null; c = c.getNextSibling(), i++) {
            String cType = parseExpr(c, st);
            if (params.get(i) != cType) {
                Er.errType(c, params.get(i), cType);
            }
        }

        return method.type.substring(Defs.DESC_METHOD.length());
    }

    private static String parseArrayElement(AST t, ST st) {
        String type = st.getType(t.getText());
        AST c = t.getFirstChild();
        String subType = parseExpr(c, st);
        if (subType != Defs.DESC_TYPE_INT && subType != Defs.DESC_TYPE_WILDCARD) {
            Er.errArrayIndexNotInt(t, t.getText(), subType);
            return null;
        }
        // TODO: boundary check
        if (type == Defs.DESC_ARRAY_BOOL) {
            return Defs.DESC_TYPE_INT;
        }
        return Defs.DESC_TYPE_BOOL;
    }

    private static void parseIf(AST t, ST st) {
        AST c = t.getFirstChild();
        String type = parseExpr(c, st);
        if (Defs.DESC_TYPE_BOOL != type) {
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
        AST c = t.getFirstChild();
        parseSimpleAssign(c, st);
        c = c.getNextSibling();
        parseMoreAssign(c, st);
        c = c.getNextSibling();
        parseBlock(c, st);
    }

    private static void parseWhile(AST t, ST st) {
        AST c = t.getFirstChild();
        String type = parseExpr(c, st);
        if (Defs.DESC_TYPE_BOOL != type) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        c = c.getNextSibling();
        parseBlock(c, st);
    }

    // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    private static String parseExpr(AST t, ST st) {
        if (AstUtils.isBinaryOp(t)) {
            // TODO
            return null;
        }
        if (AstUtils.isBinaryBoolOp(t)) {
            // TODO
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
                if (type == Defs.DESC_METHOD) {
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
                if (subType != Defs.DESC_TYPE_INT) {
                    Er.errType(t, Defs.DESC_TYPE_INT, subType);
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.EXCLAM:
                String subType0 = parseExpr(t.getFirstChild(), st);
                if (subType0 != Defs.DESC_TYPE_BOOL) {
                    Er.errType(t, Defs.DESC_TYPE_BOOL, subType0); 
                }
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.TK_len:
                // TODO: parse array
                return null;
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
                // TODO: break
                break;
            case DecafScannerTokenTypes.TK_return:
                // TODO: return
                break;
            case DecafScannerTokenTypes.TK_continue:
                // TODO: continue
                break;
        }
        return null;
    }
}
