package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileMethod;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.tools.Err;

class Method {
    static Map<String, VarType> stringToVarType = new HashMap<>(){{
        put("void", VarType.VOID);
        put("bool", VarType.BOOL);
        put("int", VarType.INT);
    }};
    static AST declare(AST t, List<String> codes) {
        for (; t != null && AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST c = t.getFirstChild();
            VarType returnType = stringToVarType.get(c.getText());
            MethodDesc methodDesc = new MethodDesc(returnType, t.getText());
            if (Program.importST.getMethod(t.getText()) != null || !Manager.push(methodDesc, false)) {
                Err.errDuplicatedDeclaration(t, t.getText());
            }
            boolean isMain = t.getText().equals("main");
            if (isMain) {
                Program.mainDeclared = true;
            }
            Manager.enterScope(returnType);
            c = c.getNextSibling();
            // parse parameters
            List<VarType> params = new ArrayList<>();
            List<Descriptor> paramsDesc = new ArrayList<>();
            for (; c != null && c.getNumberOfChildren() == 1 && AstUtils.isType(c.getFirstChild()) && AstUtils.isID(c); c = c.getNextSibling()) {
                Descriptor desc = new VarDesc(stringToVarType.get(c.getFirstChild().getText()), c.getText());
                paramsDesc.add(desc);
                if (!Manager.push(desc, true)) {
                    Err.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                params.add(stringToVarType.get(c.getFirstChild().getText()));
            }
            if (isMain && (params.size() > 0 || !returnType.isVoid())) {
                Err.errMalformedMain(t, returnType, params.size());
            }
            methodDesc.setParamsList(params);
            // parse block
            List<String> codesMethod = new ArrayList<>();
            Structure.block(c, codesMethod);
            CompileMethod.declare(t.getText(), returnType, paramsDesc, codesMethod, codes);
            Manager.leaveScope();
        }
        return t;
    }

    // return method type
    static VarType call(AST t, List<String> codes, boolean needReturnValue) {
        String methodName = t.getText();
        Descriptor desc = Manager.getDesc(methodName);
        if (desc != null && !desc.getType().isMethod()) {
            Err.errDuplicatedDeclaration(t, methodName);
        }
        MethodDesc methodDesc = Manager.getMethod(methodName);
        List<Oprand> argsList = new ArrayList<>();
        VarType methodType;
        if (methodDesc == null) {
            methodDesc = Program.importST.getMethod(methodName);
            if (methodDesc == null) {
                Err.errNotDefined(t, methodName);
                return null;
            }
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
