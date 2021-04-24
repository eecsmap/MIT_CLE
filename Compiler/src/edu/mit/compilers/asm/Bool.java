package edu.mit.compilers.asm;

// Number
public class Bool extends Oprand {
    Boolean b;

    public Bool(Boolean b) {
        this.b = b;
    }

    public Boolean Exclam() {
        return new Bool(!this.b);
    }

    @Override
    public String toString() {
        return this.b ? "$1" : "$0";
    }
}
