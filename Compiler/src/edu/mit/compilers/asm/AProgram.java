package edu.mit.compilers.asm;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.cfg.CMethod;

public class AProgram {
    private ABlock globalHead = new ABlock();
    private List<ABlock> methods = new ArrayList<>();
    private ABlock globalTail = new ABlock();

    public AProgram() {}

    public ABlock getHead() {
        return this.globalHead;
    }

    public ABlock getTail() {
        return this.globalTail;
    }

    public void addMethod(ABlock method) {
        this.methods.add(method);
    }

    public List<CMethod> split() {
        List<CMethod> methods = new ArrayList<>();
        for (int i = 0; i < this.methods.size(); i++) {
            methods.add(new CMethod(this.methods.get(i)));
        }
        return methods;
    }

    public void join(List<CMethod> methods) {
        this.methods = new ArrayList<>();
        for (int i = 0; i < methods.size(); i++) {
            this.methods.add(methods.get(i).makeAMethod());
        }
        return;
    }

    public void print(PrintStream stream) {
        this.globalHead.print(stream);
        for (int i = 0; i < this.methods.size(); i++) {
            this.methods.get(i).print(stream);
        }
        this.globalTail.print(stream);
    }
}
