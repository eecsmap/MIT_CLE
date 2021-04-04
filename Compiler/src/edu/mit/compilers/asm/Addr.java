package edu.mit.compilers.asm;

public class Addr extends Oprand {
    private Boolean isVarGlobal;
    private String name;
    private Integer offset;


    Addr(Boolean isGlobal, String name) {
        this.isVarGlobal = isGlobal;
        this.name = name;
    }

    Addr(Boolean isGlobal, Integer offset) {
        this.isVarGlobal = isGlobal;
        this.offset = offset;
    }

    @Override
    public String toString() {
        if (this.isVarGlobal) {
            return String.format("%s(%rip)", this.name);
        }
        return String.format("%d(%rbp)", this.offset);
    }
}
