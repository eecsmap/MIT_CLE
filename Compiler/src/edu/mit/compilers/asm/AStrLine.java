package edu.mit.compilers.asm;

// comments && directives
public class AStrLine extends ALine {
    private String string;

    public AStrLine(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
