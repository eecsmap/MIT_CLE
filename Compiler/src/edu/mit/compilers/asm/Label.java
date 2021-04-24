package edu.mit.compilers.asm;

public class Label {
    private static int globalConstNumber = 0;
    private static int globalNumber = 0;
    private final String name;

    public Label() {
        Integer number = globalNumber++;
        this.name = String.format(".L%d", number);
    }

    public Label(Boolean isConst) {
        if (!isConst) {
            Integer number = globalNumber++;
            this.name = String.format(".L%d", number);
            return;
        }
        Integer number = globalConstNumber++;
        this.name = String.format(".LC%d", number);
    }

    public String toString() {
        return this.name;
    }
}
