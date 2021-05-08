package edu.mit.compilers.optimizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.mit.compilers.cfg.CBlock;
import edu.mit.compilers.cfg.CMethod;

public class CSE {
    private static EBlock fullSet;
    private CSE() {}

    public static EBlock evalGen(CBlock codes) {
        // TODO
        return null;
    }

    public static EBlock killGen(CBlock codes) {
        // TODO
        return null;
    }

    // global common subexpression elimination
    public static void commonSubexpressionElimination(CMethod method) {
        // 1. get available expressions
        // 2. run gcse on each method
        // 3. CBlock should provide a list for saving commands
        // 4. when elimination done, should delete/add backwards
        // initialization
        fullSet = new EBlock();
        List<CBlock> blocks = method.getBlocks();
        List<EBlock> AEin = new ArrayList<>();
        List<EBlock> AEout = new ArrayList<>();
        List<EBlock> Eval = new ArrayList<>();
        List<EBlock> Kill = new ArrayList<>();
        Set<Integer> changed = new HashSet<>();
        int N = blocks.size();
        for (int i = 0; i < N; i++) {
            AEin.add(new EBlock());
            AEout.add(new EBlock());
            Eval.add(evalGen(blocks.get(0)));
            Kill.add(killGen(blocks.get(0)));
            changed.add(i);
        }
        changed.remove(0);
        // fix point algo
        while (!changed.isEmpty()) {
            for (Iterator<Integer> iter = changed.iterator(); iter.hasNext();) {
                int i = iter.next();
                iter.remove();

                AEin.set(i, fullSet);
                for (Integer p: blocks.get(i).getPred()) {
                    AEin.get(i).intersect(AEout.get(p));
                }

                EBlock newAEout = new EBlock(AEin.get(i));
                newAEout.subtract(Kill.get(i));
                newAEout.union(Eval.get(i));

                if (!newAEout.equals(AEout.get(i))) {
                    AEout.set(i, newAEout);
                    for (Integer s: blocks.get(i).getSucc()) {
                        changed.add(s);
                    }
                }
            }
        }
    }
}
