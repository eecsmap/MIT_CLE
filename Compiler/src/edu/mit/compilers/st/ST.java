package edu.mit.compilers.st;

import java.util.ArrayList;
import java.util.Stack;

// field symbol table -> field desc []
// param symbol table -> param desc [], last local ST (if have) used in for loop 
// local symbol table -> local desc [], param ST
// method symbol table -> method desc []
// type symbol table -> type desc []
public class ST {
    private ST subST;
    private ArrayList<Descriptor> table;
    private Stack<Integer> context;

    public ST() {
        this.subST = null;
        this.table = new ArrayList<>();
        this.context = new Stack<>();
    }

    public ST(ST subst) {
        this.subST = subst;
        this.table = new ArrayList<>();
    }

    public String getType(String text) {
        for (int i = 0; i < this.table.size(); i++) {
            Descriptor desc = this.table.get(i);
            if(desc.text.equals(text)) {
                return desc.type;
            }
        }
        if (this.subST != null) {
            return this.subST.getType(text);
        }
        return null;
    }

    public Descriptor getMethod(String text) {
        for (int i = 0; i < this.table.size(); i++) {
            Descriptor desc = this.table.get(i);
            if(desc.text.equals(text) && desc.type.startsWith(Defs.DESC_METHOD)) {
                return desc;
            }
        }
        if (this.subST != null) {
            return this.subST.getMethod(text);
        }
        return null;
    }

    public Descriptor getArray(String text) {
        for (int i = 0; i < this.table.size(); i++) {
            Descriptor desc = this.table.get(i);
            if(desc.text.equals(text) && desc.type.startsWith(Defs.ARRAY_PREFIX)) {
                return desc;
            }
        }
        if (this.subST != null) {
            return this.subST.getArray(text);
        }
        return null;
    }

    // only for GeneralDesc
    public boolean push(String type, String text) {
        if (getType(text) != null) {
            return false;
        }
        this.table.add(new VarDesc(type, text));
        return true;
    }

    public boolean push(Descriptor desc) {
        if (getType(desc.text) != null) {
            return false;
        }
        this.table.add(desc);
        return true;
    }

    // remove the last in array
    public boolean pop() {
        int last = this.table.size() - 1;
        if (last < 0) {
            return false;
        }
        this.table.remove(last);
        return true;
    }

    public boolean setSubST(ST st) {
        this.subST = st;
        return true;
    }

    public void print(int level) {
        String tab = new String(new char[level]).replace("\0", "\t");
        for (Descriptor desc: this.table) {
            System.out.println(tab + desc.type + " " + desc.text);
        }
        this.subST.print(level + 1);
    }

    public int getContext() {
        return this.context.peek();
    }

    public void pushContext(int cxt) {
        this.context.push(cxt);
    }

    public void popContext() {
        this.context.pop();
    }
}
