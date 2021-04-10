package edu.mit.compilers.cfg;
import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.Label;

public class Block {
    private List<BinaryOp> ops;
    private List<String> codeList;
    private Label label;

    public Block() {
        this.ops = new ArrayList<>();
        this.codeList = new ArrayList<>();
        this.label = new Label();
        for (BinaryOp op: this.ops) {
            codeList.addAll(op.getCodeList());
        }
    }

    public List<String> getCodeList() {
        return this.codeList;
    }

    public Label getLabel() {
        return this.label;
    }

    public void print() {
        for (String asmInst: this.codeList) {
            System.out.println(asmInst);
        }
    }
}
