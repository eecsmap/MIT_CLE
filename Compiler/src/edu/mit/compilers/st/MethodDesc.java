package edu.mit.compilers.st;

public class MethodDesc extends Descriptor{
    public ST localST;

    public final String findVar(String text) {
        return localST.getType(text);
    }

    public final String findMethod(String text) {
        return null;
    }
}
