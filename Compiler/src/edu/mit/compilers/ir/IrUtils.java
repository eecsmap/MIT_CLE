package edu.mit.compilers.ir;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.grammar.*;

public class IrUtils {
    // parse an AST to IRTree with the help of Symbol Tree
    public static void irParse(AST t) {
        // treat import Symbol Table as special one
        ST importST = new ST();
        ST globalST = new ST();
        t = importDecl(t, importST);
        t = fieldDecl(t, globalST);
        t = methodDecl(t, globalST);
    }

    // return the next AST to parse
    private static AST importDecl(AST t, ST importST) {
        for (; AstUtils.isImport(t); t = t.getNextSibling()) {
            // parse single import statement
            MethodDesc desc = new MethodDesc(Defs.DESC_METHOD, t.getText());
            importST.push(desc);
        }
        return t;
    }

    private static AST fieldDecl(AST t, ST currentST) {
        for (; AstUtils.isType(t); t = t.getNextSibling()) {
            // parse single import statement
            String type = AstUtils.t0.get(t.getType());
            parseFields(t, type, currentST);
        }
        return t;
    }

  /**
   * int x, y, z, A[100]; */
    private static void parseFields(AST t, String type, ST currentST) {
        for (; t != null; t = t.getNextSibling()) {
            AST child = t.getFirstChild();
            if (child != null) {
                // child is null -> is array
                currentST.push(new ArrayDesc("array_" + type, t.getText(), child.getText()));
                continue;
            }
            // child is null -> it's single Variable
            currentST.push(new VarDesc(type, t.getText()));
        }
        return;
    }

    private static AST methodDecl(AST t, ST globalST) {
        ST paramST = new ST(globalST);
        ST localST = new ST(paramST);
        // ================TODO===================
        for (; AstUtils.isType(t); t = t.getNextSibling()) {
            // parse single import statement
            String type = AstUtils.t0.get(t.getType());
            parseFields(t, type, currentST);
        }
        // ================TODO===================
        return null;
    }
}
