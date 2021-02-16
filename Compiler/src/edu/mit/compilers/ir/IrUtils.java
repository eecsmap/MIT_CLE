package edu.mit.compilers.ir;

import antlr.collections.AST;
import edu.mit.compilers.ir.AstUtils;
import edu.mit.compilers.ir.IRImport;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.grammar.*;

public class IrUtils {
    // parse an AST to IRTree with the help of Symbol Tree
    public static void IRparse(AST t) {

    }

    // TODO
    private static AST parseImport(AST t) {
        while(t.getType() == DecafParserTokenTypes.TK_import) {
            break;
        }
        return t;
    }
}
