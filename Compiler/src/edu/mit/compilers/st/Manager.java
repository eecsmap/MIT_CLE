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
public class Manager {
    private Manager(){}
    private static SymbolTable symbolTable = new SymbolTable();
    private static String returnType;
    private static Label returnLabel;

    // for / while
    private static Stack<Boolean> contextStack = new Stack<>();
    private static Stack<Label> continueLabelStack = new Stack<>();
    private static Stack<Label> breakLabelStack = new Stack<>();
    // only for non-global ST
    private static Integer varOffset = 0;
    private static Integer tmpCounter = 0;

    private static Stack<Oprand> tmpStack = new Stack<>();
    private static Map<String, Reg> callerSavedRegsUsage = new TreeMap<>();

    // enter a method
    public static void enterScope(String methodReturnType) {
        returnType = methodReturnType;
        returnLabel = new Label();
        symbolTable = new SymbolTable(symbolTable);
    }

    public static void enterScope(boolean isLoop) {
        if (isLoop) {
            continueLabelStack.push(new Label());
            breakLabelStack.push(new Label());
        }
        contextStack.push(isLoop);
        symbolTable = new SymbolTable(symbolTable);
    }

    public static void leaveScope() {
        if (!contextStack.empty()) {
            if (contextStack.pop()) {
                continueLabelStack.pop();
                breakLabelStack.pop();
            }
        } else {
            varOffset = 0;
            tmpCounter = 0;
            tmpStack.clear();
            assert breakLabelStack.empty();
            assert callerSavedRegsUsage.isEmpty();
        }
        symbolTable = symbolTable.getParent();
    }

    private static void argumentOffsetIncrement() {
        if (varOffset > -48 && varOffset <= 0) {
            // first six
            varOffset -= 8;
        } else if (varOffset <= -48) {
            // the seventh (return address and saved rbp)
            varOffset = 16;
        } else {
            // and after
            varOffset += 8;
        }
    }

    private static void localOffsetIncrement() {
        if (varOffset > 0) {
            varOffset = -56;
        }
        varOffset -= 8;
    }

    public static final Descriptor getDesc(String text) {
        return symbolTable.getDesc(text);
    }

    public static final Descriptor getMethod(String text) {
        return symbolTable.getMethod(text);
    }

    public static final Descriptor getArray(String text) {
        return symbolTable.getArray(text);
    }

    public static final Boolean push(Descriptor desc, boolean isArgument) {
        Long sizeToAlloc = symbolTable.push(desc, isArgument);
        if (sizeToAlloc == 0L)
            return false;
        if (isGlobal() && !Defs.isMethodType(desc.getType())) {
            desc.setAddr(new Addr(desc.getText(), false));
        } else {
            for (int i = 0; i < sizeToAlloc; i++) {
                if (isArgument)
                    argumentOffsetIncrement();
                else
                    localOffsetIncrement();
            }
            desc.setAddr(new Addr(varOffset, desc.getText()));
        }
        return true;
    }

    public static final Boolean isInLoop() {
        return !breakLabelStack.empty();
    }

    public static final String getReturnType() {
        return returnType;
    }

    public static final Label getContinueLabel() {
        return continueLabelStack.peek();
    }

    public static final Label getBreakLabel() {
        return breakLabelStack.peek();
    }

    public static final Boolean isGlobal() {
        return symbolTable.getParent() == null;
    }

    public static final Label getReturnLabel() {
        return returnLabel;
    }

    // 1, 2, 4 bytes
    public static final Reg newTmpReg() {
        for(Reg reg: Constants.callerSavedReg) {
            if (!callerSavedRegsUsage.containsKey(reg.getRegName())) {
                String name = String.format("tmp%d", tmpCounter++);
                return new Reg(reg, name);
            }
        }
        return null;
    }

    // 1, 2, 4 bytes
    public static final Reg newTmpReg(Reg exclude) {
        for(Reg reg: Constants.callerSavedReg) {
            if (!callerSavedRegsUsage.containsKey(reg.getRegName()) && exclude.getRegName() != reg.getRegName()) {
                String name = String.format("tmp%d", tmpCounter++);
                return new Reg(reg, name);
            }
        }
        return null;
    }

    public static final Addr newTmpAddr() {
        localOffsetIncrement();
        String name = String.format("tmp%d", tmpCounter++);
        return new Addr(varOffset, name);
    }

    public static final Integer bytesToAllocate() {
        Integer bytes = (varOffset > 0) ? 48 : -varOffset;
        return (bytes + 15) / 16 * 16;
    }

    public static final Integer getOffset() {
        return varOffset;
    }

    public static final void tmpPush(Oprand tmp) {
        if (tmp instanceof Reg) {
            callerSavedRegsUsage.put(((Reg)tmp).getRegName(), ((Reg)tmp));
        } else if (tmp instanceof Addr) {
            ((Addr)tmp).getReservedRegs().forEach(e -> callerSavedRegsUsage.put(e.getRegName(), e));
        }
        tmpStack.push(tmp);
    }

    public static final Oprand tmpPop() {
        Oprand returnOp = tmpStack.pop();
        if (returnOp instanceof Reg) {
            callerSavedRegsUsage.remove(((Reg)returnOp).getRegName());
        } else if (returnOp instanceof Addr) {
            ((Addr)returnOp).getReservedRegs().forEach(e -> callerSavedRegsUsage.remove(e.getRegName()));
        }
        return returnOp;
    }

    public static final Oprand tmpPeek() {
        return tmpStack.peek();
    }

    public static final List<Reg> getUsedCallerSavedRegs() {
        List<Reg> res = new ArrayList<>();
        callerSavedRegsUsage.forEach((k, v) -> res.add(v));
        return res;
    }
}
