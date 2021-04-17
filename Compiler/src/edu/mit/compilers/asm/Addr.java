package edu.mit.compilers.asm;

public class Addr extends Oprand {
    private Boolean isVarGlobal;
    private String name;
    private Integer offset;


    public Addr(String name) {
        this.isVarGlobal = true;
        this.name = name;
    }

    public Addr(Integer offset) {
        this.isVarGlobal = false;
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
