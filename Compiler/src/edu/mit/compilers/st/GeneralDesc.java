package edu.mit.compilers.st;

// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
public class GeneralDesc extends Descriptor {
    public final String findVar(String name) {
        return null;
    }

    public final String findMethod(String name) {
        return null;
    }
}
