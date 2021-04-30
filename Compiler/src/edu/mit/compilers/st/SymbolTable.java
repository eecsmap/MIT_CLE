package edu.mit.compilers.st;

import java.util.HashMap;
import java.util.Map;

import edu.mit.compilers.defs.Defs;
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

    private final String getTypeNonRecursive(String text) {
        Descriptor desc = this.table.get(text);
        return (desc != null) ? desc.getType() : null;
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
        if(desc != null && desc.getText().equals(text) && Defs.isMethodType(desc.getType())) {
            return desc;
        }
        if (this.parent != null) {
            return this.parent.getMethod(text);
        }
        return null;
    }

    public final Descriptor getArray(String text) {
        Descriptor desc = this.table.get(text);
        if(desc != null && desc.getText().equals(text) && Defs.isArrayType(desc.getType())) {
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
        if (!Er.hasError() && !Defs.isMethodType(desc.getType())) {
            if (desc instanceof ArrayDesc) {
                size = ((ArrayDesc)desc).getCap();
            } else {
                size = 1L;
            }
        }
        if (this.getTypeNonRecursive(desc.getText()) != null) {
            return 0L;
        }
        this.table.put(desc.getText(), desc);
        if (Defs.isMethodType(desc.getType()))
            return -1L;
        return size;
    }
}
