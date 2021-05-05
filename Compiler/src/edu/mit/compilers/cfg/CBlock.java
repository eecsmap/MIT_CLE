package edu.mit.compilers.cfg;

import java.util.List;

import edu.mit.compilers.asm.ALine;

public class CBlock {
    private List<CBlock> pred;
    private List<CBlock> succ;
    private List<ALine> aLines;

    public CBlock(List<ALine> aLines) {
        this.aLines = aLines;
    }
}
