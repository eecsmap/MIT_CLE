package edu.mit.compilers.asm;

// Instructions
public class asm {
    public static String bin(String instruction, Oprand src, Oprand dst) {
        return String.format("\t%s %s, %s", instruction, src, dst);
    }

    public static String uni(String instruction, Oprand dst) {
        return String.format("\t%s %s", instruction, dst);
    }

    public static String label(Label lable) {
        return lable.toString();
    }
    
    public static String jmp(String instruction, Label dst) {
        return String.format("\t%s %s", instruction, dst);
    }

    public static String run(String instruction) {
        return instruction;
    }
}