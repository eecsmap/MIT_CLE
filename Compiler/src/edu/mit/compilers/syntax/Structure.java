package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Bool;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileStructure;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ArrayDesc;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.MethodUtils;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.grammar.*;

public class Structure {
    private Structure() {}
    private static boolean isBinaryAnyOp(AST t) {
        return 
        AstUtils.isBinaryOp(t)
        || AstUtils.isBinaryCompOp(t)
        || AstUtils.isBinaryBoolOp(t)
        || AstUtils.isBinaryIntCompOp(t);
    }

    private static String binaryExpr(AST t, List<String> codes) {
        AST l = t.getFirstChild();
        AST r = l.getNextSibling();
        List<String> leftCodes = new ArrayList<>();
        List<String> rightCodes = new ArrayList<>();
        String lType = expr(l, ActionType.STORE, leftCodes);
        String rType = expr(r, ActionType.LOAD, rightCodes);
        String returnType = null;
        if (AstUtils.isBinaryOp(t)) {
            if (!Defs.equals(lType, rType)) {
                Er.errType(l, lType, rType);
            }
            returnType = lType;
            CompileStructure.binaryOpExpr(t.getType(), leftCodes, rightCodes, codes);
        } else if (AstUtils.isBinaryCompOp(t)) {
            if ((!Defs.equals(lType, rType) || Defs.equals(Defs.DESC_TYPE_VOID, lType))) {
                Er.errType(r, lType, rType);
            }
            returnType = Defs.DESC_TYPE_BOOL;
            CompileStructure.binaryCompExpr(t.getType(), leftCodes, rightCodes, codes);
        } else if (AstUtils.isBinaryBoolOp(t)) {
            if (!Defs.equals(Defs.DESC_TYPE_BOOL, lType)) {
                Er.errType(l, Defs.DESC_TYPE_BOOL, lType);
            }
            if (rType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, rType)) {
                Er.errType(r, Defs.DESC_TYPE_BOOL, rType);
            }
            returnType = Defs.DESC_TYPE_BOOL;
            CompileStructure.binaryBoolExpr(t.getType(), leftCodes, rightCodes, codes);
        } else if (AstUtils.isBinaryIntCompOp(t)) {
            if (!Defs.equals(Defs.DESC_TYPE_INT, lType)) {
                Er.errType(l, Defs.DESC_TYPE_INT, lType);
            }
            if (rType != null && !Defs.equals(Defs.DESC_TYPE_INT, rType)) {
                Er.errType(r, Defs.DESC_TYPE_INT, rType);
            }
            returnType = Defs.DESC_TYPE_BOOL;
            CompileStructure.binaryCompExpr(t.getType(), leftCodes, rightCodes, codes); 
        }
        return returnType;
    }

    private static String idExpr(AST t, ActionType action, List<String> codes) {
        Descriptor desc = MethodUtils.getDesc(t.getText());
        if (desc == null) {
            desc = Program.importST.getDesc(t.getText());
            if (desc == null) {
                Er.errNotDefined(t, t.getText());
                return Defs.DESC_TYPE_WILDCARD;
            }
        }
        String type = desc.getType();
        // array
        if (Defs.isArrayType(type)) {
            if (t.getNumberOfChildren() == 0) {
                return type;
            }
            return Element.arrayElement(t, action, codes);
        }
        // method
        if (Defs.isMethodType(type)) {
            return Method.call(t, codes, true);
        }
        // var
        MethodUtils.tmpPush(desc.getAddr());
        return type;
    }

    private static String minusExpr(AST t, List<String> codes) {
        if (t.getNumberOfChildren() == 1 && t.getFirstChild().getType() == DecafScannerTokenTypes.INTLITERAL) {
            return Element.intLiteral(t.getFirstChild(), true);
        }
        String subType = expr(t.getFirstChild(), ActionType.LOAD, codes);
        if (subType != null && !Defs.equals(Defs.DESC_TYPE_INT, subType)) {
            Er.errType(t, Defs.DESC_TYPE_INT, subType);
        }
        CompileStructure.minusExpr(codes);
        return Defs.DESC_TYPE_INT;
    }

    private static String exclamExpr(AST t, List<String> codes) {
        String subType = expr(t.getFirstChild(), ActionType.LOAD, codes);
        if (subType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, subType)) {
            Er.errType(t, Defs.DESC_TYPE_BOOL, subType); 
        }
        CompileStructure.exclamExpr(codes);
        return Defs.DESC_TYPE_BOOL;
    }

    // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    static String expr(AST t, ActionType action, List<String> codes) {
        if (t == null) {
            MethodUtils.tmpPush(null);
            return null;
        }
        if (isBinaryAnyOp(t) && t.getNumberOfChildren() == 2) {
            return binaryExpr(t, codes);
        }
        switch (t.getType()) {
            case DecafScannerTokenTypes.ID:
                return idExpr(t, action, codes);
            case DecafScannerTokenTypes.INTLITERAL:
                return Element.intLiteral(t, false);
            case DecafScannerTokenTypes.TK_true:
                MethodUtils.tmpPush(new Bool(true));
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.TK_false:
                MethodUtils.tmpPush(new Bool(false));
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.MINUS:
                return minusExpr(t, codes);
            case DecafScannerTokenTypes.EXCLAM:
                return exclamExpr(t, codes);
            case DecafScannerTokenTypes.TK_len:
                AST c = t.getFirstChild();
                Descriptor desc = MethodUtils.getDesc(c.getText());
                String subType = desc.getType();
                if (subType == null || !Defs.isArrayType(subType)) {
                    Er.errNotDefined(c, c.getText());
                }
                MethodUtils.tmpPush(new Num(((ArrayDesc)desc).getCap()));
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.STRINGLITERAL:
                if (Program.shouldCompile()) {
                    Addr stringAddr = Program.addStringLiteral(t.getText());
                    MethodUtils.tmpPush(stringAddr);
                }
                return Defs.TYPE_STRING_LITERAL;
            case DecafScannerTokenTypes.CHARLITERAL:
                CompileStructure.charLiteral(t.getText().charAt(1), codes);
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.COLON:
                return BasicOpration.relOps(t, codes);
            default:
                return null;
        }
    }

    // if null -> return; if TK_else -> return current AST
    static AST block(AST t, List<String> codes) {
        // parse fields
        t = Field.declare(t, codes);
        // parse statements
        for (; t != null; t = t.getNextSibling()) {
            if (t.getType() == DecafScannerTokenTypes.TK_else) {
                return t;
            }
            parseStmt(t, codes);
        }
        return null;
    }

    static void parseStmt(AST t, List<String> codes) {
        codes.add("");
        switch (t.getType()) {
            case DecafScannerTokenTypes.ID:
                Method.call(t, codes, false);
                return;
            case DecafScannerTokenTypes.ASSIGN:
            case DecafScannerTokenTypes.PLUSASSIGN:
            case DecafScannerTokenTypes.MINUSASSIGN:
            case DecafScannerTokenTypes.INCRE:
            case DecafScannerTokenTypes.DECRE:
                BasicOpration.moreAssign(t, codes);
                return;
            case DecafScannerTokenTypes.TK_if:
                ControlFlow.ifFlow(t, codes);
                return;
            case DecafScannerTokenTypes.TK_for:
                ControlFlow.forFlow(t, codes);
                return;
            case DecafScannerTokenTypes.TK_while:
                ControlFlow.whileFlow(t, codes);
                return;
            case DecafScannerTokenTypes.TK_break:
                if (!MethodUtils.isInLoop()) {
                    Er.errBreak(t);
                }
                CompileStructure.tkBreak(codes);
                return;
            case DecafScannerTokenTypes.TK_continue:
                if (!MethodUtils.isInLoop()) {
                    Er.errContinue(t);
                }
                CompileStructure.tkContinue(codes);
                return;
            case DecafScannerTokenTypes.TK_return:
                String expectedReturnType = MethodUtils.getReturnType();
                if (expectedReturnType == null) {
                    Er.report(t, "null return");
                    return;
                }
                String actualReturnType = expr(t.getFirstChild(), ActionType.LOAD, codes);
                if (actualReturnType == null) {
                    actualReturnType = Defs.DESC_TYPE_VOID;
                }
                if (!Defs.equals(expectedReturnType, actualReturnType)) {
                    Er.errType(t, expectedReturnType, actualReturnType);
                }
                CompileStructure.tkReturn(expectedReturnType, codes);
                return;
            default:
                return;
        }
    }
}
