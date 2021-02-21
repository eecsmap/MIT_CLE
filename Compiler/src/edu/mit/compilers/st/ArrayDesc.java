package edu.mit.compilers.st;

public class ArrayDesc extends Descriptor {
    private int cap;
    private boolean isCapSet = false;

    public ArrayDesc(String type, String text) {
        this.type = type;
        this.text = text;
    }

    // find if index is overbound
    public final String findVar(String idxStr) {
        int idx = Integer.parseInt(idxStr);
        if (idx >= cap) {
            return "";
        }
        return null;
    }

    public final String findMethod(String text) {
        return null;
    }

    public boolean setCap(String cap) {
        if (isCapSet) {
            return false;
        }
        this.cap = Integer.parseInt(cap);
        isCapSet = true;
        return true;
    }
}
