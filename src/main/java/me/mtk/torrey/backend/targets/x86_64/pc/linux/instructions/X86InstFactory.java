package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;
import me.mtk.torrey.frontend.ir.instructions.IROpType;

public class X86InstFactory 
{
    public static X86Inst makeJccInstFromIROpType(IROpType irOpType, LabelAddress label)
    {
        final X86OpType x86OpType = X86OpType
            .getConditionCodeFromIROpType(irOpType);

        if (x86OpType == X86OpType.JL)
        {
            return new Jl(label);
        }
        else if (x86OpType == X86OpType.JLE)
        {
            return new Jle(label);
        }
        else if (x86OpType == X86OpType.JG)
        {
            return new Jg(label);
        }
        else if (x86OpType == X86OpType.JGE)
        {
            return new Jge(label);
        }
        else if (x86OpType == X86OpType.JE)
        {
            return new Je(label);
        }
        else if (x86OpType == X86OpType.JNE)
        {
            return new Jne(label);
        }
        else
        {
            throw new Error(String.format(
                "Unexpected x86 op type", x86OpType));
        }
    }
}
