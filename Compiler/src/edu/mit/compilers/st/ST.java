package edu.mit.compilers.st;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// field symbol table -> field desc []
// param symbol table -> param desc [], last local ST (if have) used in for loop 
// local symbol table -> local desc [], param ST
// method symbol table -> method desc []
// type symbol table -> type desc []
public class ST {
    private ST subST = null;
    private String returnType = null;
    // text -> Descriptor
    private Map<String, Descriptor> table = new HashMap<>();
    private Stack<Integer> context = new Stack<>();

    public ST() {}

    public ST(ST subst) {
        this.subST = subst;
    }

    public ST(ST subst, String type) {
        this.subST = subst;
        this.returnType = type;
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
}
