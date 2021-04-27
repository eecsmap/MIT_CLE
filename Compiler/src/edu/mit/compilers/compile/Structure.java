package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Bool;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.AsmUtils;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ArrayDesc;
import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.grammar.*;

public class Structure {
    private Structure() {}
    private static boolean isBinaryAnyOp(AST t) {
        return 
        AstUtils.isBinaryOp(t)
        || AstUtils.isBinaryCompOp(t)
        || AstUtils.isBinaryBoolOp(t)
        || AstUtils.isBinaryIntCompOp(t);
    }

    private static String binaryExpr(AST t, ST st, List<String> codes) {
        AST l = t.getFirstChild();
        AST r = l.getNextSibling();
        List<String> leftCodes = new ArrayList<>();
        List<String> rightCodes = new ArrayList<>();
        String lType = expr(l, st, ActionType.STORE, leftCodes);
        String rType = expr(r, st, ActionType.LOAD, rightCodes);
        String returnType = null;
        if (AstUtils.isBinaryOp(t)) {
            if (lType != null && !Defs.equals(lType, rType)) {
                System.err.printf("16 ");
                Er.errType(l, lType, rType);
            }
            returnType = lType;
        } else if (AstUtils.isBinaryCompOp(t)) {
            if (lType != null && (!Defs.equals(lType, rType) || Defs.equals(Defs.DESC_TYPE_VOID, lType))) {
                System.err.printf("31 ");
                Er.errType(r, lType, rType);
            }
            returnType = Defs.DESC_TYPE_BOOL;    
        } else if (AstUtils.isBinaryBoolOp(t)) {
            if (lType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, lType)) {
                System.err.printf("17 ");
                Er.errType(l, Defs.DESC_TYPE_BOOL, lType);
            }
            if (rType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, rType)) {
                System.err.printf("18 ");
                Er.errType(r, Defs.DESC_TYPE_BOOL, rType);
            }
            returnType = Defs.DESC_TYPE_BOOL;
        } else if (AstUtils.isBinaryIntCompOp(t)) {
            if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
                System.err.printf("27 ");
                Er.errType(l, Defs.DESC_TYPE_INT, lType);
            }
            if (rType != null && !Defs.equals(Defs.DESC_TYPE_INT, rType)) {
                System.err.printf("28 ");
                Er.errType(r, Defs.DESC_TYPE_INT, rType);
            }
            returnType = Defs.DESC_TYPE_BOOL;
        }
        if (Program.shouldCompile()) {
            Reg resReg = st.newTmpReg();
            Oprand rOp = st.tmpPop();
            Oprand lOp = st.tmpPop();
            List<String> glueCodes = new ArrayList<>();
            if (AstUtils.isBinaryBoolOp(t)) {
                String jmpOp = t.getType() == DecafScannerTokenTypes.AND ? "je" : "jne";
                Label endLabel = new Label();
                if (lOp instanceof Reg) {
                    Collections.addAll(leftCodes,
                        asm.bin("cmp", new Num(0L), ((Reg)lOp).bite()),
                        asm.jmp(jmpOp, endLabel)
                    );
                } else {
                    Collections.addAll(leftCodes,
                        asm.bin("movq", lOp, resReg),
                        asm.bin("cmp", new Num(0L), resReg),
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
                        asm.bin("cmp", new Num(0L), resReg)
                    );
                }
                Collections.addAll(glueCodes,
                    asm.label(endLabel),
                    asm.uni("setne", resReg.bite())
                );
            } else if (AstUtils.isBinaryCompOp(t) || AstUtils.isBinaryIntCompOp(t)) {
                Reg interReg = st.newTmpReg();
                Collections.addAll(glueCodes,
                    asm.bin("movq", lOp, interReg),
                    asm.bin("cmp", rOp, interReg),
                    asm.uni(AsmUtils.setOnCondition.get(t.getType()), resReg.bite())
                );
            } else if (t.getType() == DecafParserTokenTypes.SLASH || t.getType() == DecafParserTokenTypes.PERCENT) {
                Reg resultReg = t.getType() == DecafParserTokenTypes.SLASH ? Reg.rax : Reg.rdx;
                Addr divisor = st.newTmpAddr();
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
                    asm.bin(AsmUtils.binaryOpToken2Inst.get(t.getType()), rOp, resReg)
                );
            }
            codes.addAll(leftCodes);
            codes.addAll(rightCodes);
            codes.addAll(glueCodes);
            st.tmpPush(resReg);
        }
        return returnType;
    }

    private static String idExpr(AST t, ST st, ActionType action, List<String> codes) {
        Descriptor desc = st.getDesc(t.getText());
        if (desc == null) {
            desc = Program.importST.getDesc(t.getText());
            if (desc == null) {
                System.out.printf("19 ");
                Er.errNotDefined(t, t.getText());
                return Defs.DESC_TYPE_WILDCARD;
            }
        }
        String type = desc.getType();
        // array
        if (Defs.isArrayType(type)) {
            if (t.getNumberOfChildren() == 0) {
                return type;
            }
            return Element.arrayElement(t, st, action, codes);
        }
        // method
        if (Defs.isMethodType(type)) {
            return Method.call(t, st, codes, true);
        }
        // var
        if (Program.shouldCompile()) {
            st.tmpPush(desc.getAddr());
        }
        return type;
    }

    private static String minusExpr(AST t, ST st, List<String> codes) {
        if (t.getNumberOfChildren() == 1 && t.getFirstChild().getType() == DecafScannerTokenTypes.INTLITERAL) {
            return Element.intLiteral(t.getFirstChild(), st, true);
        }
        String subType = expr(t.getFirstChild(), st, ActionType.LOAD, codes);
        if (subType != null && !Defs.equals(Defs.DESC_TYPE_INT, subType)) {
            System.err.printf("20 ");
            Er.errType(t, Defs.DESC_TYPE_INT, subType);
        }
        if (Program.shouldCompile()) {
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
        return Defs.DESC_TYPE_INT;
    }

    private static String exclamExpr(AST t, ST st, List<String> codes) {
        String subType = expr(t.getFirstChild(), st, ActionType.LOAD, codes);
        if (subType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, subType)) {
            System.err.printf("21 ");
            Er.errType(t, Defs.DESC_TYPE_BOOL, subType); 
        }
        if (Program.shouldCompile()) {
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
        return Defs.DESC_TYPE_BOOL;
    }

    // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    static String expr(AST t, ST st, ActionType action, List<String> codes) {
        if (t == null) {
            st.tmpPush(null);
            return null;
        }
        if (isBinaryAnyOp(t) && t.getNumberOfChildren() == 2) {
            return binaryExpr(t, st, codes);
        }
        switch (t.getType()) {
            case DecafScannerTokenTypes.ID:
                return idExpr(t, st, action, codes);
            case DecafScannerTokenTypes.INTLITERAL:
                return Element.intLiteral(t, st, false);
            case DecafScannerTokenTypes.TK_true:
                if (Program.shouldCompile()) {
                    st.tmpPush(new Bool(true));
                }
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.TK_false:
                if (Program.shouldCompile()) {
                    st.tmpPush(new Bool(false));
                }
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.MINUS:
                return minusExpr(t, st, codes);
            case DecafScannerTokenTypes.EXCLAM:
                return exclamExpr(t, st, codes);
            case DecafScannerTokenTypes.TK_len:
                AST c = t.getFirstChild();
                Descriptor desc = st.getDesc(c.getText());
                String subType = desc.getType();
                if (subType == null || !Defs.isArrayType(subType)) {
                    System.err.printf("22 ");
                    Er.errNotDefined(c, c.getText());
                }
                if (Program.shouldCompile()) {
                    st.tmpPush(new Num(((ArrayDesc)desc).getCap()));
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.STRINGLITERAL:
                if (Program.shouldCompile()) {
                    Addr stringAddr = Program.addStringLiteral(t.getText());
                    st.tmpPush(stringAddr);
                }
                return Defs.TYPE_STRING_LITERAL;
            case DecafScannerTokenTypes.CHARLITERAL:
                if (Program.shouldCompile()) {
                    // ? WHERE IS IT
                }
                return Defs.TYPE_CHAR_LITERAL;
            case DecafScannerTokenTypes.COLON:
                return BasicOpration.relOps(t, st, codes);
            default:
                return null;
        }
    }

    // if null -> return; if TK_else -> return current AST
    static AST block(AST t, ST st, List<String> codes) {
        // parse fields
        t = Field.declare(t, st, codes);
        // parse statements
        for (; t != null; t = t.getNextSibling()) {
            if (t.getType() == DecafScannerTokenTypes.TK_else) {
                return t;
            }
            parseStmt(t, st, codes);
        }
        return null;
    }

    static void parseStmt(AST t, ST st, List<String> codes) {
        codes.add("");
        switch (t.getType()) {
            case DecafScannerTokenTypes.ID:
                Method.call(t, st, codes, false);
                return;
            case DecafScannerTokenTypes.ASSIGN:
            case DecafScannerTokenTypes.PLUSASSIGN:
            case DecafScannerTokenTypes.MINUSASSIGN:
            case DecafScannerTokenTypes.INCRE:
            case DecafScannerTokenTypes.DECRE:
                BasicOpration.moreAssign(t, st, codes);
                return;
            case DecafScannerTokenTypes.TK_if:
                ControlFlow.ifFlow(t, st, codes);
                return;
            case DecafScannerTokenTypes.TK_for:
                ControlFlow.forFlow(t, st, codes);
                return;
            case DecafScannerTokenTypes.TK_while:
                ControlFlow.whileFlow(t, st, codes);
                return;
            case DecafScannerTokenTypes.TK_break:
                if (!AstUtils.isLoop(st.getContext())) {
                    System.err.printf("23 ");
                    Er.errBreak(t);
                }
                if (Program.shouldCompile()) {
                    codes.add(
                        asm.jmp("jmp", st.getBreakLabel())
                    );
                }
                return;
            case DecafScannerTokenTypes.TK_continue:
                if (!AstUtils.isLoop(st.getContext())) {
                    System.err.printf("24 ");
                    Er.errContinue(t);
                }
                if (Program.shouldCompile()) {
                    codes.add(
                        asm.jmp("jmp", st.getContinueLabel())
                    );
                }
                return;
            case DecafScannerTokenTypes.TK_return:
                String expectedReturnType = st.getReturnType();
                if (expectedReturnType == null) {
                    System.err.printf("25 ");
                    Er.report(t, "null return");
                    return;
                }
                String actualReturnType = expr(t.getFirstChild(), st, ActionType.LOAD, codes);
                if (actualReturnType == null) {
                    actualReturnType = Defs.DESC_TYPE_VOID;
                }
                if (!Defs.equals(expectedReturnType, actualReturnType)) {
                    System.err.printf("26 ");
                    Er.errType(t, expectedReturnType, actualReturnType);
                }
                if (Program.shouldCompile()) {
                    Oprand returnVar = st.tmpPop();
                    Collections.addAll(codes, 
                        asm.bin("movq", returnVar, Reg.rax),
                        asm.jmp("jmp", st.getReturnLabel())
                    );
                }
                return;
            default:
                return;
        }
    }
}
