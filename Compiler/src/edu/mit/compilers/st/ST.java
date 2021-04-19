package edu.mit.compilers.st;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Label;
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
    private Integer varOffset = 4;

    public ST() {
        this.isGlobal = true;
    }

    public ST(ST subst) {
        this.isGlobal = false;
        this.subST = subst;
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

    public final String getType(String text) {
        String type = getTypeNonRecursive(text);
        if (type != null) {
            return type;
        }
        if (this.subST != null) {
            return this.subST.getType(text);
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

    public final boolean push(Descriptor desc) {
        if (!Er.hasError() && !Defs.isMethodType(desc.getType())) {
            if (this.isGlobal) {
                desc.setAddr(new Addr(desc.getText()));
            } else {
                desc.setAddr(new Addr(this.varOffset));
                if (Defs.isArrayType(desc.getType())) {
                    this.varOffset += 4 * ((ArrayDesc)desc).getCap();
                } else {
                    this.varOffset += 4;
                }
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
}
