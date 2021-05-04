package edu.mit.compilers;

import java.io.*;

import antlr.ASTFactory;
import antlr.CharStreamException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStreamException;
import antlr.collections.AST;
import edu.mit.compilers.ast.AstWithPosition;
import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.grammar.*;
import edu.mit.compilers.syntax.Program;
import edu.mit.compilers.tools.CLI;
import edu.mit.compilers.tools.Err;
import edu.mit.compilers.tools.CLI.Action;

class Main {
    static String[] opts = {"cse"};
    public static void main(String[] args) {
        try {
            CLI.parse(args, opts);
            InputStream inputStream = args.length == 0 ? System.in : new java.io.FileInputStream(CLI.infile);
            PrintStream outputStream = 
                (CLI.outfile == null)
                ? System.out
                : new java.io.PrintStream(new java.io.FileOutputStream(CLI.outfile));
            if (CLI.target == Action.SCAN) {
                scan(inputStream, outputStream);
            } else if (CLI.target == Action.PARSE) {
                parse(inputStream, true);
            } else if (CLI.target == Action.INTER) {
                inter(inputStream);
            } else if (CLI.target == Action.ASSEMBLY || CLI.target == Action.DEFAULT) {
                assembly(inputStream, outputStream);
            }
        } catch (Exception e) {
            // print the error:
            e.printStackTrace(new java.io.PrintStream(System.err));
            // System.err.println(CLI.infile+" "+e);
            System.exit(1);
        }
    }

    private static void scan(InputStream inputStream, PrintStream outputStream) throws CharStreamException {
        DecafScanner scanner = new DecafScanner(new DataInputStream(inputStream));
        scanner.setTrace(CLI.debug);
        Token token;
        boolean done = false;
        while (!done) {
            try {
                for (
                token = scanner.nextToken();
                token.getType() != DecafParserTokenTypes.EOF;
                token = scanner.nextToken()) {
                    String type = "";
                    String text = token.getText();
                    switch (token.getType()) {
                        case DecafScannerTokenTypes.CHARLITERAL:
                            type = " CHARLITERAL";
                            break;
                        case DecafScannerTokenTypes.INTLITERAL:
                            type = " INTLITERAL";
                            break;
                        case DecafScannerTokenTypes.STRINGLITERAL:
                            type = " STRINGLITERAL";
                            break;
                        case DecafScannerTokenTypes.ID:
                            type = " IDENTIFIER";
                            break;
                        case DecafScannerTokenTypes.TK_false:
                        case DecafScannerTokenTypes.TK_true:
                            type = " BOOLEANLITERAL";
                            break;
                        default:
                            type = "";
                            break;
                    }
                    outputStream.println(token.getLine() + type + " " + text);
                }
                done = true;
            } catch(Exception e) {
                // print the error:
                System.err.println(CLI.infile + " " + e);
                scanner.consume();
            }
        }
    }

    private static AST parse(InputStream inputStream, Boolean toPrint) throws RecognitionException, TokenStreamException {
        DecafScanner scanner = new DecafScanner(new DataInputStream(inputStream));
        DecafParser parser = new DecafParser(scanner);
        ASTFactory factory = new ASTFactory();                         
        factory.setASTNodeClass(AstWithPosition.class);
        parser.setASTFactory(factory);
        parser.program();
        if(parser.getError()) {
            System.exit(1);
        }
        if (toPrint) {
            AstUtils.printAST(parser.getAST(), 0);
        }
        return parser.getAST();
    }

    private static void inter(InputStream inputStream) throws RecognitionException, TokenStreamException {
        AST t = parse(inputStream, false);
        // AstUtils.printAST(t, 0);
        if (CLI.debug) {
            Err.setTrace();
        }
        Program.irParse(t, null);
        if(Err.hasError()) {
            System.exit(1);
        }
    }

    private static void assembly(InputStream inputStream, PrintStream outputStream) 
    throws RecognitionException, TokenStreamException {
        AST t = parse(inputStream, false);
        // AstUtils.printAST(t, 0);
        if (CLI.debug) {
            Err.setTrace();
        }
        ABlock codes = new ABlock();
        Program.irParse(t, codes);
        if(Err.hasError()) {
            System.exit(1);
        } else {
            codes.print(outputStream);
        }
    }
}
