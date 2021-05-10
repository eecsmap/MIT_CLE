package edu.mit.compilers.optimizer;

import java.util.List;

import edu.mit.compilers.asm.AProgram;
import edu.mit.compilers.cfg.CMethod;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Manager;

public class Optimizer {
    private Optimizer() {}

    public static AProgram optimize(AProgram program) {
        if (!Defs.isAnyOptimizationEnabled()) return program;
        List<CMethod> methods;
        methods = program.split();
        if (Defs.isGCSEEnabled()) {
            for (CMethod method: methods) {
                Manager.enterOptimizationScope(method.getOffset());
                CSE.globalCSE(method);
                method.changeOffset();
            }
        }
        program.join(methods);
        return program;
    }
}
