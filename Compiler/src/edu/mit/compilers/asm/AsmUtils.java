package edu.mit.compilers.asm;
import java.util.HashMap;
import java.util.Map;

import edu.mit.compilers.grammar.*;

public class AsmUtils {
    // TODO
    public static Map<Integer, String> binaryOpToken2Inst = new HashMap<>(){{
        put(DecafParserTokenTypes.MINUS, "subq");
        put(DecafParserTokenTypes.PLUS, "addq");
        put(DecafParserTokenTypes.TIMES, "imulq");
        put(DecafParserTokenTypes.SLASH, "idivq");
        put(DecafParserTokenTypes.PERCENT, "divq");
        put(DecafParserTokenTypes.GREATER, "setg");
        put(DecafParserTokenTypes.LESS, "setl");
        put(DecafParserTokenTypes.GE, "setge");
        put(DecafParserTokenTypes.LE, "setle");
        put(DecafParserTokenTypes.EQ, "sete");
        put(DecafParserTokenTypes.NEQ, "setne");
    }};
}
