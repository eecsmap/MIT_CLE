package edu.mit.compilers.syntax;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.asm.basic.Addr;
import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileProgram;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.tools.Err;


public class Program {
    private Program() {}
    static boolean compile;
    public static boolean shouldCompile() {
        return !Err.hasError() && compile;
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
    public static void irParse(AST t, ABlock codes) {
        // treat import Symbol Table as special one
        compile = (codes != null);
        t = importDecl(t);
        t = Field.declare(t, codes);
        t = Method.declare(t, codes);
        if (Manager.getMethod("main") == null) {
            Err.errMainNotDefined(t);
        }
        CompileProgram.addROData(stringLiteralList, stringLiteralLabelList, codes);
        CompileProgram.addArrayOutBoundReturn(codes);
    }

    // return the next AST to parse
    static AST importDecl(AST t) {
        for (; t != null && AstUtils.isImport(t); t = t.getNextSibling()) {
            // parse single import statement
            String methodName = t.getFirstChild().getText();
            if (methodName.equals("main")) {
                Err.errBadImport(t.getFirstChild(), methodName);
                continue;
            }
            if (!Manager.push(new MethodDesc(VarType.WILDCARD, methodName), false)) {
                Err.errDuplicatedDeclaration(t, methodName);
            }
        }
        return t;
    }
}