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

    private int varCount = 0;
    private Type type;
    private Long privateLong;
    private BigInteger primeProduct;

    public Expr(Oprand op) {
        this.type = Type.DEFAULT;
        if (op instanceof Num) {
            this.privateLong = ((Num)op).getValue();
        } else {
            this.primeProduct = op.getPrime();
        }
    }

    // public Expr(Expr rhs) {
    //     this.type = rhs.type;
    //     this.privateLong = rhs.privateLong;
    //     this.primeProduct = rhs.primeProduct;
    // }

    public void put(Type type, Oprand op) {
        if (this.varCount >= 2) {
            return;
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
    }

    // public Expr makePut(Oprand op) {
    //     Expr res = new Expr(this.type, this.privateLong, this.primeProduct);
    //     if (op instanceof Num) {
    //         Long value = ((Num)op).getValue();
    //         if (res.type.equals(Type.ADD)) {
    //             res.privateLong += value;
    //         } else {
    //             res.privateLong *= value;
    //         }
    //     } else {
    //         res.primeProduct = res.primeProduct.multiply(op.getPrime());
    //     }
    //     return res;
    // }

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
}
