package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;

public class ControlFlow {
    static void parseIf(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        AST c = t.getFirstChild();
        Label ifExecutionEndLabel = new Label();
        Label ifElseEndLabel = new Label();
        // condition
        List<String> codesCondition = new ArrayList<>();
        String type = parseExpr(c, localST, codesCondition);
        Boolean hasElse = false;
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            System.err.printf("14 ");
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // if block
        List<String> codesIfExecution = new ArrayList<>();
        c = parseBlock(c.getNextSibling(), localST, codesIfExecution);
        // else block
        List<String> codesElseExecution = new ArrayList<>();
        if (c != null) {
            hasElse = true;
            parseBlock(c.getFirstChild(), localST, codesElseExecution);
        }
        if (!Er.hasError() && Program.compile) {
            codesCondition.add(
                asm.jmp("je", ifExecutionEndLabel)
            );
            if (hasElse) 
            codesIfExecution.add(
                asm.jmp("jmp", ifElseEndLabel)
            );
            codes.addAll(codesCondition);
            codes.addAll(codesIfExecution);
            codes.add(asm.label(ifExecutionEndLabel));
            if (hasElse)
            {
            codes.addAll(codesElseExecution);
            codes.add(asm.label(ifElseEndLabel));
            }
        }
    }

    // doesn't suppeort declaration in for loop
    static void parseFor(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        Label executionBeginLabel = new Label();
        Label conditionBeginLabel = new Label();
        Label loopEndLabel = new Label();
        localST.pushContext(t.getType());
        if (!Er.hasError() && Program.compile) {
            localST.pushContinueLabel(conditionBeginLabel);
            localST.pushBreakLabel(loopEndLabel);
        }
        AST c = t.getFirstChild();
        // simple assign
        List<String> codesInit = new ArrayList<>();
        parseSimpleAssign(c, localST, codesInit);
        c = c.getNextSibling();
        // condition expr
        List<String> codesCondition = new ArrayList<>();
        String conditionExprType = parseExpr(c, localST, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, conditionExprType)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, conditionExprType);
        }
        c = c.getNextSibling();
        // more assign
        List<String> codesIncrement = new ArrayList<>();
        parseMoreAssign(c, localST, codesIncrement);
        c = c.getNextSibling();
        // block
        List<String> codesExecution = new ArrayList<>();
        parseBlock(c, localST, codesExecution);
        if (!Er.hasError() && Program.compile) {
            codesInit.add(
                asm.jmp("jmp", conditionBeginLabel)
            );
            codesCondition.add(
                asm.jmp("jle", executionBeginLabel)
            );
            codes.addAll(codesInit);
            codes.add(asm.label(executionBeginLabel));
            codes.addAll(codesExecution);
            codes.addAll(codesIncrement);
            codes.add(asm.label(conditionBeginLabel));
            codes.addAll(codesCondition);
            codes.add(asm.label(loopEndLabel));
        }
        localST.popContext();
        if (!Er.hasError() && Program.compile) {
            localST.popContinueLabel();
            localST.popBreakLabel();
        }
    }

    static void parseWhile(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        Label executionBeginLabel = new Label();
        Label conditionBeginLabel = new Label();
        Label loopEndLabel = new Label();
        localST.pushContext(t.getType());
        if (!Er.hasError() && Program.compile) {
            localST.pushContinueLabel(conditionBeginLabel);
            localST.pushBreakLabel(loopEndLabel);
        }
        // condition
        AST c = t.getFirstChild();
        List<String> codesCondition = new ArrayList<>();
        String type = parseExpr(c, localST, codesCondition);
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            System.err.printf("15 ");
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // execution block
        c = c.getNextSibling();
        List<String> codesExecution = new ArrayList<>();
        parseBlock(c, localST, codesCondition);
        if (!Er.hasError() && Program.compile) {
            codesExecution.add(0,
                asm.jmp("jmp", conditionBeginLabel)
            );
            codesCondition.add(
                asm.jmp("jle", executionBeginLabel)
            );
            codes.add(asm.label(executionBeginLabel));
            codes.addAll(codesExecution);
            codes.add(asm.label(conditionBeginLabel));
            codes.addAll(codesCondition);
            codes.add(asm.label(loopEndLabel));
        }
        localST.popContext();
        if (!Er.hasError() && Program.compile) {
            localST.popContinueLabel();
            localST.popBreakLabel();
        }
    }
}
