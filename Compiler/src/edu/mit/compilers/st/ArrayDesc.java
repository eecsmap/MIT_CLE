package edu.mit.compilers.st;

import edu.mit.compilers.defs.VarType;

public class ArrayDesc extends Descriptor {
    private Long cap;

    public ArrayDesc(VarType type, String text, Long cap) {
        super(type.makeArray(), text);
        this.cap = cap;
    }

    public final Long getCap() {
        return this.cap;
    }
}
