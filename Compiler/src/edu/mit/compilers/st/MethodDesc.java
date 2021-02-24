package edu.mit.compilers.st;

public class MethodDesc extends Descriptor{
    public final ST localST = new ST();

    public MethodDesc(String type, String text) {
        super(Defs.DESC_METHOD + type, text);
    }

    public final String findVar(String text) {
        return localST.getType(text);
    }

    public final String findMethod(String text) {
        return null;
    }
}