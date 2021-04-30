package edu.mit.compilers.st;

import java.util.ArrayList;
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

// field symbol table -> field desc []
// param symbol table -> param desc [], last local ST (if have) used in for loop 
// local symbol table -> local desc [], param ST
// method symbol table -> method desc []
// type symbol table -> type desc []
public class MethodUtils {
    private SymbolTable symbolTable;
    private String returnType;
    private Label returnLabel;

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

    public MethodUtils() {
        this.isGlobal = true;
        this.symbolTable = new SymbolTable();
    }

    public MethodUtils(MethodUtils global, String returnType) {
        this.isGlobal = false;
        this.returnType = returnType;
        this.returnLabel = new Label();
        this.symbolTable = new SymbolTable(global.symbolTable);
    }

    public void enterScope() {
        this.symbolTable = new SymbolTable(this.symbolTable);
    }

    public void leaveScope() {
        this.symbolTable = this.symbolTable.getParent();
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
            this.varOffset = -56;
        }
        this.varOffset -= 8;
    }

    public final Descriptor getDesc(String text) {
        return this.symbolTable.getDesc(text);
    }

    public final Descriptor getMethod(String text) {
        return this.symbolTable.getMethod(text);
    }

    public final Descriptor getArray(String text) {
        return this.symbolTable.getArray(text);
    }

    public final Boolean push(Descriptor desc, boolean isArgument) {
        Long sizeToAlloc = this.symbolTable.push(desc, isArgument);
        if (sizeToAlloc == 0L)
            return false;
        for (int i = 0; i < sizeToAlloc; i++) {
            if (isArgument)
                this.argumentOffsetIncrement();
            else
                this.localOffsetIncrement();
        }
        if (this.isGlobal && !Defs.isMethodType(desc.getType())) {
            desc.setAddr(new Addr(desc.getText(), false));
        } else {
            desc.setAddr(new Addr(this.varOffset, desc.getText()));
        }
        return true;
    }


    public final int getContext() {
        if (this.context.empty()) {
            return -1;
        }
        return this.context.peek();
    }

    public final void pushContext(int cxt) {
        this.context.push(cxt);
    }

    public final void popContext() {
        this.context.pop();
    }

    public final String getReturnType() {
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
        if (this.continueLabelStack.empty()) {
            return null;
        }
        return this.continueLabelStack.peek();
    }

    public final Label getBreakLabel() {
        if (this.breakLabelStack.empty()) {
            return null;
        }
        return this.breakLabelStack.peek();
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
