package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileBasicOperation;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.MethodUtils;
import edu.mit.compilers.tools.Er;

public class BasicOpration {
    // return lType
    private static String leftValue(AST t, List<String> codes) {
        AST c = t.getFirstChild();
        String lID = c.getText();
        Descriptor lDesc = MethodUtils.getDesc(lID);
        String lType = lDesc.getType();
        if (lType == null) {
            Er.errNotDefined(c, c.getText());
        } else if (Defs.isArrayType(lType)) {
            lType = Element.arrayElement(c, ActionType.STORE, codes);
        } else if (c.getNumberOfChildren() > 0) {
            Er.errVarIsNotArray(c, lID);
        } else {
            MethodUtils.tmpPush(lDesc.getAddr());
        }
        return lType;
    }

    // =, +=, -=
    private static void binaryAssign(AST t, String op, List<String> codes) {
        String lType = leftValue(t, codes);
        AST c = t.getFirstChild();
        c = c.getNextSibling();
        String rType = Structure.expr(c, ActionType.LOAD, codes);
        if (lType != null && (!Defs.equals(lType, rType) || (!op.equals("=") && !Defs.equals(Defs.DESC_TYPE_INT, lType)))) {
            Er.errType(c, lType, rType);
        }
        CompileBasicOperation.binaryAssign(op, codes);
    }

    // ++, --
    private static void unaryAssign(AST t, List<String> codes) {
        String lType = leftValue(t, codes);
        AST c = t.getFirstChild();
        if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
            Er.errType(c, Defs.DESC_TYPE_INT, lType);
        }
        CompileBasicOperation.unaryAssign(t.getType(), codes);
    }

    // only =, forwarder
    static void simpleAssign(AST t, List<String> codes) {
        binaryAssign(t, "=", codes);
    }

    // +=, -=, =, ++, --, forwarder
    static void moreAssign(AST t, List<String> codes) {
        String op = t.getText();
        if (AstUtils.isBinaryAssignOp(t)) {
            binaryAssign(t, op, codes);
        } else {
            unaryAssign(t, codes);
        }
    }

    static String relOps(AST t, List<String> codes) {
        AST c = t.getFirstChild();
        AST cc = c.getFirstChild();
        List<String> codesCondition = new ArrayList<>();
        List<String> codesIfExecution = new ArrayList<>();
        List<String> codesElseExecution = new ArrayList<>();
        String cond = Structure.expr(cc, ActionType.LOAD, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, cond)) {
            Er.errType(t, Defs.DESC_TYPE_BOOL, cond);
        }
        cc = cc.getNextSibling();
        String ifType = Structure.expr(cc, ActionType.LOAD, codesIfExecution);
        c = c.getNextSibling();
        String elseType = Structure.expr(c, ActionType.LOAD, codesElseExecution);
        if (!Defs.equals(ifType, elseType)) {
            Er.errType(t, ifType, elseType);
        }
        CompileBasicOperation.relOps(codesCondition, codesIfExecution, codesElseExecution, codes);
        return ifType;
    }
}
