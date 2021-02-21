package me.mtk.torrey.ir;

public enum BinaryOpType
{
    ADD,
    SUB,
    MULT,
    DIV;

    public static BinaryOpType transBinaryOp(String rawText)
    {
        switch (rawText)
        {
            case "+": return BinaryOpType.ADD;
            case "-": return BinaryOpType.SUB;
            case "*": return BinaryOpType.MULT;
            case "/": return BinaryOpType.DIV;
            default: 
                throw new Error("Error: Cannot translate raw"
                    + " text to an IR binary operator");
        }
    }

}