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

    public Boolean union(EBlock rhs) {
        Boolean changed = false;
        for (Expr expr: rhs.set) {
            if (!this.set.contains(expr)) {
                changed = true;
                this.set.add(expr);
            }
        }
        return changed;
    }

    public Boolean intersect(EBlock rhs) {
        Boolean changed = false;
        for (Iterator<Expr> i = this.set.iterator(); i.hasNext();) {
            Expr expr = i.next();
            if (!rhs.set.contains(expr)) {
                changed = true;
                i.remove();
            }
        }
        return changed;
    }

    public Boolean subtract(EBlock rhs) {
        Boolean changed = false;
        for (Expr expr: rhs.set) {
            if (this.set.contains(expr)) {
                changed = true;
                this.set.remove(expr);
            }
        }
        return changed;
    }
}
