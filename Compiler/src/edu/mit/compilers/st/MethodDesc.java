package edu.mit.compilers.st;

import edu.mit.compilers.defs.VarType;

public class MethodDesc extends Descriptor{
    public MethodDesc(VarType type, String text) {
        super(type.makeMethod(), text);
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