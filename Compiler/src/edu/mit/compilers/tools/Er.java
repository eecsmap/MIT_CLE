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
        if (!trace) {
            return;
        }
        System.err.println(String.format("%d, %d: " + fmt, t.getLine(), t.getColumn(), args));
    }
}
