package edu.mit.compilers.asm;

public class Addr extends Oprand {
    private Boolean isVarGlobal;
    private String str;
    private Integer offset;
    private Reg index;
    private Boolean isStringLiteral = false;

    // for global variable
    public Addr(String str, Boolean isStringLiteral) {
        this.isVarGlobal = true;
        this.str = String.format("%s(%%rip)", str);
        this.isStringLiteral = isStringLiteral;
    }

    public Addr(Integer offset) {
        this.isVarGlobal = false;
        this.offset = offset;
        this.str = String.format("%d(%%rbp)", this.offset);
    }

    public Addr(Integer offset, Reg index) {
        this.isVarGlobal = false;
        this.offset = offset;
        this.index = index;
        this.str = String.format("%d(%%rbp, %s, 4)", this.offset, this.index);
    }

    public Integer getOffset() {
        return this.offset;
    }

    public Boolean isGlobal() {
        return this.isVarGlobal;
    }

    public Boolean isStringLiteral() {
        return this.isStringLiteral;
    }

    @Override
    public String toString() {
        return this.str;
    }
}
