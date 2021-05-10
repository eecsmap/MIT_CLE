package edu.mit.compilers.tools;

import java.math.BigInteger;
import java.util.Iterator;
/*
    copied from https://stackoverflow.com/questions/1042902/most-elegant-way-to-generate-prime-numbers @dfa
*/
public class PrimeGenerator implements Iterator<BigInteger>, Iterable<BigInteger> {

    private BigInteger p = BigInteger.ONE;

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public BigInteger next() {
        p = p.nextProbablePrime();
        return p;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Iterator<BigInteger> iterator() {
        return this;
    }
}