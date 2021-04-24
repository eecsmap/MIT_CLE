package edu.mit.compilers.asm;
import java.util.HashMap;
import java.util.Map;

import edu.mit.compilers.grammar.*;

public class asmUtils {
    public static Map<Integer, String> binaryOpToken2Inst = new HashMap<>(){{
        put(DecafParserTokenTypes.MINUS, "subl");
        put(DecafParserTokenTypes.PLUS, "addl");
        put(DecafParserTokenTypes.TIMES, "imul");
        put(DecafParserTokenTypes.GREATER, "setg");
        put(DecafParserTokenTypes.LESS, "setl");
        put(DecafParserTokenTypes.GE, "setge");
        put(DecafParserTokenTypes.LE, "setle");
        put(DecafParserTokenTypes.EQ, "sete");
        put(DecafParserTokenTypes.NEQ, "setne");
    }};
}
