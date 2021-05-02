package edu.mit.compilers.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.grammar.DecafParserTokenTypes;


public class AsmConstants {
    private AsmConstants() {};
    public static final Map<Integer, Reg> argRegMap = new HashMap<>(){{
        put(0, Reg.rdi);
        put(1, Reg.rsi);
        put(2, Reg.rdx);
        put(3, Reg.rcx);
        put(4, Reg.r8);
        put(5, Reg.r9);
    }};
    public static final List<Reg> callerSavedReg = new ArrayList<>(){{
        add(Reg.rcx);
        add(Reg.r8);
        add(Reg.r9);
        add(Reg.r10);
        add(Reg.r11);
        add(Reg.rdx);
        add(Reg.rax);
    }};
    public static final List<Reg> calleeSavedReg = new ArrayList<>(){{
        add(Reg.rbx);
        add(Reg.rbp);
        add(Reg.rdi);
        add(Reg.rsi);
        add(Reg.rsp);
        add(Reg.r12);
        add(Reg.r13);
        add(Reg.r14);
        add(Reg.r15);
    }};

    public static Map<Integer, String> setOnCondition = new HashMap<>(){{
        put(DecafParserTokenTypes.GREATER, "setg");
        put(DecafParserTokenTypes.LESS, "setl");
        put(DecafParserTokenTypes.GE, "setge");
        put(DecafParserTokenTypes.LE, "setle");
        put(DecafParserTokenTypes.EQ, "sete");
        put(DecafParserTokenTypes.NEQ, "setne");
    }};  


    public static Map<Integer, String> binaryOpToken2Inst = new HashMap<>(){{
        put(DecafParserTokenTypes.MINUS, "subq");
        put(DecafParserTokenTypes.PLUS, "addq");
        put(DecafParserTokenTypes.TIMES, "imulq");
    }};
}
