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
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.syntax.Program;

public class CompileStructure {
    public static final void binaryOpExpr(Integer operator, List<String> leftCodes, List<String> rightCodes, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg resReg = Manager.newTmpReg();
        Oprand rOp = Manager.tmpPop();
        Oprand lOp = Manager.tmpPop();
        List<String> glueCodes = new ArrayList<>();
        if (operator == DecafParserTokenTypes.SLASH || operator == DecafParserTokenTypes.PERCENT) {
            Reg resultReg = operator == DecafParserTokenTypes.SLASH ? Reg.rax : Reg.rdx;
            Reg divisor = Manager.newTmpReg();
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
        Manager.tmpPush(resReg);
    }

    public static final void binaryBoolExpr(Integer operator, List<String> leftCodes, List<String> rightCodes, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg resReg = Manager.newTmpReg();
        Oprand rOp = Manager.tmpPop();
        Oprand lOp = Manager.tmpPop();
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
        Manager.tmpPush(resReg);
    }


    public static final void binaryCompExpr(Integer operator, List<String> leftCodes, List<String> rightCodes, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg resReg = Manager.newTmpReg();
        Oprand rOp = Manager.tmpPop();
        Oprand lOp = Manager.tmpPop();
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
        Manager.tmpPush(resReg);
    }

    public static final void minusExpr(List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = Manager.newTmpReg();
        Oprand op = Manager.tmpPop();
        if (op instanceof Num) {
            Manager.tmpPush(((Num)op).neg());
        } else {
            Collections.addAll(codes,
                asm.bin("movq", op, tmpReg),
                asm.uni("negq", tmpReg)
            );
            Manager.tmpPush(tmpReg);
        }
    }

    public static final void exclamExpr(List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = Manager.newTmpReg();
        Oprand op = Manager.tmpPop();
        if (op instanceof Bool) {
            Manager.tmpPush(((Bool)op).exclam());
        } else {
            Collections.addAll(codes,
                asm.bin("cmp", new Bool(false), op),
                asm.uni("sete", tmpReg.bite()),
                asm.bin("movzbq", tmpReg.bite(), tmpReg)
            );
            Manager.tmpPush(tmpReg);
        }
    }

    public static final void charLiteral(char theChar, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = Manager.newTmpReg();
        int ascii = theChar;
        codes.add(
            asm.bin("movq", new Num(Long.valueOf(ascii)), tmpReg)
        );
        Manager.tmpPush(tmpReg);
    }

    public static final void tkBreak(List<String> codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.jmp("jmp", Manager.getBreakLabel())
        );
    }

    public static final void tkContinue(List<String> codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.jmp("jmp", Manager.getContinueLabel())
        );
    }

    public static final void tkReturn(String expectedReturnType, List<String> codes) {
        if (!Program.shouldCompile()) return;
        if (Defs.DESC_TYPE_VOID.equals(expectedReturnType)) {
            Manager.tmpPop();
            codes.add(
                asm.jmp("jmp", Manager.getReturnLabel())
            );
        } else {
            Oprand returnVar = Manager.tmpPop();
            Collections.addAll(codes, 
                asm.bin("movq", returnVar, Reg.rax),
                asm.jmp("jmp", Manager.getReturnLabel())
            );
        }
    }
}
