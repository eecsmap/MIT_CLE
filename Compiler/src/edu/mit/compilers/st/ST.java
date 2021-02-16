package edu.mit.compilers.st;

import java.util.ArrayList;
import edu.mit.compilers.st.Descriptor;

// field symbol table -> field desc []
// param symbol table -> param desc [], last local ST (if have) used in for loop 
// local symbol table -> local desc [], param ST
// method symbol table -> method desc []
// type symbol table -> type desc []
public class ST {
    private ST subST;
    private ArrayList<Descriptor> table;

    public String getType(String text) {
        for(int i = 0; i < table.size(); i++) {
            Descriptor desc = table.get(i);
            if(desc.text == text) {
                return desc.type;
            }
        }
        if(subST != null) {
            return subST.getType(text);
        }
        return null;
    }

    // TODO
    public boolean add(String type, String text) {
        if(getType(text) != null) {
            return false;
        }
        // table.add();
        return true;
    }

    public boolean add(Descriptor desc) {
        if(getType(desc.text) != null) {
            return false;
        }
        table.add(desc);
        return true;
    }

    // TODO
    public boolean delete(String text) {
        return false;
    }
}
