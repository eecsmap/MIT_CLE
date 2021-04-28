package edu.mit.compilers.compile;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ast.AstUtils;
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
                    if (Program.shouldCompile()) {
                        if (st.isGlobal()) {
                            codes.addAll(
                                asm.globalDecl(c.getText(), size * cap)
                            );
                        } else {
                            Integer topOffset = st.getOffset();
                            Integer baseOffset = st.getDesc(c.getText()).getAddr().getOffset();
                            for (int i = baseOffset; i < topOffset; i += 8) {
                                codes.add(
                                    asm.bin("movq", new Num(0L), new Addr(i, "array init"))
                                );
                            }
                        }
                    }
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
                if (Program.shouldCompile()) {
                    if (st.isGlobal()) {
                        codes.addAll(
                            asm.globalDecl(c.getText(), size)
                        );
                    } else {
                        codes.add(
                            asm.bin("movq", new Num(0L), st.getDesc(c.getText()).getAddr())
                        );
                    }
                }
            }
        }
        return t;
    }
}
