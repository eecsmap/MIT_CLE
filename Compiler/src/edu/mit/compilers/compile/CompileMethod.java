package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.syntax.Program;

public class CompileMethod {
    public static final void declare(ST localST, String name, String returnType, List<Descriptor> paramsDesc, List<String> codesMethod, List<String> codes) {
        if (!Program.shouldCompile()) {
            return;
        }
        codes.addAll(asm.methodDeclStart(name, paramsDesc, localST.bytesToAllocate()));
        codes.addAll(codesMethod);
        codes.addAll(asm.methodDeclEnd(localST.getReturnLabel(), Defs.DESC_TYPE_VOID.equals(returnType)));
    }

    public static final void callParseArgs(ST st, List<Oprand> argsList, List<String> codes) {
        if (!Program.shouldCompile()) {
            return;
        }
        Oprand arg = st.tmpPop();
        if (arg instanceof Reg) {
            Addr tmp = st.newTmpAddr();
            codes.add(
                asm.bin("movq", arg, tmp)
            );
            argsList.add(tmp);
        } else {
            argsList.add(arg);
        }
    }

    public static final void call(ST st, List<Oprand> argsList, Boolean needReturnValue, String methodName, List<String> codes) {
        if (!Program.shouldCompile()) {
            return;
        }
        List<Addr> addrs = new ArrayList<>();
        if (needReturnValue) {
            Reg res = st.newTmpReg();
            codes.addAll(asm.saveRegs(st, addrs));
            codes.addAll(asm.methodCall(methodName, argsList));
            codes.add(
                asm.bin("movq", Reg.rax, res)
            );
            codes.addAll(asm.recoverRegs(st, addrs));
            st.tmpPush(res);
        } else {
            codes.addAll(asm.saveRegs(st, addrs));
            codes.addAll(asm.methodCall(methodName, argsList));
            codes.addAll(asm.recoverRegs(st, addrs));
        }
        argsList.clear();
    }
}
