package edu.mit.compilers.cfg;
import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.Label;
import edu.mit.compilers.ir.IrOp;

public class Block {
    private List<Operator> ops;
    private List<String> codeList;
    private Label beginLabel;
    private Label endLabel;

    public Block(List<IrOp> IrOpList) {
        this.ops = new ArrayList<>();
        this.codeList = new ArrayList<>();
        this.beginLabel= new Label();
        this.endLabel = new Label();
        IrOpList.forEach(
            e -> ops.add(e.makeOperator())
        );
        ops.forEach(
            e -> codeList.addAll(e.getCodeList())
        );
    }

    public List<String> getCodeList() {
        return this.codeList;
    }

    public Label getBeginLabel() {
        return this.beginLabel;
    }

    public Label getEndLabel() {
        return this.endLabel;
    }

    public void appendLeft(String code) {
        this.codeList.add(0, code);
    }

    public void append(String code) {
        this.codeList.add(code);
    }

    public void print() {
        for (String asmInst: this.codeList) {
            System.out.println(asmInst);
        }
    }
}
