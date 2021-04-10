package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ir.IrOp;
import edu.mit.compilers.st.ST;

public class ControlIfElse implements Control {
    Block conditionBlock;
    Block executionIfBlock;
    Block executionElseBlock;

    public ControlIfElse(List<IrOp> condition, List<IrOp> executionIf, List<IrOp> executionElse, ST st) {
        conditionBlock = new Block(condition);
        executionIfBlock = new Block(executionIf);
        executionElseBlock = new Block(executionElse);

        conditionBlock.append(
            asm.jmp("je", executionElseBlock.getBeginLabel())
        );

        executionIfBlock.append(
            asm.jmp("jmp", executionElseBlock.getEndLabel())
        );
    }

    @Override
    public List<String> getCodeList() {
        List<String> codeList = new ArrayList<>();
        codeList.addAll(conditionBlock.getCodeList());
        codeList.addAll(executionIfBlock.getCodeList());
        codeList.addAll(executionElseBlock.getCodeList());
        return codeList;
    }
}
