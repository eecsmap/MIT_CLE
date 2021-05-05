package edu.mit.compilers.asm;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.cfg.CMethod;

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

    public void addLeftAll(ABlock rhs) {
        this.aLines.addAll(0, rhs.aLines);
    }

    public ALine get(int i) {
        return aLines.get(i);
    }

    public int size() {
        return aLines.size();
    }

    public void print(PrintStream stream) {
        this.aLines.forEach(stream::println);
    }
}
