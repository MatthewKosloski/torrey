package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.*;

public final class IRIfInst extends IRQuadruple
{
    public IRIfInst(IRBinaryOpType op, IRAddress arg1, IRAddress arg2,
        IRLabelAddress result)
    {
        super(new IRBinaryOperator(op), arg1, arg2, result);
    }

    public IRIfInst(IRConstAddress bool, IRLabelAddress result)
    {
        super(null, bool, null, result);
    }

    /**
     * The string representation of this instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        if (arg1 != null && arg2 != null && result != null && op != null)
            return String.format("if %s %s %s goto %s", arg1, 
                op.opText(), arg2, result);
        return String.format("if %s goto %s", arg1, result);
    }
}
