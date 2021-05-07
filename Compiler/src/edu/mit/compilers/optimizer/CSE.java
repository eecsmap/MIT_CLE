package edu.mit.compilers.optimizer;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.cfg.CBlock;
import edu.mit.compilers.cfg.CMethod;

public class CSE {
    private CSE() {}

    // global common subexpression elimination
    public static void commonSubexpressionElimination(CMethod method) {
        // 1. get available expressions
        // 2. run gcse on each method
        // 3. CBlock should provide a list for saving commands
        // 4. when elimination done, should delete/add backwards
        // initialization
        List<CBlock> blocks = method.getBlocks();
        List<List<Expr>> AEin = new ArrayList<>();
        List<List<Expr>> AEout = new ArrayList<>();
        List<List<Expr>> Eval = new ArrayList<>();
        List<List<Expr>> Kill = new ArrayList<>();
        int N = blocks.size();
        while (AEin.size() < N) {
            AEin.add(new ArrayList<>());
            AEout.add(new ArrayList<>());
            Eval.add(new ArrayList<>());
            Kill.add(new ArrayList<>());
        }
        // fix point algo
        boolean changed = true;
        while (changed) {
            changed = false;
        }
    }
}
