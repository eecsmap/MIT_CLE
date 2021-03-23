package edu.mit.compilers.cfg;

import edu.mit.compilers.st.Descriptor;

// TODO: which descriptor?
// action: [load store]
public class Term {
    private Descriptor desc;
    private String action;

    public Term(Descriptor desc, String action) {
        this.desc = desc;
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }

    public String getDescAddr() {
        return this.desc.getAddr();
    }
}
