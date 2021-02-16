package edu.mit.compilers.st;

public class ClassDesc extends Descriptor {
    private ST methodST;
    private ST fieldST;

    public final String findVar(String name) {
        return fieldST.getType(name);
    }

    public final String findMethod(String name) {
        return methodST.getType(name);
    }
}
