package edu.mit.compilers.compile;

import java.util.Collections;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
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
        String indexType = Structure.expr(c, st, codes);
        if (indexType == null) {
            Er.errType(t, Defs.getArrayType(type), type);
            return Defs.getArrayType(type);
        }
        if (!Defs.equals(Defs.DESC_TYPE_INT, indexType)) {
            System.err.printf("12 ");
            Er.errArrayIndexNotInt(t, Defs.DESC_TYPE_INT, indexType);
            return Defs.getArrayType(type);
        }
        if (c.getType() == DecafScannerTokenTypes.INTLITERAL && desc.findVar(c.getText()) == null) {
            System.err.printf("13 ");
            Er.errArrayOutbound(t, desc.getText(), c.getText());
        }
        if (Program.shouldCompile()) {
            String varName = String.format("%s[]", desc.getText());
            Oprand index = st.tmpPop();
            Reg indexReg = st.newTmpReg();
            Reg resReg = st.newTmpReg();
            Integer offset = desc.getAddr().getOffset();
            Collections.addAll(codes,
                asm.bin("movl", index, indexReg),
                asm.bin("movl", new Addr(offset, indexReg, varName), resReg)
            );
            st.tmpPush(resReg);
        }
        return Defs.getArrayType(type);
    }

    static String intLiteral(AST t, ST st, boolean isNegative) {
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
        if (Program.shouldCompile()) {
            st.tmpPush(new Num(result));
        }
        return Defs.DESC_TYPE_INT;
    }
}
