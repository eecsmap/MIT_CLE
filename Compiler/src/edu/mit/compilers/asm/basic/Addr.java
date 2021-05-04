package edu.mit.compilers.asm.basic;

public class Addr extends Oprand {
    private Boolean isVarGlobal;
    private String str;
    private Integer offset;
    private Boolean isStringLiteral = false;
    private String varName = "";

    // for global variable
    public Addr(String str, Boolean isStringLiteral) {
        this.isVarGlobal = true;
        this.str = String.format("%s(%%rip)", str);
        this.isStringLiteral = isStringLiteral;
        this.varName = str;
    }

    // local variable
    public Addr(Integer offset, String varName) {
        this.isVarGlobal = false;
        this.offset = offset;
        this.str = String.format("%d(%%rbp)", this.offset);
        this.varName = varName;
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
    
    @Override
    public String getName() {
        return this.varName;
    }
}
