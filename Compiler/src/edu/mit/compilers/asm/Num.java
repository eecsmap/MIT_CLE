package edu.mit.compilers.asm;

// Number
public class Num extends Oprand {
    Integer num;

    public Num(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return String.format("$%d", this.num);
    }
}
