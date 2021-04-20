package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;


public class Program {
    static final ST importST = new ST();
    static final ST globalST = new ST();
    static final Map<String, ArrayList<String>> methodMap = new HashMap<>();
    static boolean mainDeclared = false;
    static boolean compile;

    // parse an AST to IRTree with the help of Symbol Tree
    public static void irParse(AST t, List<String> codes) {
        // treat import Symbol Table as special one
        if (codes == null) {
            compile = false;
        }
        t = importDecl(t, importST);
        t = FieldDecl.parse(t, globalST, codes);
        t = Method.declare(t, globalST, codes);
        if (!mainDeclared) {
            Er.errMainNotDefined(t);
        }
    }

    // return the next AST to parse
    static AST importDecl(AST t, ST importST) {
        for (; t != null && AstUtils.isImport(t); t = t.getNextSibling()) {
            // parse single import statement
            String methodName = t.getFirstChild().getText();
            if (methodName.equals("main")) {
                Er.errBadImport(t.getFirstChild(), methodName);
                continue;
            }
            MethodDesc desc = new MethodDesc(Defs.DESC_METHOD_WILDCARD, methodName);
            if (!importST.push(desc)) {
                Er.errDuplicatedDeclaration(t, methodName);
            }
        }
        return t;
    }
}
