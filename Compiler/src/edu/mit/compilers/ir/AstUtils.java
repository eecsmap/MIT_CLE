package edu.mit.compilers.ir;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import antlr.collections.AST;

import edu.mit.compilers.grammar.*;

// for debug only
public class AstUtils {
    // num to strignn
    public static final Map<Integer, String> t0;
    // type (method)
    public static final Map<Integer, String> t1;
    // binary ops (boolean)
    public static final Set<Integer> s0;
    // binary ops (original type)
    public static final Set<Integer> s1;

    static {
        Map<Integer, String> m1 = new HashMap<>();
        m1.put(4, "TK_bool");
        m1.put(5, "TK_break");
        m1.put(6, "TK_import");
        m1.put(7, "TK_continue");
        m1.put(8, "TK_else");
        m1.put(9, "TK_false");
        m1.put(10, "TK_for");
        m1.put(11, "TK_while");
        m1.put(12, "TK_if");
        m1.put(13, "TK_int");
        m1.put(14, "TK_return");
        m1.put(15, "TK_true");
        m1.put(16, "TK_void");
        m1.put(17, "TK_len");
        m1.put(18, "LCURLY");
        m1.put(19, "RCURLY");
        m1.put(20, "LSQUAR");
        m1.put(21, "RSQUAR");
        m1.put(22, "LPAREN");
        m1.put(23, "RPAREN");
        m1.put(24, "SEMICOLON");
        m1.put(25, "MINUS");
        m1.put(26, "PLUS");
        m1.put(27, "ASSIGN");
        m1.put(28, "TIMES");
        m1.put(29, "GREATER");
        m1.put(30, "LESS");
        m1.put(31, "GE");
        m1.put(32, "LE");
        m1.put(33, "EQ");
        m1.put(34, "NEQ");
        m1.put(35, "AND");
        m1.put(36, "OR");
        m1.put(37, "COMMA");
        m1.put(38, "DIGIT");
        m1.put(39, "LOWERCASE");
        m1.put(40, "UPPERCASE");
        m1.put(41, "HEXDIGIT");
        m1.put(42, "DEC");
        m1.put(43, "HEX");
        m1.put(44, "INTLITERAL");
        m1.put(45, "ESC");
        m1.put(46, "CHAR");
        m1.put(47, "CHARLITERAL");
        m1.put(48, "STRINGLITERAL");
        m1.put(49, "ALPHA");
        m1.put(50, "ID");
        m1.put(51, "WS_");
        m1.put(52, "SL_COMMENT");
        m1.put(53, "IL_COMMENT");
        m1.put(54, "QUESTION");
        m1.put(55, "SLASH");
        m1.put(56, "PERCENT");
        m1.put(57, "PLUSASSIGN");
        m1.put(58, "MINUSASSIGN");
        m1.put(59, "EXCLAM");
        m1.put(60, "COLON");
        m1.put(61, "INCRE");
        m1.put(62, "DECRE");
        t0 = Collections.unmodifiableMap(m1);
        Map<Integer, String> m2 = new HashMap<>();
        m2.put(4, "bool");
        m2.put(13, "int");
        m2.put(16, "void");
        t1 = Collections.unmodifiableMap(m2);
        s0 = new HashSet<>();
        s1 = new HashSet<>();
        s0.add(DecafParserTokenTypes.GREATER);
        s0.add(DecafParserTokenTypes.LESS);
        s0.add(DecafParserTokenTypes.GE);
        s0.add(DecafParserTokenTypes.LE);
        s0.add(DecafParserTokenTypes.EQ);
        s0.add(DecafParserTokenTypes.NEQ);
        s0.add(DecafParserTokenTypes.AND);
        s0.add(DecafParserTokenTypes.OR);
        s1.add(DecafParserTokenTypes.MINUS);
        s1.add(DecafParserTokenTypes.PLUS);
        s1.add(DecafParserTokenTypes.TIMES);
    }

    public static void printAST(AST t, int level) {
        while(t != null){
            String tab = new String(new char[level]).replace("\0", "\t");
            System.out.printf("%s%s\t\t\t\t\t\t%s%n", tab, t.getText(), t0.get(t.getType()));
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

    public static final boolean isReturnType(AST t) {
        return isType(t) || t.getType() == DecafParserTokenTypes.TK_void;
    }

    public static final boolean isID(AST t) {
        return t.getType() == DecafParserTokenTypes.ID;
    }

    public static final boolean isBinaryOp(AST t) {
        return s0.contains(t.getType());
    }

    public static final boolean isBinaryBoolOp(AST t) {
        return s1.contains(t.getType());
    }
}
