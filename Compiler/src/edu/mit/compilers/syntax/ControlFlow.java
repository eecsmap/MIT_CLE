package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.compile.CompileControlFlow;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.MethodUtils;
import edu.mit.compilers.tools.Er;

public class ControlFlow {
    static void ifFlow(AST t, MethodUtils st, List<String> codes) {
        st.enterScope();
        AST c = t.getFirstChild();

        // condition
        List<String> codesCondition = new ArrayList<>();
        String type = Structure.expr(c, st, ActionType.LOAD, codesCondition);
        Boolean hasElse = false;
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // if block
        List<String> codesIfExecution = new ArrayList<>();
        c = Structure.block(c.getNextSibling(), st, codesIfExecution);
        // else block
        List<String> codesElseExecution = new ArrayList<>();
        if (c != null) {
            hasElse = true;
            Structure.block(c.getFirstChild(), st, codesElseExecution);
        }
        CompileControlFlow.ifFlow(st, st, hasElse, codesCondition, codesIfExecution, codesElseExecution, codes);
        st.leaveScope();
    }

    // doesn't suppeort declaration in for loop
    static void forFlow(AST t, MethodUtils st, List<String> codes) {
        st.enterScope();
        Label incrementBeginLabel = new Label();
        Label loopEndLabel = new Label();
        st.pushContext(t.getType());
        if (Program.shouldCompile()) {
            st.pushContinueLabel(incrementBeginLabel);
            st.pushBreakLabel(loopEndLabel);
        }
        AST c = t.getFirstChild();
        // simple assign
        List<String> codesInit = new ArrayList<>();
        BasicOpration.simpleAssign(c, st, codesInit);
        c = c.getNextSibling();
        // condition expr
        List<String> codesCondition = new ArrayList<>();
        String conditionExprType = Structure.expr(c, st, ActionType.LOAD, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, conditionExprType)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, conditionExprType);
        }
        c = c.getNextSibling();
        // more assign
        List<String> codesIncrement = new ArrayList<>();
        BasicOpration.moreAssign(c, st, codesIncrement);
        c = c.getNextSibling();
        // block
        List<String> codesExecution = new ArrayList<>();
        Structure.block(c, st, codesExecution);

        st.popContext();
        if (Program.shouldCompile()) {
            st.popContinueLabel();
            st.popBreakLabel();
        }
        CompileControlFlow.forFlow(st, st, incrementBeginLabel, loopEndLabel, codesInit, codesCondition, codesIncrement, codesExecution, codes);
        st.leaveScope();
    }

    static void whileFlow(AST t, MethodUtils st, List<String> codes) {
        st.enterScope();
        Label conditionBeginLabel = new Label();
        Label loopEndLabel = new Label();
        st.pushContext(t.getType());
        if (Program.shouldCompile()) {
            st.pushContinueLabel(conditionBeginLabel);
            st.pushBreakLabel(loopEndLabel);
        }
        // condition
        AST c = t.getFirstChild();
        List<String> codesCondition = new ArrayList<>();
        String type = Structure.expr(c, st, ActionType.LOAD, codesCondition);
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // execution block
        c = c.getNextSibling();
        List<String> codesExecution = new ArrayList<>();
        Structure.block(c, st, codesExecution);
        st.popContext();
        if (Program.shouldCompile()) {
            st.popContinueLabel();
            st.popBreakLabel();
        }
        CompileControlFlow.whileFlow(st, st, conditionBeginLabel, loopEndLabel, codesCondition, codesExecution, codes);
        st.leaveScope();
    }
}
