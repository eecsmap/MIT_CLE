package edu.mit.compilers.cfg;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import antlr.collections.AST;


public class cfgBlock {
    private Defs.Type type;
    private List<BinaryOp> ops;

    cfgBlock(Defs.Type type) {
        this.type = type;
        this.ops = new ArrayList<>();
    }
    
    public List<BinaryOp> get() {
        return this.ops;
    }

    public List<String> codegen() {
        // use type
        List<String> codes = new ArrayList<>();
        for (BinaryOp op: this.ops) {
            codes.addAll(op.codegen());
        }
        return codes;
    }

    public void print() {
        for (String asmInst: this.codegen()) {
            System.out.println(asmInst);
        }
    }
}
