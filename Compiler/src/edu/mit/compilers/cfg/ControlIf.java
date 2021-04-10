package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ir.IrOp;
import edu.mit.compilers.st.ST;

public class ControlIf implements Control {
    Block conditionBlock;
    Block executionBlock;

    public ControlIf(List<IrOp> condition, List<IrOp> execution, ST st) {
        conditionBlock = new Block(condition);
        executionBlock = new Block(execution);

        conditionBlock.append(
            asm.jmp("je", executionBlock.getEndLabel())
        );
    }

    @Override
    public List<String> getCodeList() {
        List<String> codeList = new ArrayList<>();
        codeList.addAll(conditionBlock.getCodeList());
        codeList.addAll(executionBlock.getCodeList());
        return codeList;
    }
}
