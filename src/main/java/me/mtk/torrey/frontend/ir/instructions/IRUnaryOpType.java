package me.mtk.torrey.frontend.ir.instructions;

import java.util.Optional;
import me.mtk.torrey.frontend.lexer.TokenType;

public enum IRUnaryOpType
{
    MINUS;

    /**
     * Gets the IR unary operator type corresponding
     * to the given Torrey token type.
     * 
     * @param torreyOp The Torrey operator.
     * @return The corresponding IR operator.
     */
    public static Optional<IRUnaryOpType> getUnaryOpType(TokenType tokType)
    {
        switch (tokType)
        {
            case MINUS: return Optional.of(IRUnaryOpType.MINUS);
            default: return Optional.empty();
        }
    }

}
