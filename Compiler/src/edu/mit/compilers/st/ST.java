package edu.mit.compilers.st;

import java.util.ArrayList;
import java.util.Stack;

// field symbol table -> field desc []
// param symbol table -> param desc [], last local ST (if have) used in for loop 
// local symbol table -> local desc [], param ST
// method symbol table -> method desc []
// type symbol table -> type desc []
public class ST {
    private ST subST = null;
    private String returnType = null;
    private ArrayList<Descriptor> table = new ArrayList<>();
    private Stack<Integer> context = new Stack<>();

    public ST() {}

    public ST(String type) {
        this.returnType = type;
    }

    public ST(ST subst) {
        this.subST = subst;
    }

    public ST(ST subst, String type) {
        this.subST = subst;
        this.returnType = type;
    }

    public final String getType(String text) {
        // System.out.println("search " + text);
        for (int i = 0; i < this.table.size(); i++) {
            Descriptor desc = this.table.get(i);
            if(desc.getText().equals(text)) {
                return desc.getType();
            }
            // System.out.println("has " + desc.getText() + " " + desc.getType());
        }
        if (this.subST != null) {
            // System.out.println("search in subST");
            return this.subST.getType(text);
        }
        return null;
    }

    public final Descriptor getMethod(String text) {
        for (int i = 0; i < this.table.size(); i++) {
            Descriptor desc = this.table.get(i);
            if(desc.getText().equals(text) && Defs.isMethodType(desc.getType())) {
                return desc;
            }
        }
        if (this.subST != null) {
            return this.subST.getMethod(text);
        }
        return null;
    }

    public final Descriptor getArray(String text) {
        for (int i = 0; i < this.table.size(); i++) {
            Descriptor desc = this.table.get(i);
            if(desc.getText().equals(text) && Defs.isArrayType(desc.getType())) {
                return desc;
            }
        }
        if (this.subST != null) {
            return this.subST.getArray(text);
        }
        return null;
    }

    // only for GeneralDesc
    public final boolean push(String type, String text) {
        if (this.getType(text) != null) {
            return false;
        }
        this.table.add(new VarDesc(type, text));
        return true;
    }

    public final boolean push(Descriptor desc) {
        if (this.getType(desc.getText()) != null) {
            return false;
        }
        this.table.add(desc);
        return true;
    }

    // remove the last in array
    public final boolean pop() {
        int last = this.table.size() - 1;
        if (last < 0) {
            return false;
        }
        this.table.remove(last);
        return true;
    }

    public final boolean setSubST(ST st) {
        this.subST = st;
        return true;
    }

    public final void print(int level) {
        String tab = new String(new char[level]).replace("\0", "\t");
        for (Descriptor desc: this.table) {
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
