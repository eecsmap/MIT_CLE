package edu.mit.compilers.st;

import edu.mit.compilers.asm.basic.Addr;
import edu.mit.compilers.defs.VarType;

public abstract class Descriptor {
    private VarType type;
    private String text;
    private Addr addr;

    Descriptor(VarType type, String text) {
        this.type = type;
        this.text = text;
    }
    public VarType getType() { return this.type; }
    public String getText() { return this.text; }
    public void setAddr(Addr addr) { this.addr = addr; }
    public Addr getAddr() { return this.addr; }
}
