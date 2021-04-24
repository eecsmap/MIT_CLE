package edu.mit.compilers.st;

public class ArrayDesc extends Descriptor {
    private Long cap;

    public ArrayDesc(String type, String text, Long cap) {
        super(type, text);
        this.cap = cap;
    }

    // find if index is overbound
    @Override
    public final String findVar(String idxStr) {
        Long idx = Long.parseLong(idxStr);
        if (idx < cap) {
            return "ok";
        }
        return null;
    }

    @Override
    public final String findMethod(String text) {
        return null;
    }

    public final Long getCap() {
        return this.cap;
    }
}
