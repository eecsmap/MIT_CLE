header {
package edu.mit.compilers.grammar;
}

options
{
  mangleLiteralPrefix = "TK_";
  language = "Java";
}

{@SuppressWarnings("unchecked")}
class DecafScanner extends Lexer;
options
{
  k = 2;
}

tokens 
{
  "class"; 
  "if"; "while"; "for";
  "return"; "break"; "continue";
  "false"; "true";
  "boolean"; "callout"; "else";
  "void"; "int";
}

// Selectively turns on debug tracing mode.
// You can insert arbitrary Java code into your parser/lexer this way.
{
  /** Whether to display debug information. */
  private boolean trace = false;

  public void setTrace(boolean shouldTrace) {
    trace = shouldTrace;
  }
  @Override
  public void traceIn(String rname) throws CharStreamException {
    if (trace) {
      super.traceIn(rname);
    }
  }
  @Override
  public void traceOut(String rname) throws CharStreamException {
    if (trace) {
      super.traceOut(rname);
    }
  }
}

SUB : "-";
ADD : "+";
MUL : "*";
DIV : "/";
MOD : "%";
LT  : "<";
LE  : "<=";
GT  : ">";
GE  : ">=";
NE  : "!=";
EQ  : "==";
AND : "&&";
OR  : "||";
// INC : "++";
// DEC : "--";
ME  : "-=";
AE  : "+=";
ASS : "=";


LCURLY options { paraphrase = "{"; } : "{";
RCURLY options { paraphrase = "}"; } : "}";
COMMA   : ",";
LPAR  : "(";
RPAR  : ")";
LBRA  : "[";
RBRA  : "]";
SEMICOLON : ";";



ID options { paraphrase = "an identifier"; testLiterals=true; } 
  : ('a'..'z' | 'A'..'Z' | '_' ) ('0'..'9' | 'a'..'z' | 'A'..'Z' | '_' )*
  ;

// Note that here, the {} syntax allows you to literally command the lexer
// to skip mark this token as skipped, or to advance to the next line
// by directly adding Java commands.
WS_ : (' ' | '\t' | '\n' {newline();}) {_ttype = Token.SKIP; };
SL_COMMENT : "//" (~'\n')* '\n' {_ttype = Token.SKIP; newline (); };

CHARLITERAL
  :	'\'' ( ESC | ~('\''|'\n'|'\r'|'\\'|'"'|'\t') ) '\''
  ;

STRINGLITERAL : '"' ( ESC | ~('\''|'\n'|'\r'|'\\'|'"'|'\t') )* '"';

protected
ESC :  '\\' ('n'|'"'|'t'|'\\'|'\'');

INTLITERAL
  : ('0'..'9')+
  | "0x" ('0'..'9' | 'A'..'F' | 'a'..'f')+
  ;