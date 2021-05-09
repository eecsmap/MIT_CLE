package edu.mit.compilers.optimizer;

import java.util.List;

import edu.mit.compilers.asm.AProgram;
import edu.mit.compilers.cfg.CMethod;
import edu.mit.compilers.defs.Defs;

public class Optimizer {
    private Optimizer() {}

    public static AProgram optimize(AProgram program) {
        if (!Defs.isAnyOptimizationEnabled()) return program;
        List<CMethod> methods;
        methods = program.split();
        if (Defs.isGCSEEnabled()) {
            methods.forEach(CSE::globalCSE);
        }
        program.join(methods);
        return program;
    }
}
