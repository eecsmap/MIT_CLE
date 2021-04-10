package edu.mit.compilers.cfg;

public class Defs {
    public static enum Type {
        IMPORT,
        FIELD,
        FUNCTION_DECLARE,
        IF,
        WHILE,
        FOR,
        ELSE,
    }

    public static enum Action {
        LOAD,
        STORE,
    }
}
