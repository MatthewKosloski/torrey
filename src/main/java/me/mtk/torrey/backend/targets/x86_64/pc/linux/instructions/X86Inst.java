package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.*;

public abstract class X86Inst
{
    private X86OpType opType;
    private X86Address arg1;
    private X86Address arg2;

    public X86Inst(X86OpType opType, X86Address arg1, X86Address arg2)
    {
        if (arg2 != null && arg2.mode() == AddressingMode.IMMEDIATE)
            throw new Error("X86Inst(): Destination address"
                + " cannot be an immediate");

        this.opType = opType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String op()
    {
        return opType.toString();
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
            return String.format("%s %s, %s", opType, arg1, arg2);
        else if (arg1 != null)
            return String.format("%s %s", opType, arg1);
        else
            return String.format("%s", opType); 
    }

}
