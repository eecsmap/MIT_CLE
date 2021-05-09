package edu.mit.compilers.optimizer;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import edu.mit.compilers.asm.basic.Addr;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;


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

    public BigInteger process(String inst, Oprand l, Oprand r) {
        // tmp registers
        if (r instanceof Reg) {
            Reg rReg = (Reg)r;
            if (!rReg.isTmp()) {
                return null;
            }
            if (inst.equals("movq")) {

            } else if (inst.equals("addq")) {

            } else if (inst.equals("subq")) {

            } else if (inst.equals("imulq")) {

            }
        }
        // non-tmp variables
        if (r instanceof Addr) {
            Addr rAddr = (Addr)r;
            if (rAddr.isTmp()) {
                return null;
            }
            if (inst.equals("movq")) {

            } else if (inst.equals("addq")) {

            } else if (inst.equals("subq")) {

            } else if (inst.equals("imulq")) {
                
            }
        }
        return null;
    }

    public void process(String inst, Oprand op) {
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
