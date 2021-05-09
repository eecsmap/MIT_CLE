package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import edu.mit.compilers.asm.ALine;
import edu.mit.compilers.optimizer.EBlock.ModifyAction;

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

    public List<ALine> getLines() {
        return this.aLines;
    }

    public void modify(TreeMap<Integer, ModifyAction> toModify) {
        for (Entry<Integer, ModifyAction> entry: toModify.entrySet()) {
            int lineNumber = entry.getKey();
            ModifyAction action = entry.getValue();
            if (action.equals(ModifyAction.SAVE)) {
                //
            } else if (action.equals(ModifyAction.DELETE)) {
                this.aLines.remove(lineNumber);
            } else if (action.equals(ModifyAction.REPLACE)) {
                int replaceLine = action.getLineNumber();
                //
            }
        }
    }
}
