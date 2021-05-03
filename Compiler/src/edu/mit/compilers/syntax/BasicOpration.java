package edu.mit.compilers.syntax;

import antlr.collections.AST;
import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileBasicOperation;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.defs.Defs.ActionType;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.tools.Err;

public class BasicOpration {
    // return lType
    private static VarType leftValue(AST t, ABlock codes) {
        AST c = t.getFirstChild();
        String lID = c.getText();
        Descriptor lDesc = Manager.getDesc(lID);
        VarType lType = lDesc.getType();
        if (lType == null) {
            Err.errNotDefined(c, c.getText());
        } else if (lDesc.getType().isArray()) {
            lType = Element.arrayElement(c, ActionType.STORE, codes);
        } else if (c.getNumberOfChildren() > 0) {
            Err.errVarIsNotArray(c, lID);
        } else {
            Manager.tmpPush(lDesc.getAddr());
        }
        return lType;
    }

    // =, +=, -=
    private static void binaryAssign(AST t, String op, ABlock codes) {
        VarType lType = leftValue(t, codes);
        AST c = t.getFirstChild();
        c = c.getNextSibling();
        VarType rType = Structure.expr(c, ActionType.LOAD, codes);
        if (lType != null && (!lType.equals(rType) || (!op.equals("=") && !lType.isInt()))) {
            Err.errType(c, lType, rType);
        }
        CompileBasicOperation.binaryAssign(op, codes);
    }

    // ++, --
    private static void unaryAssign(AST t, ABlock codes) {
        VarType lType = leftValue(t, codes);
        AST c = t.getFirstChild();
        if (lType != null && !lType.isInt()) {
            Err.errType(c, VarType.INT, lType);
        }
        CompileBasicOperation.unaryAssign(t.getType(), codes);
    }

    // only =, forwarder
    static void simpleAssign(AST t, ABlock codes) {
        binaryAssign(t, "=", codes);
    }

    // +=, -=, =, ++, --, forwarder
    static void moreAssign(AST t, ABlock codes) {
        String op = t.getText();
        if (AstUtils.isBinaryAssignOp(t)) {
            binaryAssign(t, op, codes);
        } else {
            unaryAssign(t, codes);
        }
    }

    static VarType relOps(AST t, ABlock codes) {
        AST c = t.getFirstChild();
        AST cc = c.getFirstChild();
        ABlock codesCondition = new ABlock();
        ABlock codesIfExecution = new ABlock();
        ABlock codesElseExecution = new ABlock();
        VarType cond = Structure.expr(cc, ActionType.LOAD, codesCondition);
        if (!cond.isBool()) {
            Err.errType(t, VarType.BOOL, cond);
        }
        cc = cc.getNextSibling();
        VarType ifType = Structure.expr(cc, ActionType.LOAD, codesIfExecution);
        c = c.getNextSibling();
        VarType elseType = Structure.expr(c, ActionType.LOAD, codesElseExecution);
        if (!ifType.equals(elseType)) {
            Err.errType(t, ifType, elseType);
        }
        CompileBasicOperation.relOps(codesCondition, codesIfExecution, codesElseExecution, codes);
        return ifType;
    }
}
