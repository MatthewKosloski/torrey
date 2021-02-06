package me.mtk.torrey.X86;

import java.util.List;
import me.mtk.torrey.IR.Quadruple;
import me.mtk.torrey.IR.CopyInst;
import me.mtk.torrey.IR.UnaryInst;
import me.mtk.torrey.IR.BinaryInst;
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
        // asm.add(new X86Inst("movq", inst., dest))
    }

    private void gen(UnaryInst inst)
    {
    
    }

    private void gen(BinaryInst inst)
    {
    
    }

    private void gen(ParamInst inst)
    {
    
    }

    private void gen(CallInst inst)
    {
    
    }

}
