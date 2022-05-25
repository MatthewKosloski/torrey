package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.passes;

import java.util.*;
import me.mtk.torrey.frontend.ir.addressing.*;
import me.mtk.torrey.frontend.ir.gen.IRProgram;
import me.mtk.torrey.frontend.ir.instructions.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Global.RuntimeProcedure;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.X86Program;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.*;

public final class GeneratePseudoX86Program implements Pass<X86Program>
{
  // The temp names pointing to parameters of subsequent call instructions.
  private Queue<String> params;

  // The IR program from which x86 code will be generated.
  private IRProgram ir;

  // An x86 program that is equivalent to the input IR program.
  private X86Program x86;

  public GeneratePseudoX86Program(IRProgram input)
  {
    this.ir = input;

    // By default, allocate 8 bytes per temp variable.
    int stackSize = ir.temps().size() * 8;

    // Ensure the stack pointer is 16-bytes aligned
    // by setting stackSize to the nearest
    // multiple of 16.
    if (stackSize % 16 != 0)
      // stackSize isn't a multiple of 16,
      // so set it to the nearest multiple of 16.
      stackSize = closestMultiple(stackSize, 16);

    this.x86 = new X86Program(stackSize);
    this.params = new LinkedList<>();
  }

  public X86Program pass()
  {
    for (Quadruple quad : ir.quads())
    {
      if (quad instanceof IRCopyInst)
        gen((IRCopyInst) quad);
      else if (quad instanceof IRUnaryInst)
        gen((IRUnaryInst) quad);
      else if (quad instanceof IRBinaryInst)
        gen((IRBinaryInst) quad);
      else if (quad instanceof IRParamInst)
        gen((IRParamInst) quad);
      else if (quad instanceof IRCallInst)
        gen((IRCallInst) quad);
      else if (quad instanceof IRIfInst)
        gen((IRIfInst) quad);
      else if (quad instanceof IRLabelInst)
        gen((IRLabelInst) quad);
      else if (quad instanceof IRGotoInst)
        gen((IRGotoInst) quad);
      else
        throw new Error("Cannot generate x86 instruction");
    }

      return x86;
  }

  private void gen(IRIfInst inst)
  {
    final LabelAddress labelAddr = new LabelAddress(inst.result().toString());

    if (inst.opType() != null)
    {
      // The test condition is a comparison expression.

      // Convert the IR operands to x86 addresses.
      final X86Address arg1 = transAddress(inst.arg1());
      final X86Address arg2 = transAddress(inst.arg2());

      // move the operands to temporary registers to
      // perform the comparison.
      x86.addInst(new Movq(arg1, Register.R10));
      x86.addInst(new Movq(arg2, Register.R11));

      // Perform the comparison.
      x86.addInst(new Cmp(Register.R11, Register.R10));
      x86.addInst(X86InstFactory.makeJccInstFromIROpType(inst.opType(),
        labelAddr));
    }
    else
    {
      // The test condition is a primitive boolean expression.
      final int bool = (Integer) inst.arg1().value();
      x86.addInst(new Movq(new Immediate(0), Register.R10));
      x86.addInst(new Movq(new Immediate(1), Register.R11));
      x86.addInst(new Cmp(Register.R11, Register.R10));
      x86.addInst(bool == 1 ? new Jne(labelAddr) : new Je(labelAddr));
    }
  }

  private void gen(IRLabelInst inst)
  {
    x86.addInst(new Label(new LabelAddress(inst.arg1().toString())));
  }

  private void gen(IRGotoInst inst)
  {
    x86.addInst(new Jmp(new LabelAddress(inst.result().toString())));
  }

  private void gen(IRCopyInst inst)
  {
    final IRAddress irSrcAddr = inst.arg1();
    final IRAddress irDestAddr = inst.result();

    final X86Address x86SrcAddr = transAddress(irSrcAddr);
    final X86Address x86DestAddr = transAddress(irDestAddr);

    final int signedIntMinValue = -1 << 31;
    final int signedIntMaxValue = (1 << 31) - 1;

    if (inst.arg1().value() instanceof Long
      && ((long) inst.arg1().value() < signedIntMinValue
      || (long) inst.arg1().value() > signedIntMaxValue))
    {
      x86.addInst(new Movq(x86SrcAddr, Register.R10));
      x86.addInst(new Movq(Register.R10, x86DestAddr));
    }
    else
    {
      x86.addInst(new Movq(x86SrcAddr, x86DestAddr));
    }
  }

