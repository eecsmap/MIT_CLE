package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.compilers.asm.AJmpLine;
import edu.mit.compilers.asm.ALabelLine;
import edu.mit.compilers.asm.ALine;
import edu.mit.compilers.asm.AMethod;
import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.defs.Defs;

public class CMethod {
    private List<CBlock> blocks = new ArrayList<>();

    public CMethod(AMethod aMethod) {
        int last = 0;
        Map<CBlock, String> jmpMap = new HashMap<>();
        Map<String, CBlock> labelMap = new HashMap<>();
        for (int i = 0; i < aMethod.size(); i++) {
            Boolean hasPred = i > 0;
            Boolean hasSucc = i < aMethod.size() - 1;
            ALine line = aMethod.get(i);
            // condition jump
            if (line instanceof AJmpLine) {
                String inst = ((AJmpLine)line).getInst();
                Label label = ((AJmpLine)line).getLabel();
                if (!label.equals(Defs.EXIT_ARRAY_OUTBOUND_LABEL)) {
                    CBlock block = new CBlock(aMethod.subLines(last, i + 1));
                    jmpMap.put(block, label.toString());
                    this.blocks.add(block);
                    last = i + 1;
                }
                if (inst.equals("jmp")) {
                    hasSucc = false;
                }
            }
            // must be used
            if (line instanceof ALabelLine) {
                Label label = ((ALabelLine)line).getLabel();
                CBlock block = new CBlock(aMethod.subLines(last, i));
                labelMap.put(label.toString(), block);
                this.blocks.add(block);
                last = i;
            }
            if (hasPred) this.blocks.get(i).addPred(this.blocks.get(i-1));
            if (hasSucc) this.blocks.get(i).addSucc(this.blocks.get(i+1));
        }
        this.blocks.get(this.blocks.size()-1).addPred(this.blocks.get(this.blocks.size()-2));
        // link to jmp
        for (Map.Entry<CBlock, String> entry : jmpMap.entrySet()) {
            CBlock from = entry.getKey();
            CBlock to = labelMap.get(entry.getValue());
            from.addSucc(from);
            to.addSucc(from);
        }
        // some block may be mergeable but it's OK, at least it 
        // keeps current block order and won't affect the results.
    }

    public AMethod makeAMethod() {
        AMethod method = new AMethod();
        for (int i = 0; i < this.blocks.size(); i++) {
            method.addAll(this.blocks.get(i).aLines);
        }
        return method;
    }
}
