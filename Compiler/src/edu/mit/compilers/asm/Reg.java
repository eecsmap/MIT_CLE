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

    private String name;

    public Reg(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%%s", this.name);
    }
}
