package me.mtk.torrey.frontend.ir.instructions;

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
     * Translates a Torrey binary operator to an IR operator.
     * 
     * @param torreyOp The Torrey operator.
     * @return The corresponding IR operator.
     */
    public static BinaryOpType transTorreyOp(String torreyOp)
    {
        switch (torreyOp)
        {
            case "+": return BinaryOpType.ADD;
            case "-": return BinaryOpType.SUB;
            case "*": return BinaryOpType.MULT;
            case "/": return BinaryOpType.DIV;
            case "==": return BinaryOpType.EQUAL;
            case "<": return BinaryOpType.LT;
            case "<=": return BinaryOpType.LTE;
            case ">": return BinaryOpType.GT;
            case ">=": return BinaryOpType.GTE;

            default: 
                throw new Error("BinaryOpType.transTorreyOp(): Cannot"
                    + " translate raw text to an IR binary operator");
        }
    }

    /**
     * Negates the given IR operator, returning the inverse operator.
     * 
     * @param opType An IR operator.
     * @return The inverse IR operator.
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