package edu.mit.compilers.ir;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.grammar.*;

public class IrUtils {
    private static final ST importST = new ST();
    private static final ST globalST = new ST();
    private static final Map<String, ArrayList<String>> methodMap = new HashMap<>();
    private static boolean mainDeclared = false;
    private static boolean compile;

    // parse an AST to IRTree with the help of Symbol Tree
    public static void irParse(AST t, List<String> codes) {
        // treat import Symbol Table as special one
        if (codes == null) {
            compile = false;
        }
        t = importDecl(t, importST);
        t = fieldDecl(t, globalST, codes);
        t = methodDecl(t, globalST, codes);
        if (!mainDeclared) {
            Er.errMainNotDefined(t);
        }
    }

    // return the next AST to parse
    private static AST importDecl(AST t, ST importST) {
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

    private static AST fieldDecl(AST t, ST st) {
        for (; t != null && AstUtils.isType(t); t = t.getNextSibling()) {
            String type = null;
            switch (t.getType()) {
                case DecafScannerTokenTypes.TK_bool:
                    type = Defs.DESC_TYPE_BOOL;
                    break;
                case DecafScannerTokenTypes.TK_int:
                    type = Defs.DESC_TYPE_INT;
                    break;
            }
            AST c = t.getFirstChild();
            for (; c != null; c = c.getNextSibling()) {
                AST cc = c.getFirstChild();
                if (cc != null) {
                    // cc is not null -> is array
                    String cap = cc.getText();
                    if(Integer.parseInt(cap) <= 0) {
                        Er.errBadArrayCap(cc);
                        cap = "9999999";
                    }
                    if (st == globalST && importST.getMethod(c.getText()) != null) {
                        Er.errDuplicatedDeclaration(c, c.getText());
                        continue;
                    }
                    if (!st.push(new ArrayDesc(Defs.makeArrayType(type), c.getText(), cap))) {
                        Er.errDuplicatedDeclaration(c, c.getText());
                    }
                    continue;
                }
                if (importST.getMethod(c.getText()) != null) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                // cc is null -> it's single Variable
                if (!st.push(new VarDesc(type, c.getText()))) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                }
            }
        }
        return t;
    }

    private static AST methodDecl(AST t, ST globalST) {
        for (; t != null && AstUtils.isID(t); t = t.getNextSibling()) {
            // parse method type
            AST c = t.getFirstChild();
            String returnType = c.getText();
            if (importST.getMethod(t.getText()) != null || !globalST.push(new MethodDesc(Defs.makeMethodType(returnType), t.getText()))) {
                Er.errDuplicatedDeclaration(t, t.getText());
            }
            boolean isMain = t.getText().equals("main");
            if (isMain) {
                mainDeclared = true;
            }
            ST localST = new ST(globalST, returnType);
            c = c.getNextSibling();
            // parse parameters
            ArrayList<String> params = new ArrayList<>();
            for (; c != null && c.getNumberOfChildren() == 1 && AstUtils.isType(c.getFirstChild()) && AstUtils.isID(c); c = c.getNextSibling()) {
                if (!localST.push(new VarDesc(c.getFirstChild().getText(), c.getText()))) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                params.add(c.getFirstChild().getText());
            }
            if (isMain && (params.size() > 0 || !returnType.equals(Defs.DESC_TYPE_VOID))) {
                Er.errMalformedMain(t, returnType, params.size());
            }
            methodMap.put(t.getText(), params);
            // parse block
            parseBlock(c, localST);
        }
        return t;
    }

    // return lType
    private static String parseAssign(AST t, ST st) {
        AST c = t.getFirstChild();
        String lID = c.getText();
        String lType = st.getType(lID);
        if (lType == null) {
            System.err.printf("1 ");
            Er.errNotDefined(c, c.getText());
        } else if (Defs.isArrayType(lType)) {
            lType = parseArrayElement(c, st);
        } else if (c.getNumberOfChildren() > 0) {
            Er.errVarIsNotArray(c, lID);
        }
        return lType;
    }

    private static void parseBinaryAssign(AST t, ST st, boolean simple) {
        String lType = parseAssign(t, st);
        AST c = t.getFirstChild();
        c = c.getNextSibling();
        String rType = parseExpr(c, st);
        if (lType != null && (!Defs.equals(lType, rType) || (!simple && !Defs.equals(Defs.DESC_TYPE_INT, lType)))) {
            System.err.printf("2 ");
            Er.errType(c, lType, rType);
        }
    }

