package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Action.ActionType;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileMethod;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;

class Method {
    static AST declare(AST t, List<String> codes) {
        for (; t != null && AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST c = t.getFirstChild();
            String returnType = c.getText();
            if (Program.importST.getMethod(t.getText()) != null || !MethodUtils.push(new MethodDesc(returnType, t.getText()), false)) {
                Er.errDuplicatedDeclaration(t, t.getText());
            }
            boolean isMain = t.getText().equals("main");
            if (isMain) {
                Program.mainDeclared = true;
            }
            MethodUtils.enterScope(returnType);
            c = c.getNextSibling();
            // parse parameters
            ArrayList<String> params = new ArrayList<>();
            ArrayList<Descriptor> paramsDesc = new ArrayList<>();
            for (; c != null && c.getNumberOfChildren() == 1 && AstUtils.isType(c.getFirstChild()) && AstUtils.isID(c); c = c.getNextSibling()) {
                Descriptor desc = new VarDesc(c.getFirstChild().getText(), c.getText());
                paramsDesc.add(desc);
                if (!MethodUtils.push(desc, true)) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                params.add(c.getFirstChild().getText());
            }
            if (isMain && (params.size() > 0 || !returnType.equals(Defs.DESC_TYPE_VOID))) {
                Er.errMalformedMain(t, returnType, params.size());
            }
            Program.methodMap.put(t.getText(), params);
            // parse block
            List<String> codesMethod = new ArrayList<>();
            Structure.block(c, codesMethod);
            CompileMethod.declare(t.getText(), returnType, paramsDesc, codesMethod, codes);
            MethodUtils.leaveScope();
        }
        return t;
    }

    // return method type
    static String call(AST t, List<String> codes, boolean needReturnValue) {
        String methodName = t.getText();
        Descriptor desc = MethodUtils.getDesc(methodName);
        if (desc != null && !Defs.isMethodType(desc.getType())) {
            Er.errDuplicatedDeclaration(t, methodName);
        }
        Descriptor method = MethodUtils.getMethod(methodName);
        List<Oprand> argsList = new ArrayList<>();
        String methodType;
        if (method == null) {
            method = Program.importST.getMethod(methodName);
            if (method == null) {
                Er.errNotDefined(t, methodName);
                return null;
            }
            methodType = Defs.DESC_TYPE_WILDCARD;
            for (AST c = t.getFirstChild(); c != null; c = c.getNextSibling()) {
                Structure.expr(c, ActionType.LOAD, codes);
                CompileMethod.callParseArgs(argsList, codes);
            }
        } else {
            AST c = t.getFirstChild();
            ArrayList<String> params = Program.methodMap.get(method.getText());
            if (params == null) {
                Er.errNotDefined(c, method.getText());
                return Defs.DESC_TYPE_WILDCARD;
            }
            if (params.size() != t.getNumberOfChildren()) {
                Er.errArrayArgsMismatch(t);
                return Defs.getMethodType(method.getType());
            }
            for (int i = 0; c != null; c = c.getNextSibling(), i++) {
                String cType = Structure.expr(c, ActionType.LOAD, codes);
                if (!Defs.equals(params.get(i), cType)) {
                    Er.errType(c, params.get(i), cType);
                }
                CompileMethod.callParseArgs(argsList, codes);
            }
            methodType = Defs.getMethodType(method.getType());
        }
        CompileMethod.call(argsList, needReturnValue, methodName, codes);
        return methodType;
    }
}
