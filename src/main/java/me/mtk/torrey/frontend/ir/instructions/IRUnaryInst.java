package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents a unary IR instruction.
 */
public class IRUnaryInst extends Quadruple
{
    /**
     * Instantiates a new unary IR instruction.
     * 
     * @param tokType The token type of the Torrey expression from 
     * which this IR instruction is derived.
     * @param arg The address at which the operand is located.
     * @param result The temp address at which the result of the operation
     * is to be stored.
     */
    public IRUnaryInst(TokenType tokType, IRAddress arg, IRTempAddress result)
    {
        super(OpType.getUnaryOpTypeFromTokenType(tokType), arg, null, result);
    }

    /**
     * Instantiates a new unary IR instruction
     * with no result address.
     * 
     * @param tokType The token type of the Torrey expression from 
     * which this IR instruction is derived.
     * @param arg The address at which the operand is located.
     */
    public IRUnaryInst(TokenType tokType, IRAddress arg)
    {
        this(tokType, arg, null);
    }

    /**
     * The string representation of this unary instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s = %s %s", result, opType, arg1);
    }
}
