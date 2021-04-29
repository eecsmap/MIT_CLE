package edu.mit.compilers.syntax;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileField;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.grammar.*;

class Field {
    static final AST declare(AST t, ST st, List<String> codes) {
        for (; t != null && AstUtils.isType(t); t = t.getNextSibling()) {
            String type = null;
            Integer size = 0;
            switch (t.getType()) {
                case DecafScannerTokenTypes.TK_bool:
                    type = Defs.DESC_TYPE_BOOL;
                    size = 8;
                    break;
                case DecafScannerTokenTypes.TK_int:
                    type = Defs.DESC_TYPE_INT;
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
                    if (st.isGlobal() && Program.importST.getMethod(c.getText()) != null) {
                        Er.errDuplicatedDeclaration(c, c.getText());
                        continue;
                    }
                    if (!st.push(new ArrayDesc(type, c.getText(), Long.valueOf(cap)), false)) {
                        Er.errDuplicatedDeclaration(c, c.getText());
                    }
                    CompileField.declareArray(st, c.getText(), size, cap, codes);
                    continue;
                }
                if (Program.importST.getMethod(c.getText()) != null) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                // cc is null -> it's single Variable
                if (!st.push(new VarDesc(type, c.getText()), false)) {
                    Er.errDuplicatedDeclaration(c, c.getText());
                }
                CompileField.declareVariable(st, c.getText(), size, codes);
            }
        }
        return t;
    }
}
