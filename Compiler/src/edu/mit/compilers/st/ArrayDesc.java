package edu.mit.compilers.st;

public class ArrayDesc extends Descriptor {
    private Integer cap;

    public ArrayDesc(String type, String text, Integer cap) {
        super(type, text);
        this.cap = cap;
    }

    // find if index is overbound
    @Override
    public final String findVar(String idxStr) {
        int idx = Integer.parseInt(idxStr);
        if (idx < cap) {
            return "ok";
        }
        return null;
    }

    @Override
    public final String findMethod(String text) {
        return null;
    }

    public final Integer getCap() {
        return this.cap;
    }
}
