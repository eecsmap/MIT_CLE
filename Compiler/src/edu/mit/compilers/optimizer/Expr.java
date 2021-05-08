package edu.mit.compilers.optimizer;

import java.math.BigInteger;

import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Oprand;

public class Expr {
    public enum Type {
        ADD, // supports add and sub
        MUL, // supports mul
    }

    private Type type;
    private Long privateLong;
    private BigInteger primeProduct;

    public Expr(Type type, Long privateLong, BigInteger primeProduct) {
        this.type = type;
        this.privateLong = privateLong;
        this.primeProduct = primeProduct;
    }

    public Expr(Expr rhs) {
        this.type = rhs.type;
        this.privateLong = rhs.privateLong;
        this.primeProduct = rhs.primeProduct;
    }

    public Expr makePut(Oprand op) {
        Expr res = new Expr(this.type, this.privateLong, this.primeProduct);
        if (op instanceof Num) {
            Long value = ((Num)op).getValue();
            if (res.type.equals(Type.ADD)) {
                res.privateLong += value;
            } else {
                res.privateLong *= value;
            }
        } else {
            res.primeProduct = res.primeProduct.multiply(op.getPrime());
        }
        return res;
    }

    public boolean contains(Oprand op) {
        return this.primeProduct.divideAndRemainder(op.getPrime())[1].equals(BigInteger.ZERO) ;
    }

    public int compare(Expr rhs) {
        if (this.type.equals(Type.ADD) && rhs.type.equals(Type.MUL)) {
            return -1;
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
