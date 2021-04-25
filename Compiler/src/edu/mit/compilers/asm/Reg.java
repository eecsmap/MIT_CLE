package edu.mit.compilers.asm;

// Register
public class Reg extends Oprand {
    public static Reg rax = new Reg("rax");
    public static Reg rcx = new Reg("rcx");
    public static Reg rdx = new Reg("rdx");
    public static Reg rbx = new Reg("rbx");
    public static Reg rsp = new Reg("rsp");
    public static Reg rbp = new Reg("rbp");
    public static Reg rsi = new Reg("rsi");
    public static Reg rdi = new Reg("rdi");
    public static Reg eax = new Reg("eax");
    public static Reg ecx = new Reg("ecx");
    public static Reg edx = new Reg("edx");
    public static Reg ebx = new Reg("ebx");
    public static Reg esp = new Reg("esp");
    public static Reg ebp = new Reg("ebp");
    public static Reg esi = new Reg("esi");
    public static Reg edi = new Reg("edi");
    public static Reg al = new Reg("al");

    public static Reg r8 = new Reg("r8");
    public static Reg r9 = new Reg("r9");
    public static Reg r10 = new Reg("r10");
    public static Reg r11 = new Reg("r11");
    public static Reg r12 = new Reg("r12");
    public static Reg r13 = new Reg("r13");
    public static Reg r14 = new Reg("r14");
    public static Reg r15 = new Reg("r15");

    public static Reg r8d = new Reg("r8d");
    public static Reg r9d = new Reg("r9d");
    public static Reg r10d = new Reg("r10d");
    public static Reg r11d = new Reg("r11d");
    public static Reg r12d = new Reg("r12d");
    public static Reg r13d = new Reg("r13d");
    public static Reg r14d = new Reg("r14d");
    public static Reg r15d = new Reg("r15d");

    private String regName;
    // use as tmp
    private String tmpName = "";

    public Reg(String regName) {
        this.regName = regName;
    }

    public Reg(Reg parent, String tmpName) {
        this.regName = parent.getRegName();
        this.tmpName = tmpName;
    }

    public String getRegName() {
        return this.regName;
    }

    @Override
    public String toString() {
        return String.format("%%%s", this.regName);
    }

    @Override
    public String getName() {
        return tmpName;
    }
}
