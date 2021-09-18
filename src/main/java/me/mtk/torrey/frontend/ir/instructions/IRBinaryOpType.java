package me.mtk.torrey.frontend.ir.instructions;

import java.util.Optional;

import me.mtk.torrey.frontend.lexer.TokenType;

public enum IRBinaryOpType
{
    ADD,
    SUB,
    MULT,
    DIV,
    EQUAL,
    NEQUAL,
    LT,
    LTE,
    GT,
    GTE;

    /**
     * Gets the binary IR operator type corresponding
     * to the given Torrey token type.
     * 
     * @param tokType A Torrey token type.
     * @return The corresponding binary IR operator type.
     */
    public static Optional<IRBinaryOpType> getBinaryOpType(TokenType tokType)
    {
        switch (tokType)
        {
            case PLUS: return Optional.of(IRBinaryOpType.ADD);
            case MINUS: return Optional.of(IRBinaryOpType.SUB);
            case STAR: return Optional.of(IRBinaryOpType.MULT);
            case SLASH: return Optional.of(IRBinaryOpType.DIV);
            case EQUAL: return Optional.of(IRBinaryOpType.EQUAL);
            case LT: return Optional.of(IRBinaryOpType.LT);
            case LTE: return Optional.of(IRBinaryOpType.LTE);
            case GT: return Optional.of(IRBinaryOpType.GT);
            case GTE: return Optional.of(IRBinaryOpType.GTE);
            default: return Optional.empty();
        }
    }

    /**
     * Negates the given IR operator type, 
     * returning the inverse operator type.
     * 
     * @param opType An IR operator type.
     * @return The inverse IR operator type.
     */
    public static Optional<IRBinaryOpType> negate(IRBinaryOpType opType)
    {
        switch (opType)
        {
            case ADD: return Optional.of(SUB);
            case SUB: return Optional.of(ADD);
            case MULT: return Optional.of(DIV);
            case DIV: return Optional.of(MULT);
            case EQUAL: return Optional.of(NEQUAL);
            case NEQUAL: return Optional.of(EQUAL);
            case LT: return Optional.of(GTE);
            case LTE: return Optional.of(GT);
            case GT: return Optional.of(LTE);
            case GTE: return Optional.of(LT);
            default: return Optional.empty();
        }
    }

}