package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileBasicOperation;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;

public class BasicOpration {
    // return lType
    private static String leftValue(AST t, ST st, List<String> codes) {
        AST c = t.getFirstChild();
        String lID = c.getText();
        Descriptor lDesc = st.getDesc(lID);
        String lType = lDesc.getType();
        if (lType == null) {
            System.err.printf("1 ");
            Er.errNotDefined(c, c.getText());
        } else if (Defs.isArrayType(lType)) {
            lType = Element.arrayElement(c, st, ActionType.STORE, codes);
        } else if (c.getNumberOfChildren() > 0) {
            Er.errVarIsNotArray(c, lID);
        } else {
            st.tmpPush(lDesc.getAddr());
        }
        return lType;
    }

    // =, +=, -=
    private static void binaryAssign(AST t, ST st, String op, List<String> codes) {
        String lType = leftValue(t, st, codes);
        AST c = t.getFirstChild();
        c = c.getNextSibling();
        String rType = Structure.expr(c, st, ActionType.LOAD, codes);
        if (lType != null && (!Defs.equals(lType, rType) || (!op.equals("=") && !Defs.equals(Defs.DESC_TYPE_INT, lType)))) {
            System.err.printf("2 ");
            Er.errType(c, lType, rType);
        }
        CompileBasicOperation.binaryAssign(st, op, codes);
    }

    // ++, --
    private static void unaryAssign(AST t, ST st, List<String> codes) {
        String lType = leftValue(t, st, codes);
        AST c = t.getFirstChild();
        if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
            System.err.printf("31 ");
            Er.errType(c, Defs.DESC_TYPE_INT, lType);
        }
        CompileBasicOperation.unaryAssign(st, t.getType(), codes);
    }

    // only =, forwarder
    static void simpleAssign(AST t, ST st, List<String> codes) {
        binaryAssign(t, st, "=", codes);
    }

    // +=, -=, =, ++, --, forwarder
    static void moreAssign(AST t, ST st, List<String> codes) {
        String op = t.getText();
        if (AstUtils.isBinaryAssignOp(t)) {
            binaryAssign(t, st, op, codes);
        } else {
            unaryAssign(t, st, codes);
        }
    }

    static String relOps(AST t, ST st, List<String> codes) {
        AST c = t.getFirstChild();
        AST cc = c.getFirstChild();
        List<String> codesCondition = new ArrayList<>();
        List<String> codesIfExecution = new ArrayList<>();
        List<String> codesElseExecution = new ArrayList<>();
        String cond = Structure.expr(cc, st, ActionType.LOAD, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, cond)) {
            Er.errType(t, Defs.DESC_TYPE_BOOL, cond);
        }
        cc = cc.getNextSibling();
        String ifType = Structure.expr(cc, st, ActionType.LOAD, codesIfExecution);
        c = c.getNextSibling();
        String elseType = Structure.expr(c, st, ActionType.LOAD, codesElseExecution);
        if (!Defs.equals(ifType, elseType)) {
            Er.errType(t, ifType, elseType);
        }
        CompileBasicOperation.relOps(st, codesCondition, codesIfExecution, codesElseExecution, codes);
        return ifType;
    }
}
