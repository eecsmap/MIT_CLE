package edu.mit.compilers.st;
 
// ClassDesc -> "class name" "name", "fieldST" "methodST"
// MethodDesc -> "return type" "name", "localST"
// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
// ThisDesc -> "this" "classdesc name", ""
public abstract class Descriptor {
    public String type;
    
    public String text;

    protected Descriptor() { 
        // default descriptor
    }

    protected Descriptor(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public abstract String findVar(String text);

    public abstract String findMethod(String text);
}
