package edu.mit.compilers.compile;

import edu.mit.compilers.asm.AMethod;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.basic.Addr;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.syntax.Program;

public class CompileField {
    public static final void declareArray(String name, Integer cap, AMethod codes) {
        if (!Program.shouldCompile()) return;
        if (Manager.isGlobal()) {
            codes.addAll(
                asm.globalDecl(name, Defs.varSize * cap)
            );
        } else {
            Integer topOffset = Manager.getDesc(name).getAddr().getOffset();
            for (int i = 0; i < cap; i++) {
                codes.add(
                    asm.bin("movq", new Num(0L), new Addr(topOffset + 8 * i, "array init"))
                );
            }
        }
    }

    public static final void declareVariable(String name, AMethod codes) {
        if (!Program.shouldCompile()) return;
        if (Manager.isGlobal()) {
            codes.addAll(
                asm.globalDecl(name, Defs.varSize)
            );
        } else {
            codes.add(
                asm.bin("movq", new Num(0L), Manager.getDesc(name).getAddr())
            );
        }
    }
}
