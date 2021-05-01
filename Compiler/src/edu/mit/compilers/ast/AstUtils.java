package edu.mit.compilers.ast;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import antlr.collections.AST;

import edu.mit.compilers.grammar.*;

// for debug only
public class AstUtils {
    // num to strignn
    public static final Map<Integer, String> t0 = new HashMap<>() {{
        put(4, "TK_bool");
        put(5, "TK_break");
        put(6, "TK_import");
        put(7, "TK_continue");
        put(8, "TK_else");
        put(9, "TK_false");
        put(10, "TK_for");
        put(11, "TK_while");
        put(12, "TK_if");
        put(13, "TK_int");
        put(14, "TK_return");
        put(15, "TK_true");
        put(16, "TK_void");
        put(17, "TK_len");
        put(18, "LCURLY");
        put(19, "RCURLY");
        put(20, "LSQUAR");
        put(21, "RSQUAR");
        put(22, "LPAREN");
        put(23, "RPAREN");
        put(24, "SEMICOLON");
        put(25, "MINUS");
        put(26, "PLUS");
        put(27, "ASSIGN");
        put(28, "TIMES");
        put(29, "GREATER");
        put(30, "LESS");
        put(31, "GE");
        put(32, "LE");
        put(33, "EQ");
        put(34, "NEQ");
        put(35, "AND");
        put(36, "OR");
        put(37, "COMMA");
        put(38, "DIGIT");
        put(39, "LOWERCASE");
        put(40, "UPPERCASE");
        put(41, "HEXDIGIT");
        put(42, "DEC");
        put(43, "HEX");
        put(44, "INTLITERAL");
        put(45, "ESC");
        put(46, "CHAR");
        put(47, "CHARLITERAL");
        put(48, "STRINGLITERAL");
        put(49, "ALPHA");
        put(50, "ID");
        put(51, "WS_");
        put(52, "SL_COMMENT");
        put(53, "IL_COMMENT");
        put(54, "QUESTION");
        put(55, "SLASH");
        put(56, "PERCENT");
        put(57, "PLUSASSIGN");
        put(58, "MINUSASSIGN");
        put(59, "EXCLAM");
        put(60, "COLON");
        put(61, "INCRE");
        put(62, "DECRE");
    }};
    // binary ops (int -> int)
    public static final Set<Integer> s0 = new HashSet<>() {{
        add(DecafParserTokenTypes.MINUS);
        add(DecafParserTokenTypes.PLUS);
        add(DecafParserTokenTypes.TIMES);
        add(DecafParserTokenTypes.SLASH);
        add(DecafParserTokenTypes.PERCENT);
    }};
    // binary ops (int -> bool)
    public static final Set<Integer> s1 = new HashSet<>() {{
        add(DecafParserTokenTypes.GREATER);
        add(DecafParserTokenTypes.LESS);
        add(DecafParserTokenTypes.GE);
        add(DecafParserTokenTypes.LE);
    }};
    // binary ops (bool -> bool)
    public static final Set<Integer> s2 = new HashSet<>() {{
        add(DecafParserTokenTypes.AND);
        add(DecafParserTokenTypes.OR);
    }};
    // binary ops (assignment: =, +=, -=)
    public static final Set<Integer> s3 = new HashSet<>() {{
        add(DecafParserTokenTypes.ASSIGN);
        add(DecafParserTokenTypes.MINUSASSIGN);
        add(DecafParserTokenTypes.PLUSASSIGN);
    }};
    // unary ops (assignment: ++, --)
    public static final Set<Integer> s4 = new HashSet<>() {{
        add(DecafParserTokenTypes.INCRE);
        add(DecafParserTokenTypes.DECRE);
    }};
    // binary ops (* -> bool)
    public static final Set<Integer> s5 = new HashSet<>() {{
        add(DecafParserTokenTypes.EQ);
        add(DecafParserTokenTypes.NEQ);
    }};

    public static void printAST(AST t, int level) {
        while(t != null){
            String tab = new String(new char[level]).replace("\0", "\t");
            System.err.printf("%s%s\t\t\t\t\t\t%s%n", tab, t.getText(), t0.get(t.getType()));
            AST fc = t.getFirstChild();
            if (fc != null) {
                printAST(fc, level+1);
            }
            t = t.getNextSibling();
        }
    }

    public static final boolean isImport(AST t) {
        return t.getType() == DecafParserTokenTypes.TK_import;
    }

    public static final boolean isType(AST t) {
        int type = t.getType();
        return type == DecafParserTokenTypes.TK_int || type == DecafParserTokenTypes.TK_bool;
    }

    public static final boolean isID(AST t) {
        return t.getType() == DecafParserTokenTypes.ID;
    }

    public static final boolean isBinaryOp(AST t) {
        return s0.contains(t.getType());
    }

    public static final boolean isBinaryCompOp(AST t) {
        return s5.contains(t.getType());
    }

    public static final boolean isBinaryIntCompOp(AST t) {
        return s1.contains(t.getType());
    }

    public static final boolean isBinaryBoolOp(AST t) {
        return s2.contains(t.getType());
    }

    public static final boolean isBinaryAssignOp(AST t) {
        return s3.contains(t.getType());
    }

    public static final boolean isUnaryAssignOp(AST t) {
        return s4.contains(t.getType());
    }
}
