package edu.mit.compilers.defs;
import java.util.HashMap;
import java.util.Map;

import edu.mit.compilers.asm.Label;

public class Defs {
    private Defs() {}

    // didnt differentiate variable size between int and bool
    public static Integer varSize = 8;
    public static Integer callRegCount = 6;

    public static Map<String, VarType> stringToVarType = new HashMap<>(){{
        put("void", VarType.VOID);
        put("bool", VarType.BOOL);
        put("int", VarType.INT);
    }};

    public static final Label EXIT_ARRAY_OUTBOUND_LABEL = new Label(".ExitArrayOutBound");
}
