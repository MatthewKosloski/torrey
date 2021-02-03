package me.mtk.torrey.IR;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.AST.ASTNode;
import me.mtk.torrey.AST.BinaryExpr;
import me.mtk.torrey.AST.Expr;
import me.mtk.torrey.AST.IntegerExpr;
import me.mtk.torrey.AST.PrintExpr;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.AST.UnaryExpr;

/**
 * Translates an AST to an equivalent program in a 
 * linear intermediate language. This is an important
 * step of the compiler because it transforms the AST,
 * which is a non-linear data structure, to a linear
 * data structure that better resembles assembly code.
 * 
 * The README of this compiler contains the grammar of
 * this intermediate language.
 */
public class IRGenerator
{
    // current temp var number
    private int tempCounter;
    // accumulated list of instructions
    private List<IRInst> instrs;

    /**
     * Instantiates a new instance of IRGenerator
     * with an empty list of instructions.
     */
    public IRGenerator()
    {
        instrs = new ArrayList<>();
    }

    /**
     * Translates the given AST to an equivalent program
     * in the linear intermediate language.
     * 
     * @param program The root node of an AST.
     * @return The list of intermediate language instructions. 
     */
    public List<IRInst> gen(Program program)
    {
        for (ASTNode child : program.children())
            gen((Expr)child, newtemp());
        
        return instrs;
    }

    /**
     * Generates one or more IR instructions for the given AST node.
     * 
     * @param expr An AST node.
     * @param lval The address at which the value of the 
     * instruction is to be stored.
     */
    public void gen(Expr expr, Address lval)
    {
        // A switch statement on token type would probably
        // be better, but we need a way to differentiate
        // binary subtraction from unary negation
        if (expr instanceof IntegerExpr)
            gen((IntegerExpr)expr, lval);
        else if (expr instanceof UnaryExpr)
            gen((UnaryExpr)expr, lval);
        else if (expr instanceof BinaryExpr)
            gen((BinaryExpr)expr, lval);
        else if (expr instanceof PrintExpr)
            gen((PrintExpr)expr);
        else
            // TODO: better error message here
            throw new Error("ERROR: Cannot generate expression.");
    }

    public void gen(IntegerExpr expr, Address lval)
    {
        instrs.add(new IntegerInst(lval, 
            Integer.parseInt(expr.token().rawText())));
    }

    public void gen(UnaryExpr expr, Address lval)
    {
        final UnaryOpType op = transUnaryOp(expr.token().rawText());
        final Address operandLval = newtemp();

        // generate the instructions for the operand
        gen((Expr)expr.first(), operandLval);

        instrs.add(new UnaryInst(lval, op, operandLval));
    }

    public void gen(BinaryExpr expr, Address lval)
    {
        final BinaryOpType op = transBinaryOP(expr.token().rawText());

        // The lvals of the operands.
        final Address firstOpLval = newtemp();
        final Address secondOpLval = newtemp();

        // generate the instructions for both operands
        gen((Expr)expr.first(), firstOpLval);
        gen((Expr)expr.second(), secondOpLval);

        instrs.add(new BinaryInst(lval, op, firstOpLval, secondOpLval));
    }

    public void gen(PrintExpr expr)
    {
        final List<Address> paramTemps = new ArrayList<>();
        
        // generate the instructions for the parameters
        for (ASTNode child : expr.children())
        {
            final Address paramTemp = newtemp();
            gen((Expr)child, paramTemp);
            paramTemps.add(paramTemp);
        }

        // create parameters for this print instruction
        for (Address paramTemp : paramTemps)
            genParam(paramTemp);
    
        instrs.add(new CallInst(expr.token().rawText(), 
            expr.children().size()));
    }

    private void genParam(Address temp)
    {
        instrs.add(new ParamInst(temp));
    }

    /*
     * Maps the raw text of an AST token to the equivalent
     * operator for an IR unary instruction.
     *  
     * @param rawText
     * @return
     */
    private UnaryOpType transUnaryOp(String rawText)
    {
        switch (rawText)
        {
            case "-": return UnaryOpType.MINUS;
            default: 
                throw new Error("Error: Cannot translate raw"
                    + " text to an IR unary operator");
        }
    }

    private BinaryOpType transBinaryOP(String rawText)
    {
        switch (rawText)
        {
            case "+": return BinaryOpType.ADD;
            case "-": return BinaryOpType.SUB;
            case "*": return BinaryOpType.MULT;
            case "/": return BinaryOpType.DIV;
            default: 
                throw new Error("Error: Cannot translate raw"
                    + " text to an IR unary operator");
        }
    }
    
    private Address newtemp()
    {
        return new Address(String.format("t%d", tempCounter++));
    }
}
