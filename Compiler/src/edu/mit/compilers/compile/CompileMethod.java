package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.basic.Addr;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.syntax.Program;

public class CompileMethod {
    public static final void declare(String name, VarType returnType, List<Descriptor> paramsDesc, ABlock codesMethod, ABlock codes) {
        if (!Program.shouldCompile()) return;
        codes.addAll(asm.methodDeclStart(name, paramsDesc, Manager.bytesToAllocate()));
        codes.addAll(codesMethod);
        codes.addAll(asm.methodDeclEnd(Manager.getReturnLabel(), returnType.isVoid()));
    }

    public static final void callParseArgs(List<Oprand> argsList, ABlock codes) {
        if (!Program.shouldCompile()) return;
        Oprand arg = Manager.tmpPop();
        if (arg instanceof Reg) {
            Addr tmp = Manager.newTmpAddr();
            codes.add(
                asm.bin("movq", arg, tmp)
            );
            argsList.add(tmp);
        } else {
            argsList.add(arg);
        }
    }

    public static final void call(List<Oprand> argsList, Boolean needReturnValue, String methodName, ABlock codes) {
        if (!Program.shouldCompile()) return;
        List<Addr> addrs = new ArrayList<>();
        if (needReturnValue) {
            Reg res = Manager.newTmpReg();
            codes.addAll(asm.saveRegs(addrs));
            codes.addAll(asm.methodCall(methodName, argsList));
            if (asm.isRAXused) {
                codes.add(
                    // removed methodName here
                    asm.bin("movq", new Reg(Reg.rax, ""), res)
                );
            } else {
                // removed methodName here
                res = new Reg(Reg.rax, "");
            }
            codes.addAll(asm.recoverRegs(addrs));
            Manager.tmpPush(res);
        } else {
            codes.addAll(asm.saveRegs(addrs));
            codes.addAll(asm.methodCall(methodName, argsList));
            codes.addAll(asm.recoverRegs(addrs));
        }
        argsList.clear();
    }
}
