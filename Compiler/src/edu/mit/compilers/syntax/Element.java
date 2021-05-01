package edu.mit.compilers.syntax;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.compile.CompileElement;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.tools.Err;

public class Element {
    static VarType arrayElement(AST t, ActionType action, List<String> codes) {
        Descriptor desc = Manager.getArray(t.getText());
        if (desc == null) {
            Err.errNotDefined(t, t.getText());
            return null;
        }
        VarType type = desc.getType();
        AST c = t.getFirstChild();
        if (!type.isArray()) {
            Err.errNotArrayType(t, type);
            return type;
        }
        VarType indexType = Structure.expr(c, ActionType.LOAD, codes);
        if (!indexType.isInt()) {
            Err.errArrayIndexNotInt(t, desc.getText(), indexType);
            return type.plain();
        }
        CompileElement.arrayElement(action, desc, codes);
        return type.plain();
    }

    static VarType intLiteral(AST t, boolean isNegative) {
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
                Err.errIntegerTooLarge(t, number);
            }
        }
        Manager.tmpPush(new Num(result));
        return VarType.INT;
    }
}
