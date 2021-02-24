package edu.mit.compilers.st;

import java.util.ArrayList;

// field symbol table -> field desc []
// param symbol table -> param desc [], last local ST (if have) used in for loop 
// local symbol table -> local desc [], param ST
// method symbol table -> method desc []
// type symbol table -> type desc []
public class ST {
    private ST subST;
    private ArrayList<Descriptor> table;

    public ST() {
        subST = null;
        table = new ArrayList<>();
    }

    public ST(ST subst) {
        this.subST = subst;
        table = new ArrayList<>();
    }

    public String getType(String text) {
        for (int i = 0; i < table.size(); i++) {
            Descriptor desc = table.get(i);
            if(desc.text.equals(text)) {
                return desc.type;
            }
        }
        if (subST != null) {
            return subST.getType(text);
        }
        return null;
    }

    public Descriptor getMethod(String text) {
        for (int i = 0; i < table.size(); i++) {
            Descriptor desc = table.get(i);
            if(desc.text.equals(text) && desc.type.startsWith(Defs.DESC_METHOD)) {
                return desc;
            }
        }
        if (subST != null) {
            return subST.getMethod(text);
        }
        return null;
    }

    // only for GeneralDesc
    public boolean push(String type, String text) {
        if (getType(text) != null) {
            return false;
        }
        table.add(new VarDesc(type, text));
        return true;
    }

    public boolean push(Descriptor desc) {
        if (getType(desc.text) != null) {
            return false;
        }
        table.add(desc);
        return true;
    }

    // remove the last in array
    public boolean pop() {
        int last = table.size() - 1;
        if (last < 0) {
            return false;
        }
        table.remove(last);
        return true;
    }

    public boolean setSubST(ST st) {
        subST = st;
        return true;
    }

    public void print(int level) {
        String tab = new String(new char[level]).replace("\0", "\t");
        for (Descriptor desc: table) {
            System.out.println(tab + desc.type + " " + desc.text);
        }
        subST.print(level + 1);
    }
}
