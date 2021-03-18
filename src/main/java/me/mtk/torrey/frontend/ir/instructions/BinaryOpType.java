package me.mtk.torrey.frontend.ir.instructions;

public enum BinaryOpType
{
    ADD,
    SUB,
    MULT,
    DIV,
    EQUAL,
    LT,
    LTE,
    GT,
    GTE;

    public static BinaryOpType transBinaryOp(String rawText)
    {
        switch (rawText)
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
                throw new Error("BinaryOpType.transBinaryOp(): Cannot"
                    + " translate raw text to an IR binary operator");
        }
    }

}