package edu.mit.compilers.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Constants {
    private Constants() {};
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
}
