package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileProgram;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;


public class Program {
    private Program() {}
    static final SymbolTable importST = new SymbolTable();
    static final Map<String, ArrayList<String>> methodMap = new HashMap<>();
    static boolean mainDeclared = false;
    static boolean compile;
    public static boolean shouldCompile() {
        return !Er.hasError() && compile;
    }
    private static List<Label> stringLiteralLabelList = new ArrayList<>();
    private static List<String> stringLiteralList = new ArrayList<>();

    static Addr addStringLiteral(String string) {
        Label stringLabel = new Label(true);
        stringLiteralLabelList.add(stringLabel);
        stringLiteralList.add(string);
        return new Addr(stringLabel.toString(), true);
    }

    // parse an AST to IRTree with the help of Symbol Tree
    public static void irParse(AST t, List<String> codes) {
        // treat import Symbol Table as special one
        compile = (codes != null);
        t = importDecl(t, importST);
        t = Field.declare(t, codes);
        t = Method.declare(t, codes);
        if (!mainDeclared) {
            Er.errMainNotDefined(t);
        }
        CompileProgram.addROData(stringLiteralList, stringLiteralLabelList, codes);
        CompileProgram.addArrayOutBoundReturn(codes);
    }

    // return the next AST to parse
    static AST importDecl(AST t, SymbolTable importST) {
        for (; t != null && AstUtils.isImport(t); t = t.getNextSibling()) {
            // parse single import statement
            String methodName = t.getFirstChild().getText();
            if (methodName.equals("main")) {
                Er.errBadImport(t.getFirstChild(), methodName);
                continue;
            }
            MethodDesc desc = new MethodDesc(Defs.DESC_METHOD_WILDCARD, methodName);
            if (importST.push(desc, false) != -1L) {
                Er.errDuplicatedDeclaration(t, methodName);
            }
        }
        return t;
    }
}
