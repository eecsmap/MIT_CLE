package edu.mit.compilers.cfg;

import java.util.List;

import edu.mit.compilers.asm.ALine;

public class CBlock {
    private List<CBlock> pred;
    private List<CBlock> succ;
    private List<ALine> aLines;

    void addPred(CBlock pred) {
        this.pred.add(pred);
    }

    void addSucc(CBlock succ) {
        this.succ.add(succ);
    }

    public CBlock(List<ALine> aLines) {
        this.aLines = aLines;
    }

    public List<ALine> getALines() {
        return this.aLines;
    }
}
