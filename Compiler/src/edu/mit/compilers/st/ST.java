package edu.mit.compilers.st;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Constants;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;

// field symbol table -> field desc []
// param symbol table -> param desc [], last local ST (if have) used in for loop 
// local symbol table -> local desc [], param ST
// method symbol table -> method desc []
// type symbol table -> type desc []
public class ST {
    private ST subST = null;
    private String returnType = null;
    private Label returnLabel = null;
    // text -> Descriptor
    private Map<String, Descriptor> table = new HashMap<>();
    // for / while
    private Stack<Integer> context = new Stack<>();
    private Stack<Label> continueLabelStack = new Stack<>();
    private Stack<Label> breakLabelStack = new Stack<>();
    private Boolean isGlobal;
    // only for non-global ST
    private Integer varOffset = 0;
    private Integer tmpCounter = 0;

    private Stack<Oprand> tmpStack = new Stack<>();
    private Map<String, Reg> callerSavedRegsUsage = new TreeMap<>();

    public ST() {
        this.isGlobal = true;
    }

    public ST(ST subst) {
        this.isGlobal = false;
        this.subST = subst;
        this.varOffset = subst.varOffset;
        this.returnType = subst.returnType;
        this.returnLabel = subst.returnLabel;
    }

    public ST(ST subst, String type) {
        this.isGlobal = false;
        this.subST = subst;
        this.returnType = type;
        this.returnLabel = new Label();
    }

    private final String getTypeNonRecursive(String text) {
        Descriptor desc = this.table.get(text);
        return (desc != null) ? desc.getType() : null;
    }

    private void argumentOffsetIncrement() {
        if (this.varOffset > -48 && this.varOffset <= 0) {
            // first six
            this.varOffset -= 8;
        } else if (this.varOffset <= -48) {
            // the seventh (return address and saved rbp)
            this.varOffset = 16;
        } else {
            // and after
            this.varOffset += 8;
        }
    }

    private void localOffsetIncrement() {
        if (this.varOffset > 0) {
            this.varOffset = -24;
        }
        this.varOffset -= 8;
    }

    public final Descriptor getDesc(String text) {
        Descriptor desc = this.table.get(text);
        if (desc != null) {
            return desc;
        }
        if (this.subST != null) {
            return this.subST.getDesc(text);
        }
        return null;
    }

    public final Descriptor getMethod(String text) {
        Descriptor desc = this.table.get(text);
        if(desc != null && desc.getText().equals(text) && Defs.isMethodType(desc.getType())) {
            return desc;
        }
        if (this.subST != null) {
            return this.subST.getMethod(text);
        }
        return null;
    }

    public final Descriptor getArray(String text) {
        Descriptor desc = this.table.get(text);
        if(desc != null && desc.getText().equals(text) && Defs.isArrayType(desc.getType())) {
            return desc;
        }
        if (this.subST != null) {
            return this.subST.getArray(text);
        }
        return null;
    }

    public final boolean push(Descriptor desc, boolean isArgument) {
        if (!Er.hasError() && !Defs.isMethodType(desc.getType())) {
            if (this.isGlobal) {
                desc.setAddr(new Addr(desc.getText(), false));
            } else {
                if (desc instanceof ArrayDesc) {
                    for (int i = 0; i < ((ArrayDesc)desc).getCap(); i++)
                    if (isArgument) {
                        this.argumentOffsetIncrement();
                    } else {
                        this.localOffsetIncrement();
                    }
                } else {
                    if (isArgument) {
                        this.argumentOffsetIncrement();
                    } else {
                        this.localOffsetIncrement();
                    }
                }
                desc.setAddr(new Addr(this.varOffset, desc.getText()));
            }
        }
        if (this.getTypeNonRecursive(desc.getText()) != null) {
            return false;
        }
        this.table.put(desc.getText(), desc);
        return true;
    }

    public final void print(int level) {
        String tab = new String(new char[level]).replace("\0", "\t");
        for (Descriptor desc: this.table.values()) {
            System.out.println(tab + desc.getType() + " " + desc.getText());
        }
        this.subST.print(level + 1);
    }

