package edu.mit.compilers.ir;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;

public class Operation {
    // return lType
    static String parseAssign(AST t, ST st) {
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
        return lType;
    }

    static void parseBinaryAssign(AST t, ST st, boolean simple) {
        String lType = parseAssign(t, st);
        AST c = t.getFirstChild();
        c = c.getNextSibling();
        String rType = parseExpr(c, st);
        if (lType != null && (!Defs.equals(lType, rType) || (!simple && !Defs.equals(Defs.DESC_TYPE_INT, lType)))) {
            System.err.printf("2 ");
            Er.errType(c, lType, rType);
        }
    }

    static void parseUnaryAssign(AST t, ST st) {
        String lType = parseAssign(t, st);
        AST c = t.getFirstChild();
        if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
            System.err.printf("31 ");
            Er.errType(c, Defs.DESC_TYPE_INT, lType);
        }
    }

    // only =
    static void parseSimpleAssign(AST t, ST st, List<String> codes) {
        String op = "=";
        parseBinaryAssign(t, st, true);
    }

    // +=, -=, =, ++, --
    static void parseMoreAssign(AST t, ST st, List<String> codes) {
        String op = t.getText();
        if (AstUtils.isBinaryAssignOp(t)) {
            parseBinaryAssign(t, st, op.equals("="));
        } else {
            parseUnaryAssign(t, st);
        }
    }

    static String parseRelOps(AST t, ST st) {
        AST c = t.getFirstChild();
        AST cc = c.getFirstChild();
        String cond = parseExpr(cc, st);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, cond)) {
            Er.errType(t, Defs.DESC_TYPE_BOOL, cond);
        }
        cc = cc.getNextSibling();
        String trueType = parseExpr(cc, st);
        c = c.getNextSibling();
        String falseType = parseExpr(c, st);
        if (!Defs.equals(trueType, falseType)) {
            Er.errType(t, trueType, falseType);
        }
        return trueType;
    }
}
