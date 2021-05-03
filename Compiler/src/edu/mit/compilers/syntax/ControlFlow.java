package edu.mit.compilers.syntax;

import antlr.collections.AST;
import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.compile.CompileControlFlow;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.defs.Defs.ActionType;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.tools.Err;

public class ControlFlow {
    static void ifFlow(AST t, ABlock codes) {
        Manager.enterScope(false);
        AST c = t.getFirstChild();
        // condition
        ABlock codesCondition = new ABlock();
        VarType type = Structure.expr(c, ActionType.LOAD, codesCondition);
        Boolean hasElse = false;
        if (type != null && !type.isBool()) {
            Err.errType(c, VarType.BOOL, type);
        }
        // if block
        ABlock codesIfExecution = new ABlock();
        c = Structure.block(c.getNextSibling(), codesIfExecution);
        // else block
        ABlock codesElseExecution = new ABlock();
        if (c != null) {
            hasElse = true;
            Structure.block(c.getFirstChild(), codesElseExecution);
        }
        CompileControlFlow.ifFlow(hasElse, codesCondition, codesIfExecution, codesElseExecution, codes);
        Manager.leaveScope();
    }

    // doesn't suppeort declaration in for loop
    static void forFlow(AST t, ABlock codes) {
        Manager.enterScope(true);
        AST c = t.getFirstChild();
        // simple assign
        ABlock codesInit = new ABlock();
        BasicOpration.simpleAssign(c, codesInit);
        c = c.getNextSibling();
        // condition expr
        ABlock codesCondition = new ABlock();
        VarType conditionExprType = Structure.expr(c, ActionType.LOAD, codesCondition);
        if (!conditionExprType.isBool()) {
            Err.errType(c, VarType.BOOL, conditionExprType);
        }
        c = c.getNextSibling();
        // more assign
        ABlock codesIncrement = new ABlock();
        BasicOpration.moreAssign(c, codesIncrement);
        c = c.getNextSibling();
        // block
        ABlock codesExecution = new ABlock();
        Structure.block(c, codesExecution);
        CompileControlFlow.forFlow(codesInit, codesCondition, codesIncrement, codesExecution, codes);
        Manager.leaveScope();
    }

    static void whileFlow(AST t, ABlock codes) {
        Manager.enterScope(true);
        // condition
        AST c = t.getFirstChild();
        ABlock codesCondition = new ABlock();
        VarType type = Structure.expr(c, ActionType.LOAD, codesCondition);
        if (type != null && !type.isBool()) {
            Err.errType(c, VarType.BOOL, type);
        }
        // execution block
        c = c.getNextSibling();
        ABlock codesExecution = new ABlock();
        Structure.block(c, codesExecution);
        CompileControlFlow.whileFlow(codesCondition, codesExecution, codes);
        Manager.leaveScope();
    }
}
