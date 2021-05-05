package edu.mit.compilers.optimizer;

import edu.mit.compilers.cfg.CMethod;

public class CSE {
    private CSE() {}

    // global common subexpression elimination
    public static void commonSubexpressionElimination(CMethod method) {
        // 1. get available expressions
        // 2. run gcse on each method
        // 3. CBlock should provide a list for saving commands
        // 4. when elimination done, should delete/add backwards
    }
}
