package edu.mit.compilers.syntax;

import antlr.collections.AST;
import edu.mit.compilers.asm.AMethod;
import edu.mit.compilers.compile.CompileControlFlow;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.defs.Defs.ActionType;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.tools.Err;

public class ControlFlow {
    static void ifFlow(AST t, AMethod codes) {
        Manager.enterScope(false);
        AST c = t.getFirstChild();
        // condition
        AMethod codesCondition = new AMethod();
        VarType type = Structure.expr(c, ActionType.LOAD, codesCondition);
        Boolean hasElse = false;
        if (type != null && !type.isBool()) {
            Err.errType(c, VarType.BOOL, type);
        }
        // if block
        AMethod codesIfExecution = new AMethod();
        c = Structure.block(c.getNextSibling(), codesIfExecution);
        // else block
        AMethod codesElseExecution = new AMethod();
        if (c != null) {
            hasElse = true;
            Structure.block(c.getFirstChild(), codesElseExecution);
        }
        CompileControlFlow.ifFlow(hasElse, codesCondition, codesIfExecution, codesElseExecution, codes);
        Manager.leaveScope();
    }

    // doesn't suppeort declaration in for loop
    static void forFlow(AST t, AMethod codes) {
        Manager.enterScope(true);
        AST c = t.getFirstChild();
        // simple assign
        AMethod codesInit = new AMethod();
        BasicOpration.simpleAssign(c, codesInit);
        c = c.getNextSibling();
        // condition expr
        AMethod codesCondition = new AMethod();
        VarType conditionExprType = Structure.expr(c, ActionType.LOAD, codesCondition);
        if (!conditionExprType.isBool()) {
            Err.errType(c, VarType.BOOL, conditionExprType);
        }
        c = c.getNextSibling();
        // more assign
        AMethod codesIncrement = new AMethod();
        BasicOpration.moreAssign(c, codesIncrement);
        c = c.getNextSibling();
        // block
        AMethod codesExecution = new AMethod();
        Structure.block(c, codesExecution);
        CompileControlFlow.forFlow(codesInit, codesCondition, codesIncrement, codesExecution, codes);
        Manager.leaveScope();
    }

    static void whileFlow(AST t, AMethod codes) {
        Manager.enterScope(true);
        // condition
        AST c = t.getFirstChild();
        AMethod codesCondition = new AMethod();
        VarType type = Structure.expr(c, ActionType.LOAD, codesCondition);
        if (type != null && !type.isBool()) {
            Err.errType(c, VarType.BOOL, type);
        }
        // execution block
        c = c.getNextSibling();
        AMethod codesExecution = new AMethod();
        Structure.block(c, codesExecution);
        CompileControlFlow.whileFlow(codesCondition, codesExecution, codes);
        Manager.leaveScope();
    }
}
