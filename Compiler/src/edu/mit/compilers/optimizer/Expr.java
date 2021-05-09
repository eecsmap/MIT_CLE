package edu.mit.compilers.optimizer;

import java.math.BigInteger;

import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Oprand;

public class Expr {
    public enum Type {
        DEFAULT(0),
        ADD(1), // supports add
        SUB(2), // supports sub
        MUL(3); // supports mul

        public final Integer innerValue;

        private Type(Integer innerValue) {
            this.innerValue = innerValue;
        }
    }

    private int varCount;
    private String str = "";
    private Type type;
    private Long privateLong;
    private BigInteger primeProduct;

    public Expr(Oprand op) {
        this.varCount = 1;
        this.type = Type.DEFAULT;
        if (op instanceof Num) {
            this.privateLong = ((Num)op).getValue();
            this.primeProduct = new BigInteger("1");
        } else {
            this.privateLong = 0L;
            this.primeProduct = op.getPrime();
        }
        // debug
        this.str = op.toString();
    }

    public boolean put(Type type, Oprand op) {
        if (this.varCount >= 2) {
            return false;
        }
        this.type = type;
        this.varCount++;
        if (op instanceof Num) {
            Long value = ((Num)op).getValue();
            if (this.type.equals(Type.ADD)) {
                this.privateLong += value;
            } else if (this.type.equals(Type.SUB)) {
                this.privateLong -= value;
            } else {
                this.privateLong *= value;
            }
        } else {
            this.primeProduct = this.primeProduct.multiply(op.getPrime());
        }
        // debug
        this.str += ", " + op.toString();
        return true;
    }

    public boolean contains(Oprand op) {
        return this.primeProduct.divideAndRemainder(op.getPrime())[1].equals(BigInteger.ZERO);
    }

    public int compare(Expr rhs) {
        if (!this.type.equals(rhs.type)) {
            return this.type.innerValue - rhs.type.innerValue;
        }
        if (this.privateLong != rhs.privateLong) {
            return (this.privateLong > rhs.privateLong) ? 1 : -1;
        }
        return this.primeProduct.compareTo(rhs.primeProduct);
    }

    public boolean equals(Expr rhs) {
        return this.type.equals(rhs.type) && this.privateLong == rhs.privateLong && this.primeProduct.equals(rhs.primeProduct);
    }

    public String toString() {
        return this.str;
    }
}
