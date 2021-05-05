package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Map<Integer, String> jMap = new HashMap<>();
        Map<String, Integer> lMap = new HashMap<>();
        Set<Integer> blocksEndWithJmp = new HashSet<>();
        for (int i = 0, blockIndex = 0; i < aMethod.size(); i++, blockIndex = this.blocks.size()) {
            ALine line = aMethod.get(i);
            // condition jump
            if (line instanceof AJmpLine) {
                Label label = ((AJmpLine)line).getLabel();
                if (label.equals(Defs.EXIT_ARRAY_OUTBOUND_LABEL)) {
                    continue;
                }
                CBlock block = new CBlock(aMethod.subLines(last, i + 1));
                jMap.put(blockIndex, label.toString());
                if (((AJmpLine)line).getInst().equals("jmp")) {
                    blocksEndWithJmp.add(blockIndex);
                }
                this.blocks.add(block);
                last = i + 1;
            }
            // must be used
            if (line instanceof ALabelLine) {
                Label label = ((ALabelLine)line).getLabel();
                CBlock block = new CBlock(aMethod.subLines(last, i));
                lMap.put(label.toString(), blockIndex + 1);
                this.blocks.add(block);
                last = i;
            }
        }
        this.blocks.add(new CBlock(aMethod.subLines(last, aMethod.size())));
        // link to next
        this.blocks.get(0).addSucc(this.blocks.get(1));
        for (int i = 1; i < this.blocks.size() - 1; i++) {
            this.blocks.get(i).addPred(this.blocks.get(i-1));
            if (!blocksEndWithJmp.contains(i)) {
                this.blocks.get(i).addSucc(this.blocks.get(i+1));
            }
        }
        this.blocks.get(this.blocks.size()-1).addPred(this.blocks.get(this.blocks.size()-2));
        // link to jmp
        for (Map.Entry<Integer, String> entry : jMap.entrySet()) {
            CBlock from = this.blocks.get(entry.getKey());
            CBlock to = this.blocks.get(lMap.get(entry.getValue()));
            from.addSucc(to);
            to.addPred(from);
        }
        // some block may be mergeable but it's OK, at least it 
        // keeps current block order and won't affect the results.
        return;
    }

    public AMethod makeAMethod() {
        AMethod method = new AMethod();
        for (int i = 0; i < this.blocks.size(); i++) {
            method.addAll(this.blocks.get(i).aLines);
        }
        return method;
    }
}
