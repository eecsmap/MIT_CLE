package edu.mit.compilers.st;

import edu.mit.compilers.st.ST;

public class ClassDesc extends Descriptor {
    private ST methodST;
    private ST fieldST;

    public final boolean findVar(String name) {
        return fieldST.contains(name);
    }

    public final boolean findMethod(String name) {
        return methodST.contains(name);
    }
}
