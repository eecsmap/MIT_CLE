package edu.mit.compilers.st;
 
// ClassDesc -> "class name" "name", "fieldST" "methodST"
// MethodDesc -> "return type" "name", "localST"
// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
// ThisDesc -> "this" "classdesc name", ""
public abstract class Descriptor {
    protected String type;
    protected String text;
    protected String addr;

    protected Descriptor() { 
        // default descriptor
    }

    protected Descriptor(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public abstract String findVar(String text);

    public abstract String findMethod(String text);

    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return this.type;
    }
    
    public String getText() {
        return this.text;
    }

    public String getAddr() {
        return this.addr;
    }
}
