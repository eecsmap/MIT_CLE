package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.compile.CompileControlFlow;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;

public class ControlFlow {
    static void ifFlow(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        AST c = t.getFirstChild();

        // condition
        List<String> codesCondition = new ArrayList<>();
        String type = Structure.expr(c, localST, ActionType.LOAD, codesCondition);
        Boolean hasElse = false;
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // if block
        List<String> codesIfExecution = new ArrayList<>();
        c = Structure.block(c.getNextSibling(), localST, codesIfExecution);
        // else block
        List<String> codesElseExecution = new ArrayList<>();
        if (c != null) {
            hasElse = true;
            Structure.block(c.getFirstChild(), localST, codesElseExecution);
        }
        CompileControlFlow.ifFlow(st, localST, hasElse, codesCondition, codesIfExecution, codesElseExecution, codes);
    }

    // doesn't suppeort declaration in for loop
    static void forFlow(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        Label incrementBeginLabel = new Label();
        Label loopEndLabel = new Label();
        localST.pushContext(t.getType());
        if (Program.shouldCompile()) {
            localST.pushContinueLabel(incrementBeginLabel);
            localST.pushBreakLabel(loopEndLabel);
        }
        AST c = t.getFirstChild();
        // simple assign
        List<String> codesInit = new ArrayList<>();
        BasicOpration.simpleAssign(c, localST, codesInit);
        c = c.getNextSibling();
        // condition expr
        List<String> codesCondition = new ArrayList<>();
        String conditionExprType = Structure.expr(c, localST, ActionType.LOAD, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, conditionExprType)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, conditionExprType);
        }
        c = c.getNextSibling();
        // more assign
        List<String> codesIncrement = new ArrayList<>();
        BasicOpration.moreAssign(c, localST, codesIncrement);
        c = c.getNextSibling();
        // block
        List<String> codesExecution = new ArrayList<>();
        Structure.block(c, localST, codesExecution);

        localST.popContext();
        if (Program.shouldCompile()) {
            localST.popContinueLabel();
            localST.popBreakLabel();
        }
        CompileControlFlow.forFlow(st, localST, incrementBeginLabel, loopEndLabel, codesInit, codesCondition, codesIncrement, codesExecution, codes);
    }

    static void whileFlow(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        Label conditionBeginLabel = new Label();
        Label loopEndLabel = new Label();
        localST.pushContext(t.getType());
        if (Program.shouldCompile()) {
            localST.pushContinueLabel(conditionBeginLabel);
            localST.pushBreakLabel(loopEndLabel);
        }
        // condition
        AST c = t.getFirstChild();
        List<String> codesCondition = new ArrayList<>();
        String type = Structure.expr(c, localST, ActionType.LOAD, codesCondition);
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // execution block
        c = c.getNextSibling();
        List<String> codesExecution = new ArrayList<>();
        Structure.block(c, localST, codesExecution);
        localST.popContext();
        if (Program.shouldCompile()) {
            localST.popContinueLabel();
            localST.popBreakLabel();
        }
        CompileControlFlow.whileFlow(st, localST, conditionBeginLabel, loopEndLabel, codesCondition, codesExecution, codes);
    }
}
