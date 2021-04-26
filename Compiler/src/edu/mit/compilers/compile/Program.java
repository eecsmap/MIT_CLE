package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.Reg;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;


public class Program {
    private Program() {}
    static final ST importST = new ST();
    static final ST globalST = new ST();
    static final Map<String, ArrayList<String>> methodMap = new HashMap<>();
    static boolean mainDeclared = false;
    static boolean compile;
    static boolean shouldCompile() {
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
        t = Field.declare(t, globalST, codes);
        t = Method.declare(t, globalST, codes);
        if (!mainDeclared) {
            Er.errMainNotDefined(t);
        }
        addROData(codes);
        addArrayOutBoundReturn(codes);
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
            if (!importST.push(desc, false)) {
                Er.errDuplicatedDeclaration(t, methodName);
            }
        }
        return t;
    }

    static void addROData(List<String> codes) {
        if (shouldCompile() && !stringLiteralList.isEmpty()) {
            List<String> rodata = new ArrayList<>();
            Collections.addAll(rodata,
                asm.nonDir(".text"),
                asm.uniDir(".section", ".rodata")
            );
            for (int i = 0; i < stringLiteralList.size(); i++) {
                Collections.addAll(rodata,
                    asm.label(stringLiteralLabelList.get(i)),
                    asm.uniDir(".string", stringLiteralList.get(i))
                );
            }
            codes.addAll(0, rodata);
        }
    }

    static void addArrayOutBoundReturn(List<String> codes) {
        Collections.addAll(codes,
            asm.label(Defs.EXIT_ARRAY_OUTBOUND_LABEL),
            asm.bin("movq", new Num(1L), Reg.rax),
            asm.bin("movq", new Num(255L), Reg.rbx),
            asm.uni("int", new Num(0x80L))
        );
    }
}
