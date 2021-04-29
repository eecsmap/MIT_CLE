package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.compilers.asm.AsmUtils;
import edu.mit.compilers.asm.Bool;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.grammar.DecafParserTokenTypes;
import edu.mit.compilers.grammar.DecafScannerTokenTypes;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.syntax.Program;

public class CompileStructure {
    public static final void binaryOpExpr(ST st, Integer operator, List<String> leftCodes, List<String> rightCodes, List<String> codes) {
        Reg resReg = st.newTmpReg();
        Oprand rOp = st.tmpPop();
        Oprand lOp = st.tmpPop();
        List<String> glueCodes = new ArrayList<>();
        if (operator == DecafParserTokenTypes.SLASH || operator == DecafParserTokenTypes.PERCENT) {
            Reg resultReg = operator == DecafParserTokenTypes.SLASH ? Reg.rax : Reg.rdx;
            Reg divisor = st.newTmpReg();
            Collections.addAll(glueCodes,
                asm.bin("movq", lOp, Reg.rax),
                asm.bin("movq", rOp, divisor),
                asm.non("cqto"),
                asm.uni("idivq", divisor),
                asm.bin("movq", resultReg, resReg)
            );     
        } else {
            Collections.addAll(glueCodes,
                asm.bin("movq", lOp, resReg),
                asm.bin(AsmUtils.binaryOpToken2Inst.get(operator), rOp, resReg)
            );
        }
        codes.addAll(leftCodes);
        codes.addAll(rightCodes);
        codes.addAll(glueCodes);
        st.tmpPush(resReg);
    }

    public static final void binaryBoolExpr(ST st, Integer operator, List<String> leftCodes, List<String> rightCodes, List<String> codes) {
        Reg resReg = st.newTmpReg();
        Oprand rOp = st.tmpPop();
        Oprand lOp = st.tmpPop();
        List<String> glueCodes = new ArrayList<>();
        String jmpOp = operator == DecafScannerTokenTypes.AND ? "je" : "jne";
        Label endLabel = new Label();
        if (lOp instanceof Reg) {
            Collections.addAll(leftCodes,
                asm.bin("cmp", new Num(0L), ((Reg)lOp).bite()),
                asm.jmp(jmpOp, endLabel)
            );
        } else {
            Collections.addAll(leftCodes,
                asm.bin("movq", lOp, resReg),
                asm.bin("cmpq", new Num(0L), resReg),
                asm.jmp(jmpOp, endLabel)
            );
        }
        if (rOp instanceof Reg) {
            rightCodes.add(
                asm.bin("cmp", new Num(0L), ((Reg)rOp).bite())
            );
        } else {
            Collections.addAll(rightCodes,
                asm.bin("movq", rOp, resReg),
                asm.bin("cmpq", new Num(0L), resReg)
            );
        }
        Collections.addAll(glueCodes,
            asm.label(endLabel),
            asm.uni("setne", resReg.bite())
        );
        codes.addAll(leftCodes);
        codes.addAll(rightCodes);
        codes.addAll(glueCodes);
        st.tmpPush(resReg);
    }


    public static final void binaryCompExpr(ST st, Integer operator, List<String> leftCodes, List<String> rightCodes, List<String> codes) {
        Reg resReg = st.newTmpReg();
        Oprand rOp = st.tmpPop();
        Oprand lOp = st.tmpPop();
        List<String> glueCodes = new ArrayList<>();
        Collections.addAll(glueCodes,
            asm.bin("movq", lOp, resReg),
            asm.bin("cmpq", rOp, resReg),
            asm.uni(AsmUtils.setOnCondition.get(operator), resReg.bite()),
            asm.bin("movzbq", resReg.bite(), resReg)
        );
        codes.addAll(leftCodes);
        codes.addAll(rightCodes);
        codes.addAll(glueCodes);
        st.tmpPush(resReg);
    }

    public static final void minusExpr(ST st, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = st.newTmpReg();
        Oprand op = st.tmpPop();
        if (op instanceof Num) {
            st.tmpPush(((Num)op).neg());
        } else {
            Collections.addAll(codes,
                asm.bin("movq", op, tmpReg),
                asm.uni("negq", tmpReg)
            );
            st.tmpPush(tmpReg);
        }
    }

    public static final void exclamExpr(ST st, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = st.newTmpReg();
        Oprand op = st.tmpPop();
        if (op instanceof Bool) {
            st.tmpPush(((Bool)op).exclam());
        } else {
            Collections.addAll(codes,
                asm.bin("cmp", new Bool(false), op),
                asm.uni("sete", tmpReg.bite()),
                asm.bin("movzbq", tmpReg.bite(), tmpReg)
            );
            st.tmpPush(tmpReg);
        }
    }

    public static final void charLiteral(ST st, char theChar, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = st.newTmpReg();
        int ascii = theChar;
        codes.add(
            asm.bin("movq", new Num(Long.valueOf(ascii)), tmpReg)
        );
        st.tmpPush(tmpReg);
    }

    public static final void tkBreak(ST st, List<String> codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.jmp("jmp", st.getBreakLabel())
        );
    }

    public static final void tkContinue(ST st, List<String> codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.jmp("jmp", st.getContinueLabel())
        );
    }

    public static final void tkReturn(ST st, String expectedReturnType, List<String> codes) {
        if (!Program.shouldCompile()) return;
        if (Defs.DESC_TYPE_VOID.equals(expectedReturnType)) {
            st.tmpPop();
            codes.add(
                asm.jmp("jmp", st.getReturnLabel())
            );
        } else {
            Oprand returnVar = st.tmpPop();
            Collections.addAll(codes, 
                asm.bin("movq", returnVar, Reg.rax),
                asm.jmp("jmp", st.getReturnLabel())
            );
        }
    }
}
