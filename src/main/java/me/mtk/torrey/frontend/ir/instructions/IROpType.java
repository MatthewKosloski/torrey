package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * IR operator types and their corresponding terminal symbols.
 */

public enum IROpType
{
    // IR binary instruction operators
    ADD ("+"),
    SUB ("-"),
    MULT ("*"),
    DIV ("/"),
    EQUAL ("=="),
    NEQUAL ("!="),
    LT ("<"),
    LTE ("<="),
    GT (">"),
    GTE (">="),

    // IR call instruction operator
    CALL ("call"),

    // IR copy instruction operator
    COPY ("="),

    // IR goto instruction operator
    GOTO ("goto"),

    // IR label instruction operator
    LABEL ("label"),

    // IR param instruction operator
    PARAM ("param"),

    // IR unary instruction operator
    MINUS ("-");

    private final String terminalSymbol;

    private IROpType(String terminalSymbol)
    {
        this.terminalSymbol = terminalSymbol;
    }

    /**
     * Returns the terminal symbol of this IR operator.
     * 
     * @return An IR terminal symbol.
     */
    public String terminal()
    {
        return terminalSymbol;
    }

    /**
     * Gets the binary IR operator type corresponding
     * to the given Torrey token type.
     * 
     * @param tokType A Torrey token type.
     * @return The corresponding binary IR operator type.
     */
    public static IROpType getBinaryOpTypeFromTokenType(TokenType tokType)
    {
        switch (tokType)
        {
            case PLUS: return IROpType.ADD;
            case MINUS: return IROpType.SUB;
            case STAR: return IROpType.MULT;
            case SLASH: return IROpType.DIV;
            case EQUAL: return IROpType.EQUAL;
            case LT: return IROpType.LT;
            case LTE: return IROpType.LTE;
            case GT: return IROpType.GT;
            case GTE: return IROpType.GTE;
            default:
                throw new Error(String.format(
                    "Unexpected token type %s", tokType));
        }
    }

    /**
     * Gets the IR unary operator type corresponding
     * to the given Torrey token type.
     * 
     * @param tokType A Torrey token type.
     * @return The corresponding unary IR operator type.
     */
    public static IROpType getUnaryOpTypeFromTokenType(TokenType tokType)
    {
        switch (tokType)
        {
            case MINUS: return IROpType.MINUS;
            default:
                throw new Error(String.format(
                    "Unexpected token type %s", tokType));
        }
    }

    /**
     * Returns the inverse of the binary IR operator type corresponding
     * to the given Torrey token type.
     * 
     * @param tokType A Torrey token type.
     * @return The inverse IR operator type.
     */
    public static IROpType getInvertedBinaryOpTypeFromTokenType(TokenType tokenType)
    {
        return getInvertedBinaryOpType(getBinaryOpTypeFromTokenType(tokenType));
    }

    /**
     * Returns the inverse of the given binary IR operator type.
     * 
     * @param opType A binary IR operator type.
     * @return The inverse IR operator type.
     */
    public static IROpType getInvertedBinaryOpType(IROpType opType)
    {
        switch (opType)
        {
            case ADD: return IROpType.SUB;
            case SUB: return IROpType.ADD;
            case MULT: return IROpType.DIV;
            case DIV: return IROpType.MULT;
            case EQUAL: return IROpType.NEQUAL;
            case NEQUAL: return IROpType.EQUAL;
            case LT: return IROpType.GTE;
            case LTE: return IROpType.GT;
            case GT: return IROpType.LTE;
            case GTE: return IROpType.LT;
            default:
                throw new Error(String.format(
                    "Unexpected op type %s", opType));
        }
    }

    public String toString()
    {
        return terminalSymbol;
    }
}