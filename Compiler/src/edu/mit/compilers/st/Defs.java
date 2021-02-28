package edu.mit.compilers.st;

// ClassDesc -> "class name" "name", "fieldST" "methodST"
// MethodDesc -> "return type" "name", "localST"
// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
// ThisDesc -> "this" "classdesc name", ""
public class Defs {
    private static final String METHOD_PREFIX = "method_";
    private static final String ARRAY_PREFIX = "array_";
    public static final String DESC_METHOD_WILDCARD = "method_*";
    public static final String DESC_METHOD_INT = "method_int";
    public static final String DESC_METHOD_BOOL = "method_bool";
    public static final String DESC_METHOD_VOID = "method_void";
    public static final String DESC_TYPE_INT = "int";
    public static final String DESC_TYPE_BOOL = "bool";
    public static final String DESC_TYPE_VOID = "void";
    public static final String DESC_ARRAY_INT = "array_int";
    public static final String DESC_ARRAY_BOOL = "array_bool";
    public static final String DESC_THIS = "this";
    public static final String DESC_TYPE_WILDCARD = "*";

    public static final String getArrayType(String arrayType) {
        if (!isArrayType(arrayType)) {
            return null;
        }
        return arrayType.substring(ARRAY_PREFIX.length());
    }

    public static final String getMethodType(String methodType) {
        if (!isMethodType(methodType)) {
            return null;
        }
        return methodType.substring(METHOD_PREFIX.length());
    }

    public static final String makeArrayType(String type) {
        return ARRAY_PREFIX + type;
    }

    public static final String makeMethodType(String type) {
        return METHOD_PREFIX + type;
    }

    public static final boolean isArrayType(String type) {
        return type.startsWith(ARRAY_PREFIX);
    }

    public static final boolean isMethodType(String type) {
        return type.startsWith(METHOD_PREFIX);
    }
}
