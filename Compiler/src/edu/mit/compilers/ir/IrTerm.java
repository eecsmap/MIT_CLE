package edu.mit.compilers.ir;

import edu.mit.compilers.st.Descriptor;

// TODO: which descriptor?
// action: [load store]
public class IrTerm {
    private Descriptor desc;
    private String action;

    public IrTerm() {

    }

    public String getAction() {
        return this.action;
    }

    public String getDescAddr() {
        return this.desc.getAddr();
    }
}
