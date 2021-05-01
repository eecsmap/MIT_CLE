package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.compile.CompileControlFlow;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.tools.Er;

public class ControlFlow {
    static void ifFlow(AST t, List<String> codes) {
        Manager.enterScope(false);
        AST c = t.getFirstChild();
        // condition
        List<String> codesCondition = new ArrayList<>();
        String type = Structure.expr(c, ActionType.LOAD, codesCondition);
        Boolean hasElse = false;
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // if block
        List<String> codesIfExecution = new ArrayList<>();
        c = Structure.block(c.getNextSibling(), codesIfExecution);
        // else block
        List<String> codesElseExecution = new ArrayList<>();
        if (c != null) {
            hasElse = true;
            Structure.block(c.getFirstChild(), codesElseExecution);
        }
        CompileControlFlow.ifFlow(hasElse, codesCondition, codesIfExecution, codesElseExecution, codes);
        Manager.leaveScope();
    }

    // doesn't suppeort declaration in for loop
    static void forFlow(AST t, List<String> codes) {
        Manager.enterScope(true);
        AST c = t.getFirstChild();
        // simple assign
        List<String> codesInit = new ArrayList<>();
        BasicOpration.simpleAssign(c, codesInit);
        c = c.getNextSibling();
        // condition expr
        List<String> codesCondition = new ArrayList<>();
        String conditionExprType = Structure.expr(c, ActionType.LOAD, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, conditionExprType)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, conditionExprType);
        }
        c = c.getNextSibling();
        // more assign
        List<String> codesIncrement = new ArrayList<>();
        BasicOpration.moreAssign(c, codesIncrement);
        c = c.getNextSibling();
        // block
        List<String> codesExecution = new ArrayList<>();
        Structure.block(c, codesExecution);
        CompileControlFlow.forFlow(codesInit, codesCondition, codesIncrement, codesExecution, codes);
        Manager.leaveScope();
    }

    static void whileFlow(AST t, List<String> codes) {
        Manager.enterScope(true);
        // condition
        AST c = t.getFirstChild();
        List<String> codesCondition = new ArrayList<>();
        String type = Structure.expr(c, ActionType.LOAD, codesCondition);
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // execution block
        c = c.getNextSibling();
        List<String> codesExecution = new ArrayList<>();
        Structure.block(c, codesExecution);
        CompileControlFlow.whileFlow(codesCondition, codesExecution, codes);
        Manager.leaveScope();
    }
}
