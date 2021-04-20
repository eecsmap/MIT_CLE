package edu.mit.compilers.ir;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
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
            if (Program.importST.getMethod(t.getText()) != null || !globalST.push(new MethodDesc(Defs.makeMethodType(returnType), t.getText()))) {
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
            Structure.parseBlock(c, localST, codesMethod);
            if (!Er.hasError() && Program.compile) {
                codes.addAll(asm.methodDeclStart(t.getText(), paramsDesc));
                codes.addAll(codesMethod);
                codes.addAll(asm.methodDeclEnd());
            }
        }
        return t;
    }

    // return method type
    static String call(AST t, ST st) {
        String methodName = t.getText();
        String _type = st.getType(methodName);
        if (_type != null && !Defs.isMethodType(_type)) {
            Er.errDuplicatedDeclaration(t, methodName);
        }
        Descriptor method = st.getMethod(methodName);
        if (method == null) {
            method = Program.importST.getMethod(methodName);
            if (method == null) {
                System.err.printf("8 ");
                Er.errNotDefined(t, methodName);
                return null;
            }
            return Defs.DESC_TYPE_WILDCARD;
        }
        
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
            String cType = parseExpr(c, st);
            if (!Defs.equals(params.get(i), cType)) {
                System.err.printf("10 ");
                Er.errType(c, params.get(i), cType);
            }
        }

        return Defs.getMethodType(method.getType());
    }
}
