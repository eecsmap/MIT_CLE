package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.asm.*;
import edu.mit.compilers.ir.IrTriple;

// =, +, -, ++, --, unary -, ? :, unary !, &&, ||, ==, <, >
// IT'S NOT CHAINED!
// in the form of `d = l op r` for +, -, &&, ||, ==
// `d++`, `d--`
public class BinaryOp extends Operator {
    private String op;
    private Descriptor d; // dest
    private Descriptor l;
    private Descriptor r;
    private List<String> codeList;
    
    public BinaryOp(IrTriple triple) {
        this.op = triple.op();
        this.d = triple.d();
        this.l = triple.l();
        this.r = triple.r();
        this.codeList = makeCodeList();
    }

    private static final Map<String, String> cmpOpcodeMap = new HashMap<>() {{
        put("==", "sete");
        put("<=", "setle");
        put(">=", "setge");
        put(">", "setg");
        put("<", "setl");
    }};

    private List<String> makeCodeList() {
        List<String> codes = new ArrayList<>();
        if (this.op.equals("+") || (this.op.equals("-") && this.r != null)) {
            String opcode = this.op.equals("+") ? "addl" : "subl";
            Collections.addAll(codes, 
                asm.bin("movl", this.l.getAddr(), Reg.eax),
                asm.bin(opcode, this.r.getAddr(), Reg.eax),
                asm.bin("movl", Reg.eax, this.d.getAddr())
            );
        } else if (this.op.equals("-")) {
            // unary
            Collections.addAll(codes,
                asm.bin("movl", this.l.getAddr(), Reg.eax),
                asm.uni("negl", Reg.eax),
                asm.bin("movl", Reg.eax, this.d.getAddr())
            );
        } else if (this.op.equals("++") || this.op.equals("--")) {
            String opcode = this.op.equals("+") ? "addl" : "subl";
            Collections.addAll(codes,
                asm.bin("movl", this.l.getAddr(), Reg.eax),
                asm.bin(opcode, new Num(1L), Reg.eax),
                asm.bin("movl", Reg.eax, this.d.getAddr())
            );
        } else if (this.op.equals("!")) {
            Collections.addAll(codes,
                asm.bin("cmpl", new Num(0L), this.l.getAddr()),
                asm.uni("sete", Reg.al),
                asm.bin("movzbl", Reg.al, Reg.eax),
                asm.bin("movl", Reg.eax, this.d.getAddr())
            );
        } else if (this.op.equals("&&") || this.op.equals("||")) {
            String jeWhenAnd = this.op.equals("&&") ? "je" : "jne";
            Num oneWhenAnd = this.op.equals("&&") ? new Num(1L) : new Num(0L);
            Num zeroWhenAnd = this.op.equals("&&") ? new Num(0L) : new Num(1L);
            Label l1 = new Label();
            Label l2 = new Label();
            Collections.addAll(codes,
                asm.bin("cmpl", new Num(0L), this.l.getAddr()),
                asm.jmp(jeWhenAnd, l1),
                asm.bin("cmpl", new Num(0L), this.r.getAddr()),
                asm.jmp(jeWhenAnd, l1),
                asm.bin("movl", oneWhenAnd, Reg.eax),
                asm.jmp("jmp", l2),
                asm.label(l1),
                asm.bin("movl", zeroWhenAnd, Reg.eax),
                asm.label(l2),
                asm.bin("movl", Reg.eax, this.d.getAddr())
            );
        } else if (cmpOpcodeMap.containsKey(this.op)) {
            String opcode = cmpOpcodeMap.get(this.op);
            Collections.addAll(codes,
                asm.bin("movl", this.l.getAddr(), Reg.eax),
                asm.bin("cmpl", this.r.getAddr(), Reg.eax),
                asm.uni(opcode, Reg.al),
                asm.bin("movzbl", Reg.al, Reg.eax),
                asm.bin("movl", Reg.eax, this.d.getAddr())
            );
        }
        return codes;
    }

    @Override
    public List<String> getCodeList() {
        return this.codeList;
    }
}
