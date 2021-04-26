package edu.mit.compilers.asm;

import java.util.ArrayList;
import java.util.List;

public class Addr extends Oprand {
    private Boolean isVarGlobal;
    private String str;
    private Integer offset;
    private Reg index;
    private Boolean isStringLiteral = false;
    private String varName = "";
    private List<Reg> reservedRegs = new ArrayList<>();

    // for global variable
    public Addr(String str, Boolean isStringLiteral) {
        this.isVarGlobal = true;
        this.str = String.format("%s(%%rip)", str);
        this.isStringLiteral = isStringLiteral;
    }

    // local variable
    public Addr(Integer offset, String varName) {
        this.isVarGlobal = false;
        this.offset = offset;
        this.str = String.format("%d(%%rbp)", this.offset);
        this.varName = varName;
    }

    // local array
    public Addr(Integer offset, Reg index, String varName) {
        this.isVarGlobal = false;
        this.offset = offset;
        this.index = index;
        this.str = String.format("%d(%%rbp, %s, 8)", this.offset, this.index);
        this.varName = varName;
    }

    // global array index
    public Addr(Reg index, String varName) {
        this.isVarGlobal = false;
        this.index = index;
        this.str = String.format("0(, %s, 8)", this.index);
        this.varName = varName;
    }

    // regA + regB
    public Addr(Reg regA, Reg regB) {
        this.isVarGlobal = false;
        this.str = String.format("(%s, %s)", regA, regB);
        this.reservedRegs.add(regA);
        this.reservedRegs.add(regB);
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

    public List<Reg> getReservedRegs() {
        return this.reservedRegs;
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
