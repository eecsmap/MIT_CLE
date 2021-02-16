package edu.mit.compilers.st;

public class MethodDesc extends Descriptor{
    private ST localST;

    public final String findVar(String name) {
        return localST.getType(name);
    }

    public final String findMethod(String name) {
        return null;
    }
}
