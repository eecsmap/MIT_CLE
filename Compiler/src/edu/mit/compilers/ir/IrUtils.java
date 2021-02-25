package edu.mit.compilers.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.grammar.*;

public class IrUtils {
    private static boolean hasErorr = false;

    private static final ST importST = new ST();
    private static final ST globalST = new ST();
    private static final Map<String, ArrayList<String>> methodMap = new HashMap<>();

    private static boolean hasErorr() {
        return hasErorr;
    }

    // parse an AST to IRTree with the help of Symbol Tree
    public static void irParse(AST t) {
        // treat import Symbol Table as special one
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
        // TODO============================================================================
        for (; AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST child = t.getFirstChild();
            globalST.push(new MethodDesc(child.getText(), t.getText()));
            child = child.getNextSibling();
            // parse parameters
            ArrayList<String> params = new ArrayList<>();
            for (; child != null && AstUtils.isID(child); child = child.getNextSibling()) {
                paramST.push(new VarDesc(child.getFirstChild().getText(), child.getText()));
                params.add(child.getFirstChild().getText());
            }
            // parse block -> enter localST
            for (; child != null; child = child.getNextSibling()) {
                // parse fieldDecl
                child = fieldDecl(child, localST);
                // parse statements

            }
        }
        // TODO============================================================================
        return null;
    }

    // TODO
    private static void parseAssignment(AST t, ST st) {
        AST c = t.getFirstChild();
        String Op = "=";
        String lhs = c.getText();
        String lhsType = st.getType(lhs);
        
    }

    // TODO
    private static String parseMethodCall(AST t, ST st) {
        Descriptor method = st.getMethod(t.getText());
        if (method == null) {
            method = importST.getMethod(t.getText());
            if (method == null) {
                // TODO - report error
                return null;
            }
            return "*";
        }
        
        AST child = t.getFirstChild();
        ArrayList<String> params = methodMap.get(method.text);
        int i = 0;
        for (; child != null; child = child.getNextSibling(), i++) {
            String child_type = parseExpr(child, st);
            if (params.get(i) != child_type) {
                // TODO - report error
            }
        }

        return method.type.substring(Defs.DESC_METHOD.length());;
    }

    // TODO
    private static void parseIf(AST t, ST st) {

    }

    // TODO
    private static void parseFor(AST t, ST st) {

    }

    // TODO -> return the type of rhs
    private static String parseExpr(AST t, ST st) {
        if (AstUtils.isID(t)) {

        }
        return null;
    }
}
