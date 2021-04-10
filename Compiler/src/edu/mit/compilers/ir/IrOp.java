package edu.mit.compilers.ir;

import edu.mit.compilers.cfg.Operator;

public interface IrOp {
    public abstract Operator makeOperator();
}
