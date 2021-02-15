package edu.mit.compilers.ir;

import antlr.collections.AST;

public abstract class IR {
    protected String type;

    protected String text;

    protected IR fc;

    protected IR ns;

    public final String getType() {
        return type;
    }

    public final String getText() {
        return text;
    }

    public final IR getFirstChild() {
        return fc;
    }

    public final IR getNextSibling() {
        return ns;
    }

    public final Boolean setFirstChild(IR ir) {
        if (fc != null) {
            return false;
        }
        fc = ir;
        return true;
    }

    public final Boolean setNextSibling(IR ir) {
        if (ns != null) {
            return false;
        }
        ns = ir;
        return true;
    }

    // return type
    public abstract String execute();
}