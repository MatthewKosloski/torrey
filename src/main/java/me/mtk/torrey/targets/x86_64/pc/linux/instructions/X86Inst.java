package me.mtk.torrey.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.targets.x86_64.pc.linux.addressing.X86Address;
import me.mtk.torrey.targets.x86_64.pc.linux.addressing.AddressingMode;

public class X86Inst 
{
    // TODO: Store finite set of valid opcodes in a hash map
    // or something.
    private String op;
    private X86Address arg1;
    private X86Address arg2;

    public X86Inst(String op, X86Address arg1, X86Address arg2)
    {
        if (arg2 != null && arg2.mode() == AddressingMode.IMMEDIATE)
            throw new Error("X86Inst(): Destination address"
                + " cannot be an immediate");

        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String op()
    {
        return op;
    }

    public X86Address arg1()
    {
        return arg1;
    }

    public void setArg1(X86Address newArg1)
    {
        this.arg1 = newArg1;
    }

    public X86Address arg2()
    {
        return arg2;
    }

    public void setArg2(X86Address newArg2)
    {
        this.arg2 = newArg2;
    }

    public String toString()
    {
        if (arg2 != null)
            return String.format("%s %s, %s", op, arg1, arg2);
        else if (arg1 != null)
            return String.format("%s %s", op, arg1);
        else
            return String.format("%s", op); 
    }

}
