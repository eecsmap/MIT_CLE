package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.syntax.Program;

public class CompileProgram {
    public static void addROData(List<String> stringLiteralList, List<Label> stringLiteralLabelList, List<String> codes) {
        if (!Program.shouldCompile() || !stringLiteralList.isEmpty()) return;
        List<String> rodata = new ArrayList<>();
        Collections.addAll(rodata,
            asm.nonDir(".text"),
            asm.uniDir(".section", ".rodata")
        );
        for (int i = 0; i < stringLiteralList.size(); i++) {
            Collections.addAll(rodata,
                asm.label(stringLiteralLabelList.get(i)),
                asm.uniDir(".string", stringLiteralList.get(i))
            );
        }
        codes.addAll(0, rodata);
    }

    public static void addArrayOutBoundReturn(List<String> codes) {
        if (!Program.shouldCompile()) return;
        Collections.addAll(codes,
            asm.label(Defs.EXIT_ARRAY_OUTBOUND_LABEL),
            asm.bin("movq", new Num(1L), Reg.rax),
            asm.bin("movq", new Num(255L), Reg.rbx),
            asm.uni("int", new Num(0x80L))
        );
    }
}
