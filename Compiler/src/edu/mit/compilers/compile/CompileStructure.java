package edu.mit.compilers.compile;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.asm.Bool;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.syntax.Program;

public class CompileStructure {
    public static final void minusExpr(ST st, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = st.newTmpReg();
        Oprand op = st.tmpPop();
        if (op instanceof Num) {
            st.tmpPush(((Num)op).neg());
        } else {
            Collections.addAll(codes,
                asm.bin("movq", op, tmpReg),
                asm.uni("negq", tmpReg)
            );
            st.tmpPush(tmpReg);
        }
    }

    public static final void exclamExpr(ST st, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = st.newTmpReg();
        Oprand op = st.tmpPop();
        if (op instanceof Bool) {
            st.tmpPush(((Bool)op).exclam());
        } else {
            Collections.addAll(codes,
                asm.bin("cmp", new Bool(false), op),
                asm.uni("sete", tmpReg.bite()),
                asm.bin("movzbq", tmpReg.bite(), tmpReg)
            );
            st.tmpPush(tmpReg);
        }
    }

    public static final void charLiteral(ST st, char theChar, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = st.newTmpReg();
        int ascii = theChar;
        codes.add(
            asm.bin("movq", new Num(Long.valueOf(ascii)), tmpReg)
        );
        st.tmpPush(tmpReg);
    }

    public static final void tkBreak(ST st, List<String> codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.jmp("jmp", st.getBreakLabel())
        );
    }

    public static final void tkContinue(ST st, List<String> codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.jmp("jmp", st.getContinueLabel())
        );
    }

    public static final void tkReturn(ST st, String expectedReturnType, List<String> codes) {
        if (!Program.shouldCompile()) return;
        if (Defs.DESC_TYPE_VOID.equals(expectedReturnType)) {
            st.tmpPop();
            codes.add(
                asm.jmp("jmp", st.getReturnLabel())
            );
        } else {
            Oprand returnVar = st.tmpPop();
            Collections.addAll(codes, 
                asm.bin("movq", returnVar, Reg.rax),
                asm.jmp("jmp", st.getReturnLabel())
            );
        }
    }
}