    private static void parseUnaryAssign(AST t, ST st) {
        String lType = parseAssign(t, st);
        AST c = t.getFirstChild();
        if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
            System.err.printf("31 ");
            Er.errType(c, Defs.DESC_TYPE_INT, lType);
        }
    }

    // only =
    private static void parseSimpleAssign(AST t, ST st, List<String> codes) {
        String op = "=";
        parseBinaryAssign(t, st, true);
    }

    // +=, -=, =, ++, --
    private static void parseMoreAssign(AST t, ST st, List<String> codes) {
        String op = t.getText();
        if (AstUtils.isBinaryAssignOp(t)) {
            parseBinaryAssign(t, st, op.equals("="));
        } else {
            parseUnaryAssign(t, st);
        }
    }

    private static String parseRelOps(AST t, ST st) {
        AST c = t.getFirstChild();
        AST cc = c.getFirstChild();
        String cond = parseExpr(cc, st);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, cond)) {
            Er.errType(t, Defs.DESC_TYPE_BOOL, cond);
        }
        cc = cc.getNextSibling();
        String trueType = parseExpr(cc, st);
        c = c.getNextSibling();
        String falseType = parseExpr(c, st);
        if (!Defs.equals(trueType, falseType)) {
            Er.errType(t, trueType, falseType);
        }
        return trueType;
    }

    // return method type
    private static String parseMethodCall(AST t, ST st) {
        String methodName = t.getText();
        String _type = st.getType(methodName);
        if (_type != null && !Defs.isMethodType(_type)) {
            Er.errDuplicatedDeclaration(t, methodName);
        }
        Descriptor method = st.getMethod(methodName);
        if (method == null) {
            method = importST.getMethod(methodName);
            if (method == null) {
                System.err.printf("8 ");
                Er.errNotDefined(t, methodName);
                return null;
            }
            return Defs.DESC_TYPE_WILDCARD;
        }
        
        AST c = t.getFirstChild();
        ArrayList<String> params = methodMap.get(method.getText());
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

    private static String parseArrayElement(AST t, ST st) {
        Descriptor desc = st.getArray(t.getText());
        if (desc == null) {
            System.err.printf("11 ");
            Er.errNotDefined(t, t.getText());
            return null;
        }
        String type = desc.getType();
        AST c = t.getFirstChild();
        String subType = parseExpr(c, st);
        if (subType == null) {
            Er.errType(t, Defs.getArrayType(type), type);
            return Defs.getArrayType(type);
        }
        if (!Defs.equals(Defs.DESC_TYPE_INT, subType)) {
            System.err.printf("12 ");
            Er.errArrayIndexNotInt(t, Defs.DESC_TYPE_INT, subType);
            return Defs.getArrayType(type);
        }
        if (c.getType() == DecafScannerTokenTypes.INTLITERAL && desc.findVar(c.getText()) == null) {
            System.err.printf("13 ");
            Er.errArrayOutbound(t, desc.getText(), c.getText());
        }
        return Defs.getArrayType(type);
    }

    private static void parseIf(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        AST c = t.getFirstChild();
        Label ifExecutionEndLabel = new Label();
        Label ifElseEndLabel = new Label();
        // condition
        List<String> codesCondition = new ArrayList<>();
        String type = parseExpr(c, localST, codesCondition);
        Boolean hasElse = false;
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            System.err.printf("14 ");
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // if block
        List<String> codesIfExecution = new ArrayList<>();
        c = parseBlock(c.getNextSibling(), localST, codesIfExecution);
        // else block
        List<String> codesElseExecution = new ArrayList<>();
        if (c != null) {
            hasElse = true;
            parseBlock(c.getFirstChild(), localST, codesElseExecution);
        }
        if (!Er.hasError()) {
            codesCondition.add(
                asm.jmp("je", ifExecutionEndLabel)
            );
            if (hasElse) 
            codesIfExecution.add(
                asm.jmp("jmp", ifElseEndLabel)
            );
            codes.addAll(codesCondition);
            codes.addAll(codesIfExecution);
            codes.add(asm.label(ifExecutionEndLabel));
            if (hasElse)
            {
            codes.addAll(codesElseExecution);
            codes.add(asm.label(ifElseEndLabel));
            }
        }
    }

    // doesn't suppeort declaration in for loop
    private static void parseFor(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        Label executionBeginLabel = new Label();
        Label conditionBeginLabel = new Label();
        Label loopEndLabel = new Label();
        localST.pushContext(t.getType());
        if (!Er.hasError()) {
            localST.pushContinueLabel(conditionBeginLabel);
            localST.pushBreakLabel(loopEndLabel);
        }
        AST c = t.getFirstChild();
        // simple assign
        List<String> codesInit = new ArrayList<>();
        parseSimpleAssign(c, localST, codesInit);
        c = c.getNextSibling();
        // condition expr
        List<String> codesCondition = new ArrayList<>();
        String conditionExprType = parseExpr(c, localST, codesCondition);
        if (!Defs.equals(Defs.DESC_TYPE_BOOL, conditionExprType)) {
            Er.errType(c, Defs.DESC_TYPE_BOOL, conditionExprType);
        }
        c = c.getNextSibling();
        // more assign
        List<String> codesIncrement = new ArrayList<>();
        parseMoreAssign(c, localST, codesIncrement);
        c = c.getNextSibling();
        // block
        List<String> codesExecution = new ArrayList<>();
        parseBlock(c, localST, codesExecution);
        if (!Er.hasError()) {
            codesInit.add(
                asm.jmp("jmp", conditionBeginLabel)
            );
            codesCondition.add(
                asm.jmp("jle", executionBeginLabel)
            );
            codes.addAll(codesInit);
            codes.add(asm.label(executionBeginLabel));
            codes.addAll(codesExecution);
            codes.addAll(codesIncrement);
            codes.add(asm.label(conditionBeginLabel));
            codes.addAll(codesCondition);
            codes.add(asm.label(loopEndLabel));
        }
        localST.popContext();
        if (!Er.hasError()) {
            localST.popContinueLabel();
            localST.popBreakLabel();
        }
    }

    private static void parseWhile(AST t, ST st, List<String> codes) {
        ST localST = new ST(st);
        Label executionBeginLabel = new Label();
        Label conditionBeginLabel = new Label();
        Label loopEndLabel = new Label();
        localST.pushContext(t.getType());
        if (!Er.hasError()) {
            localST.pushContinueLabel(conditionBeginLabel);
            localST.pushBreakLabel(loopEndLabel);
        }
        // condition
        AST c = t.getFirstChild();
        List<String> codesCondition = new ArrayList<>();
        String type = parseExpr(c, localST, codesCondition);
        if (type != null && !Defs.equals(Defs.DESC_TYPE_BOOL, type)) {
            System.err.printf("15 ");
            Er.errType(c, Defs.DESC_TYPE_BOOL, type);
        }
        // execution block
        c = c.getNextSibling();
        List<String> codesExecution = new ArrayList<>();
        parseBlock(c, localST, codesCondition);
        if (!Er.hasError()) {
            codesExecution.add(0,
                asm.jmp("jmp", conditionBeginLabel)
            );
            codesCondition.add(
                asm.jmp("jle", executionBeginLabel)
            );
            codes.add(asm.label(executionBeginLabel));
            codes.addAll(codesExecution);
            codes.add(asm.label(conditionBeginLabel));
            codes.addAll(codesCondition);
            codes.add(asm.label(loopEndLabel));
        }
        localST.popContext();
        if (!Er.hasError()) {
            localST.popContinueLabel();
            localST.popBreakLabel();
        }
    }

    private static String parseIntLiteral(AST t, boolean isNegative) {
        String number;
        if (isNegative) {
            number = "-" + t.getText();
        } else {
            number = t.getText();
        }
        try {
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            try {
                Long.decode(number);
            } catch (Exception ee) {
                Er.errIntegerTooLarge(t, number);
            }
        }
        return Defs.DESC_TYPE_INT;
    }

    // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    private static String parseExpr(AST t, ST st, List<String> codes) {
        if (t == null) {
            return null;
        }
        if (AstUtils.isBinaryOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && !Defs.equals(lType, rType)) {
                System.err.printf("16 ");
                Er.errType(l, lType, rType);
            }
            return lType;
        }
        if (AstUtils.isBinaryCompOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && (!Defs.equals(lType, rType) || Defs.equals(Defs.DESC_TYPE_VOID, lType))) {
                System.err.printf("31 ");
                Er.errType(r, lType, rType);
            }
            return Defs.DESC_TYPE_BOOL;
        }
        if (AstUtils.isBinaryBoolOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, lType)) {
                System.err.printf("17 ");
                Er.errType(l, Defs.DESC_TYPE_BOOL, lType);
            }
            if (rType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, rType)) {
                System.err.printf("18 ");
                Er.errType(r, Defs.DESC_TYPE_BOOL, rType);
            }
            return Defs.DESC_TYPE_BOOL;
        }
        if (AstUtils.isBinaryIntCompOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            String lType = parseExpr(l, st);
            String rType = parseExpr(r, st);
            if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
                System.err.printf("27 ");
                Er.errType(l, Defs.DESC_TYPE_INT, lType);
            }
            if (rType != null && !Defs.equals(Defs.DESC_TYPE_INT, rType)) {
                System.err.printf("28 ");
                Er.errType(r, Defs.DESC_TYPE_INT, rType);
            }
            return Defs.DESC_TYPE_BOOL;
        }
        switch(t.getType()) {
            case DecafScannerTokenTypes.ID:
                String type = st.getType(t.getText());
                if (type == null) {
                    type = importST.getType(t.getText());
                    if (type == null) {
                        System.out.printf("19 ");
                        Er.errNotDefined(t, t.getText());
                        return Defs.DESC_TYPE_WILDCARD;
                    }
                }
                // array
                if (Defs.isArrayType(type)) {
                    if (t.getNumberOfChildren() == 0) {
                        return type;
                    }
                    return parseArrayElement(t, st);
                }
                // method
                if (Defs.isMethodType(type)) {
                    return parseMethodCall(t, st);
                }
                // var
                return type;
            case DecafScannerTokenTypes.INTLITERAL:
                return parseIntLiteral(t, false);
            case DecafScannerTokenTypes.TK_true:
            case DecafScannerTokenTypes.TK_false:
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.MINUS:
                if (t.getNumberOfChildren() == 1 && t.getFirstChild().getType() == DecafScannerTokenTypes.INTLITERAL) {
                    return parseIntLiteral(t.getFirstChild(), true);
                }
                String subType = parseExpr(t.getFirstChild(), st);
                if (subType != null && !Defs.equals(Defs.DESC_TYPE_INT, subType)) {
                    System.err.printf("20 ");
                    Er.errType(t, Defs.DESC_TYPE_INT, subType);
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.EXCLAM:
                String subType0 = parseExpr(t.getFirstChild(), st);
                if (subType0 != null && !Defs.equals(Defs.DESC_TYPE_BOOL, subType0)) {
                    System.err.printf("21 ");
                    Er.errType(t, Defs.DESC_TYPE_BOOL, subType0); 
                }
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.TK_len:
                AST c = t.getFirstChild();
                String subType1 = st.getType(c.getText());
                if (subType1 == null || !Defs.isArrayType(subType1)) {
                    System.err.printf("22 ");
                    Er.errNotDefined(c, c.getText());
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.STRINGLITERAL:
                return Defs.TYPE_STRING_LITERAL;
            case DecafScannerTokenTypes.CHARLITERAL: 
                return Defs.TYPE_CHAR_LITERAL;
            case DecafScannerTokenTypes.COLON:
                return parseRelOps(t, st);
        }
        return null;
    }

    // if null -> return; if TK_else -> return current AST
    private static AST parseBlock(AST t, ST st, List<String> codes) {
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

    private static void parseStmt(AST t, ST st) {
        switch (t.getType()) {
            case DecafScannerTokenTypes.ID:
                parseMethodCall(t, st);
                break;
            case DecafScannerTokenTypes.ASSIGN:
            case DecafScannerTokenTypes.PLUSASSIGN:
            case DecafScannerTokenTypes.MINUSASSIGN:
            case DecafScannerTokenTypes.INCRE:
            case DecafScannerTokenTypes.DECRE:
                parseMoreAssign(t, st);
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
                if (!AstUtils.isLoop(st.getContext())) {
                    System.err.printf("23 ");
                    Er.errBreak(t);
                }
                break;
            case DecafScannerTokenTypes.TK_continue:
                if (!AstUtils.isLoop(st.getContext())) {
                    System.err.printf("24 ");
                    Er.errContinue(t);
                }
                break;
            case DecafScannerTokenTypes.TK_return:
                String expectedReturnType = st.getReturnType();
                if (expectedReturnType == null) {
                    System.err.printf("25 ");
                    Er.report(t, "null return");
                    break;
                }
                String actualReturnType = parseExpr(t.getFirstChild(), st);
                if (actualReturnType == null) {
                    actualReturnType = Defs.DESC_TYPE_VOID;
                }
                if (!Defs.equals(expectedReturnType, actualReturnType)) {
                    System.err.printf("26 ");
                    Er.errType(t, expectedReturnType, actualReturnType);
                }
                break;
        }
    }
}
