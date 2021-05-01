package edu.mit.compilers.syntax;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileField;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.grammar.*;

class Field {
    static final AST declare(AST t, List<String> codes) {
        for (; t != null && AstUtils.isType(t); t = t.getNextSibling()) {
            VarType type = null;
            Integer size = 0;
            switch (t.getType()) {
                case DecafScannerTokenTypes.TK_bool:
                    type = VarType.BOOL;
                    size = 8;
                    break;
                case DecafScannerTokenTypes.TK_int:
                    type = VarType.INT;
                    size = 8;
                    break;
            }
            for (AST c = t.getFirstChild(); c != null; c = c.getNextSibling()) {
                AST cc = c.getFirstChild();
                if (cc != null) {
                    // cc is not null -> is array
                    Integer cap = Integer.parseInt(cc.getText());
                    if (cap <= 0) {
                        Er.errBadArrayCap(cc);
                        cap = 1000000000;
                    }
                    if (Manager.isGlobal() && Program.importST.getMethod(c.getText()) != null) {
                        Er.errDuplicatedDeclaration(c, c.getText());
                        continue;
                    }
                    if (!Manager.push(new ArrayDesc(type, c.getText(), Long.valueOf(cap)), false)) {
                        Er.errDuplicatedDeclaration(c, c.getText());
                    }
                    CompileField.declareArray(c.getText(), size, cap, codes);
                    continue;
                }
                if (Program.importST.getMethod(c.getText()) != null) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                // cc is null -> it's single Variable
                if (!Manager.push(new VarDesc(type, c.getText()), false)) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                }
                CompileField.declareVariable(c.getText(), size, codes);
            }
        }
        return t;
    }
}
