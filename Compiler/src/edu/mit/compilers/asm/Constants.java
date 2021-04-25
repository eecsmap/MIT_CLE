package edu.mit.compilers.asm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Constants {
    private Constants() {};
    static final Map<Integer, Reg> argRegMap = new HashMap<>(){{
        put(0, Reg.rdi);
        put(1, Reg.rsi);
        put(2, Reg.rdx);
        put(3, Reg.rcx);
        put(4, Reg.r8);
        put(5, Reg.r9);
    }};
    static final Set<Reg> callerSavedReg = new HashSet<>(){{
        add(Reg.rax);
        add(Reg.rcx);
        add(Reg.rdx);
        add(Reg.r8);
        add(Reg.r9);
        add(Reg.r10);
        add(Reg.r11);
    }};
    static final Set<Reg> calleeSavedReg = new HashSet<>(){{
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
