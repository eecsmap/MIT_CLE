package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.asm.AJmpLine;
import edu.mit.compilers.asm.ALabelLine;
import edu.mit.compilers.asm.ALine;
import edu.mit.compilers.asm.AMethod;

public class CMethod {
    private CBlock entry;
    private List<CBlock> blocks;
    private CBlock exit;

    public CMethod(AMethod aMethod) {
        List<CBlock> blocks = new ArrayList<>();
        int last = 0;
        for (int i = 0; i < aMethod.size(); i++) {
            ALine line = aMethod.get(i);
            if (line instanceof AJmpLine) {
                blocks.add(new CBlock(aMethod.subLines(last, i + 1)));
                last = i + 1;
            }
            if (line instanceof ALabelLine) {
                blocks.add(new CBlock(aMethod.subLines(last, i)));
                last = i;
            }
        }
        this.entry = blocks.get(0);
        this.blocks = blocks.subList(1, blocks.size());
        this.exit = new CBlock(aMethod.subLines(last, aMethod.size()));
    }

    public AMethod makeAMethod() {
        AMethod method = new AMethod();
        method.addAll(this.entry.getALines());
        for (int i = 0; i < this.blocks.size(); i++) {
            method.addAll(this.blocks.get(i).getALines());
        }
        method.addAll(this.exit.getALines());
        return method;
    }
}
