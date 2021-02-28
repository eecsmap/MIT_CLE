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
        if (globalST.getMethod("main") == null) {
            Er.errMainNotDefined(t);
        }
    }

    // return the next AST to parse
    private static AST importDecl(AST t, ST importST) {
        for (; AstUtils.isImport(t); t = t.getNextSibling()) {
            // parse single import statement
            String methodName = t.getFirstChild().getText();
            if (methodName.equals("main")) {
                Er.errBadImport(t.getFirstChild(), methodName);
                continue;
            }
            MethodDesc desc = new MethodDesc(Defs.DESC_METHOD_WILDCARD, methodName);
            importST.push(desc);
        }
        return t;
    }

    private static AST fieldDecl(AST t, ST st) {
        for (; t != null && AstUtils.isType(t); t = t.getNextSibling()) {
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
                    String cap = cc.getText();
                    if(Integer.parseInt(cap) <= 0) {
                        Er.errBadArrayCap(cc);
                        cap = "9999999";
                    }
                    if (!st.push(new ArrayDesc(Defs.makeArrayType(type), c.getText(), cap))) {
                        Er.errDuplicatedDeclaration(c, c.getText());
                    }
                    continue;
                }
                // cc is null -> it's single Variable
                if (!st.push(new VarDesc(type, c.getText()))) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                }
            }
        }
        return t;
    }

    private static AST methodDecl(AST t, ST globalST) {
        for (; t != null && AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST c = t.getFirstChild();
            globalST.push(new MethodDesc(Defs.makeMethodType(c.getText()), t.getText()));
            ST paramST = new ST(globalST);
            ST localST = new ST(paramST, c.getText());
            c = c.getNextSibling();
            // parse parameters
            ArrayList<String> params = new ArrayList<>();
            for (; c != null && AstUtils.isID(c); c = c.getNextSibling()) {
                paramST.push(new VarDesc(c.getFirstChild().getText(), c.getText()));
                params.add(c.getFirstChild().getText());
            }
            methodMap.put(t.getText(), params);
            parseBlock(c, localST);
        }
        return t;
    }

    private static void parseAssign(AST t, ST st, boolean simple) {
        AST c = t.getFirstChild();
        String lID = c.getText();
        String lType = st.getType(lID);
        if (lType == null) {
            System.err.printf("1 ");
            Er.errNotDefined(c, c.getText());
        } else if (Defs.isArrayType(lType)) {
            lType = parseArrayElement(c, st);
        } else if (c.getNumberOfChildren() > 0) {
            Er.errVarIsNotArray(c, lID);
        }
        c = c.getNextSibling();
        String rType = parseExpr(c, st);
        if (lType != null && (!Defs.equals(lType, rType) || (!simple && !Defs.equals(Defs.DESC_TYPE_INT, lType)))) {
            System.err.printf("2 ");
            Er.errType(c, lType, rType);
        }        
    }

    // only =
    private static void parseSimpleAssign(AST t, ST st) {
        String op = "=";
        parseAssign(t, st, true);
    }

    // +=, -=, =
    private static void parseMoreAssign(AST t, ST st) {
        String op = t.getText();
        parseAssign(t, st, op.equals("="));
    }

    private static void parseIncre(AST t, ST st) {
        String op = t.getText();
        AST c = t.getFirstChild();
        String lID = c.getText();
        String lType = st.getType(lID);
        if (lType == null) {
            System.err.printf("5 ");
            Er.errNotDefined(c, lID);
            return;
        }
        if (Defs.isArrayType(lType)) {
            String type = parseArrayElement(c, st);
            if (type != null && !Defs.equals(Defs.DESC_TYPE_INT, type)) {
                System.err.printf("6 ");
                Er.errType(c, Defs.DESC_TYPE_INT, type);
            }
            return;
        }
        if (!Defs.equals(Defs.DESC_TYPE_INT, lType)) {
            System.err.printf("7 ");
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
                System.err.printf("8 ");
                Er.errNotDefined(t, t.getText());
                return null;
            }
            return Defs.DESC_TYPE_WILDCARD;
        }
        
        AST c = t.getFirstChild();
        ArrayList<String> params = methodMap.get(method.getText());
        if (params == null) {
            System.err.printf("9 ");
            Er.errNotDefined(c, method.getText());
            return Defs.DESC_TYPE_WILDCARD;
        }
        if (params.size() != t.getNumberOfChildren()) {
            Er.errArrayArgsMismatch(t);
            return Defs.getMethodType(method.getType());
        }
        int i = 0;
        for (; c != null; c = c.getNextSibling(), i++) {
            String cType = parseExpr(c, st);
            if (!Defs.equals(params.get(i), cType)) {
                System.err.printf("10 ");
                Er.errType(c, params.get(i), cType);
            }
        }

        return Defs.getMethodType(method.getType());
    }

    private static String parseArrayElement(AST t, ST st) {
        Descriptor desc = st.getArray(t.getText());
        if (desc == null) {
            System.err.printf("11 ");
            Er.errNotDefined(t, t.getText());
            return null;
        }
        String type = desc.getType();
        AST c = t.getFirstChild();
        String subType = parseExpr(c, st);
        if (subType == null) {
            return Defs.getArrayType(type);
        }
        if (!Defs.equals(Defs.DESC_TYPE_INT, subType)) {
            System.err.printf("12 ");
            Er.errArrayIndexNotInt(t, Defs.DESC_TYPE_INT, subType);
            return Defs.getArrayType(type);
        }
        if (c.getType() == DecafScannerTokenTypes.INTLITERAL && desc.findVar(c.getText()) == null) {
            System.err.printf("13 ");
            Er.errArrayOutbound(t, desc.getText(), c.getText());
        }
        return Defs.getArrayType(type);
    }

    private static void parseIf(AST t, ST st) {
        AST c = t.getFirstChild();
        String type = parseExpr(c, st);
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            System.err.printf("14 ");
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
        parseExpr(c, st);
        c = c.getNextSibling();
        parseBlock(c, st);
        st.popContext();
    }

    private static void parseWhile(AST t, ST st) {
        st.pushContext(t.getType());
        AST c = t.getFirstChild();
        String type = parseExpr(c, st);
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            System.err.printf("15 ");
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
        if (AstUtils.isBinaryOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && !Defs.equals(lType, rType)) {
                System.err.printf("16 ");
                Er.errType(l, lType, rType);
            }
            return lType;
        }
        if (AstUtils.isBinaryBoolOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, lType)) {
                System.err.printf("17 ");
                Er.errType(l, Defs.DESC_TYPE_BOOL, lType);
            }
            if (rType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, rType)) {
                System.err.printf("18 ");
                Er.errType(r, Defs.DESC_TYPE_BOOL, rType);
            }
            return Defs.DESC_TYPE_BOOL;
        }
        if (AstUtils.isBinaryCompOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
                System.err.printf("27 ");
                Er.errType(l, Defs.DESC_TYPE_INT, lType);
            }
            if (rType != null && !Defs.equals(Defs.DESC_TYPE_INT, rType)) {
                System.err.printf("28 ");
                Er.errType(r, Defs.DESC_TYPE_INT, rType);
            }
            return Defs.DESC_TYPE_BOOL;
        }
        switch(t.getType()) {
            case DecafScannerTokenTypes.ID:
                String type = st.getType(t.getText());
                if (type == null) {
                    type = importST.getType(t.getText());
                    if (type == null) {
                        System.out.printf("19 ");
                        Er.errNotDefined(t, t.getText());
                        return null;
                    }
                }
                // array
                if (Defs.isArrayType(type)) {
                    return parseArrayElement(t, st);
                }
                // method
                if (Defs.isMethodType(type)) {
                    return parseMethodCall(t, st);
                }
                // var
                return type;
            case DecafScannerTokenTypes.INTLITERAL:
                try {
                    Integer.parseInt(t.getText());
                } catch (NumberFormatException e) {
                    Er.errIntegerTooLarge(t, t.getText());
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.TK_true:
            case DecafScannerTokenTypes.TK_false:
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.MINUS:
                String subType = parseExpr(t.getFirstChild(), st);
                if (subType != null && !Defs.equals(Defs.DESC_TYPE_INT, subType)) {
                    System.err.printf("20 ");
                    Er.errType(t, Defs.DESC_TYPE_INT, subType);
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.EXCLAM:
                String subType0 = parseExpr(t.getFirstChild(), st);
                if (subType0 != null && !Defs.equals(Defs.DESC_TYPE_BOOL, subType0)) {
                    System.err.printf("21 ");
                    Er.errType(t, Defs.DESC_TYPE_BOOL, subType0); 
                }
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.TK_len:
                AST c = t.getFirstChild();
                String subType1 = st.getType(c.getText());
                if (subType1 == null || !Defs.isArrayType(subType1)) {
                    System.err.printf("22 ");
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

    private static void parseStmt(AST t, ST st) {
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
                    System.err.printf("23 ");
                    Er.errBreak(t);
                }
                break;
            case DecafScannerTokenTypes.TK_continue:
                if (!AstUtils.isLoop(st.getContext())) {
                    System.err.printf("24 ");
                    Er.errContinue(t);
                }
                break;
            case DecafScannerTokenTypes.TK_return:
                String expectedReturnType = st.getReturnType();
                if (expectedReturnType == null) {
                    System.err.printf("25 ");
                    Er.report(t, "null return");
                    break;
                }
                String actualReturnType = parseExpr(t.getFirstChild(), st);
                if (actualReturnType == null) {
                    actualReturnType = Defs.DESC_TYPE_VOID;
                }
                if (!Defs.equals(expectedReturnType, actualReturnType)) {
                    System.err.printf("26 ");
                    Er.errType(t, expectedReturnType, actualReturnType);
                }
                break;
        }
    }
}
