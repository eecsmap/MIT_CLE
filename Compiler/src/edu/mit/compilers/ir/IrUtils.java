package edu.mit.compilers.ir;

import antlr.collections.AST;
import edu.mit.compilers.st.MethodDesc;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.grammar.*;

public class IrUtils {
    // parse an AST to IRTree with the help of Symbol Tree
    public static void IRparse(AST t) {
        // treat import Symbol Table as special one
        ST importST = new ST();
        ST globalST = new ST();
        t = parseImport(t, importST);

    }

    // return the next AST to parse
    private static AST parseImport(AST t, ST importST) {
        for (; t.getType() == DecafParserTokenTypes.TK_import; t = t.getNextSibling()) {
            // parse single import statement
            MethodDesc desc = new MethodDesc(Defs.DESC_METHOD, t.getText());
            importST.push(desc);
        }
        return t;
    }

    private static AST parseField(AST t, ST globalST) {
        return t;
    }
}
