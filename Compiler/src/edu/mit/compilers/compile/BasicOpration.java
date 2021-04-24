package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.*;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.grammar.DecafParserTokenTypes;
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
            lType = Element.arrayElement(c, st, codes);
        } else if (c.getNumberOfChildren() > 0) {
            Er.errVarIsNotArray(c, lID);
        } else {
            Program.result.push(lDesc.getAddr());
        }
        return lType;
    }

    // =
    private static void binaryAssign(AST t, ST st, boolean simple, List<String> codes) {
        String lType = leftValue(t, st, codes);
        AST c = t.getFirstChild();
        c = c.getNextSibling();
        String rType = Structure.expr(c, st, codes);
        if (lType != null && (!Defs.equals(lType, rType) || (!simple && !Defs.equals(Defs.DESC_TYPE_INT, lType)))) {
            System.err.printf("2 ");
            Er.errType(c, lType, rType);
        }
        if (Program.shouldCompile()) {
            Oprand rAddr = Program.result.pop();
            Oprand lAddr = Program.result.pop();
            codes.add(
                asm.bin("movl", rAddr, lAddr)  
            );
        }
    }

    // ++, --
    private static void unaryAssign(AST t, ST st, List<String> codes) {
        String lType = leftValue(t, st, codes);
        AST c = t.getFirstChild();
        if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
            System.err.printf("31 ");
            Er.errType(c, Defs.DESC_TYPE_INT, lType);
        }
        if (Program.shouldCompile()) {
            Oprand lAddr = Program.result.pop();
            String op = t.getType() == DecafParserTokenTypes.INCRE ? "add" : "sub";
            codes.add(
                asm.bin(op, new Num(1L), lAddr)  
            );
        }
    }

    // only =, forwarder
    static void simpleAssign(AST t, ST st, List<String> codes) {
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
        List<String> codesCondition = new ArrayList<>();
        List<String> codesIfExecution = new ArrayList<>();
        List<String> codesElseExecution = new ArrayList<>();
        Label ifExecutionEndLabel = new Label();
        Label ifElseEndLabel = new Label();
        String cond = Structure.expr(cc, st, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, cond)) {
            Er.errType(t, Defs.DESC_TYPE_BOOL, cond);
        }
        cc = cc.getNextSibling();
        String ifType = Structure.expr(cc, st, codesIfExecution);
        c = c.getNextSibling();
        String elseType = Structure.expr(c, st, codesElseExecution);
        if (!Defs.equals(ifType, elseType)) {
            Er.errType(t, ifType, elseType);
        }
        if (Program.shouldCompile()) {
            Oprand elseOp = Program.result.pop();
            Oprand ifOp = Program.result.pop();
            Addr resultAddr = st.newTmpAddr();
            codesCondition.add(
                asm.jmp("je", ifExecutionEndLabel)
            );
            Collections.addAll(codesIfExecution,
                asm.bin("movl", ifOp, resultAddr),
                asm.jmp("jmp", ifElseEndLabel)
            );
            codesElseExecution.add(
                asm.bin("movl", elseOp, resultAddr)
            );
            codes.addAll(codesCondition);
            codes.addAll(codesIfExecution);
            codes.add(asm.label(ifExecutionEndLabel));
            codes.addAll(codesElseExecution);
            codes.add(asm.label(ifElseEndLabel));
            Program.result.push(resultAddr);
        }
        return ifType;
    }
}
