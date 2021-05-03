package edu.mit.compilers.asm;

import edu.mit.compilers.asm.basic.Label;

public class AJmpLine extends ALine {
    private String inst;
    private Label label;

    public AJmpLine(String inst, Label label) {
        this.inst = inst;
        this.label = label;
    }

    @Override
    public String toString() {
        return "\t" + this.inst + "\t" + this.label;
    }
    
}
