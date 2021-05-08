package edu.mit.compilers.optimizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.mit.compilers.cfg.CBlock;
import edu.mit.compilers.cfg.CMethod;

public class CSE {
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
        List<CBlock> blocks = method.getBlocks();
        List<EBlock> AEin = new ArrayList<>();
        List<EBlock> AEout = new ArrayList<>();
        List<EBlock> Eval = new ArrayList<>();
        List<EBlock> Kill = new ArrayList<>();
        int N = blocks.size();
        while (AEin.size() < N) {
            AEin.add(new EBlock());
            AEout.add(new EBlock());
            Eval.add(evalGen(blocks.get(0)));
            Kill.add(killGen(blocks.get(0)));
        }
        // fix point algo
        Set<EBlock> changed = new HashSet<>();
        while (!changed.isEmpty()) {
            for (Iterator<EBlock> i = changed.iterator(); i.hasNext();) {
                EBlock block = i.next();
                i.remove();
                /*
                for all nodes n in N
                    OUT[n] = E; // OUT[n] = E - KILL[n];
                IN[Entry] = emptyset;
                OUT[Entry] = GEN[Entry];
                Changed = N - { Entry }; // N = all nodes in graph
                
                while (Changed != emptyset)
                    choose a node n in Changed;
                    Changed = Changed - { n };
                
                    IN[n] = E; // E is set of all expressions
                    for all nodes p in predecessors(n)
                        IN[n] = IN[n] ∩ OUT[p];
                
                    OUT[n] = GEN[n] U (IN[n] - KILL[n]);
                
                    if (OUT[n] changed)
                        for all nodes s in successors(n)
                            Changed = Changed U { s };
                */
            }
        }
    }
}
