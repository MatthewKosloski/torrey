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

/**
 * Generates 64-bit x86 assembly code
 * from three-address code represented by
 * a collection of quadruples of the form
 * (op, arg1, arg2, result).
 */
public final class X86Generator 
{
    
    private List<Quadruple> quads;
    private List<X86Inst> asm;

    public X86Generator(List<Quadruple> quads)
    {
        this.quads = quads;
        this.asm = new ArrayList<>();
    }

    public void gen()
    {
        for (Quadruple quad : quads)
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
    }

    private void gen(CopyInst inst)
    {
        // asm.add(new X86Inst("movq", inst.arg1(), dest))
    }

    private void gen(UnaryInst inst)
    {
        asm.add(new X86Inst("negq", inst.arg1().value(), null));
    }

    private void gen(BinaryInst inst)
    {
        final X86Inst mov = new X86Inst(
            "movq", 
            inst.arg1().value(), 
            inst.result().value());
            
        asm.add(mov);

        if (inst.op().opText() == "+")
        {
            asm.add(new X86Inst(
                "addq", 
                inst.arg2().value(), 
                inst.result().value()));
        }
        else if (inst.op().opText() == "-")
        {
            asm.add(new X86Inst(
                "subq", 
                inst.arg2().value(), 
                inst.result().value()));
        }
        else if (inst.op().opText() == "*")
        {
            asm.add(new X86Inst(op, src, dest))
        }
    }

    private void gen(ParamInst inst)
    {
    
    }

    private void gen(CallInst inst)
    {
    
    }

}
