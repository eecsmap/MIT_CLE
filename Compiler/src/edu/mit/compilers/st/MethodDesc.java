package edu.mit.compilers.st;

public class MethodDesc extends Descriptor{
    public MethodDesc(String type, String text) {
        super(type, text);
    }

    @Override
    public final String findVar(String text) {
        return null;
    }

    @Override
    public final String findMethod(String text) {
        return null;
    }
}