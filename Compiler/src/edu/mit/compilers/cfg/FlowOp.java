package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.defs.Defs;

// return, continue, break;
public class FlowOp extends Operator {
    private List<String> codeList;

    public FlowOp(Defs.FlowOp op, Label label) {
        this.codeList = new ArrayList<>();
        if (op == Defs.FlowOp.CONTINUE) {
            Collections.addAll(codeList,
                asm.jmp("jmp", label)
            );
        } else if (op == Defs.FlowOp.BREAK) {
            Collections.addAll(codeList,
                asm.jmp("jmp", label)
            );
        }
    }

    // TODO: wrong
    public FlowOp(Defs.FlowOp op, Addr returnVar) {
        if (op == Defs.FlowOp.RETURN) {
            Collections.addAll(codeList,
                asm.bin("movl", returnVar, Reg.eax), 
                asm.run("ret")
            );
        }
    }

    public List<String> getCodeList() {
        return this.codeList;
    }
}
