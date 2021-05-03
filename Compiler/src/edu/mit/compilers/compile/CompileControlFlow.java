package edu.mit.compilers.compile;

import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.syntax.Program;

public class CompileControlFlow {
    public static final void ifFlow(Boolean hasElse, ABlock codesCondition, ABlock codesIfExecution, ABlock codesElseExecution, ABlock codes) {
        if (!Program.shouldCompile()) return;
        Label ifExecutionEndLabel = new Label();
        Label ifElseEndLabel = new Label();
        Oprand conditionOp = Manager.tmpPop();
        Reg condition;
        if (conditionOp instanceof Reg) {
            condition = (Reg)conditionOp;
        } else {
            condition = Manager.newTmpReg();
            codesCondition.add(
                asm.bin("movq", conditionOp, condition)    
            );
        }
        codesCondition.add(
            asm.bin("cmp", new Num(0L), condition.bite()),
            asm.jmp("je", ifExecutionEndLabel)
        );
        if (hasElse)
        codesIfExecution.add(
            asm.jmp("jmp", ifElseEndLabel)
        );
        codes.add(asm.cmt("if - start"));
        codes.add(asm.cmt("if - condition"));
        codes.addAll(codesCondition);
        codes.add(asm.cmt("if - ifExecution"));
        codes.addAll(codesIfExecution);
        codes.add(asm.label(ifExecutionEndLabel));
        if (hasElse)
        {
        codes.add(asm.cmt("if - elseExecution"));
        codes.addAll(codesElseExecution);
        codes.add(asm.label(ifElseEndLabel));
        }
        codes.add(asm.cmt("if - end"));
    }

    public static final void forFlow(ABlock codesInit, ABlock codesCondition, ABlock codesIncrement, ABlock codesExecution, ABlock codes) {
        if (!Program.shouldCompile()) return;
        Label executionBeginLabel = new Label();
        Label conditionBeginLabel = new Label();
        Oprand conditionOp = Manager.tmpPop();
        Reg condition;
        if (conditionOp instanceof Reg) {
            condition = (Reg)conditionOp;
        } else {
            condition = Manager.newTmpReg();
            codesCondition.add(
                asm.bin("movq", conditionOp, condition)    
            );
        }
        codesInit.add(
            asm.jmp("jmp", conditionBeginLabel)
        );
        codesCondition.add(
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
        codes.add(asm.label(Manager.getContinueLabel()));
        codes.addAll(codesIncrement);
        codes.add(asm.label(conditionBeginLabel));
        codes.add(asm.cmt("for loop - condition"));
        codes.addAll(codesCondition);
        codes.add(asm.label(Manager.getBreakLabel()));
        codes.add(asm.cmt("for loop - end"));
    }

    public static final void whileFlow(ABlock codesCondition, ABlock codesExecution, ABlock codes) {
        if (!Program.shouldCompile()) return;
        Label executionBeginLabel = new Label();
        Oprand conditionOp = Manager.tmpPop();
        Reg condition;
        if (conditionOp instanceof Reg) {
            condition = (Reg)conditionOp;
        } else {
            condition = Manager.newTmpReg();
            codesCondition.add(
                asm.bin("movq", conditionOp, condition)    
            );
        }
        codesCondition.add(
            asm.bin("cmp", new Num(0L), condition.bite()),
            asm.jmp("jne", executionBeginLabel)
        );
        codes.add(asm.cmt("while - start"));
        codes.add(asm.jmp("jmp", Manager.getContinueLabel()));
        codes.add(asm.cmt("while - codesExecution "));
        codes.add(asm.label(executionBeginLabel));
        codes.addAll(codesExecution);
        codes.add(asm.cmt("while - codesCondition "));
        codes.add(asm.label(Manager.getContinueLabel()));
        codes.addAll(codesCondition);
        codes.add(asm.label(Manager.getBreakLabel()));
        codes.add(asm.cmt("while - end"));
    }
}
