package edu.mit.compilers.compile;

import java.util.Collections;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ArrayDesc;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;

public class Element {
    static String arrayElement(AST t, ST st, ActionType action, List<String> codes) {
        Descriptor desc = st.getArray(t.getText());
        if (desc == null) {
            System.err.printf("11 ");
            Er.errNotDefined(t, t.getText());
            return null;
        }
        String type = desc.getType();
        AST c = t.getFirstChild();
        String indexType = Structure.expr(c, st, ActionType.LOAD, codes);
        if (indexType == null) {
            Er.errType(t, Defs.getArrayType(type), type);
            return Defs.getArrayType(type);
        }
        if (!Defs.equals(Defs.DESC_TYPE_INT, indexType)) {
            System.err.printf("12 ");
            Er.errArrayIndexNotInt(t, Defs.DESC_TYPE_INT, indexType);
            return Defs.getArrayType(type);
        }
        if (Program.shouldCompile()) {
            String varName = String.format("%s[]", desc.getText());
            Oprand index = st.tmpPop();
            Reg resReg = st.newTmpReg();
            Reg indexReg = st.newTmpReg(resReg);
            Integer offset = desc.getAddr().getOffset();
            Collections.addAll(codes,
                asm.bin("movq", index, indexReg),
                asm.bin("cmp", new Num(((ArrayDesc)desc).getCap()), indexReg),
                asm.jmp("jge", Defs.EXIT_ARRAY_OUTBOUND_LABEL),
                asm.bin("cmp", new Num(0L), indexReg),
                asm.jmp("jl", Defs.EXIT_ARRAY_OUTBOUND_LABEL)
            );
            if (desc.getAddr().isGlobal()) {
                Collections.addAll(codes,
                    asm.bin("movq", index, indexReg),
                    asm.bin("leaq", new Addr(indexReg, varName), indexReg),
                    asm.bin("leaq", desc.getAddr(), resReg)
                );
                if (action == ActionType.STORE) {
                    st.tmpPush(new Addr(indexReg, resReg));
                } else {
                    codes.add(
                        asm.bin("movq", new Addr(indexReg, resReg), resReg)
                    );
                    st.tmpPush(resReg);
                }
            } else {
                Collections.addAll(codes,
                    asm.bin("movq", index, indexReg),
                    asm.bin("movq", new Addr(offset, indexReg, varName), resReg)
                );
                st.tmpPush(resReg);
            }
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
