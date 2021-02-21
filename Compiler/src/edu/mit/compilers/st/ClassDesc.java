package edu.mit.compilers.st;

public class ClassDesc extends Descriptor {
    public final ST methodST = new ST();
    public final ST fieldST = new ST();

    public final String findVar(String text) {
        return fieldST.getType(text);
    }

    public final String findMethod(String text) {
        return methodST.getType(text);
    }
}
