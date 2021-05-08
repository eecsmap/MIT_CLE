package edu.mit.compilers.optimizer;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;


public class EBlock {
    private TreeSet<Expr> set = new TreeSet<Expr>(
        new Comparator<Expr>() {
            @Override
            public int compare(Expr lhs, Expr rhs) {
                return lhs.compare(rhs);
            }
        }
    );

    public EBlock() {}

    public void union(EBlock rhs) {
        for (Expr expr: rhs.set) {
            this.set.add(expr);
        }
    }

    public void intersect(EBlock rhs) {
        for (Iterator<Expr> i = this.set.iterator(); i.hasNext();) {
            Expr expr = i.next();
            if (!rhs.set.contains(expr)) {
                i.remove();
            }
        }
    }

    public void subtract(EBlock rhs) {
        for (Expr expr: rhs.set) {
            this.set.remove(expr);
        }
    }
}
