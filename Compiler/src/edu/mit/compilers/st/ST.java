package edu.mit.compilers.st;

import java.util.ArrayList;
import edu.mit.compilers.st.Descriptor;

public class ST {
    private ST subST;
    private ArrayList<Descriptor> table;

    public boolean contains(String name) {
        for(int i = 0; i < table.size(); i++) {
            Descriptor desc = table.get(i);
            if(desc.text == name) {
                return true;
            }
        }
        if(subST != null) {
            return subST.contains(name);
        }
        return false;
    }
}
