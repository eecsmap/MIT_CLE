package edu.mit.compilers.ir;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;

class MethodDecl {
    static AST parse(AST t, ST globalST, List<String> codes) {
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
            Program.parseBlock(c, localST, codesMethod);
            if (!Er.hasError() && Program.compile) {
                codes.addAll(asm.methodDeclStart(t.getText(), paramsDesc));
                codes.addAll(codesMethod);
                codes.addAll(asm.methodDeclEnd());
            }
        }
        return t;
    }
}
