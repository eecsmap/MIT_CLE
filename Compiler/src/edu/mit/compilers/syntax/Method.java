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
import edu.mit.compilers.tools.Er;

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
            if (Program.importST.getMethod(t.getText()) != null || !Manager.push(new MethodDesc(returnType, t.getText()), false)) {
                Er.errDuplicatedDeclaration(t, t.getText());
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
                    Er.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                params.add(stringToVarType.get(c.getFirstChild().getText()));
            }
            if (isMain && (params.size() > 0 || !returnType.isVoid())) {
                Er.errMalformedMain(t, returnType, params.size());
            }
            Program.methodMap.put(t.getText(), params);
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
            Er.errDuplicatedDeclaration(t, methodName);
        }
        Descriptor method = Manager.getMethod(methodName);
        List<Oprand> argsList = new ArrayList<>();
        VarType methodType;
        if (method == null) {
            method = Program.importST.getMethod(methodName);
            if (method == null) {
                Er.errNotDefined(t, methodName);
                return null;
            }
            methodType = VarType.WILDCARD;
            for (AST c = t.getFirstChild(); c != null; c = c.getNextSibling()) {
                Structure.expr(c, ActionType.LOAD, codes);
                CompileMethod.callParseArgs(argsList, codes);
            }
        } else {
            AST c = t.getFirstChild();
            List<VarType> params = Program.methodMap.get(method.getText());
            if (params == null) {
                Er.errNotDefined(c, method.getText());
                return VarType.WILDCARD;
            }
            if (params.size() != t.getNumberOfChildren()) {
                Er.errArrayArgsMismatch(t);
                return method.getType().plain();
            }
            for (int i = 0; c != null; c = c.getNextSibling(), i++) {
                VarType cType = Structure.expr(c, ActionType.LOAD, codes);
                if (!params.get(i).equals(cType)) {
                    Er.errType(c, params.get(i), cType);
                }
                CompileMethod.callParseArgs(argsList, codes);
            }
            methodType = method.getType();
        }
        CompileMethod.call(argsList, needReturnValue, methodName, codes);
        return methodType.plain();
    }
}
