package edu.mit.compilers.asm;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.cfg.CMethod;

public class AProgram {
    private AMethod globalHead = new AMethod();
    private List<AMethod> methods = new ArrayList<>();
    private AMethod globalTail = new AMethod();

    public AProgram() {}

    public AMethod getHead() {
        return this.globalHead;
    }

    public AMethod getTail() {
        return this.globalTail;
    }

    public void addMethod(AMethod method) {
        this.methods.add(method);
    }

    public List<CMethod> split() {
        List<CMethod> methods = new ArrayList<>();
        for (int i = 0; i < this.methods.size(); i++) {
            methods.add(new CMethod(this.methods.get(i)));
        }
        return methods;
    }

    public void print(PrintStream stream) {
        this.globalHead.print(stream);
        for (int i = 0; i < this.methods.size(); i++) {
            this.methods.get(i).print(stream);
        }
        this.globalTail.print(stream);
    }
}
