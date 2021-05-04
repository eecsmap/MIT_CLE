package edu.mit.compilers.optimizer;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.cfg.CMethod;
import edu.mit.compilers.defs.Defs;

public class Optimizer {
    private Optimizer() {}

    public static ABlock optimize(ABlock program) {
        List<CMethod> methods = new ArrayList<>();
        if (Defs.isAnyOptimizationEnabled()) {
            // TODO: maybe split ABlock and get Available Expression
        }
        if (Defs.isGCSEEnabled()) {
            methods.forEach(GCSE::globalCommonSubexpressionElimination);
        }
        if (Defs.isAnyOptimizationEnabled()) {
            // TODO: recover methods to ABlock
        }
        return program;
    }
}
