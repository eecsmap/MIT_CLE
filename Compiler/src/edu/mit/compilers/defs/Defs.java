package edu.mit.compilers.defs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.grammar.DecafParserTokenTypes;
import edu.mit.compilers.tools.CLI;

public class Defs {
    private Defs() {}

    // didnt differentiate variable size between int and bool
    public static Integer varSize = 8;
    public static Integer callRegCount = 6;

    public static String regSaveStart = "save - start";
    public static String regRecoverEnd = "recover - end";

    public static Boolean isAnyOptimizationEnabled() {
        Boolean enabled = false;
        for (int i = 0; i < CLI.opts.length; i++) {
            enabled = enabled || CLI.opts[i];
        }
        return enabled;
    }

    public static Boolean isGCSEEnabled() {
        return CLI.opts[0];
    }

    public static Map<String, VarType> stringToVarType = new HashMap<>(){{
        put("void", VarType.VOID);
        put("bool", VarType.BOOL);
        put("int", VarType.INT);
    }};

    public static final Label EXIT_ARRAY_OUTBOUND_LABEL = new Label(".ExitArrayOutBound");

    public static enum ActionType {
        LOAD,
        STORE
    }

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
