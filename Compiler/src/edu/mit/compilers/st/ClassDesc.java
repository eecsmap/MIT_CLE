package edu.mit.compilers.st;

public class ClassDesc extends Descriptor {
    public ST methodST;
    public ST fieldST;

    public final String findVar(String text) {
        return fieldST.getType(text);
    }

    public final String findMethod(String text) {
        return methodST.getType(text);
    }
}
