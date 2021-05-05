package edu.mit.compilers.asm;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ABlock {
    private List<ALine> aLines = new ArrayList<>();

    public ABlock() {}

    public void add(ALine... lines) {
        for (int i = 0; i < lines.length; i++)
            this.aLines.add(lines[i]);
    }

    public void addAll(ABlock rhs) {
        this.aLines.addAll(rhs.aLines);
    }

    public void addAll(List<ALine> aLines) {
        this.aLines.addAll(aLines);
    }

    public void addLeftAll(ABlock rhs) {
        this.aLines.addAll(0, rhs.aLines);
    }

    public ALine get(int i) {
        return aLines.get(i);
    }

    public List<ALine> subLines(int start, int end) {
        return this.aLines.subList(start, end);
    }

    public int size() {
        return aLines.size();
    }

    public void print(PrintStream stream) {
        this.aLines.forEach(stream::println);
    }
}
