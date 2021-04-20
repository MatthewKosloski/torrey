package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.frontend.ir.instructions.BinaryOpType;

public enum ConditionCode
{
    JL,
    JLE,
    JG,
    JGE,
    JE,
    JNE;
    
    /**
     * Translates an IR binary operator to an x86 condition code.
     * 
     * @param irOp An IR binary operator type.
     * @return The corresponding condition code in x86.
     */
    public static ConditionCode transIrOp(BinaryOpType irOp)
    {
        switch (irOp)
        {
            case LT: return ConditionCode.JL;
            case LTE: return ConditionCode.JLE;
            case GT: return ConditionCode.JG;
            case GTE: return ConditionCode.JGE;
            case EQUAL: return ConditionCode.JE;
            case NEQUAL: return ConditionCode.JNE;

            default: 
                throw new Error("ConditionCode.transIrOp(BinaryOpType):"
                    + " Cannot translate");
        }
    }
    
}
