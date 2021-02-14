package edu.mit.compilers;

import java.io.*;
import java.text.DecimalFormat;

import antlr.CharStreamException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStreamException;
import antlr.collections.AST;
import edu.mit.compilers.grammar.*;
import edu.mit.compilers.tools.CLI;
import edu.mit.compilers.tools.CLI.Action;
import edu.mit.compilers.ir.*;

class Main {
  public static void main(String[] args) {
    try {
      CLI.parse(args, new String[0]);
      InputStream inputStream = args.length == 0 ?
          System.in : new java.io.FileInputStream(CLI.infile);
      PrintStream outputStream = CLI.outfile == null ? System.out : new java.io.PrintStream(new java.io.FileOutputStream(CLI.outfile));
      if (CLI.target == Action.SCAN) {
        scan(inputStream, outputStream);
      } else if (CLI.target == Action.PARSE) {
        parse(inputStream);
      } else if (CLI.target == Action.INTER || CLI.target == Action.DEFAULT) {
        inter(inputStream);
      }
    } catch(Exception e) {
      // print the error:
      System.err.println(CLI.infile+" "+e);
    }
  }

  private static void scan(InputStream inputStream, PrintStream outputStream) throws CharStreamException {
    DecafScanner scanner =
    new DecafScanner(new DataInputStream(inputStream));
    scanner.setTrace(CLI.debug);
    Token token;
    boolean done = false;
    while (!done) {
      try {
        for (token = scanner.nextToken();
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
            case DecafScannerTokenTypes.Variable:
              type = " IDENTIFIER";
              break;
            case DecafScannerTokenTypes.TK_false:
            case DecafScannerTokenTypes.TK_true:
              type = " BOOLEANLITERAL";
              break;
            default:
              type = Integer.toString(token.getType());
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

  private static void parse(InputStream inputStream) throws RecognitionException, TokenStreamException {
    DecafScanner scanner = new DecafScanner(new DataInputStream(inputStream));
    DecafParser parser = new DecafParser(scanner);
    parser.setTrace(CLI.debug);
    parser.program();
    if(parser.getError()) {
      System.exit(1);
    }
  }

  private static void inter(InputStream inputStream) throws RecognitionException, TokenStreamException {
    DecafScanner scanner = new DecafScanner(new DataInputStream(inputStream));
    DecafParser parser = new DecafParser(scanner);
    parser.setTrace(CLI.debug);
    parser.program();
    if(parser.getError()) {
      System.exit(1);
    }
    AST t = parser.getAST();
    AstUtils.printAST(t, 0);
  }
}
