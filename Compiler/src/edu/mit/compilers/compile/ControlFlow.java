package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;

public class ControlFlow {
    static void ifFlow(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        AST c = t.getFirstChild();
        Label ifExecutionEndLabel = new Label();
        Label ifElseEndLabel = new Label();
        // condition
        List<String> codesCondition = new ArrayList<>();
        String type = Structure.expr(c, localST, codesCondition);
        Boolean hasElse = false;
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            System.err.printf("14 ");
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // if block
        List<String> codesIfExecution = new ArrayList<>();
        c = Structure.block(c.getNextSibling(), localST, codesIfExecution);
        // else block
        List<String> codesElseExecution = new ArrayList<>();
        if (c != null) {
            hasElse = true;
            Structure.block(c.getFirstChild(), localST, codesElseExecution);
        }
        if (Program.shouldCompile()) {
            Reg condition = (Reg)localST.tmpPop();
            Collections.addAll(codesCondition,
                asm.bin("cmp", new Num(0L), condition.bite()),
                asm.jmp("je", ifExecutionEndLabel)
            );
            if (hasElse) 
            codesIfExecution.add(
                asm.jmp("jmp", ifElseEndLabel)
            );
            codes.addAll(codesCondition);
            codes.addAll(codesIfExecution);
            codes.add(asm.label(ifExecutionEndLabel));
            if (hasElse)
            {
            codes.addAll(codesElseExecution);
            codes.add(asm.label(ifElseEndLabel));
            }
        }
    }

    // doesn't suppeort declaration in for loop
    static void forFlow(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        Label executionBeginLabel = new Label();
        Label conditionBeginLabel = new Label();
        Label loopEndLabel = new Label();
        localST.pushContext(t.getType());
        if (Program.shouldCompile()) {
            localST.pushContinueLabel(conditionBeginLabel);
            localST.pushBreakLabel(loopEndLabel);
        }
        AST c = t.getFirstChild();
        // simple assign
        List<String> codesInit = new ArrayList<>();
        BasicOpration.simpleAssign(c, localST, codesInit);
        c = c.getNextSibling();
        // condition expr
        List<String> codesCondition = new ArrayList<>();
        String conditionExprType = Structure.expr(c, localST, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, conditionExprType)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, conditionExprType);
        }
        c = c.getNextSibling();
        // more assign
        List<String> codesIncrement = new ArrayList<>();
        BasicOpration.moreAssign(c, localST, codesIncrement);
        c = c.getNextSibling();
        // block
        List<String> codesExecution = new ArrayList<>();
        Structure.block(c, localST, codesExecution);
        if (Program.shouldCompile()) {
            Reg condition = (Reg)localST.tmpPop();
            codesInit.add(
                asm.jmp("jmp", conditionBeginLabel)
            );
            Collections.addAll(codesCondition,
                asm.bin("cmp", new Num(0L), condition.bite()),
                asm.jmp("jne", executionBeginLabel)
            );
            codes.add(asm.cmt("for loop - start"));
            codes.add(asm.cmt("for loop - variable initialization"));
            codes.addAll(codesInit);
            codes.add(asm.cmt("for loop - execution"));
            codes.add(asm.label(executionBeginLabel));
            codes.addAll(codesExecution);
            codes.add(asm.cmt("for loop - increment"));
            codes.addAll(codesIncrement);
            codes.add(asm.label(conditionBeginLabel));
            codes.add(asm.cmt("for loop - condition"));
            codes.addAll(codesCondition);
            codes.add(asm.label(loopEndLabel));
            codes.add(asm.cmt("for loop - end"));
        }
        localST.popContext();
        if (Program.shouldCompile()) {
            localST.popContinueLabel();
            localST.popBreakLabel();
        }
    }

    static void whileFlow(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        Label executionBeginLabel = new Label();
        Label conditionBeginLabel = new Label();
        Label loopEndLabel = new Label();
        localST.pushContext(t.getType());
        if (Program.shouldCompile()) {
            localST.pushContinueLabel(conditionBeginLabel);
            localST.pushBreakLabel(loopEndLabel);
        }
        // condition
        AST c = t.getFirstChild();
        List<String> codesCondition = new ArrayList<>();
        String type = Structure.expr(c, localST, codesCondition);
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            System.err.printf("15 ");
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // execution block
        c = c.getNextSibling();
        List<String> codesExecution = new ArrayList<>();
        Structure.block(c, localST, codesExecution);
        if (Program.shouldCompile()) {
            Reg condition = (Reg)localST.tmpPop();
            Collections.addAll(codesCondition,
                asm.bin("cmp", new Num(0L), condition.bite()),
                asm.jmp("jne", executionBeginLabel)
            );
            codes.add(asm.cmt("while - start"));
            codes.add(asm.jmp("jmp", conditionBeginLabel));
            codes.add(asm.cmt("while - codesExecution "));
            codes.add(asm.label(executionBeginLabel));
            codes.addAll(codesExecution);
            codes.add(asm.cmt("while - codesCondition "));
            codes.add(asm.label(conditionBeginLabel));
            codes.addAll(codesCondition);
            codes.add(asm.label(loopEndLabel));
            codes.add(asm.cmt("while - end"));
        }
        localST.popContext();
        if (Program.shouldCompile()) {
            localST.popContinueLabel();
            localST.popBreakLabel();
        }
    }
}
