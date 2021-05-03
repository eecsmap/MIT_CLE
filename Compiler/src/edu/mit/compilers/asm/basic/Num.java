package edu.mit.compilers.asm.basic;

// Number
public class Num extends Oprand {
    Long num;

    public Num(Long num) {
        this.num = num;
    }

    public Num neg() {
        return new Num(-this.num);
    }

    @Override
    public String toString() {
        return String.format("$%d", this.num);
    }

    @Override
    public String getName() {
        return "";
    }
}
