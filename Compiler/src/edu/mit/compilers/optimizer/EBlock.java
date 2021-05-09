package edu.mit.compilers.optimizer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.mit.compilers.asm.basic.Addr;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.optimizer.Expr.Type;


public class EBlock {
    private TreeSet<Expr> set = new TreeSet<Expr>(
        new Comparator<Expr>() {
            @Override
            public int compare(Expr lhs, Expr rhs) {
                return lhs.compare(rhs);
            }
        }
    );

    private Map<String, Expr> tmp2Exp = new HashMap<>();

    public EBlock() {}

    public EBlock(EBlock rhs) {
        this.set.addAll(rhs.set);
    }

    public void eval(Expr expr) {
        this.set.add(expr);
    }

    public boolean kill(Oprand var) {
        boolean changed = false;
        for (Iterator<Expr> iter = this.set.iterator(); iter.hasNext();) {
            Expr expr = iter.next();
            if (expr.contains(var)) {
                changed = true;
                iter.remove();
            }
        }
        return changed;
    }

    public Oprand process(String inst, Oprand l, Oprand r) {
        // tmp registers
        if (r instanceof Reg) {
            Reg rReg = (Reg)r;
            if (!rReg.isTmp()) {
                return null;
            }
            boolean newExpr = false;
            if (inst.equals("movq")) {
                tmp2Exp.remove(rReg.getName());
                tmp2Exp.put(rReg.getName(), new Expr(l));
            } else if (inst.equals("addq")) {
                newExpr = tmp2Exp.get(rReg.getName()).put(Type.ADD, l);
            } else if (inst.equals("subq")) {
                newExpr = tmp2Exp.get(rReg.getName()).put(Type.SUB, l);
            } else if (inst.equals("imulq")) {
                newExpr = tmp2Exp.get(rReg.getName()).put(Type.MUL, l);
            }
            if (newExpr) {
                this.set.add(tmp2Exp.get(rReg.getName()));
                CSE.fullSet.set.add(tmp2Exp.get(rReg.getName()));
            }
        }
        // non-tmp variables
        if (r instanceof Addr) {
            Addr rAddr = (Addr)r;
            if (rAddr.isTmp()) {
                return null;
            }
            if (inst.equals("movq")) {
                this.kill(r);
                return r;
            } else if (inst.equals("addq")) {
                this.kill(r);
                return r;
            } else if (inst.equals("subq")) {
                this.kill(r);
                return r;
            } else if (inst.equals("imulq")) {
                this.kill(r);
                return r;
            }
        }
        return null;
    }

    public Oprand process(String inst, Oprand op) {
        // TODO
        return null;
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

    public Boolean subtract(Set<Oprand> toRemove) {
        Boolean changed = false;
        for (Oprand var: toRemove) {
            changed = changed || this.kill(var);
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
