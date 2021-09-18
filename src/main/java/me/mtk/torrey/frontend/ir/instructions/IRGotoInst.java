package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRLabelAddress;

public final class IRGotoInst extends IRQuadruple
{
    /**
     * Instantiates a new goto instruction.
     * 
     * @param label The name of the label.
     */
    public IRGotoInst(IRLabelAddress label)
    {
        super(new IRGotoOperator(), null, null, label);
    }

    /**
     * The string representation of this binary arithmetic instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s %s", op.opText(), result);
    }
}