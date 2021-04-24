package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Oprand;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;


public class Program {
    static final ST importST = new ST();
    static final ST globalST = new ST();
    static final Map<String, ArrayList<String>> methodMap = new HashMap<>();
    static boolean mainDeclared = false;
    static boolean compile;
    static Stack<Oprand> result = new Stack<>();
    static boolean shouldCompile() {
        return !Er.hasError() && compile;
    }
    private static List<Addr> stringLiteralAddrList = new ArrayList<>();
    private static List<String> stringLiteralList = new ArrayList<>();

    static Addr addStringLiteral(String string) {
        Addr stringAddr = new Addr(new Label(true).toString());
        stringLiteralAddrList.add(stringAddr);
        stringLiteralList.add(string);
        return stringAddr;
    }

    // parse an AST to IRTree with the help of Symbol Tree
    public static void irParse(AST t, List<String> codes) {
        // treat import Symbol Table as special one
        if (codes == null) {
            compile = false;
        }
        t = importDecl(t, importST);
        t = Field.declare(t, globalST, codes);
        t = Method.declare(t, globalST, codes);
        if (!mainDeclared) {
            Er.errMainNotDefined(t);
        }
        if (shouldCompile() && stringLiteralList.size() > 0) {
            List<String> rodata = new ArrayList<>();
            // TODO: add stringliterals into rodata
            codes.addAll(0, rodata);
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
