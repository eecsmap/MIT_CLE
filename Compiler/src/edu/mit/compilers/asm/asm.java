package edu.mit.compilers.asm;
import edu.mit.compilers.st.Descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Instructions
public class asm {
    private static boolean isFirstGlobalVariable = true;
    private static final Map<Integer, Reg> argRegMap = new HashMap<>(){{
        put(0, Reg.rdi);
        put(1, Reg.rsi);
        put(2, Reg.rdx);
        put(3, Reg.rcx);
        put(4, Reg.r8d);
        put(5, Reg.r9d);
    }};

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
        return String.format("\t%s\t%s, %s\t%s", instruction, src, dst, cmt(src.getName(), dst.getName()));
    }

    public static final String uni(String instruction, Oprand dst) {
        return String.format("\t%s\t%s\t%s", instruction, dst, cmt(dst.getName()));
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
        return lableStr;
    }
    
    public static final String jmp(String instruction, Label dst) {
        return String.format("\t%s\t%s", instruction, dst);
    }

    public static final String run(String instruction) {
        return instruction;
    }

    public static final String cmt(String... comments) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t#");
        for (String comment: comments) {
            sb.append(" " + comment);
        }
        return sb.toString();
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
            label(func + ":"),
            uniDir(".zero", sizeStr)
        );
        if (isFirstGlobalVariable) {
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
        Collections.addAll(codes,
            uniDir(".globl", name),
            binDir(".type", name, "@function"),
            label(name + ":"),
            uni("pushq", Reg.rbp),
            bin("movq", Reg.rsp, Reg.rbp),
            bin("subq", new Num(Long.valueOf(bytesToAllocate)), Reg.rsp)
        );
        // move arguments to memory
        for (int i = 0; i < argsDesc.size(); i++) {
            codes.add(
                bin("movq", argRegMap.get(i), argsDesc.get(i).getAddr())
            );
        }
        return codes;
    }

    public static final List<String> methodDeclEnd() {
        List<String> codes = new ArrayList<>();
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
            String instruction;
            Oprand oprand = argsList.get(i);
            if (argsCount - i - 1 < 6) {
                String op;
                if (oprand instanceof Addr) {
                    op = ((Addr)oprand).isStringLiteral() ? "leaq" : "movq";
                } else {
                    op = "movq";
                }
                instruction = bin(op, oprand, argRegMap.get(i));
            } else {
                instruction = uni("pushq", oprand);
            }
            codes.add(instruction);
        }
        codes.add(
            asm.uniDir("call", name)
        );
        return codes;
    }
}