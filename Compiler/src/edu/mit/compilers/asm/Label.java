package edu.mit.compilers.asm;

public class Label {
    private static int globalNumber = 0;
    private final int number;

    public Label() {
        this.number = globalNumber++;
    }

    public String toString() {
        return String.format(".L%d", this.number);
    }
}
