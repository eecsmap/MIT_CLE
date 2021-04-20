package edu.mit.compilers.compile;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;

public class Operation {
    // return lType
    static String assign(AST t, ST st, List<String> codes) {
        AST c = t.getFirstChild();
        String lID = c.getText();
        String lType = st.getType(lID);
        if (lType == null) {
            System.err.printf("1 ");
            Er.errNotDefined(c, c.getText());
        } else if (Defs.isArrayType(lType)) {
            lType = Element.arrayElement(c, st);
        } else if (c.getNumberOfChildren() > 0) {
            Er.errVarIsNotArray(c, lID);
        }
        return lType;
    }

    static void binaryAssign(AST t, ST st, boolean simple, List<String> codes) {
        String lType = assign(t, st);
        AST c = t.getFirstChild();
        c = c.getNextSibling();
        String rType = Structure.expr(c, st);
        if (lType != null && (!Defs.equals(lType, rType) || (!simple && !Defs.equals(Defs.DESC_TYPE_INT, lType)))) {
            System.err.printf("2 ");
            Er.errType(c, lType, rType);
        }
    }

    static void unaryAssign(AST t, ST st, List<String> codes) {
        String lType = assign(t, st);
        AST c = t.getFirstChild();
        if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
            System.err.printf("31 ");
            Er.errType(c, Defs.DESC_TYPE_INT, lType);
        }
    }

    // only =, forwarder
    static void simpleAssign(AST t, ST st, List<String> codes) {
        String op = "=";
        binaryAssign(t, st, true, codes);
    }

    // +=, -=, =, ++, --, forwarder
    static void moreAssign(AST t, ST st, List<String> codes) {
        String op = t.getText();
        if (AstUtils.isBinaryAssignOp(t)) {
            binaryAssign(t, st, op.equals("="), codes);
        } else {
            unaryAssign(t, st, codes);
        }
    }

    static String relOps(AST t, ST st, List<String> codes) {
        AST c = t.getFirstChild();
        AST cc = c.getFirstChild();
        String cond = Structure.expr(cc, st);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, cond)) {
            Er.errType(t, Defs.DESC_TYPE_BOOL, cond);
        }
        cc = cc.getNextSibling();
        String trueType = Structure.expr(cc, st);
        c = c.getNextSibling();
        String falseType = Structure.expr(c, st);
        if (!Defs.equals(trueType, falseType)) {
            Er.errType(t, trueType, falseType);
        }
        return trueType;
    }
}
