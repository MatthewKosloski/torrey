package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.lexer.TokenType;

public enum UnaryOpType
{
    MINUS;

    /**
     * Gets the IR unary operator type corresponding
     * to the given Torrey token type.
     * 
     * @param torreyOp The Torrey operator.
     * @return The corresponding IR operator.
     */
    public static UnaryOpType getUnaryOpType(TokenType tokType)
    {
        switch (tokType)
        {
            case MINUS: return UnaryOpType.MINUS;
            default: 
                throw new Error("Error: Cannot translate raw"
                    + " text to an IR unary operator");
        }
    }

}
