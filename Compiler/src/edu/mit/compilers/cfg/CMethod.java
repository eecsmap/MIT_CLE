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
import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.asm.AInstLine;
import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.Manager;

public class CMethod {
    private int varOffset;
    private List<CBlock> blocks = new ArrayList<>();

    public CMethod(ABlock aMethod) {
        this.varOffset = aMethod.getVarOffset();
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
        this.blocks.get(0).addSucc(1);
        for (int i = 1; i < this.blocks.size() - 1; i++) {
            this.blocks.get(i).addPred(i-1);
            if (!blocksEndWithJmp.contains(i)) {
                this.blocks.get(i).addSucc(i+1);
            }
        }
        this.blocks.get(this.blocks.size()-1).addPred(this.blocks.size()-2);
        // link to jmp
        for (Map.Entry<Integer, String> entry : jMap.entrySet()) {
            Integer from = entry.getKey();
            Integer to = lMap.get(entry.getValue());
            this.blocks.get(from).addSucc(to);
            this.blocks.get(to).addPred(from);
        }
        // some block may be mergeable but it's OK, at least it 
        // keeps current block order and won't affect the results.
        aMethod.clear();
        return;
    }

    public int getOffset() {
        return this.varOffset;
    }

    public void changeOffset() {
        // line is `subq $X, %rsp`
        ALine line = this.blocks.get(1).aLines.get(3);
        if (line instanceof AInstLine) {
            AInstLine instLine = (AInstLine)line;
            if (instLine.getOpCount() == 2 && instLine.getRight().toString().equals("%rsp")) {
                instLine.setLeft(new Num(Long.valueOf(Manager.bytesToAllocate())));
            } else {
                this.blocks.get(1).aLines.add(3,
                    new AInstLine("subq", new Num(Long.valueOf(Manager.bytesToAllocate())), Reg.rsp)
                );
            }
        }
    }

    public List<CBlock> getBlocks() {
        return this.blocks;
    }

    public ABlock makeAMethod() {
        ABlock method = new ABlock();
        for (int i = 0; i < this.blocks.size(); i++) {
            method.addAll(this.blocks.get(i).aLines);
        }
        return method;
    }
}
