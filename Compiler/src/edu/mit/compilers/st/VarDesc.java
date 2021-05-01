package edu.mit.compilers.st;

import edu.mit.compilers.defs.VarType;

// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
public class VarDesc extends Descriptor {
    public VarDesc(VarType type, String text) {
        super(type, text);
    }

    @Override
    public final String findVar(String text) {
        return null;
    }

    @Override
    public final String findMethod(String text) { return null; }
}
