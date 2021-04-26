package edu.mit.compilers.asm;

// Register
public class Reg extends Oprand {
    public static final Reg rax = new Reg("rax", "eax", "ax", "al");
    public static final Reg rcx = new Reg("rcx", "ecx", "cx", "cl");
    public static final Reg rdx = new Reg("rdx", "edx", "dx", "dl");
    public static final Reg rbx = new Reg("rbx", "ebx", "bx", "bl");
    public static final Reg rsp = new Reg("rsp", "esp", "sp", "spl");
    public static final Reg rbp = new Reg("rbp", "ebp", "bp", "bpl");
    public static final Reg rsi = new Reg("rsi", "esi", "si", "sil");
    public static final Reg rdi = new Reg("rdi", "edi", "di", "dil");

    public static final Reg r8 = new Reg("r8", "r8d", "r8w", "r8b");
    public static final Reg r9 = new Reg("r9", "r9d", "r9w", "r9b");
    public static final Reg r10 = new Reg("r10", "r10d", "r10w", "r10b");
    public static final Reg r11 = new Reg("r11", "r11d", "r11w", "r11b");
    public static final Reg r12 = new Reg("r12", "r12d", "r12w", "r12b");
    public static final Reg r13 = new Reg("r13", "r13d", "r13w", "r13b");
    public static final Reg r14 = new Reg("r14", "r14d", "r14w", "r14b");
    public static final Reg r15 = new Reg("r15", "r15d", "r15w", "r15b");

    private String regName;
    private String dWord = "";
    private String word = "";
    private String bite = "";
    // use as tmp
    private String tmpName = "";

    private Reg(String regName, String tmpName) {
        this.regName = regName;
        this.tmpName = tmpName;
    }

    public Reg(Reg parent, String tmpName) {
        this.regName = parent.getRegName();
        this.dWord = parent.dWord;
        this.word = parent.word;
        this.bite = parent.bite;
        this.tmpName = tmpName;
    }

    public Reg(String regName, String dWord, String word, String bite) {
        this.regName = regName;
        this.dWord = dWord;
        this.word = word;
        this.bite = bite;
    }

    public Reg dWord() {
        return new Reg(this.dWord, this.tmpName);
    }

    public Reg word() {
        return new Reg(this.word, this.tmpName);
    }

    public Reg bite() {
        return new Reg(this.bite, this.tmpName);
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
