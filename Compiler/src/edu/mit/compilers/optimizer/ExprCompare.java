package edu.mit.compilers.optimizer;

import java.util.Comparator;

public class ExprCompare implements Comparator<Expr> {

    @Override
    public int compare(Expr lhs, Expr rhs) {
        return lhs.compare(rhs);
    }
}
