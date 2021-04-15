package edu.mit.compilers.ir;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.cfg.FlowOp;
import edu.mit.compilers.cfg.Operator;
import edu.mit.compilers.defs.Defs;

// simply forward
public class IrFlow implements IrOp {
    private FlowOp flowOp;

    public IrFlow(Defs.FlowOp op, Label label) {
        this.flowOp = new FlowOp(op, label);
    }

    public IrFlow(Defs.FlowOp op, Addr returnVar) {
        this.flowOp = new FlowOp(op, returnVar);
    }

    @Override
    public Operator makeOperator() {
        return this.flowOp;
    }
    
}
