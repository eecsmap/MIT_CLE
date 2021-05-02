package edu.mit.compilers.asm;

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

    public ALine get(int i) {
        return aLines.get(i);
    }

    public int size() {
        return aLines.size();
    }
}
