package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.Address;
import me.mtk.torrey.frontend.ir.addressing.ConstAddress;
import me.mtk.torrey.frontend.ir.addressing.LabelAddress;

public final class IfInst extends Quadruple
{
    public IfInst(BinaryOpType op, Address arg1, Address arg2, LabelAddress result)
    {
        super(new BinaryOperator(op), arg1, arg2, result);
    }

    public IfInst(ConstAddress bool, LabelAddress result)
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
