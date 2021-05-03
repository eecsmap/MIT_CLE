package edu.mit.compilers.ast;
import antlr.CommonAST;
import antlr.Token;

public class AstWithPosition extends CommonAST {
    private int line = 0;
    private int column = 0;
    
    public void initialize(Token token) {
        super.initialize(token);
        line = token.getLine();
        column = token.getColumn();
    }
    public int getLine() { 
        return line; 
    }

    public int getColumn() { 
        return column; 
    }
}
