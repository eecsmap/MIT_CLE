package edu.mit.compilers.tools;

import antlr.collections.AST;

// Error Reporter
public class Er {
    private static boolean trace = false;
    private static boolean error = false;

    public static final void setTrace() {
        trace = true;
    }

    public static final void setError() {
        error = true;
    }

    public static final boolean hasError() {
        return error;
    }

    public static final void report(AST t, String fmt, Object... args) {
        setError();
        if (!trace) {
            return;
        }
        System.err.println(String.format("%d, %d: " + fmt, t.getLine(), t.getColumn(), args));
    }

    public static final void errNotDefined(AST t, String var) {
        report(t, "not defined '%s'", var);
    }

    public static final void errArrayIndexNotInt(AST t, String arrayID, String type) {
        report(t, "array (%s) index is not int, given %s", arrayID, type);
    }

    public static final void errType(AST t, String expectedType, String givenType) {
        report(t, "expect type %s, given %s", expectedType, givenType);
    }

    public static final void errArrayOutbound(AST t, String arrayID, String given) {
        report(t, "array %s index %s is outbound", arrayID, given);
    }
}
