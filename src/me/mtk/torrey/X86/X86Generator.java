package me.mtk.torrey.X86;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.IR.Quadruple;
import me.mtk.torrey.IR.CopyInst;
import me.mtk.torrey.IR.UnaryInst;
import me.mtk.torrey.IR.BinaryInst;
import me.mtk.torrey.IR.BinaryOpType;
import me.mtk.torrey.IR.ParamInst;
import me.mtk.torrey.IR.CallInst;
import me.mtk.torrey.IR.Address;
import me.mtk.torrey.IR.TempAddress;
import me.mtk.torrey.IR.ConstAddress;
import me.mtk.torrey.IR.IRProgram;

/**
 * Generates 64-bit x86 assembly code
 * from three-address code represented by
 * a collection of quadruples of the form
 * (op, arg1, arg2, result).
 */
public final class X86Generator 
{
    
    // The IR program from which x86 code will be generated.
    private IRProgram ir;

    // An x86 program that is equivalent to the input IR program.
    private X86Program x86;

    public X86Generator(IRProgram ir)
    {
        this.ir = ir;
        this.x86 = new X86Program();
    }

    public X86Program gen()
    {
        for (Quadruple quad : ir.quads())
        {
            if (quad instanceof CopyInst)
                gen((CopyInst) quad);
            else if (quad instanceof UnaryInst)
                gen((UnaryInst) quad);
            else if (quad instanceof BinaryInst)
                gen((BinaryInst) quad);
            else if (quad instanceof ParamInst)
                gen((ParamInst) quad);
            else if (quad instanceof CallInst)
                gen((CallInst) quad);
            else
                throw new Error("Cannot generate x86 instruction");
        }

        return x86;
    }

    private void gen(CopyInst inst)
    {
        final Address srcAddr = inst.arg1();
        final TempAddress destAddr = inst.result();

        String src = null;
        final String dest = destAddr.toString();

        if (srcAddr instanceof ConstAddress)
            src = transConstAddress((ConstAddress) srcAddr);
        else if (srcAddr instanceof TempAddress)
            src = srcAddr.toString();
        else
            throw new Error("X86Generator.gen(CopyInst):"
                + " Unhandled Address.");

        x86.addInst(new X86Inst("movq", src, dest));
    }

    private void gen(UnaryInst inst)
    { 
        final Address srcAddr = inst.arg1();
        final TempAddress destAddr = inst.result();

        String src = null;
        final String dest = destAddr.toString();

        if (srcAddr instanceof ConstAddress)
            src = transConstAddress((ConstAddress) srcAddr);
        else if (srcAddr instanceof TempAddress)
            src = srcAddr.toString();
        else
            throw new Error("X86Generator.gen(UnaryInst):"
                + " Unhandled Address.");

        x86.addInst(new X86Inst("movq", src, dest));
        x86.addInst(new X86Inst("negq", dest, null));

        // TODO: The x86 instruction corresponding to the IR instruction
        // `x = - y` is `negq y`, and thus the new destination is y. We
        // need to replace all subsequent occurrences of x with y.
    }

    private void gen(BinaryInst inst)
    {
        // final X86Inst mov = new X86Inst(
        //     "movq", 
        //     inst.arg1().value(), 
        //     inst.result().value());
            
        // asm.add(mov);

        // if (inst.op().opText() == "+")
        // {
        //     asm.add(new X86Inst(
        //         "addq", 
        //         inst.arg2().value(), 
        //         inst.result().value()));
        // }
        // else if (inst.op().opText() == "-")
        // {
        //     asm.add(new X86Inst(
        //         "subq", 
        //         inst.arg2().value(), 
        //         inst.result().value()));
        // }
        // else if (inst.op().opText() == "*")
        // {
        //     asm.add(new X86Inst(op, src, dest))
        // }
    }

    private void gen(ParamInst inst)
    {
    
    }

    private void gen(CallInst inst)
    {
    
    }

    private String transConstAddress(ConstAddress addr)
    {
        return String.format("$%s", addr);
    }

}
