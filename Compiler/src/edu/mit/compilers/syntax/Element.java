package edu.mit.compilers.syntax;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.compile.CompileElement;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.tools.Er;

public class Element {
    static String arrayElement(AST t, ActionType action, List<String> codes) {
        Descriptor desc = Manager.getArray(t.getText());
        if (desc == null) {
            Er.errNotDefined(t, t.getText());
            return null;
        }
        String type = desc.getType();
        AST c = t.getFirstChild();
        String indexType = Structure.expr(c, ActionType.LOAD, codes);
        if (indexType == null) {
            Er.errType(t, Defs.getArrayType(type), type);
            return Defs.getArrayType(type);
        }
        if (!Defs.equals(Defs.DESC_TYPE_INT, indexType)) {
            Er.errArrayIndexNotInt(t, Defs.DESC_TYPE_INT, indexType);
            return Defs.getArrayType(type);
        }
        CompileElement.arrayElement(action, desc, codes);
        return Defs.getArrayType(type);

    }

    static String intLiteral(AST t, boolean isNegative) {
        String number;
        Long result = null;
        if (isNegative) {
            number = "-" + t.getText();
        } else {
            number = t.getText();
        }
        try {
            result = Long.parseLong(number);
        } catch (NumberFormatException e) {
            try {
                result = Long.decode(number);
            } catch (Exception ee) {
                Er.errIntegerTooLarge(t, number);
            }
        }
        Manager.tmpPush(new Num(result));
        return Defs.DESC_TYPE_INT;
    }
}
