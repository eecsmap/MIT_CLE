package edu.mit.compilers.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Instructions
public class asm {
    private static Boolean isFirstGlobalVariable = true;

    private static final Integer calculateAlign(Integer size) {
        int align = 4;
        while (align < size && align < 32) {
            align <<= 1;
        }
        return align;
    }

    public static final String bin(String instruction, Oprand src, Oprand dst) {
        return String.format("\t%s\t%s, %s", instruction, src, dst);
    }

    public static final String uni(String instruction, Oprand dst) {
        return String.format("\t%s\t%s", instruction, dst);
    }

    public static final String label(Label lable) {
        return lable.toString();
    }
    
    public static final String jmp(String instruction, Label dst) {
        return String.format("\t%s\t%s", instruction, dst);
    }

    public static final String run(String instruction) {
        return instruction;
    }

    public static final List<String> globalDecl(String name, Integer size) {
        List<String> codes = new ArrayList<>();
        Collections.addAll(codes, 
            String.format("\t.globl\t%s", name),
            String.format("\t.align\t%d", calculateAlign(size)),
            String.format("\t.type\t%s, @object", name),
            String.format("\t.size\t%s, %d", name, size),
            String.format("%s:", name),
            String.format("\t.zero\t%d", size)
        );
        if (isFirstGlobalVariable) {
            codes.add(1, "\t.bss");
            isFirstGlobalVariable = false;
        }
        return codes;
    }

    // .LFB0:
	// pushq	%rbp	#
	// movq	%rsp, %rbp	#,
	// movl	%edi, -4(%rbp)	# a, a
    public static final List<String> methodDeclStart(String name) {
        List<String> codes = new ArrayList<>();
        Collections.addAll(codes,
            String.format("%s:", name),
            String.format("\tpushq\t%s", Reg.rbp),
            String.format("\tmovq\t%s, %s", Reg.rsp, Reg.rbp)
            // move arguments to memory
        );
        return codes;
    }

    // popq	%rbp	#
	// ret	
    public static final List<String> methodDeclEnd() {
        List<String> codes = new ArrayList<>();
        Collections.addAll(codes,
            String.format("\tpopq\t%s", Reg.rbp),
            "ret"
        );
        return codes;
    }
}