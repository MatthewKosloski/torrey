package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.LabelAddress;

public final class LabelInst extends Quadruple
{
    /**
     * Instantiates a new label instruction.
     * 
     * @param label The name of the label.
     */
    public LabelInst(LabelAddress label)
    {
        super(new LabelOperator(), label);
    }

    /**
     * The string representation of this binary arithmetic instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s %s:", op.opText(), arg1);
    }
}