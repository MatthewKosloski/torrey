package me.mtk.torrey.frontend.ir.instructions;

public enum UnaryOpType
{
    MINUS;

    public static UnaryOpType transUnaryOp(String rawText)
    {
        switch (rawText)
        {
            case "-": return UnaryOpType.MINUS;
            default: 
                throw new Error("Error: Cannot translate raw"
                    + " text to an IR unary operator");
        }
    }

}
