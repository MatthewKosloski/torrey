package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.LabelAddress;

public final class GotoInst extends Quadruple
{
    /**
     * Instantiates a new goto instruction.
     * 
     * @param label The name of the label.
     */
    public GotoInst(LabelAddress label)
    {
        super(new GotoOperator(), null, null, label);
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