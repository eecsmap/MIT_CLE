// $ANTLR 2.7.7 (2006-11-01): "parser.g" -> "DecafParser.java"$

package edu.mit.compilers.grammar;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

public class DecafParser extends antlr.LLkParser       implements DecafParserTokenTypes
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

protected DecafParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public DecafParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected DecafParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public DecafParser(TokenStream lexer) {
  this(lexer,3);
}

public DecafParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void program() throws RecognitionException, TokenStreamException {
		
		traceIn("program");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST program_AST = null;
			
			try {      // for error handling
				{
				_loop3:
				do {
					if ((LA(1)==TK_import)) {
						import_decl();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop3;
					}
					
				} while (true);
				}
				{
				_loop5:
				do {
					if ((LA(1)==TK_bool||LA(1)==TK_int) && (LA(2)==ID) && (LA(3)==LSQUAR||LA(3)==SEMICOLON||LA(3)==COMMA)) {
						field_decl();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop5;
					}
					
				} while (true);
				}
				{
				_loop7:
				do {
					if ((LA(1)==TK_bool||LA(1)==TK_int||LA(1)==TK_void)) {
						method_decl();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop7;
					}
					
				} while (true);
				}
				match(Token.EOF_TYPE);
				program_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			}
			returnAST = program_AST;
		} finally { // debugging
			traceOut("program");
		}
	}
	
	public final void import_decl() throws RecognitionException, TokenStreamException {
		
		traceIn("import_decl");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST import_decl_AST = null;
			
			try {      // for error handling
				AST tmp2_AST = null;
				tmp2_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp2_AST);
				match(TK_import);
				AST tmp3_AST = null;
				tmp3_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp3_AST);
				match(ID);
				match(SEMICOLON);
				import_decl_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			}
			returnAST = import_decl_AST;
		} finally { // debugging
			traceOut("import_decl");
		}
	}
	
	public final void field_decl() throws RecognitionException, TokenStreamException {
		
		traceIn("field_decl");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST field_decl_AST = null;
			
			try {      // for error handling
				{
				switch ( LA(1)) {
				case TK_int:
				{
					AST tmp5_AST = null;
					tmp5_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp5_AST);
					match(TK_int);
					break;
				}
				case TK_bool:
				{
					AST tmp6_AST = null;
					tmp6_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp6_AST);
					match(TK_bool);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				field();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop12:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						field();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop12;
					}
					
				} while (true);
				}
				match(SEMICOLON);
				field_decl_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_2);
			}
			returnAST = field_decl_AST;
		} finally { // debugging
			traceOut("field_decl");
		}
	}
	
	public final void method_decl() throws RecognitionException, TokenStreamException {
		
		traceIn("method_decl");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST method_decl_AST = null;
			
			try {      // for error handling
				return_type();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp9_AST = null;
				tmp9_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp9_AST);
				match(ID);
				match(LPAREN);
				method_params();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				block();
				astFactory.addASTChild(currentAST, returnAST);
				method_decl_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			}
			returnAST = method_decl_AST;
		} finally { // debugging
			traceOut("method_decl");
		}
	}
	
	public final void field() throws RecognitionException, TokenStreamException {
		
		traceIn("field");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST field_AST = null;
			
			try {      // for error handling
				AST tmp12_AST = null;
				tmp12_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp12_AST);
				match(ID);
				{
				switch ( LA(1)) {
				case LSQUAR:
				{
					match(LSQUAR);
					AST tmp14_AST = null;
					tmp14_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp14_AST);
					match(INTLITERAL);
					match(RSQUAR);
					break;
				}
				case SEMICOLON:
				case COMMA:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				field_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			}
			returnAST = field_AST;
		} finally { // debugging
			traceOut("field");
		}
	}
	
	public final void return_type() throws RecognitionException, TokenStreamException {
		
		traceIn("return_type");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST return_type_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case TK_bool:
				case TK_int:
				{
					type();
					astFactory.addASTChild(currentAST, returnAST);
					return_type_AST = (AST)currentAST.root;
					break;
				}
				case TK_void:
				{
					AST tmp16_AST = null;
					tmp16_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp16_AST);
					match(TK_void);
					return_type_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
			returnAST = return_type_AST;
		} finally { // debugging
			traceOut("return_type");
		}
	}
	
	public final void method_params() throws RecognitionException, TokenStreamException {
		
		traceIn("method_params");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST method_params_AST = null;
			
			try {      // for error handling
				{
				switch ( LA(1)) {
				case TK_bool:
				case TK_int:
				{
					method_param();
					astFactory.addASTChild(currentAST, returnAST);
					{
					_loop20:
					do {
						if ((LA(1)==COMMA)) {
							match(COMMA);
							method_param();
							astFactory.addASTChild(currentAST, returnAST);
						}
						else {
							break _loop20;
						}
						
					} while (true);
					}
					break;
				}
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				method_params_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			}
			returnAST = method_params_AST;
		} finally { // debugging
			traceOut("method_params");
		}
	}
	
	public final void block() throws RecognitionException, TokenStreamException {
		
		traceIn("block");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST block_AST = null;
			
			try {      // for error handling
				match(LCURLY);
				{
				_loop24:
				do {
					if ((LA(1)==TK_bool||LA(1)==TK_int)) {
						field_decl();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop24;
					}
					
				} while (true);
				}
				{
				_loop26:
				do {
					if ((_tokenSet_7.member(LA(1)))) {
						statement();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop26;
					}
					
				} while (true);
				}
				match(RCURLY);
				block_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			}
			returnAST = block_AST;
		} finally { // debugging
			traceOut("block");
		}
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		traceIn("type");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST type_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case TK_int:
				{
					AST tmp20_AST = null;
					tmp20_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp20_AST);
					match(TK_int);
					type_AST = (AST)currentAST.root;
					break;
				}
				case TK_bool:
				{
					AST tmp21_AST = null;
					tmp21_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp21_AST);
					match(TK_bool);
					type_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
			returnAST = type_AST;
		} finally { // debugging
			traceOut("type");
		}
	}
	
	public final void method_param() throws RecognitionException, TokenStreamException {
		
		traceIn("method_param");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST method_param_AST = null;
			
			try {      // for error handling
				type();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp22_AST = null;
				tmp22_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp22_AST);
				match(ID);
				method_param_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_9);
			}
			returnAST = method_param_AST;
		} finally { // debugging
			traceOut("method_param");
		}
	}
	
	public final void statement() throws RecognitionException, TokenStreamException {
		
		traceIn("statement");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST statement_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case TK_if:
				{
					if_();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				case TK_for:
				{
					for_();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				case TK_while:
				{
					while_();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				case TK_break:
				{
					break_();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				case TK_return:
				{
					return_();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				case TK_continue:
				{
					continue_();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				default:
					if ((LA(1)==ID) && (_tokenSet_10.member(LA(2)))) {
						location();
						astFactory.addASTChild(currentAST, returnAST);
						{
						switch ( LA(1)) {
						case ASSIGN:
						case PLUSASSIGN:
						case MINUSASSIGN:
						{
							{
							switch ( LA(1)) {
							case ASSIGN:
							{
								AST tmp23_AST = null;
								tmp23_AST = astFactory.create(LT(1));
								astFactory.makeASTRoot(currentAST, tmp23_AST);
								match(ASSIGN);
								break;
							}
							case PLUSASSIGN:
							{
								AST tmp24_AST = null;
								tmp24_AST = astFactory.create(LT(1));
								astFactory.makeASTRoot(currentAST, tmp24_AST);
								match(PLUSASSIGN);
								break;
							}
							case MINUSASSIGN:
							{
								AST tmp25_AST = null;
								tmp25_AST = astFactory.create(LT(1));
								astFactory.makeASTRoot(currentAST, tmp25_AST);
								match(MINUSASSIGN);
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
							expr();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case INCRE:
						case DECRE:
						{
							{
							switch ( LA(1)) {
							case INCRE:
							{
								AST tmp26_AST = null;
								tmp26_AST = astFactory.create(LT(1));
								astFactory.makeASTRoot(currentAST, tmp26_AST);
								match(INCRE);
								break;
							}
							case DECRE:
							{
								AST tmp27_AST = null;
								tmp27_AST = astFactory.create(LT(1));
								astFactory.makeASTRoot(currentAST, tmp27_AST);
								match(DECRE);
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						match(SEMICOLON);
						statement_AST = (AST)currentAST.root;
					}
					else if ((LA(1)==ID) && (LA(2)==LPAREN)) {
						method_call();
						astFactory.addASTChild(currentAST, returnAST);
						match(SEMICOLON);
						statement_AST = (AST)currentAST.root;
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = statement_AST;
		} finally { // debugging
			traceOut("statement");
		}
	}
	
	public final void location() throws RecognitionException, TokenStreamException {
		
		traceIn("location");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST location_AST = null;
			
			try {      // for error handling
				AST tmp30_AST = null;
				tmp30_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp30_AST);
				match(ID);
				{
				switch ( LA(1)) {
				case LSQUAR:
				{
					match(LSQUAR);
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					match(RSQUAR);
					break;
				}
				case RSQUAR:
				case RPAREN:
				case SEMICOLON:
				case MINUS:
				case PLUS:
				case ASSIGN:
				case TIMES:
				case GREATER:
				case LESS:
				case GE:
				case LE:
				case EQ:
				case NEQ:
				case AND:
				case OR:
				case COMMA:
				case QUESTION:
				case SLASH:
				case PERCENT:
				case PLUSASSIGN:
				case MINUSASSIGN:
				case COLON:
				case INCRE:
				case DECRE:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				location_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_12);
			}
			returnAST = location_AST;
		} finally { // debugging
			traceOut("location");
		}
	}
	
	public final void expr() throws RecognitionException, TokenStreamException {
		
		traceIn("expr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr_AST = null;
			
			try {      // for error handling
				quesexpr();
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			}
			returnAST = expr_AST;
		} finally { // debugging
			traceOut("expr");
		}
	}
	
	public final void method_call() throws RecognitionException, TokenStreamException {
		
		traceIn("method_call");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST method_call_AST = null;
			
			try {      // for error handling
				AST tmp33_AST = null;
				tmp33_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp33_AST);
				match(ID);
				match(LPAREN);
				{
				switch ( LA(1)) {
				case TK_false:
				case TK_true:
				case LPAREN:
				case MINUS:
				case INTLITERAL:
				case CHARLITERAL:
				case STRINGLITERAL:
				case ID:
				case EXCLAM:
				{
					method_args();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(RPAREN);
				method_call_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			}
			returnAST = method_call_AST;
		} finally { // debugging
			traceOut("method_call");
		}
	}
	
	public final void if_() throws RecognitionException, TokenStreamException {
		
		traceIn("if_");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST if__AST = null;
			
			try {      // for error handling
				AST tmp36_AST = null;
				tmp36_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp36_AST);
				match(TK_if);
				match(LPAREN);
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				block();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case TK_else:
				{
					else_();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case TK_break:
				case TK_continue:
				case TK_for:
				case TK_while:
				case TK_if:
				case TK_return:
				case RCURLY:
				case ID:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if__AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = if__AST;
		} finally { // debugging
			traceOut("if_");
		}
	}
	
	public final void for_() throws RecognitionException, TokenStreamException {
		
		traceIn("for_");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST for__AST = null;
			
			try {      // for error handling
				AST tmp39_AST = null;
				tmp39_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp39_AST);
				match(TK_for);
				match(LPAREN);
				assignment();
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMICOLON);
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMICOLON);
				for_assign();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				block();
				astFactory.addASTChild(currentAST, returnAST);
				for__AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = for__AST;
		} finally { // debugging
			traceOut("for_");
		}
	}
	
	public final void while_() throws RecognitionException, TokenStreamException {
		
		traceIn("while_");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST while__AST = null;
			
			try {      // for error handling
				AST tmp44_AST = null;
				tmp44_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp44_AST);
				match(TK_while);
				match(LPAREN);
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				block();
				astFactory.addASTChild(currentAST, returnAST);
				while__AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = while__AST;
		} finally { // debugging
			traceOut("while_");
		}
	}
	
	public final void break_() throws RecognitionException, TokenStreamException {
		
		traceIn("break_");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST break__AST = null;
			
			try {      // for error handling
				AST tmp47_AST = null;
				tmp47_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp47_AST);
				match(TK_break);
				match(SEMICOLON);
				break__AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = break__AST;
		} finally { // debugging
			traceOut("break_");
		}
	}
	
	public final void return_() throws RecognitionException, TokenStreamException {
		
		traceIn("return_");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST return__AST = null;
			
			try {      // for error handling
				AST tmp49_AST = null;
				tmp49_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp49_AST);
				match(TK_return);
				{
				switch ( LA(1)) {
				case TK_false:
				case TK_true:
				case LPAREN:
				case MINUS:
				case INTLITERAL:
				case CHARLITERAL:
				case ID:
				case EXCLAM:
				{
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMICOLON:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(SEMICOLON);
				return__AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = return__AST;
		} finally { // debugging
			traceOut("return_");
		}
	}
	
	public final void continue_() throws RecognitionException, TokenStreamException {
		
		traceIn("continue_");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST continue__AST = null;
			
			try {      // for error handling
				AST tmp51_AST = null;
				tmp51_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp51_AST);
				match(TK_continue);
				match(SEMICOLON);
				continue__AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = continue__AST;
		} finally { // debugging
			traceOut("continue_");
		}
	}
	
	public final void assignment() throws RecognitionException, TokenStreamException {
		
		traceIn("assignment");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST assignment_AST = null;
			
			try {      // for error handling
				location();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp53_AST = null;
				tmp53_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp53_AST);
				match(ASSIGN);
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				assignment_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_15);
			}
			returnAST = assignment_AST;
		} finally { // debugging
			traceOut("assignment");
		}
	}
	
	public final void for_assign() throws RecognitionException, TokenStreamException {
		
		traceIn("for_assign");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST for_assign_AST = null;
			
			try {      // for error handling
				location();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case PLUSASSIGN:
				case MINUSASSIGN:
				{
					{
					switch ( LA(1)) {
					case PLUSASSIGN:
					{
						AST tmp54_AST = null;
						tmp54_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp54_AST);
						match(PLUSASSIGN);
						break;
					}
					case MINUSASSIGN:
					{
						AST tmp55_AST = null;
						tmp55_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp55_AST);
						match(MINUSASSIGN);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case INCRE:
				case DECRE:
				{
					{
					switch ( LA(1)) {
					case INCRE:
					{
						AST tmp56_AST = null;
						tmp56_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp56_AST);
						match(INCRE);
						break;
					}
					case DECRE:
					{
						AST tmp57_AST = null;
						tmp57_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp57_AST);
						match(DECRE);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				for_assign_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			}
			returnAST = for_assign_AST;
		} finally { // debugging
			traceOut("for_assign");
		}
	}
	
	public final void else_() throws RecognitionException, TokenStreamException {
		
		traceIn("else_");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST else__AST = null;
			
			try {      // for error handling
				AST tmp58_AST = null;
				tmp58_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp58_AST);
				match(TK_else);
				block();
				astFactory.addASTChild(currentAST, returnAST);
				else__AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = else__AST;
		} finally { // debugging
			traceOut("else_");
		}
	}
	
	public final void method_args() throws RecognitionException, TokenStreamException {
		
		traceIn("method_args");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST method_args_AST = null;
			
			try {      // for error handling
				method_arg();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop49:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						method_arg();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop49;
					}
					
				} while (true);
				}
				method_args_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			}
			returnAST = method_args_AST;
		} finally { // debugging
			traceOut("method_args");
		}
	}
	
	public final void method_arg() throws RecognitionException, TokenStreamException {
		
		traceIn("method_arg");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST method_arg_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case TK_false:
				case TK_true:
				case LPAREN:
				case MINUS:
				case INTLITERAL:
				case CHARLITERAL:
				case ID:
				case EXCLAM:
				{
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					method_arg_AST = (AST)currentAST.root;
					break;
				}
				case STRINGLITERAL:
				{
					AST tmp60_AST = null;
					tmp60_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp60_AST);
					match(STRINGLITERAL);
					method_arg_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_9);
			}
			returnAST = method_arg_AST;
		} finally { // debugging
			traceOut("method_arg");
		}
	}
	
	public final void quesexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("quesexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST quesexpr_AST = null;
			
			try {      // for error handling
				orexpr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case QUESTION:
				{
					AST tmp61_AST = null;
					tmp61_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp61_AST);
					match(QUESTION);
					quesexpr();
					astFactory.addASTChild(currentAST, returnAST);
					AST tmp62_AST = null;
					tmp62_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp62_AST);
					match(COLON);
					quesexpr();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RSQUAR:
				case RPAREN:
				case SEMICOLON:
				case COMMA:
				case COLON:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				quesexpr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_16);
			}
			returnAST = quesexpr_AST;
		} finally { // debugging
			traceOut("quesexpr");
		}
	}
	
	public final void atom() throws RecognitionException, TokenStreamException {
		
		traceIn("atom");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST atom_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case TK_false:
				case TK_true:
				case INTLITERAL:
				case CHARLITERAL:
				{
					literal();
					astFactory.addASTChild(currentAST, returnAST);
					atom_AST = (AST)currentAST.root;
					break;
				}
				case LPAREN:
				{
					match(LPAREN);
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					match(RPAREN);
					atom_AST = (AST)currentAST.root;
					break;
				}
				default:
					if ((LA(1)==ID) && (_tokenSet_17.member(LA(2)))) {
						location();
						astFactory.addASTChild(currentAST, returnAST);
						atom_AST = (AST)currentAST.root;
					}
					else if ((LA(1)==ID) && (LA(2)==LPAREN)) {
						method_call();
						astFactory.addASTChild(currentAST, returnAST);
						atom_AST = (AST)currentAST.root;
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			}
			returnAST = atom_AST;
		} finally { // debugging
			traceOut("atom");
		}
	}
	
	public final void literal() throws RecognitionException, TokenStreamException {
		
		traceIn("literal");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST literal_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case INTLITERAL:
				{
					AST tmp65_AST = null;
					tmp65_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp65_AST);
					match(INTLITERAL);
					literal_AST = (AST)currentAST.root;
					break;
				}
				case CHARLITERAL:
				{
					AST tmp66_AST = null;
					tmp66_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp66_AST);
					match(CHARLITERAL);
					literal_AST = (AST)currentAST.root;
					break;
				}
				case TK_true:
				{
					AST tmp67_AST = null;
					tmp67_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp67_AST);
					match(TK_true);
					literal_AST = (AST)currentAST.root;
					break;
				}
				case TK_false:
				{
					AST tmp68_AST = null;
					tmp68_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp68_AST);
					match(TK_false);
					literal_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			}
			returnAST = literal_AST;
		} finally { // debugging
			traceOut("literal");
		}
	}
	
	public final void minusexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("minusexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST minusexpr_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case MINUS:
				{
					AST tmp69_AST = null;
					tmp69_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp69_AST);
					match(MINUS);
					minusexpr();
					astFactory.addASTChild(currentAST, returnAST);
					minusexpr_AST = (AST)currentAST.root;
					break;
				}
				case TK_false:
				case TK_true:
				case LPAREN:
				case INTLITERAL:
				case CHARLITERAL:
				case ID:
				{
					atom();
					astFactory.addASTChild(currentAST, returnAST);
					minusexpr_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			}
			returnAST = minusexpr_AST;
		} finally { // debugging
			traceOut("minusexpr");
		}
	}
	
	public final void exclamexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("exclamexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST exclamexpr_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case EXCLAM:
				{
					AST tmp70_AST = null;
					tmp70_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp70_AST);
					match(EXCLAM);
					exclamexpr();
					astFactory.addASTChild(currentAST, returnAST);
					exclamexpr_AST = (AST)currentAST.root;
					break;
				}
				case TK_false:
				case TK_true:
				case LPAREN:
				case MINUS:
				case INTLITERAL:
				case CHARLITERAL:
				case ID:
				{
					minusexpr();
					astFactory.addASTChild(currentAST, returnAST);
					exclamexpr_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			}
			returnAST = exclamexpr_AST;
		} finally { // debugging
			traceOut("exclamexpr");
		}
	}
	
	public final void multiexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("multiexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST multiexpr_AST = null;
			
			try {      // for error handling
				exclamexpr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop61:
				do {
					if ((LA(1)==TIMES||LA(1)==SLASH||LA(1)==PERCENT)) {
						{
						switch ( LA(1)) {
						case TIMES:
						{
							AST tmp71_AST = null;
							tmp71_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp71_AST);
							match(TIMES);
							break;
						}
						case SLASH:
						{
							AST tmp72_AST = null;
							tmp72_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp72_AST);
							match(SLASH);
							break;
						}
						case PERCENT:
						{
							AST tmp73_AST = null;
							tmp73_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp73_AST);
							match(PERCENT);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						exclamexpr();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop61;
					}
					
				} while (true);
				}
				multiexpr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_18);
			}
			returnAST = multiexpr_AST;
		} finally { // debugging
			traceOut("multiexpr");
		}
	}
	
	public final void addexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("addexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST addexpr_AST = null;
			
			try {      // for error handling
				multiexpr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop65:
				do {
					if ((LA(1)==MINUS||LA(1)==PLUS)) {
						{
						switch ( LA(1)) {
						case PLUS:
						{
							AST tmp74_AST = null;
							tmp74_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp74_AST);
							match(PLUS);
							break;
						}
						case MINUS:
						{
							AST tmp75_AST = null;
							tmp75_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp75_AST);
							match(MINUS);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						multiexpr();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop65;
					}
					
				} while (true);
				}
				addexpr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			}
			returnAST = addexpr_AST;
		} finally { // debugging
			traceOut("addexpr");
		}
	}
	
	public final void compexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("compexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST compexpr_AST = null;
			
			try {      // for error handling
				addexpr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop69:
				do {
					if (((LA(1) >= GREATER && LA(1) <= LE))) {
						{
						switch ( LA(1)) {
						case GREATER:
						{
							AST tmp76_AST = null;
							tmp76_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp76_AST);
							match(GREATER);
							break;
						}
						case LESS:
						{
							AST tmp77_AST = null;
							tmp77_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp77_AST);
							match(LESS);
							break;
						}
						case GE:
						{
							AST tmp78_AST = null;
							tmp78_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp78_AST);
							match(GE);
							break;
						}
						case LE:
						{
							AST tmp79_AST = null;
							tmp79_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp79_AST);
							match(LE);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						addexpr();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop69;
					}
					
				} while (true);
				}
				compexpr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_20);
			}
			returnAST = compexpr_AST;
		} finally { // debugging
			traceOut("compexpr");
		}
	}
	
	public final void eqexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("eqexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST eqexpr_AST = null;
			
			try {      // for error handling
				compexpr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop73:
				do {
					if ((LA(1)==EQ||LA(1)==NEQ)) {
						{
						switch ( LA(1)) {
						case EQ:
						{
							AST tmp80_AST = null;
							tmp80_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp80_AST);
							match(EQ);
							break;
						}
						case NEQ:
						{
							AST tmp81_AST = null;
							tmp81_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp81_AST);
							match(NEQ);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						compexpr();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop73;
					}
					
				} while (true);
				}
				eqexpr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_21);
			}
			returnAST = eqexpr_AST;
		} finally { // debugging
			traceOut("eqexpr");
		}
	}
	
	public final void andexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("andexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST andexpr_AST = null;
			
			try {      // for error handling
				eqexpr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop76:
				do {
					if ((LA(1)==AND)) {
						AST tmp82_AST = null;
						tmp82_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp82_AST);
						match(AND);
						eqexpr();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop76;
					}
					
				} while (true);
				}
				andexpr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_22);
			}
			returnAST = andexpr_AST;
		} finally { // debugging
			traceOut("andexpr");
		}
	}
	
	public final void orexpr() throws RecognitionException, TokenStreamException {
		
		traceIn("orexpr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST orexpr_AST = null;
			
			try {      // for error handling
				andexpr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop79:
				do {
					if ((LA(1)==OR)) {
						AST tmp83_AST = null;
						tmp83_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp83_AST);
						match(OR);
						andexpr();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop79;
					}
					
				} while (true);
				}
				orexpr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_23);
			}
			returnAST = orexpr_AST;
		} finally { // debugging
			traceOut("orexpr");
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"bool\"",
		"\"break\"",
		"\"import\"",
		"\"continue\"",
		"\"else\"",
		"\"false\"",
		"\"for\"",
		"\"while\"",
		"\"if\"",
		"\"int\"",
		"\"return\"",
		"\"true\"",
		"\"void\"",
		"\"len\"",
		"LCURLY",
		"RCURLY",
		"LSQUAR",
		"RSQUAR",
		"LPAREN",
		"RPAREN",
		"SEMICOLON",
		"MINUS",
		"PLUS",
		"ASSIGN",
		"TIMES",
		"GREATER",
		"LESS",
		"GE",
		"LE",
		"EQ",
		"NEQ",
		"AND",
		"OR",
		"COMMA",
		"DIGIT",
		"LOWERCASE",
		"UPPERCASE",
		"HEXDIGIT",
		"DEC",
		"HEX",
		"INTLITERAL",
		"ESC",
		"CHAR",
		"CHARLITERAL",
		"STRINGLITERAL",
		"ALPHA",
		"ID",
		"WS_",
		"SL_COMMENT",
		"IL_COMMENT",
		"QUESTION",
		"SLASH",
		"PERCENT",
		"PLUSASSIGN",
		"MINUSASSIGN",
		"EXCLAM",
		"COLON",
		"INCRE",
		"DECRE"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 73810L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 1125899907464370L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 73746L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 137455730688L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 1125899906842624L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 8388608L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 1125899906866336L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 1125899907464626L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 137447342080L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 7349874592003915776L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 1125899907390624L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 8628897160913485824L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 137466216448L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 1279022568910618624L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 16777216L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 1152921642073063424L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 1279022568911667200L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 1170936177585291264L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 1170936177484627968L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 1170936169431564288L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 1170936143661760512L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 1170936109302022144L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 1170936040582545408L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	
	}
