package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;

/**
 * Represents a simply "x = y" copy instruction,
 * where the contents at address y are stored in
 * the temp address x.
 */
public final class IRCopyInst extends Quadruple
{
    /**
     * Instantiates a new copy IR instruction.
     * 
     * @param lhs The left-hand side of the copy, that is,
     * the destination. This must be a temporary address.
     * @param rhs The right-hand side of the copy, that is,
     * the source.
     */
    public IRCopyInst(IRTempAddress lhs, IRAddress rhs)
    {
        super(OpType.COPY, rhs, null, lhs);
    }

    public String toString()
    {
        return String.format("%s %s %s", result, opType.terminal(), arg1);
    }
}
