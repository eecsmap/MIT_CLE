package edu.mit.compilers.st;

import java.util.HashMap;
import java.util.Map;

import edu.mit.compilers.tools.Er;

public class SymbolTable {
    // text -> Descriptor
    private SymbolTable parent = null;
    private Map<String, Descriptor> table = new HashMap<>();

    public SymbolTable() {}

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public SymbolTable getParent() {
        return this.parent;
    }

    public final Descriptor getDesc(String text) {
        Descriptor desc = this.table.get(text);
        if (desc != null) {
            return desc;
        }
        if (this.parent != null) {
            return this.parent.getDesc(text);
        }
        return null;
    }

    public final Descriptor getMethod(String text) {
        Descriptor desc = this.table.get(text);
        if(desc != null && desc.getText().equals(text) && desc.getType().isMethod()) {
            return desc;
        }
        if (this.parent != null) {
            return this.parent.getMethod(text);
        }
        return null;
    }

    public final Descriptor getArray(String text) {
        Descriptor desc = this.table.get(text);
        if(desc != null && desc.getText().equals(text) && desc.getType().isArray()) {
            return desc;
        }
        if (this.parent != null) {
            return this.parent.getArray(text);
        }
        return null;
    }

    // return the number of new variable
    public final Long push(Descriptor desc, boolean isArgument) {
        Long size = 0L;
        if (!Er.hasError() && !desc.getType().isMethod()) {
            if (desc instanceof ArrayDesc) {
                size = ((ArrayDesc)desc).getCap();
            } else {
                size = 1L;
            }
        }
        if (this.table.get(desc.getText()) != null) {
            return 0L;
        }
        this.table.put(desc.getText(), desc);
        if (desc.getType().isMethod())
            return -1L;
        return size;
    }
}
