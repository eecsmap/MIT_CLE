package edu.mit.compilers.st;

import edu.mit.compilers.st.Descriptor;

public class MethodDesc extends Descriptor{
    private ST localST;

    public final boolean findVar(String name) {
        return localST.contains(name);
    }

    public final boolean findMethod(String name) {
        return false;
    }
}
