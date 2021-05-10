package edu.mit.compilers.asm;

import edu.mit.compilers.asm.basic.Label;

public class ALabelLine extends ALine {
    private Label label;

    public ALabelLine(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return this.label;
    }

    @Override
    public String toString() {
        return this.label.toString() + ":";
    }
}
