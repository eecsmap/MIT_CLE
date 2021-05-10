package edu.mit.compilers.optimizer;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.mit.compilers.asm.basic.Addr;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.optimizer.Expr.Type;


public class EBlock {
    public static class ModifyAction {
        public static ModifyAction SAVE = new ModifyAction(1);
        public static ModifyAction DELETE = new ModifyAction(2);
        public static ModifyAction REPLACE = new ModifyAction(3);
    
        private int innerType;
        private int lineNumer;
        private Addr tmpAddrForSave = null;

        public ModifyAction(int innerType) {
            this.innerType = innerType;
        }
    
        public ModifyAction addLineNumber(int lineNumber) {
            ModifyAction res = new ModifyAction(this.innerType);
            res.lineNumer = lineNumber;
            return res;
        }

        public void setTmpAddr(Addr tmp) {
            this.tmpAddrForSave = tmp;
        }

        public Addr getTmpAddr() {
            return this.tmpAddrForSave;
        }
    
        public int getLineNumber() {
            return this.lineNumer;
        }
        
        public boolean equals(ModifyAction rhs) {
            return this.innerType == rhs.innerType;
        }
    }

    private TreeMap<Integer, ModifyAction> toModify = new TreeMap<>(Collections.reverseOrder());
    private TreeMap<Expr, Integer> expr2lineno = new TreeMap<>(
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
        this.expr2lineno.putAll(rhs.expr2lineno);
    }

    public void eval(Expr expr, Integer lineNumber) {
        this.expr2lineno.put(expr, lineNumber);
    }

    public boolean kill(Oprand var) {
        boolean changed = false;
        for (Iterator<Expr> iter = this.expr2lineno.keySet().iterator(); iter.hasNext();) {
            Expr expr = iter.next();
            if (expr.contains(var)) {
                changed = true;
                iter.remove();
            }
        }
        return changed;
    }

    private void save(int saveLine) {
        this.toModify.put(saveLine, ModifyAction.SAVE);
    }

    private void delete(int deleteLine) {
        this.toModify.put(deleteLine, ModifyAction.DELETE);
    }

    private void replace(int deleteLine, int replaceLine) {
        this.toModify.put(deleteLine, ModifyAction.REPLACE.addLineNumber(replaceLine));
    }

    public Oprand process(int lineNumber, String inst, Oprand l, Oprand r) {
        // tmp registers
        if (r instanceof Reg) {
            Reg rReg = (Reg)r;
            if (!rReg.isTmp()) {
                return null;
            }
            Boolean newExpr = null;
            if (inst.equals("movq")) {
                tmp2Exp.remove(rReg.getName());
                tmp2Exp.put(rReg.getName(), new Expr(l));
            } else if (inst.equals("addq")) {
                newExpr = tmp2Exp.get(rReg.getName()).put(lineNumber, Type.ADD, l);
            } else if (inst.equals("subq")) {
                newExpr = tmp2Exp.get(rReg.getName()).put(lineNumber, Type.SUB, l);
            } else if (inst.equals("imulq")) {
                newExpr = tmp2Exp.get(rReg.getName()).put(lineNumber, Type.MUL, l);
            }
            if (newExpr != null && newExpr == true && this.expr2lineno.containsKey(tmp2Exp.get(rReg.getName()))) {
                this.save(this.expr2lineno.get(tmp2Exp.get(rReg.getName())));
                this.delete(lineNumber - 1);
                this.replace(lineNumber, tmp2Exp.get(rReg.getName()).getLineNumber());
            }
            if (newExpr != null && newExpr == true) {
                this.eval(tmp2Exp.get(rReg.getName()), lineNumber);
                CSE.fullSet.expr2lineno.put(tmp2Exp.get(rReg.getName()), lineNumber);
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

    public Oprand process(int lineNumber, String inst, Oprand op) {
        // TODO
        return null;
    }

    public Boolean union(EBlock rhs) {
        Boolean changed = false;
        for (Expr expr: rhs.expr2lineno.keySet()) {
            if (!this.expr2lineno.containsKey(expr)) {
                changed = true;
                this.expr2lineno.put(expr, 0);
            }
        }
        return changed;
    }

    public Boolean intersect(EBlock rhs) {
        Boolean changed = false;
        for (Iterator<Expr> i = this.expr2lineno.keySet().iterator(); i.hasNext();) {
            Expr expr = i.next();
            if (!rhs.expr2lineno.containsKey(expr)) {
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

    public TreeMap<Integer, ModifyAction> getModify() {
        return this.toModify;
    }

    public boolean equals(EBlock rhs) {
        if (this.expr2lineno.size() != rhs.expr2lineno.size()) {
            return false;
        }
        for (Expr expr: rhs.expr2lineno.keySet()) {
            if (!this.expr2lineno.containsKey(expr)) {
                return false;
            }
        }
        return true;
    }
}
