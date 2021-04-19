package me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing;

public final class Register extends X86Address
{

    public static final Register RAX = new Register(Registers.RAX);
    public static final Register RBX = new Register(Registers.RBX);
    public static final Register RCX = new Register(Registers.RCX);
    public static final Register RDX = new Register(Registers.RDX);
    public static final Register RSI = new Register(Registers.RSI);
    public static final Register RDI = new Register(Registers.RDI);
    public static final Register RBP = new Register(Registers.RBP);
    public static final Register RSP = new Register(Registers.RSP);
    public static final Register R8  = new Register(Registers.R8);
    public static final Register R9  = new Register(Registers.R9);
    public static final Register R10 = new Register(Registers.R10);
    public static final Register R11 = new Register(Registers.R11);
    public static final Register R12 = new Register(Registers.R12);
    public static final Register R13 = new Register(Registers.R13);
    public static final Register R14 = new Register(Registers.R14);
    public static final Register R15 = new Register(Registers.R15);

    public Register(Registers register)
    {
        super(AddressingMode.REGISTER, register);
    }    
}
