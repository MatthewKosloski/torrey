package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple;

public class X86InstFactory
{
  public static X86Inst makeJccInstFromIROpType(Quadruple.OpType irOpType, LabelAddress label)
  {
    final X86Inst.OpType x86OpType = X86Inst.OpType
      .getConditionCodeFromIROpType(irOpType);

    if (x86OpType == X86Inst.OpType.JL)
    {
      return new Jl(label);
    }
    else if (x86OpType == X86Inst.OpType.JLE)
    {
      return new Jle(label);
    }
    else if (x86OpType == X86Inst.OpType.JG)
    {
      return new Jg(label);
    }
    else if (x86OpType == X86Inst.OpType.JGE)
    {
      return new Jge(label);
    }
    else if (x86OpType == X86Inst.OpType.JE)
    {
      return new Je(label);
    }
    else if (x86OpType == X86Inst.OpType.JNE)
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
