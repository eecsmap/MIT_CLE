package edu.mit.compilers.cfg;

// =, +, -, ++, --, unary -, ? :, unary !, &&, ||
// IT'S NOT CHAINED!
public class BinaryOp {
    private String op;
    private Term l;
    private Term r;

    public BinaryOp(Term l, Term r, String op) {
        this.l = l;
        this.r = r;
        this.op = op;
    }
}
