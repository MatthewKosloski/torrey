package me.mtk.torrey.IR;

import java.util.List;
import java.util.ArrayList;

/**
 * The base class for the IR generator
 * containing state and helper methods.
 */
public abstract class IRGenerator
{
    // The accumulated list of intermediate instructions.
    protected List<Quadruple> quads;

    // The current temp variable number.
    private int tempCounter;

    /**
     * Instantiates a new instance of IRGenerator
     * with an empty list of instructions.
     */
    public IRGenerator()
    {
        quads = new ArrayList<>();
    }

    /**
     * Returns the generated intermediate instructions,
     * represented by a collection of quadruples.
     * 
     * @return The intermediate instructions.
     */
    public List<Quadruple> quads()
    {
        return quads;
    }

    /*
     * Maps the raw text of an AST token to the equivalent
     * operator for an IR unary instruction.
     *  
     * @param rawText The raw text of the AST node's token.
     * @return The corresponding IR unary instruction operator type.
     */
    protected UnaryOpType transUnaryOp(String rawText)
    {
        switch (rawText)
        {
            case "-": return UnaryOpType.MINUS;
            default: 
                throw new Error("Error: Cannot translate raw"
                    + " text to an IR unary operator");
        }
    }

    /*
     * Maps the raw text of an AST token to the equivalent
     * operator for an IR binary instruction.
     *  
     * @param rawText The raw text of the AST node's token.
     * @return The corresponding IR binary instruction operator type.
     */
    protected BinaryOpType transBinaryOp(String rawText)
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

    /**
     * Generates a new temp address.
     * 
     * @return A temporary address.
     */
    protected TempAddress newTemp()
    {
        return new TempAddress(String.format("t%d", tempCounter++));
    }
}