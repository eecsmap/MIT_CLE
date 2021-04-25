package edu.mit.compilers;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import antlr.ASTFactory;
import antlr.CharStreamException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStreamException;
import antlr.collections.AST;
import edu.mit.compilers.ast.myAST;
import edu.mit.compilers.compile.Program;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.grammar.*;
import edu.mit.compilers.tools.CLI;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.tools.CLI.Action;

class Main {
    public static void main(String[] args) {
        try {
            CLI.parse(args, new String[0]);
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
        factory.setASTNodeClass(myAST.class);
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
            Er.setTrace();
        }
        Program.irParse(t, null);
        if(Er.hasError()) {
            System.exit(1);
        }
    }

    private static void assembly(InputStream inputStream, PrintStream outputStream) 
    throws RecognitionException, TokenStreamException {
        AST t = parse(inputStream, false);
        // AstUtils.printAST(t, 0);
        if (CLI.debug) {
            Er.setTrace();
        }
        List<String> codes = new ArrayList<>();
        Program.irParse(t, codes);
        if(Er.hasError()) {
            System.exit(1);
        } else {
            codes.forEach(outputStream::println);
        }
    }
}
