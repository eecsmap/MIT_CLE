package edu.mit.compilers.cfg;
import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;


public class cfgBlock {
    private Defs.Type type;
    private List<BinaryOp> oprations;

    cfgBlock(Defs.Type type) {
        this.type = type;
        this.oprations = new ArrayList<>();
    }
    
    public List<BinaryOp> get() {
        return this.oprations;
    }

    public List<String> codegen() {
        // TODO
        return null;
    }

    public void print() {
        // TODO
    }
}
