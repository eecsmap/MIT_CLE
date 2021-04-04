package edu.mit.compilers.asm;

public class Label {
    private static int globalNumber = 0;
    private final int number;

    private Label(int number) {
        this.number = number;
    }

    public static Label init() {
        return new Label(globalNumber++);
    }

    public String toString() {
        return String.format(".L%d", this.number);
    }
}
