header {
package edu.mit.compilers.grammar;
}

options
{
  mangleLiteralPrefix = "TK_";
  language = "Java";
}

class DecafParser extends Parser;
options
{
  importVocab = DecafScanner;
  k = 3;
  buildAST = true;
}

// Java glue code that makes error reporting easier.
// You can insert arbitrary Java code into your parser/lexer this way.
{
  // Do our own reporting of errors so the parser can return a non-zero status
  // if any errors are detected.
  /** Reports if any errors were reported during parse. */
  private boolean error;

  @Override
  public void reportError (RecognitionException ex) {
    // Print the error via some kind of error reporting mechanism.
    System.err.println(ex.toString());
    error = true;
  }
  @Override
  public void reportError (String s) {
    // Print the error via some kind of error reporting mechanism.
    System.err.println(s);
    error = true;
  }
  public boolean getError () {
    return error;
  }

  // Selectively turns on debug mode.
 
  /** Whether to display debug information. */
  private boolean trace = false;

  public void setTrace(boolean shouldTrace) {
    trace = shouldTrace;
  }
  @Override
  public void traceIn(String rname) throws TokenStreamException {
    if (trace) {
      super.traceIn(rname);
    }
  }
  @Override
  public void traceOut(String rname) throws TokenStreamException {
    if (trace) {
      super.traceOut(rname);
    }
  }
}

program : (import_decl)* (field_decl)* (method_decl)* EOF!;

import_decl : TK_import^ ID SEMICOLON!;

field_decl : (TK_int^|TK_bool^) field (COMMA! field)* SEMICOLON!;
field : ID^ (LSQUAR! INTLITERAL RSQUAR!)?;

method_decl : return_type ID^ LPAREN! method_params RPAREN! block;
return_type : type | TK_void;
method_params : (method_param (COMMA! method_param)*)?;
method_param : type ID^;

block : LCURLY! (field_decl)* (statement)* RCURLY!;

statement :
        location ((ASSIGN^|PLUSASSIGN^|MINUSASSIGN^) expr | (INCRE^|DECRE^)) SEMICOLON!
    |   method_call SEMICOLON!
    |   if_
    |   for_
    |   while_
    |   break_
    |   return_
    |   continue_;

assignment : location ASSIGN^ expr;
for_assign : 
    location (
            (PLUSASSIGN^|MINUSASSIGN^) expr
        |   (INCRE^|DECRE^)
    );
if_ : TK_if^ LPAREN! expr RPAREN! block (else_)?;
else_ : TK_else^ block;
for_  : TK_for^ LPAREN! assignment SEMICOLON! expr SEMICOLON! for_assign RPAREN! block;
while_ : TK_while^ LPAREN! expr RPAREN! block;
break_ : TK_break^ SEMICOLON!;
return_ : TK_return^ (expr)? SEMICOLON!;
continue_ : TK_continue^ SEMICOLON!;

method_call : ID^ LPAREN! (method_args)? RPAREN!;
method_args : method_arg (COMMA! method_arg)*;
method_arg : expr | STRINGLITERAL;

location : ID^ (LSQUAR! expr RSQUAR!)?;

length : TK_len^ LPAREN! ID RPAREN!;

type : TK_int | TK_bool;
expr : quesexpr;
atom : location | literal | LPAREN! expr RPAREN! | method_call | length;
minusexpr : MINUS^ minusexpr | atom;
exclamexpr : EXCLAM^ exclamexpr | minusexpr;
multiexpr : exclamexpr ((TIMES^|SLASH^|PERCENT^) exclamexpr)*;
addexpr : multiexpr ((PLUS^|MINUS^) multiexpr)*;
compexpr : addexpr ((GREATER^|LESS^|GE^|LE^) addexpr)*;
eqexpr : compexpr ((EQ^|NEQ^) compexpr)*;
andexpr : eqexpr (AND^ eqexpr)*;
orexpr : andexpr (OR^ andexpr)*;
quesexpr : orexpr (QUESTION^ quesexpr COLON^ quesexpr)?;

literal : INTLITERAL | CHARLITERAL | TK_true | TK_false;