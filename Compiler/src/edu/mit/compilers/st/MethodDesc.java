package edu.mit.compilers.st;

import java.util.List;

import edu.mit.compilers.defs.VarType;

public class MethodDesc extends Descriptor{
    private List<VarType> paramsList;

    public MethodDesc(VarType type, String text) {
        super(type.makeMethod(), text);
    }

    public void setParamsList(List<VarType> paramsList) {
        this.paramsList = paramsList;
    }

    public List<VarType> getParamsList() {
        return this.paramsList;
    }
}