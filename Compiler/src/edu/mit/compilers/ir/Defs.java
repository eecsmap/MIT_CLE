package edu.mit.compilers.ir;

// ClassDesc -> "class name" "name", "fieldST" "methodST"
// MethodDesc -> "return type" "name", "localST"
// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
// ThisDesc -> "this" "classdesc name", ""
public class Defs {
    public static String DESC_METHOD = "method";
    public static String DESC_TYPE_INT = "int";
    public static String DESC_TYPE_BOOL = "bool";
    public static String DESC_TYPE_VOID = "void";
    public static String DESC_ARRAY_INT = "array_int";
    public static String DESC_ARRAY_BOOL = "array_bool";
    public static String DESC_THIS = "this";
}
