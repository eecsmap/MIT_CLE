package edu.mit.compilers.compile;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.grammar.*;

public class Element {
    static String arrayElement(AST t, ST st, List<String> codes) {
        Descriptor desc = st.getArray(t.getText());
        if (desc == null) {
            System.err.printf("11 ");
            Er.errNotDefined(t, t.getText());
            return null;
        }
        String type = desc.getType();
        AST c = t.getFirstChild();
        String subType = Structure.expr(c, st);
        if (subType == null) {
            Er.errType(t, Defs.getArrayType(type), type);
            return Defs.getArrayType(type);
        }
        if (!Defs.equals(Defs.DESC_TYPE_INT, subType)) {
            System.err.printf("12 ");
            Er.errArrayIndexNotInt(t, Defs.DESC_TYPE_INT, subType);
            return Defs.getArrayType(type);
        }
        if (c.getType() == DecafScannerTokenTypes.INTLITERAL && desc.findVar(c.getText()) == null) {
            System.err.printf("13 ");
            Er.errArrayOutbound(t, desc.getText(), c.getText());
        }
        return Defs.getArrayType(type);
    }

    static String intLiteral(AST t, boolean isNegative, List<String> codes) {
        String number;
        if (isNegative) {
            number = "-" + t.getText();
        } else {
            number = t.getText();
        }
        try {
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            try {
                Long.decode(number);
            } catch (Exception ee) {
                Er.errIntegerTooLarge(t, number);
            }
        }
        return Defs.DESC_TYPE_INT;
    }
}
