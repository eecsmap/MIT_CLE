package edu.mit.compilers.cfg;

import java.util.List;

import edu.mit.compilers.asm.ALine;

public class CBlock {
    List<CBlock> pred;
    List<CBlock> succ;
    List<ALine> aLines;

    void addPred(CBlock pred) {
        this.pred.add(pred);
    }

    void addSucc(CBlock succ) {
        this.succ.add(succ);
    }

    public CBlock(List<ALine> aLines) {
        this.aLines = aLines;
    }
}
