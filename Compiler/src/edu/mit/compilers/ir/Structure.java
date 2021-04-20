package edu.mit.compilers.ir;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;

public class Structure {
        // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    
    static String parseExpr(AST t, ST st, List<String> codes) {
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
        if (AstUtils.isBinaryCompOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && (!Defs.equals(lType, rType) || Defs.equals(Defs.DESC_TYPE_VOID, lType))) {
                System.err.printf("31 ");
                Er.errType(r, lType, rType);
            }
            return Defs.DESC_TYPE_BOOL;
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
        if (AstUtils.isBinaryIntCompOp(t) && t.getNumberOfChildren() == 2) {
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
                        return Defs.DESC_TYPE_WILDCARD;
                    }
                }
                // array
                if (Defs.isArrayType(type)) {
                    if (t.getNumberOfChildren() == 0) {
                        return type;
                    }
                    return parseArrayElement(t, st);
                }
                // method
                if (Defs.isMethodType(type)) {
                    return parseMethodCall(t, st);
                }
                // var
                return type;
            case DecafScannerTokenTypes.INTLITERAL:
                return parseIntLiteral(t, false);
            case DecafScannerTokenTypes.TK_true:
            case DecafScannerTokenTypes.TK_false:
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.MINUS:
                if (t.getNumberOfChildren() == 1 && t.getFirstChild().getType() == DecafScannerTokenTypes.INTLITERAL) {
                    return parseIntLiteral(t.getFirstChild(), true);
                }
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
            case DecafScannerTokenTypes.STRINGLITERAL:
                return Defs.TYPE_STRING_LITERAL;
            case DecafScannerTokenTypes.CHARLITERAL: 
                return Defs.TYPE_CHAR_LITERAL;
            case DecafScannerTokenTypes.COLON:
                return parseRelOps(t, st);
        }
        return null;
    }

    // if null -> return; if TK_else -> return current AST
    static AST parseBlock(AST t, ST st, List<String> codes) {
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

    static void parseStmt(AST t, ST st) {
        switch (t.getType()) {
            case DecafScannerTokenTypes.ID:
                parseMethodCall(t, st);
                break;
            case DecafScannerTokenTypes.ASSIGN:
            case DecafScannerTokenTypes.PLUSASSIGN:
            case DecafScannerTokenTypes.MINUSASSIGN:
            case DecafScannerTokenTypes.INCRE:
            case DecafScannerTokenTypes.DECRE:
                parseMoreAssign(t, st);
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
