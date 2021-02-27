package edu.mit.compilers.st;

// ClassDesc -> "class name" "name", "fieldST" "methodST"
// MethodDesc -> "return type" "name", "localST"
// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
// ThisDesc -> "this" "classdesc name", ""
public class Defs {
    public static final String DESC_METHOD = "method";
    public static final String DESC_TYPE_INT = "int";
    public static final String DESC_TYPE_BOOL = "bool";
    public static final String DESC_TYPE_VOID = "void";
    public static final String DESC_ARRAY_INT = "array_int";
    public static final String DESC_ARRAY_BOOL = "array_bool";
    public static final String DESC_THIS = "this";
    public static final String ARRAY_PREFIX = "array_";
    public static final String DESC_TYPE_WILDCARD = "*";
}