  private void gen(IRUnaryInst inst)
  {
    final IRAddress irSrcAddr = inst.arg1();
    final IRAddress irDestAddr = inst.result();

    final X86Address x86SrcAddr = transAddress(irSrcAddr);
    final X86Address x86DestAddr = transAddress(irDestAddr);

    x86.addInst(new Movq(x86SrcAddr, x86DestAddr));
    x86.addInst(new Negq(x86DestAddr));
  }

  private void gen(IRBinaryInst inst)
  {
    final Quadruple.OpType opType = inst.opType();

    final IRAddress irArg1Addr = inst.arg1();
    final IRAddress irArg2Addr = inst.arg2();
    final IRAddress irDestAddr = inst.result();

    final X86Address x86Arg1Addr = Register.R10;
    final X86Address x86Arg2Addr = Register.R11;
    final X86Address x86DestAddr = transAddress(irDestAddr);

    // Move operands to temporary registers %r10 and %r11.
    // We do this in case one or both operands exceed the
    // range of signed 32-bit int (because 64-bit immediates
    // cannot be moved to the stack).
    x86.addInst(new Movq(transAddress(irArg1Addr), x86Arg1Addr));
    x86.addInst(new Movq(transAddress(irArg2Addr), x86Arg2Addr));

    switch (opType)
    {
      case ADD:
      case SUB:
        // Store first argument in temp
        x86.addInst(new Movq(x86Arg1Addr, x86DestAddr));

        // Add or subtract the second argument
        // by the first, storing the result in x86DestAddr.
        x86.addInst(opType == Quadruple.OpType.ADD
          ? new Addq(x86Arg2Addr, x86DestAddr)
          : new Subq(x86Arg2Addr, x86DestAddr));
      break;
      case MULT:
        // move first argument to %rax register
        x86.addInst(new Movq(x86Arg1Addr, Register.RAX));

        // move second argument to %rbx register
        x86.addInst(new Movq(x86Arg2Addr, Register.RBX));

        // multiply the contents of %rax by %rbx, placing the low
        // 64 bits of the product in %rax.
        x86.addInst(new Imulq(Register.RBX));

        // move the product, which is in %rax, to a temp location.
        x86.addInst(new Movq(Register.RAX, x86DestAddr));
      break;
      case DIV:
        // move dividend to %rax register
        x86.addInst(new Movq(x86Arg1Addr, Register.RAX));

        // move divisor to temp destination
        x86.addInst(new Movq(x86Arg2Addr, x86DestAddr));

        // sign-extend %rax into %rdx. The former contains
        // the low 64 bits of dividend, the latter contains
        // the high 64 bits.
        x86.addInst(new Cqo());

        // divide %rdx:%rax by divisor, leaving result in %rax.
        x86.addInst(new Idivq(x86DestAddr));

        // Move contents of %rax to destination.
        x86.addInst(new Movq(Register.RAX, x86DestAddr));
      break;
      default:
        throw new Error("X86Generator.gen(BinaryInst): Unhandled binary case.");
    }
  }

  private void gen(IRParamInst inst)
  {
    params.add((String) inst.arg1().value());
  }

  private void gen(IRCallInst inst)
  {
    final String procName = (String) inst.arg1().value();
    final long numParams = (long) inst.arg2().value();

    if (procName.equals("print") || procName.equals("println"))
    {
      for (int i = 0; i < numParams; i++)
      {
        String param = params.remove();
        x86.addInst(new Movq(new Temporary(param), Register.RDI));
        x86.addInst(new Callq(new Global(RuntimeProcedure.PRINT_INT)));
        if (procName.equals("println"))
          x86.addInst(new Callq(new Global(RuntimeProcedure.PRINT_NL)));
      }
    }
  }

  /*
  * Converts the given IR address to an equivalent x86 address.
  *
  * @param addr An IR address.
  * @return An equivalent x86 address.
  */
  private X86Address transAddress(IRAddress addr)
  {
    if (addr instanceof IRConstAddress)
      return new Immediate(String.format("$%s", addr));
    else if (addr instanceof IRTempAddress)
      return new Temporary(addr.toString());
    else
      throw new Error("transAddress(Address): Cannot translate.");
  }

  private int closestMultiple(int n, int x)
  {
    if (x > n)
      return x;

    n = n + x / 2;
    n = n - (n % x);
    return n;
  }
}
