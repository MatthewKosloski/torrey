package me.mtk.torrey.frontend.ir.instructions;

import java.util.Optional;
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
    public static Optional<UnaryOpType> getUnaryOpType(TokenType tokType)
    {
        switch (tokType)
        {
            case MINUS: return Optional.of(UnaryOpType.MINUS);
            default: return Optional.empty();
        }
    }

}
