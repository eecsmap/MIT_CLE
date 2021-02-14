package edu.mit.compilers.ir;

import antlr.collections.AST;

public abstract interface IR {
    public abstract edu.mit.compilers.ir.IR buildIrNode(AST t);

    public abstract edu.mit.compilers.ir.IR getFirstChild();

    public abstract edu.mit.compilers.ir.IR getNextSibling();

    public abstract String getText();
}