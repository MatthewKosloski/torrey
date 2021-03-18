package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.Address;
import me.mtk.torrey.frontend.ir.addressing.TempAddress;

/**
 * Represents a simply "x = y" copy instruction,
 * where the contents at address y are stored in
 * the temp address x.
 */
public final class CopyInst extends Quadruple
{
    /**
     * Instantiates a new copy IR instruction.
     * 
     * @param lhs The left-hand side of the copy, that is,
     * the destination. This must be a temporary address.
     * @param rhs The right-hand side of the copy, that is,
     * the source.
     */
    public CopyInst(TempAddress lhs, Address rhs)
    {
        super(new CopyOperator(), rhs, null, lhs);
    }

    public String toString()
    {
        return String.format("%s %s %s", result, op.opText(), arg1);
    }
}
