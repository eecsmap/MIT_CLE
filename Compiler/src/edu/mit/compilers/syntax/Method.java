package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.asm.AProgram;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileMethod;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.defs.Defs.ActionType;
import edu.mit.compilers.tools.Err;

class Method {
    static AST declare(AST t, AProgram program) {
        for (; t != null && AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST c = t.getFirstChild();
            VarType returnType = Defs.stringToVarType.get(c.getText());
            MethodDesc methodDesc = new MethodDesc(returnType, t.getText());
            if (!Manager.push(methodDesc, false)) {
                Err.errDuplicatedDeclaration(t, t.getText());
            }
            Manager.enterScope(returnType, t.getText());
            c = c.getNextSibling();
            // parse parameters
            List<VarType> params = new ArrayList<>();
            List<Descriptor> paramsDesc = new ArrayList<>();
            for (; c != null && c.getNumberOfChildren() == 1 && AstUtils.isType(c.getFirstChild()) && AstUtils.isID(c); c = c.getNextSibling()) {
                Descriptor desc = new VarDesc(Defs.stringToVarType.get(c.getFirstChild().getText()), c.getText());
                paramsDesc.add(desc);
                if (!Manager.push(desc, true)) {
                    Err.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                params.add(Defs.stringToVarType.get(c.getFirstChild().getText()));
            }
            if (t.getText().equals("main") && (params.size() > 0 || !returnType.isVoid())) {
                Err.errMalformedMain(t, returnType, params.size());
            }
            methodDesc.setParamsList(params);
            // parse block
            ABlock codesWrappedMethod = new ABlock();
            ABlock codesMethod = new ABlock();
            Structure.block(c, codesMethod);
            CompileMethod.declare(t.getText(), returnType, paramsDesc, codesMethod, codesWrappedMethod);
            program.addMethod(codesWrappedMethod);
            Manager.leaveScope();
        }
        return t;
    }

    // return method type
    static VarType call(AST t, ABlock codes, boolean needReturnValue) {
        String methodName = t.getText();
        Descriptor desc = Manager.getDesc(methodName);
        if (desc != null && !desc.getType().isMethod()) {
            Err.errDuplicatedDeclaration(t, methodName);
        }
        MethodDesc methodDesc = Manager.getMethod(methodName);
        List<Oprand> argsList = new ArrayList<>();
        VarType methodType;
        if (methodDesc == null) {
            Err.errNotDefined(t, methodName);
            return null;
        }
        if (methodDesc.getType().isWildcard()) {
            methodType = VarType.WILDCARD;
            for (AST c = t.getFirstChild(); c != null; c = c.getNextSibling()) {
                Structure.expr(c, ActionType.LOAD, codes);
                CompileMethod.callParseArgs(argsList, codes);
            }
        } else {
            AST c = t.getFirstChild();
            List<VarType> params = methodDesc.getParamsList();
            if (params == null) {
                Err.errNotDefined(c, methodDesc.getText());
                return VarType.WILDCARD;
            }
            if (params.size() != t.getNumberOfChildren()) {
                Err.errArrayArgsMismatch(t);
                return methodDesc.getType().plain();
            }
            for (int i = 0; c != null; c = c.getNextSibling(), i++) {
                VarType cType = Structure.expr(c, ActionType.LOAD, codes);
                if (!params.get(i).equals(cType)) {
                    Err.errType(c, params.get(i), cType);
                }
                CompileMethod.callParseArgs(argsList, codes);
            }
            methodType = methodDesc.getType();
        }
        CompileMethod.call(argsList, needReturnValue, methodName, codes);
        return methodType.plain();
    }
}
