package edu.mit.compilers.optimizer;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import edu.mit.compilers.asm.basic.Oprand;


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

    public EBlock(EBlock rhs) {
        this.set.addAll(rhs.set);
    }

    public void add(Expr expr) {
        this.set.add(expr);
    }

    public void eval(String inst, Oprand l, Oprand r) {
        // TODO
    }

    public void eval(String inst, Oprand op) {
        // TODO
    }

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

    public boolean equals(EBlock rhs) {
        if (this.set.size() != rhs.set.size()) {
            return false;
        }
        for (Expr expr: rhs.set) {
            if (!this.set.contains(expr)) {
                return false;
            }
        }
        return true;
    }
}
