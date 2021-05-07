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

    public void put(Oprand op) {
        if (op instanceof Num) {
            Long value = ((Num)op).getValue();
            if (this.type.equals(Type.ADD)) {
                this.privateLong += value;
            } else {
                this.privateLong *= value;
            }
        } else {
            this.primeProduct = this.primeProduct.multiply(op.getPrime());
        }
    }

    public boolean contains(Oprand op) {
        return this.primeProduct.divideAndRemainder(op.getPrime())[1].equals(BigInteger.ZERO) ;
    }
}