    public final int getContext() {
        try {
            return this.context.peek();
        } catch (Exception e) {
            if (this.subST == null) {
                return -1;
            }
            return this.subST.getContext();
        }
    }

    public final void pushContext(int cxt) {
        this.context.push(cxt);
    }

    public final void popContext() {
        this.context.pop();
    }

    public final String getReturnType() {
        if (this.returnType == null && this.subST != null) {
            return this.subST.getReturnType();
        }
        return this.returnType;
    }

    public final void pushContinueLabel(Label continueLabel) {
        this.continueLabelStack.push(continueLabel);
    }

    public final void pushBreakLabel(Label breakLabel) {
        this.breakLabelStack.push(breakLabel);
    }

    public final void popContinueLabel() {
        this.continueLabelStack.pop();
    }

    public final void popBreakLabel() {
        this.breakLabelStack.pop();
    }

    public final Label getContinueLabel() {
        try {
            return this.continueLabelStack.peek();
        } catch (Exception e) {
            if (this.subST == null) {
                Er.setError();
                return null;
            }
            return this.subST.getContinueLabel();
        }
    }

    public final Label getBreakLabel() {
        try {
            return this.breakLabelStack.peek();
        } catch (Exception e) {
            if (this.subST == null) {
                Er.setError();
                return null;
            }
            return this.subST.getBreakLabel();
        }
    }

    public final Boolean isGlobal() {
        return this.isGlobal;
    }

    public final Label getReturnLabel() {
        return this.returnLabel;
    }

    // 1, 2, 4 bytes
    public final Reg newTmpReg() {
        for(Reg reg: Constants.callerSavedReg) {
            if (!this.callerSavedRegsUsage.containsKey(reg.getRegName())) {
                String name = String.format("tmp%d", this.tmpCounter++);
                return new Reg(reg, name);
            }
        }
        return null;
    }

    // 1, 2, 4 bytes
    public final Reg newTmpReg(Reg exclude) {
        for(Reg reg: Constants.callerSavedReg) {
            if (!this.callerSavedRegsUsage.containsKey(reg.getRegName()) && exclude.getRegName() != reg.getRegName()) {
                String name = String.format("tmp%d", this.tmpCounter++);
                return new Reg(reg, name);
            }
        }
        return null;
    }

    public final Addr newTmpAddr() {
        this.localOffsetIncrement();
        String name = String.format("tmp%d", this.tmpCounter++);
        return new Addr(this.varOffset, name);
    }

    public final Integer bytesToAllocate() {
        Integer bytes = (this.varOffset > 0) ? 48 : -this.varOffset;
        return (bytes + 15) / 16 * 16;
    }

    public final Integer getOffset() {
        return this.varOffset;
    }

    public final void tmpPush(Oprand tmp) {
        if (tmp instanceof Reg) {
            this.callerSavedRegsUsage.put(((Reg)tmp).getRegName(), ((Reg)tmp));
        } else if (tmp instanceof Addr) {
            ((Addr)tmp).getReservedRegs().forEach(e -> this.callerSavedRegsUsage.put(e.getRegName(), e));
        }
        this.tmpStack.push(tmp);
    }

    public final Oprand tmpPop() {
        Oprand returnOp = this.tmpStack.pop();
        if (returnOp instanceof Reg) {
            this.callerSavedRegsUsage.remove(((Reg)returnOp).getRegName());
        } else if (returnOp instanceof Addr) {
            ((Addr)returnOp).getReservedRegs().forEach(e -> this.callerSavedRegsUsage.remove(e.getRegName()));
        }
        return returnOp;
    }

    public final Oprand tmpPeek() {
        return this.tmpStack.peek();
    }

    public final List<Reg> getUsedCalleeSavedRegs() {
        List<Reg> res = new ArrayList<>();
        this.callerSavedRegsUsage.forEach((k, v) -> res.add(v));
        return res;
    }
}
