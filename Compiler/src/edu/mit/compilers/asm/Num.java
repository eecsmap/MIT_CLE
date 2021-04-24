package edu.mit.compilers.asm;

// Number
public class Num extends Oprand {
    Integer num;

    public Num(Integer num) {
        this.num = num;
    }

    public Num neg() {
        return new Num(-this.num);
    }

    @Override
    public String toString() {
        return String.format("$%d", this.num);
    }
}
