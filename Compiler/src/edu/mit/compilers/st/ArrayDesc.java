package edu.mit.compilers.st;

public class ArrayDesc extends Descriptor {
    private int cap;

    public ArrayDesc(String type, String text, String cap) {
        super(type, text);
        this.cap = Integer.parseInt(cap);
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
}
