package me.mtk.torrey.IR;

/**
 * Represents a simply "x = y" copy instruction,
 * where the contents at address y are stored in
 * the temp address x.
 */
public final class CopyInst extends UnaryInst
{
    /**
     * Instantiates a new copy IR instruction.
     * 
     * @param lhs The left-hand side of the copy, that is,
     * the destination.
     * @param rhs The right-hand side of the copy, that is,
     * the source.
     */
    public CopyInst(Address lhs, Address rhs)
    {
        super(UnaryOperator.ASSIGN, rhs, lhs);
    }

    /**
     * Instantiates a new copy IR instruction with a constant
     * right-hand side.
     * 
     * @param lhs The left-hand side of the copy, that is,
     * the destination.
     * @param rhs The right-hand side of the copy, that is,
     * the source.
     */
    public CopyInst(Address lhs, int rhs)
    {
        this(lhs, new Address(rhs));
    }

    public String toString()
    {
        return String.format("%s = %s", result, arg1);
    }
}
