package edu.mit.compilers.compile;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ArrayDesc;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.syntax.Program;

public class CompileElement {
    public static void arrayElement(ActionType action, Descriptor desc, List<String> codes) {
        if (!Program.shouldCompile()) return;
        String varName = String.format("%s[]", desc.getText());
        Oprand index = Manager.tmpPop();
        Reg resReg = Manager.newTmpReg();
        Reg indexReg = Manager.newTmpReg(resReg);
        Integer offset = desc.getAddr().getOffset();
        Collections.addAll(codes,
            asm.bin("movq", index, indexReg),
            asm.bin("cmpq", new Num(((ArrayDesc)desc).getCap()), indexReg),
            asm.jmp("jge", Defs.EXIT_ARRAY_OUTBOUND_LABEL),
            asm.bin("cmpq", new Num(0L), indexReg),
            asm.jmp("jl", Defs.EXIT_ARRAY_OUTBOUND_LABEL)
        );
        if (desc.getAddr().isGlobal()) {
            Collections.addAll(codes,
                asm.bin("leaq", new Addr(indexReg, varName), indexReg),
                asm.bin("leaq", desc.getAddr(), resReg)
            );
            if (action == ActionType.STORE) {
                Manager.tmpPush(new Addr(indexReg, resReg));
            } else {
                codes.add(
                    asm.bin("movq", new Addr(indexReg, resReg), resReg)
                );
                Manager.tmpPush(resReg);
            }
        } else {
            if (action == ActionType.STORE) {
                Manager.tmpPush(new Addr(offset, indexReg, varName));
            } else {
                codes.add(
                    asm.bin("movq", new Addr(offset, indexReg, varName), resReg)
                );
                Manager.tmpPush(resReg);
            }
        }
    }
}
