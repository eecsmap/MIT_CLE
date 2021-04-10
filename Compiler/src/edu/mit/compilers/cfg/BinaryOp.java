package edu.mit.compilers.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.mit.compilers.st.Descriptor;
import edu.mit.compilers.asm.*;

// =, +, -, ++, --, unary -, ? :, unary !, &&, ||, ==, <, >
// IT'S NOT CHAINED!
// in the form of `d = l op r` for +, -, &&, ||, ==
// `d++`, `d--`
public class BinaryOp {
    private String op;
    private Descriptor d; // dest
    private Descriptor l;
    private Descriptor r;
    
    public BinaryOp(String op, Descriptor d, Descriptor l, Descriptor r) {
        this.op = op;
        this.d = d;
        this.l = l;
        this.r = r;
    }

    private static final Map<String, String> cmpOpcodeMap = new HashMap<>() {{
        put("==", "sete");
        put("<=", "setle");
        put(">=", "setge");
        put(">", "setg");
        put("<", "setl");
    }};

    public List<String> codegen() {
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
                asm.bin(opcode, new Num(1), Reg.eax),
                asm.bin("movl", Reg.eax, this.d.getAddr())
            );
        } else if (this.op.equals("!")) {
            Collections.addAll(codes,
                asm.bin("cmpl", new Num(0), this.l.getAddr()),
                asm.uni("sete", Reg.al),
                asm.bin("movzbl", Reg.al, Reg.eax),
                asm.bin("movl", Reg.eax, this.d.getAddr())
            );
        } else if (this.op.equals("&&") || this.op.equals("||")) {
            String jeWhenAnd = this.op.equals("&&") ? "je" : "jne";
            Num oneWhenAnd = this.op.equals("&&") ? new Num(1) : new Num(0);
            Num zeroWhenAnd = this.op.equals("&&") ? new Num(0) : new Num(1);
            Label l1 = Label.init();
            Label l2 = Label.init();
            Collections.addAll(codes,
                asm.bin("cmpl", new Num(0), this.l.getAddr()),
                asm.jmp(jeWhenAnd, l1),
                asm.bin("cmpl", new Num(0), this.r.getAddr()),
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
        } else if () {
        }
        return codes;
    }
}
