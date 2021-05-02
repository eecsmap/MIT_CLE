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
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.st.ArrayDesc;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.tools.Err;
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

    private static VarType binaryExpr(AST t, List<String> codes) {
        AST l = t.getFirstChild();
        AST r = l.getNextSibling();
        List<String> leftCodes = new ArrayList<>();
        List<String> rightCodes = new ArrayList<>();
        VarType lType = expr(l, ActionType.STORE, leftCodes);
        VarType rType = expr(r, ActionType.LOAD, rightCodes);
        VarType returnType = null;
        if (AstUtils.isBinaryOp(t)) {
            if (!lType.equals(rType)) {
                Err.errType(l, lType, rType);
            }
            returnType = lType;
            CompileStructure.binaryOpExpr(t.getType(), leftCodes, rightCodes, codes);
        } else if (AstUtils.isBinaryCompOp(t)) {
            if (!lType.equals(rType) || lType.isVoid()) {
                Err.errType(r, lType, rType);
            }
            returnType = VarType.BOOL;
            CompileStructure.binaryCompExpr(t.getType(), leftCodes, rightCodes, codes);
        } else if (AstUtils.isBinaryBoolOp(t)) {
            if (!lType.isBool()) {
                Err.errType(l, VarType.BOOL, lType);
            }
            if (!rType.isBool()) {
                Err.errType(r, VarType.BOOL, rType);
            }
            returnType = VarType.BOOL;
            CompileStructure.binaryBoolExpr(t.getType(), leftCodes, rightCodes, codes);
        } else if (AstUtils.isBinaryIntCompOp(t)) {
            if (!lType.isInt()) {
                Err.errType(l, VarType.INT, lType);
            }
            if (!rType.isInt()) {
                Err.errType(r, VarType.INT, rType);
            }
            returnType = VarType.BOOL;
            CompileStructure.binaryCompExpr(t.getType(), leftCodes, rightCodes, codes); 
        }
        return returnType;
    }

    private static VarType idExpr(AST t, ActionType action, List<String> codes) {
        Descriptor desc = Manager.getDesc(t.getText());
        if (desc == null) {
            Err.errNotDefined(t, t.getText());
            return VarType.WILDCARD;
        }
        VarType type = desc.getType();
        // array
        if (type.isArray()) {
            if (t.getNumberOfChildren() == 0) {
                return type;
            }
            return Element.arrayElement(t, action, codes);
        }
        // method
        if (type.isMethod()) {
            return Method.call(t, codes, true);
        }
        // var
        Manager.tmpPush(desc.getAddr());
        return type;
    }

    private static VarType minusExpr(AST t, List<String> codes) {
        if (t.getNumberOfChildren() == 1 && t.getFirstChild().getType() == DecafScannerTokenTypes.INTLITERAL) {
            return Element.intLiteral(t.getFirstChild(), true);
        }
        VarType subType = expr(t.getFirstChild(), ActionType.LOAD, codes);
        if (subType != null && !subType.isInt()) {
            Err.errType(t, VarType.INT, subType);
        }
        CompileStructure.minusExpr(codes);
        return VarType.INT;
    }

    private static VarType exclamExpr(AST t, List<String> codes) {
        VarType subType = expr(t.getFirstChild(), ActionType.LOAD, codes);
        if (subType != null && !subType.isBool()) {
            Err.errType(t, VarType.BOOL, subType); 
        }
        CompileStructure.exclamExpr(codes);
        return VarType.BOOL;
    }

    // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    static VarType expr(AST t, ActionType action, List<String> codes) {
        if (t == null) {
            Manager.tmpPush(null);
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
                Manager.tmpPush(new Bool(true));
                return VarType.BOOL;
            case DecafScannerTokenTypes.TK_false:
                Manager.tmpPush(new Bool(false));
                return VarType.BOOL;
            case DecafScannerTokenTypes.MINUS:
                return minusExpr(t, codes);
            case DecafScannerTokenTypes.EXCLAM:
                return exclamExpr(t, codes);
            case DecafScannerTokenTypes.TK_len:
                AST c = t.getFirstChild();
                ArrayDesc desc = Manager.getArray(c.getText());
                if (desc == null) {
                    Err.errNotDefined(c, c.getText());
                }
                Manager.tmpPush(new Num(desc.getCap()));
                return VarType.INT;
            case DecafScannerTokenTypes.STRINGLITERAL:
                if (Program.shouldCompile()) {
                    Addr stringAddr = Program.addStringLiteral(t.getText());
                    Manager.tmpPush(stringAddr);
                }
                return VarType.STRING_LITERAL;
            case DecafScannerTokenTypes.CHARLITERAL:
                CompileStructure.charLiteral(t.getText().charAt(1), codes);
                return VarType.INT;
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
                if (!Manager.isInLoop()) {
                    Err.errBreak(t);
                }
                CompileStructure.tkBreak(codes);
                return;
            case DecafScannerTokenTypes.TK_continue:
                if (!Manager.isInLoop()) {
                    Err.errContinue(t);
                }
                CompileStructure.tkContinue(codes);
                return;
            case DecafScannerTokenTypes.TK_return:
                VarType expectedReturnType = Manager.getReturnType();
                VarType actualReturnType = expr(t.getFirstChild(), ActionType.LOAD, codes);
                if (actualReturnType == null) {
                    actualReturnType = VarType.VOID;
                }
                if (!expectedReturnType.equals(actualReturnType)) {
                    Err.errType(t, expectedReturnType, actualReturnType);
                }
                CompileStructure.tkReturn(expectedReturnType, codes);
                return;
            default:
                return;
        }
    }
}
