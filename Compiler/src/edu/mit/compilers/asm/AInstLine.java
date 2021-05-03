package edu.mit.compilers.asm;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.basic.Oprand;

public class AInstLine extends ALine {
    private String inst = "";
    private List<Oprand> oprands = new ArrayList<>();
    private String[] cmt = new String[0];

    public AInstLine(String inst, Oprand... oprands) {
        this.inst = inst;
        for (int i = 0; i < oprands.length; i++)
            this.oprands.add(oprands[i]);
    }

    public void addComment(String... cmt) {
        this.cmt = cmt;
    }

    public String getInst() {
        return this.inst;
    }

    public Oprand getLeft() {
        return this.oprands.get(0);
    }

    public Oprand getRight() {
        return this.oprands.get(1);
    }

    public Integer getOpCount() {
        return this.oprands.size();
    }
    
    @Override
    public String toString() {
        List<String> opStrings = new ArrayList<>();
        this.oprands.forEach(e -> opStrings.add(e.toString()));
        return "\t" + this.inst + "\t" + String.join(", ", opStrings) + "\t# " + String.join(", ", this.cmt);
    }
}
