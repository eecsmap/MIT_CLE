package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ir.IrOp;

public class ControlFor implements Control {
    private Block initBlock;
    private Block conditionBlock;
    private Block executionBlock;

    public ControlFor(
        List<IrOp> init, List<IrOp> condition, List<IrOp> execution, List<IrOp> increment)
    {
        initBlock = new Block(init);
        conditionBlock = new Block(condition);
        // append increment to execution
        execution.addAll(increment);
        executionBlock = new Block(execution);
        
        initBlock.append(
            asm.jmp("jmp", conditionBlock.getBeginLabel())
        );

        conditionBlock.append(
            asm.jmp("jle", executionBlock.getBeginLabel())
        );
    }

    @Override
    public List<String> getCodeList() {
        List<String> codeList = new ArrayList<>();
        codeList.addAll(initBlock.getCodeList());
        codeList.addAll(executionBlock.getCodeList());
        codeList.addAll(conditionBlock.getCodeList());
        return null;
    }
}
