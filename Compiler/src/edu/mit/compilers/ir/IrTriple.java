package edu.mit.compilers.ir;

import edu.mit.compilers.cfg.BinaryOp;
import edu.mit.compilers.cfg.Operator;
import edu.mit.compilers.st.Descriptor;

public class IrTriple implements IrOp {
    private String op;
    private Descriptor d; // dest
    private Descriptor l;
    private Descriptor r;

    public IrTriple(String op, Descriptor d, Descriptor l, Descriptor r) {
        this.op = op;
        this.d = d;
        this.l = l;
        this.r = r;
    }

    public String op() { return this.op; }
    public Descriptor d() { return this.d; }
    public Descriptor l() { return this.l; }
    public Descriptor r() { return this.r; }

    @Override
    public Operator makeOperator() {
        return new BinaryOp(this);
    }
}
