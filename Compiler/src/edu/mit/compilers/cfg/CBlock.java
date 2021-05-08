package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.ALine;

public class CBlock {
    List<CBlock> pred = new ArrayList<>();
    List<CBlock> succ = new ArrayList<>();
    List<ALine> aLines;

    void addPred(CBlock pred) {
        this.pred.add(pred);
    }

    void addSucc(CBlock succ) {
        this.succ.add(succ);
    }

    public List<CBlock> getPred() {
        return this.pred;
    }

    public List<CBlock> getSucc() {
        return this.succ;
    }

    public CBlock(List<ALine> aLines) {
        this.aLines = aLines;
    }
}
