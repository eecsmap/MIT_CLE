package edu.mit.compilers.asm;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.ST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Instructions
public class asm {
    private static boolean isFirstGlobalVariable = true;
    private static boolean isFirstFunction = true;

    private static final Integer calculateAlign(Integer size) {
        int align = 4;
        while (align < size && align < 32) {
            align <<= 1;
        }
        return align;
    }

    public static final String non(String instruction) {
        return String.format("\t%s", instruction);
    }

    public static final String bin(String instruction, Oprand src, Oprand dst) {
        String srcName = src.getName();
        String dstName = dst.getName();
        if (!srcName.isEmpty() || !dstName.isEmpty()) {
            return String.format("\t%s\t%s, %s %s", instruction, src, dst, cmt(srcName, dstName));
        } else {
            return String.format("\t%s\t%s, %s", instruction, src, dst);
        }   
    }

    public static final String uni(String instruction, Oprand dst) {
        String dstName = dst.getName();
        if (!dstName.isEmpty()) {
            return String.format("\t%s\t%s %s", instruction, dst, cmt(dst.getName()));
        } else {
            return String.format("\t%s\t%s", instruction, dst);
        }
    }

    public static final String nonDir(String directive) {
        return String.format("\t%s", directive);
    }

    public static final String binDir(String directive, String fst, String scd) {
        return String.format("\t%s\t%s, %s", directive, fst, scd);
    }

    public static final String uniDir(String directive, String fst) {
        return String.format("\t%s\t%s", directive, fst);
    }

    public static final String label(Label lable) {
        return lable.toString() + ":";
    }

    public static final String label(String lableStr) {
        return lableStr + ":";
    }
    
    public static final String jmp(String instruction, Label dst) {
        return String.format("\t%s\t%s", instruction, dst);
    }

    public static final String run(String instruction) {
        return instruction;
    }

    public static final String cmt(String... comments) {
        return "\t# " + String.join(", ", comments);
    }

    public static final List<String> globalDecl(String func, Integer size) {
        List<String> codes = new ArrayList<>();
        String sizeStr = Integer.toString(size);
        String alignStr = Integer.toString(calculateAlign(size));
        Collections.addAll(codes,
            uniDir(".globl", func),
            uniDir(".align", alignStr),
            binDir(".type", func, "@object"),
            binDir(".size", func, sizeStr),
            label(func),
            uniDir(".zero", sizeStr)
        );
        if (isFirstGlobalVariable) {
            codes.add(0,
                nonDir(".data")
            );
            codes.add(1,
                nonDir(".bss")
            );
            isFirstGlobalVariable = false;
        }
        return codes;
    }

    public static final List<String> methodDeclStart(String name, List<Descriptor> argsDesc, Integer bytesToAllocate) {
        // call stack initialization
        List<String> codes = new ArrayList<>();
        if (isFirstFunction) {
            codes.add(
                nonDir(".text")
            );
            isFirstFunction = false;
        }
        Collections.addAll(codes,
            uniDir(".globl", name),
            binDir(".type", name, "@function"),
            label(name),
            uni("pushq", Reg.rbp),
            bin("movq", Reg.rsp, Reg.rbp),
            bin("subq", new Num(Long.valueOf(bytesToAllocate)), Reg.rsp)
        );
        // initialize the stack
        for (int i = 8; i < bytesToAllocate; i += 8) {
            codes.add(
                bin("movq", new Num(0L), new Addr(-i, "init stack"))
            );
        }
        // move arguments to memory
        for (int i = 0; i < Math.min(argsDesc.size(), Constants.argRegMap.size()) ; i++) {
            codes.add(
                bin("movq", Constants.argRegMap.get(i), argsDesc.get(i).getAddr())
            );
        }
        return codes;
    }

    public static final List<String> methodDeclEnd(Label returnLabel, boolean returnIsVoid) {
        List<String> codes = new ArrayList<>();
        if (!returnIsVoid)
        Collections.addAll(codes,
            asm.bin("movq", new Num(1L), Reg.rax),
            asm.bin("movq", new Num(254L), Reg.rbx),
            asm.uni("int", new Num(0x80L))
        );
        codes.add(
            label(returnLabel)
        );
        Collections.addAll(codes,
            non("leave"),
            non("ret")
        );
        return codes;
    }

    public static final List<String> methodCall(String name, List<Oprand> argsList) {
        List<String> codes = new ArrayList<>();
        Integer argsCount = argsList.size();
        for (int i = 0; i < argsCount; i++) {
            Oprand oprand = argsList.get(i);
            if (i < 6) {
                String op;
                if (oprand instanceof Addr) {
                    op = ((Addr)oprand).isStringLiteral() ? "leaq" : "movq";
                } else {
                    op = "movq";
                }
                codes.add(
                    bin(op, oprand, Constants.argRegMap.get(i))
                );
            } else {
                codes.add(6,
                    uni("pushq", oprand)
                );
            }
        }
        Collections.addAll(codes,
            bin("movq", new Num(0L), Reg.rax),
            uniDir("call", name)
        );
        return codes;
    }

    public static final List<String> saveRegs(ST st, List<Addr> addrs) {
        List<String> codes = new ArrayList<>();
        List<Reg> regsToSave = st.getUsedCalleeSavedRegs();
        for (Reg reg: regsToSave) {
            Addr addr = st.newTmpAddr();
            codes.add(
                asm.bin("movq", reg, addr)
            );
            addrs.add(addr);
        }
        return codes;
    }

    public static final List<String> recoverRegs(ST st, List<Addr> addrs) {
        List<String> codes = new ArrayList<>();
        List<Reg> regsToRecover = st.getUsedCalleeSavedRegs();
        int i = 0;
        for (Reg reg: regsToRecover) {
            codes.add(
                asm.bin("movq", addrs.get(i), reg)
            );
            i++;
        }
        return codes;
    }
}