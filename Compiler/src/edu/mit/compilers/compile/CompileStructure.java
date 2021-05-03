package edu.mit.compilers.compile;

import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.basic.Bool;
import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.grammar.DecafParserTokenTypes;
import edu.mit.compilers.grammar.DecafScannerTokenTypes;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.syntax.Program;

public class CompileStructure {
    public static final void binaryOpExpr(Integer operator, ABlock leftCodes, ABlock rightCodes, ABlock codes) {
        if (!Program.shouldCompile()) return;
        Reg resReg = Manager.newTmpReg();
        Oprand rOp = Manager.tmpPop();
        Oprand lOp = Manager.tmpPop();
        ABlock glueCodes = new ABlock();
        if (operator == DecafParserTokenTypes.SLASH || operator == DecafParserTokenTypes.PERCENT) {
            Reg resultReg = operator == DecafParserTokenTypes.SLASH ? Reg.rax : Reg.rdx;
            Reg divisor = Manager.newTmpReg();
            glueCodes.add(
                asm.bin("movq", lOp, Reg.rax),
                asm.bin("movq", rOp, divisor),
                asm.non("cqto"),
                asm.uni("idivq", divisor),
                asm.bin("movq", resultReg, resReg)
            );     
        } else {
            if (lOp instanceof Reg) {
                glueCodes.add(
                    asm.bin(Defs.binaryOpToken2Inst.get(operator), rOp, lOp)
                );
            } else {
                glueCodes.add(
                    asm.bin("movq", lOp, resReg),
                    asm.bin(Defs.binaryOpToken2Inst.get(operator), rOp, resReg)
                );
            }
        }
        codes.addAll(leftCodes);
        codes.addAll(rightCodes);
        codes.addAll(glueCodes);
        if (lOp instanceof Reg) {
            Manager.tmpPush(lOp);
        } else {
            Manager.tmpPush(resReg);
        }
    }

    public static final void binaryBoolExpr(Integer operator, ABlock leftCodes, ABlock rightCodes, ABlock codes) {
        if (!Program.shouldCompile()) return;
        Reg resReg = Manager.newTmpReg();
        Oprand rOp = Manager.tmpPop();
        Oprand lOp = Manager.tmpPop();
        ABlock glueCodes = new ABlock();
        String jmpOp = operator == DecafScannerTokenTypes.AND ? "je" : "jne";
        Label endLabel = new Label();
        if (lOp instanceof Reg) {
            leftCodes.add(
                asm.bin("cmp", new Num(0L), ((Reg)lOp).bite()),
                asm.jmp(jmpOp, endLabel)
            );
        } else {
            leftCodes.add(
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
            rightCodes.add(
                asm.bin("movq", rOp, resReg),
                asm.bin("cmpq", new Num(0L), resReg)
            );
        }
        glueCodes.add(
            asm.label(endLabel),
            asm.uni("setne", resReg.bite())
        );
        codes.addAll(leftCodes);
        codes.addAll(rightCodes);
        codes.addAll(glueCodes);
        Manager.tmpPush(resReg);
    }


    public static final void binaryCompExpr(Integer operator, ABlock leftCodes, ABlock rightCodes, ABlock codes) {
        if (!Program.shouldCompile()) return;
        Reg resReg = Manager.newTmpReg();
        Oprand rOp = Manager.tmpPop();
        Oprand lOp = Manager.tmpPop();
        ABlock glueCodes = new ABlock();
        glueCodes.add(
            asm.bin("movq", lOp, resReg),
            asm.bin("cmpq", rOp, resReg),
            asm.uni(Defs.setOnCondition.get(operator), resReg.bite()),
            asm.bin("movzbq", resReg.bite(), resReg)
        );
        codes.addAll(leftCodes);
        codes.addAll(rightCodes);
        codes.addAll(glueCodes);
        Manager.tmpPush(resReg);
    }

    public static final void minusExpr(ABlock codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = Manager.newTmpReg();
        Oprand op = Manager.tmpPop();
        if (op instanceof Num) {
            Manager.tmpPush(((Num)op).neg());
        } else {
            codes.add(
                asm.bin("movq", op, tmpReg),
                asm.uni("negq", tmpReg)
            );
            Manager.tmpPush(tmpReg);
        }
    }

    public static final void exclamExpr(ABlock codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = Manager.newTmpReg();
        Oprand op = Manager.tmpPop();
        if (op instanceof Bool) {
            Manager.tmpPush(((Bool)op).exclam());
        } else {
            codes.add(
                asm.bin("cmp", new Bool(false), op),
                asm.uni("sete", tmpReg.bite()),
                asm.bin("movzbq", tmpReg.bite(), tmpReg)
            );
            Manager.tmpPush(tmpReg);
        }
    }

    public static final void charLiteral(char theChar, ABlock codes) {
        if (!Program.shouldCompile()) return;
        Reg tmpReg = Manager.newTmpReg();
        int ascii = theChar;
        codes.add(
            asm.bin("movq", new Num(Long.valueOf(ascii)), tmpReg)
        );
        Manager.tmpPush(tmpReg);
    }

    public static final void tkBreak(ABlock codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.jmp("jmp", Manager.getBreakLabel())
        );
    }

    public static final void tkContinue(ABlock codes) {
        if (!Program.shouldCompile()) return;
        codes.add(
            asm.jmp("jmp", Manager.getContinueLabel())
        );
    }

    public static final void tkReturn(VarType expectedReturnType, ABlock codes) {
        if (!Program.shouldCompile()) return;
        if (expectedReturnType.isVoid()) {
            Manager.tmpPop();
            codes.add(
                asm.jmp("jmp", Manager.getReturnLabel())
            );
        } else {
            Oprand returnVar = Manager.tmpPop();
            codes.add( 
                asm.bin("movq", returnVar, Reg.rax),
                asm.jmp("jmp", Manager.getReturnLabel())
            );
        }
    }
}
