package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ir.IrOp;

public class ControlWhile implements Control {
    private Block conditionBlock;
    private Block executionBlock;
    
    public ControlWhile(List<IrOp> condition, List<IrOp> execution) {
        conditionBlock = new Block(condition);
        executionBlock = new Block(execution);

        executionBlock.appendLeft(
            asm.jmp("jmp", conditionBlock.getBeginLabel())
        );

        conditionBlock.append(
            asm.jmp("jmp", executionBlock.getBeginLabel())
        );
    }

    @Override
    public List<String> getCodeList() {
        List<String> codeList = new ArrayList<>();
        codeList.addAll(executionBlock.getCodeList());
        codeList.addAll(conditionBlock.getCodeList());
        return null;
    }
}
