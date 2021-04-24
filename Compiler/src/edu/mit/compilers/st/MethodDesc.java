package edu.mit.compilers.st;

import edu.mit.compilers.defs.Defs;

public class MethodDesc extends Descriptor{
    public MethodDesc(String type, String text) {
        super(Defs.makeMethodType(type), text);
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