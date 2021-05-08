package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.ALine;

public class CBlock {
    List<Integer> pred = new ArrayList<>();
    List<Integer> succ = new ArrayList<>();
    List<ALine> aLines;

    void addPred(Integer pred) {
        this.pred.add(pred);
    }

    void addSucc(Integer succ) {
        this.succ.add(succ);
    }

    public List<Integer> getPred() {
        return this.pred;
    }

    public List<Integer> getSucc() {
        return this.succ;
    }

    public CBlock(List<ALine> aLines) {
        this.aLines = aLines;
    }
}
