package edu.mit.compilers.st;

public class VarType {
    private static enum Type {
        BOOL,
        INT,
        VOID,
        WILDCARD,
        STRING_LITERAL,
    }

    public static final VarType BOOL = new VarType(Type.BOOL);
    public static final VarType INT = new VarType(Type.INT);
    public static final VarType VOID = new VarType(Type.VOID);
    public static final VarType WILDCARD = new VarType(Type.WILDCARD);
    public static final VarType STRING_LITERAL = new VarType(Type.STRING_LITERAL);

    private Type type;
    private Boolean isArray = false;
    private Boolean isMethod = false;

    private VarType(Type type) { this.type = type; }

    public Boolean isArray() {
        return this.isArray;
    }

    public Boolean isMethod() {
        return this.isMethod;
    }

    VarType makeArray() {
        assert !this.isMethod;
        VarType res = new VarType(this.type);
        res.isArray = true;
        res.isMethod = false;
        return res;
    }

    VarType makeMethod() { 
        assert !this.isArray;
        VarType res = new VarType(this.type);
        res.isMethod = true;
        res.isArray = false;
        return res;
    }

    public VarType plain() {
        return new VarType(this.type);
    }

    public Boolean isBool() {
        return this.type == Type.WILDCARD || this.type == Type.BOOL;
    }

    public Boolean isInt() {
        return this.type == Type.WILDCARD || this.type == Type.INT;
    }

    public Boolean isVoid() {
        return this.type == Type.WILDCARD || this.type == Type.VOID;
    }

    public Boolean isStringLiteral() {
        return this.type == Type.WILDCARD || this.type == Type.STRING_LITERAL;
    }

    public Boolean isWildcard() {
        return this.type == Type.WILDCARD;
    }

    public String toString() {
        String trailing = "";
        if (this.isArray) {
            trailing = "[]";
        } else if (this.isMethod) {
            trailing = "()";
        }
        return this.type.toString() + trailing;
    }

    public Boolean equals(VarType rhs) {
        return (this.isWildcard() || rhs.isWildcard() || this.type == rhs.type) 
        && this.isArray == rhs.isArray && this.isMethod == rhs.isMethod;
    }
}
