package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;

class Method {
    static AST declare(AST t, ST globalST, List<String> codes) {
        for (; t != null && AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST c = t.getFirstChild();
            String returnType = c.getText();
            if (Program.importST.getMethod(t.getText()) != null || !globalST.push(new MethodDesc(returnType, t.getText()))) {
                Er.errDuplicatedDeclaration(t, t.getText());
            }
            boolean isMain = t.getText().equals("main");
            if (isMain) {
                Program.mainDeclared = true;
            }
            ST localST = new ST(globalST, returnType);
            c = c.getNextSibling();
            // parse parameters
            ArrayList<String> params = new ArrayList<>();
            ArrayList<Descriptor> paramsDesc = new ArrayList<>();
            for (; c != null && c.getNumberOfChildren() == 1 && AstUtils.isType(c.getFirstChild()) && AstUtils.isID(c); c = c.getNextSibling()) {
                Descriptor desc = new VarDesc(c.getFirstChild().getText(), c.getText());
                paramsDesc.add(desc);
                if (!localST.push(desc)) {
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
            Structure.block(c, localST, codesMethod);
            if (Program.shouldCompile()) {
                codes.addAll(asm.methodDeclStart(t.getText(), paramsDesc));
                codes.addAll(codesMethod);
                codes.addAll(asm.methodDeclEnd());
            }
        }
        return t;
    }

    // return method type
    static String call(AST t, ST st, List<String> codes) {
        String methodName = t.getText();
        Descriptor desc = st.getDesc(methodName);
        if (desc != null && !Defs.isMethodType(desc.getType())) {
            Er.errDuplicatedDeclaration(t, methodName);
        }
        Descriptor method = st.getMethod(methodName);
        List<Oprand> argsList = new ArrayList<>();
        String methodType;
        if (method == null) {
            method = Program.importST.getMethod(methodName);
            if (method == null) {
                System.err.printf("8 ");
                Er.errNotDefined(t, methodName);
                return null;
            }
            AST c = t.getFirstChild();
            methodType = Defs.DESC_TYPE_WILDCARD;
            for (; c != null; c = c.getNextSibling()) {
                Structure.expr(c, st, codes);
                if (Program.shouldCompile()) {
                    argsList.add(Program.result.pop());
                }
            }
        } else {
            AST c = t.getFirstChild();
            ArrayList<String> params = Program.methodMap.get(method.getText());
            if (params == null) {
                System.err.printf("9 ");
                Er.errNotDefined(c, method.getText());
                return Defs.DESC_TYPE_WILDCARD;
            }
            if (params.size() != t.getNumberOfChildren()) {
                Er.errArrayArgsMismatch(t);
                return Defs.getMethodType(method.getType());
            }
            for (int i = 0; c != null; c = c.getNextSibling(), i++) {
                String cType = Structure.expr(c, st, codes);
                if (!Defs.equals(params.get(i), cType)) {
                    System.err.printf("10 ");
                    Er.errType(c, params.get(i), cType);
                }
                if (Program.shouldCompile()) {
                    argsList.add(Program.result.pop());
                }
            }
            methodType = Defs.getMethodType(method.getType());
        }
        if (Program.shouldCompile()) {
            Addr res = st.newTmpAddr();
            codes.addAll(asm.methodCall(methodName, argsList));
            codes.add(
                asm.bin("movq", Reg.rax, res)
            );
            Program.result.push(res);
        }
        return methodType;
    }
}
