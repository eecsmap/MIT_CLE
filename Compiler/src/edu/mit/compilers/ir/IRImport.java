package edu.mit.compilers.ir;

import antlr.collections.AST;

// type: void
public class IRImport extends IR {
    public IRImport(AST t) {
        type = "void";
        text = t.getFirstChild().getText();
    }

    public final String execute() {
        return "";
    }
}