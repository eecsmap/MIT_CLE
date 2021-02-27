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
        // TODO============================================================================
            // parse block -> enter localST
            for (; child != null; child = child.getNextSibling()) {
                // parse fieldDecl
                child = fieldDecl(child, localST);
                // parse statements

            }
        // TODO============================================================================
        }
        return null;
    }

    private static void parseSimpleAssign(AST t, ST st) {
        // =
        String op = "=";
        AST c = t.getFirstChild();
        String lhsID = c.getText();
        String lhsType = st.getType(lhsID);
        c = c.getNextSibling();
        String rhsType = parseExpr(c, st);
        if (lhsType != rhsType) {
            // TODO - report error
        }
    }

    // TODO
    private static void parseMoreAssign(AST t, ST st) {
        // +=, -=, =
        String op = t.getText();
        AST c = t.getFirstChild();
        String lhsID = c.getText();
        String lhsType = st.getType(lhsID);
        c = c.getNextSibling();
        String rhsType = parseExpr(c, st);
        if (lhsType != rhsType) {
            // TODO - report error
        }
    }

    private static void parseIncre(AST t, ST st) {
        String op = t.getText();
        AST c = t.getFirstChild();
        String lhsID = c.getText();
        String lhsType = st.getType(lhsID);
        c = c.getNextSibling();
        // if it's normal ID
        if (c == null) {
            return;
        }
        // if array element
        String rhsType = parseExpr(c, st);
        if (rhsType != Defs.DESC_TYPE_INT) {
            // TODO - report error
        }
    }

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

        return method.type.substring(Defs.DESC_METHOD.length());
    }

    private static void parseIf(AST t, ST st) {
        AST c = t.getFirstChild();
        if (Defs.DESC_TYPE_BOOL != parseExpr(c, st)) {
            // report Error
        }
        c = parseBlock(c.getNextSibling(), st);
        if (c == null) {
            return;
        }
        // else
        parseBlock(c.getFirstChild(), st);
    }

    // doesn't suppeort declaration in for loop
    private static void parseFor(AST t, ST st) {
        AST c = t.getFirstChild();
        if (Defs.DESC_TYPE_BOOL != parseExpr(c, st)) {
            // report Error
        }
        c = c.getNextSibling();
        parseSimpleAssign(c, st);
        c = c.getNextSibling();
        parseMoreAssign(c, st);
        c = c.getNextSibling();
        parseBlock(c, st);
    }

    private static void parseWhile(AST t, ST st) {
        AST c = t.getFirstChild();
        if (Defs.DESC_TYPE_BOOL != parseExpr(c, st)) {
            // report Error
        }
        c = c.getNextSibling();
        parseBlock(c, st);
    }

    // TODO -> return the type of rhs
    // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    private static String parseExpr(AST t, ST st) {
        if (AstUtils.isBinaryOp(t)) {

            return null;
        }
        if (AstUtils.isBinaryBoolOp(t)) {

            return Defs.DESC_TYPE_BOOL;
        }
        switch(t.getType()) {
            case DecafScannerTokenTypes.ID:
                // TODO: location: method_call, array, var
                return null;
            case DecafScannerTokenTypes.INTLITERAL:
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.TK_true:
            case DecafScannerTokenTypes.TK_false:
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.MINUS:
                String subType = parseExpr(t.getFirstChild(), st);
                if (subType != Defs.DESC_TYPE_INT) {
                    // TODO: report error
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.EXCLAM:
                String subType = parseExpr(t.getFirstChild(), st);
                if (subType != Defs.DESC_TYPE_BOOL) {
                    // TODO: report error   
                }
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.TK_len:
                // TODO: parse array
                return null;
        }
        return null;
    }

    // return if null or next is else
    private static AST parseBlock(AST t, ST st) {
        // parse fields
        t = fieldDecl(t, st);
        // parse statements
        for (; t != null; t = t.getNextSibling()) {
            if (t.getType() == DecafScannerTokenTypes.TK_else) {
                return t;
            }
            parseStmt(t, st);
        }
        return null;
    }

    private static AST parseStmt(AST t, ST st) {
        switch (t.getType()) {
            case DecafScannerTokenTypes.ASSIGN:
            case DecafScannerTokenTypes.PLUSASSIGN:
            case DecafScannerTokenTypes.MINUSASSIGN:
                parseMoreAssign(t, st);
                break;
            case DecafScannerTokenTypes.INCRE:
            case DecafScannerTokenTypes.DECRE:
                parseIncre(t, st);
                break;
            case DecafScannerTokenTypes.TK_if:
                parseIf(t, st);
                break;
            case DecafScannerTokenTypes.TK_for:
                parseFor(t, st);
                break;
            case DecafScannerTokenTypes.TK_while:
                parseWhile(t, st);
                break;
            case DecafScannerTokenTypes.TK_break:
                // TODO: break
                break;
            case DecafScannerTokenTypes.TK_return:
                // TODO: return
                break;
            case DecafScannerTokenTypes.TK_continue:
                // TODO: continue
                break;
        }
        return null;
    }
}
