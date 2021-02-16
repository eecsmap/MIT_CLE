package edu.mit.compilers.st;

// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
public class GeneralDesc extends Descriptor {
    public GeneralDesc(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public final String findVar(String text) {
        return null;
    }

    public final String findMethod(String text) {
        return null;
    }
}
