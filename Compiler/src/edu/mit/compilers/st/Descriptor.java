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

    public abstract String findVar(String name);

    public abstract String findMethod(String name);
}
