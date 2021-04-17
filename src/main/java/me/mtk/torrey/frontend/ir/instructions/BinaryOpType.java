package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.lexer.TokenType;

public enum BinaryOpType
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
     * Gets the IR binary operator type corresponding
     * to the given Torrey token type.
     * 
     * @param torreyOp The Torrey operator.
     * @return The corresponding IR operator.
     */
    public static BinaryOpType getBinaryOpType(TokenType tokType)
    {
        switch (tokType)
        {
            case PLUS: return BinaryOpType.ADD;
            case MINUS: return BinaryOpType.SUB;
            case STAR: return BinaryOpType.MULT;
            case SLASH: return BinaryOpType.DIV;
            case EQUAL: return BinaryOpType.EQUAL;
            case LT: return BinaryOpType.LT;
            case LTE: return BinaryOpType.LTE;
            case GT: return BinaryOpType.GT;
            case GTE: return BinaryOpType.GTE;

            default: 
                throw new Error("BinaryOpType.transTorreyOp(): Cannot"
                    + " translate raw text to an IR binary operator");
        }
    }

    /**
     * Negates the given IR operator type, 
     * returning the inverse operator type.
     * 
     * @param opType An IR operator type.
     * @return The inverse IR operator type.
     */
    public static BinaryOpType negate(BinaryOpType opType)
    {
        switch (opType)
        {
            case ADD: return SUB;
            case SUB: return ADD;
            case MULT: return DIV;
            case DIV: return MULT;
            case EQUAL: return NEQUAL;
            case NEQUAL: return EQUAL;
            case LT: return GTE;
            case LTE: return GT;
            case GT: return LTE;
            case GTE: return LT;
            default:
                throw new Error(String.format("BinaryOpType.negate(): "
                    + " BinaryOpType %s has no corresponding negation.", 
                    opType));
        }
    }

}