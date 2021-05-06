package edu.mit.compilers.asm.basic;

import java.math.BigInteger;

import edu.mit.compilers.tools.PrimeGenerator;

public abstract class Oprand {
    private static PrimeGenerator primeGenerator = new PrimeGenerator();
    private BigInteger internalPrime;

    Oprand() {
        this.internalPrime = primeGenerator.next();
    }

    public BigInteger getPrime() {
        return this.internalPrime;
    }

    public abstract String toString();
    
    public abstract String getName();
}
