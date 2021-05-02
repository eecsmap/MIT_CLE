package edu.mit.compilers.asm;

import java.util.ArrayList;
import java.util.List;

public class RegAddr extends Oprand {
    private String str;
    private String varName = "";
    private List<Reg> reservedRegs = new ArrayList<>();

    // local array
    public RegAddr(Integer offset, Reg index, String varName) {
        this.str = String.format("%d(%%rbp, %s, 8)", offset, index);
        this.varName = varName;
        this.reservedRegs.add(index);
    }

    // global array index
    public RegAddr(Reg index, String varName) {
        this.str = String.format("0(, %s, 8)", index);
        this.varName = varName;
        this.reservedRegs.add(index);
    }

    // regA + regB
    public RegAddr(Reg regA, Reg regB) {
        this.str = String.format("(%s, %s)", regA, regB);
        this.reservedRegs.add(regA);
        this.reservedRegs.add(regB);
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
