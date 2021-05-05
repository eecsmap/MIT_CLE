package edu.mit.compilers.compile;

import java.util.List;

import edu.mit.compilers.asm.AMethod;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.syntax.Program;

public class CompileProgram {
    public static void addROData(List<String> stringLiteralList, List<Label> stringLiteralLabelList, AMethod codes) {
        if (!Program.shouldCompile() || stringLiteralList.isEmpty()) return;
        AMethod rodata = new AMethod();
        rodata.add(
            asm.nonDir(".text"),
            asm.uniDir(".section", ".rodata")
        );
        for (int i = 0; i < stringLiteralList.size(); i++) {
            rodata.add(
                asm.label(stringLiteralLabelList.get(i)),
                asm.uniDir(".string", stringLiteralList.get(i))
            );
        }
        codes.addLeftAll(rodata);
    }

    public static void addArrayOutBoundReturn(AMethod codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.label(Defs.EXIT_ARRAY_OUTBOUND_LABEL),
            asm.bin("movq", new Num(1L), Reg.rax),
            asm.bin("movq", new Num(255L), Reg.rbx),
            asm.uni("int", new Num(0x80L))
        );
    }
}
