package edu.mit.compilers.asm;

import edu.mit.compilers.asm.basic.Addr;
import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.Manager;

import java.util.List;

// Instructions
public class asm {
    private static boolean isFirstGlobalVariable = true;
    private static boolean isFirstFunction = true;
    public static boolean isRAXused = false;

    private static final Integer calculateAlign(Integer size) {
        int align = 4;
        while (align < size && align < 32) {
            align <<= 1;
        }
        return align;
    }

    public static final AInstLine non(String inst) {
        return new AInstLine(inst);
    }

    public static final AInstLine bin(String inst, Oprand src, Oprand dst) {
        String srcName = src.getName();
        String dstName = dst.getName();
        AInstLine cmd = new AInstLine(inst, src, dst);
        if (!srcName.isEmpty() || !dstName.isEmpty()) {
            cmd.addComment(srcName, dstName);
        }
        return cmd;
    }

    public static final AInstLine uni(String inst, Oprand dst) {
        String dstName = dst.getName();
        AInstLine cmd = new AInstLine(inst, dst);
        if (!dstName.isEmpty()) {
            cmd.addComment(dst.getName());
        }
        return cmd;
    }

    public static final AJmpLine jmp(String inst, Label dst) {
        return new AJmpLine(inst, dst);
    }

    public static final AStrLine nonDir(String directive) {
        return new AStrLine(String.format("\t%s", directive));
    }

    public static final AStrLine binDir(String directive, String fst, String scd) {
        return new AStrLine(String.format("\t%s\t%s, %s", directive, fst, scd));
    }

    public static final AStrLine uniDir(String directive, String fst) {
        return new AStrLine(String.format("\t%s\t%s", directive, fst));
    }

    public static final ALabelLine label(Label lable) {
        return new ALabelLine(lable);
    }

    public static final AStrLine cmt(String... comments) {
        if (comments.length == 1 && comments[0].equals(""))
            return new AStrLine("");
        return new AStrLine("\t# " + String.join(", ", comments));
    }

    public static final AMethod globalDecl(String func, Integer size) {
        AMethod codes = new AMethod();
        String sizeStr = Integer.toString(size);
        String alignStr = Integer.toString(calculateAlign(size));
        if (isFirstGlobalVariable) {
            codes.add(
                nonDir(".data"),
                nonDir(".bss")
            );
            isFirstGlobalVariable = false;
        }
        codes.add(
            uniDir(".globl", func),
            uniDir(".align", alignStr),
            binDir(".type", func, "@object"),
            binDir(".size", func, sizeStr),
            label(new Label(func)),
            uniDir(".zero", sizeStr)
        );

        return codes;
    }

    public static final AMethod methodDeclStart(String name, List<Descriptor> argsDesc, Integer bytesToAllocate) {
        // call stack initialization
        AMethod codes = new AMethod();
        if (isFirstFunction) {
            codes.add(
                nonDir(".text")
            );
            isFirstFunction = false;
        }
        codes.add(
            uniDir(".globl", name),
            binDir(".type", name, "@function"),
            label(new Label(name)),
            uni("pushq", Reg.rbp),
            bin("movq", Reg.rsp, Reg.rbp),
            bin("subq", new Num(Long.valueOf(bytesToAllocate)), Reg.rsp)
        );
        // move arguments to memory
        for (int i = 0; i < Math.min(argsDesc.size(), Defs.argRegMap.size()) ; i++) {
            codes.add(
                bin("movq", Defs.argRegMap.get(i), argsDesc.get(i).getAddr())
            );
        }
        return codes;
    }

    public static final AMethod methodDeclEnd(Label returnLabel, boolean returnIsVoid) {
        AMethod codes = new AMethod();
        if (!returnIsVoid)
        codes.add(
            asm.bin("movq", new Num(1L), Reg.rax),
            asm.bin("movq", new Num(254L), Reg.rbx),
            asm.uni("int", new Num(0x80L))
        );
        codes.add(
            label(returnLabel)
        );
        codes.add(
            non("leave"),
            non("ret")
        );
        return codes;
    }

    public static final AMethod methodCall(String name, List<Oprand> argsList) {
        AMethod codes = new AMethod();
        Integer argsCount = argsList.size();
        for (int i = argsCount - 1; i >= 0; i--) {
            Oprand oprand = argsList.get(i);
            if (i < 6) {
                String op;
                op = (oprand instanceof Addr && ((Addr)oprand).isStringLiteral()) ?  "leaq" : "movq";
                codes.add(
                    bin(op, oprand, Defs.argRegMap.get(i))
                );
            } else {
                codes.add(
                    uni("pushq", oprand)
                );
            }
        }
        codes.add(
            bin("movq", new Num(0L), Reg.rax),
            uniDir("call", name)
        );
        return codes;
    }

    public static final AMethod saveRegs(List<Addr> addrs) {
        AMethod codes = new AMethod();
        List<Reg> regsToSave = Manager.getUsedCallerSavedRegs();
        isRAXused = false;
        if (regsToSave.size() > 0) {
            codes.add(
                asm.cmt("save - start")
            );
        }
        for (Reg reg: regsToSave) {
            Addr addr = Manager.newTmpAddr();
            codes.add(
                asm.bin("movq", reg, addr)
            );
            addrs.add(addr);
            if (reg.getRegName().equals("rax")) {
                isRAXused = true;
            }
        }
        if (regsToSave.size() > 0) {
            codes.add(
                asm.cmt("save - end")
            );
        }
        return codes;
    }

    public static final AMethod recoverRegs(List<Addr> addrs) {
        AMethod codes = new AMethod();
        List<Reg> regsToRecover = Manager.getUsedCallerSavedRegs();
        if (regsToRecover.size() > 0) {
            codes.add(
                asm.cmt("recover - start")
            );
        }
        int i = 0;
        for (Reg reg: regsToRecover) {
            codes.add(
                asm.bin("movq", addrs.get(i), reg)
            );
            i++;
        }
        if (regsToRecover.size() > 0) {
            codes.add(
                asm.cmt("recover - end")
            );
        }
        return codes;
    }
}