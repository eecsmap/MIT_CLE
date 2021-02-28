package edu.mit.compilers.tools;

import java.util.Formatter;

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
        System.err.printf("%d, %d: ", t.getLine(), t.getColumn());
        System.err.printf(fmt, args);
    }

    public static final void errNotDefined(AST t, String var) { 
        report(t, "not defined $%s\n", var);
    }

    public static final void errArrayIndexNotInt(AST t, String arrayID, String type) {
        report(t, "array $%s index is not <int>, given <%s>\n", arrayID, type);
    }

    public static final void errType(AST t, String expectedType, String givenType) {
        report(t, "expect type <%s>, given <%s>\n", expectedType, givenType);
    }

    public static final void errArrayOutbound(AST t, String arrayID, String given) {
        report(t, "array $%s index %s is outbound\n", arrayID, given);
    }

    public static final void errBreak(AST t) {
        report(t, "not break inside loop\n");
    }

    public static final void errContinue(AST t) {
        report(t, "not continue inside loop\n");
    }
}
