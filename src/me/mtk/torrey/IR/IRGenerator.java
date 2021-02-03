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
public final class IRGenerator
{
    // current temp var number
    private int tempCounter;
    // accumulated list of instructions
    private List<Quadruple> instrs;

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
    public List<Quadruple> gen(Program program)
    {
        for (ASTNode child : program.children())
            gen((Expr) child, newtemp());
        
        return instrs;
    }

    /**
     * Generates one or more IR instructions for the given AST node.
     * 
     * @param expr An AST node.
     * @param lval The address at which the value of the 
     * instruction is to be stored.
     */
    private void gen(Expr expr, Address lval)
    {
        // A switch statement on token type would probably
        // be better, but we need a way to differentiate
        // binary subtraction from unary negation
        if (expr instanceof IntegerExpr)
            gen((IntegerExpr) expr, lval);
        else if (expr instanceof UnaryExpr)
            gen((UnaryExpr) expr, lval);
        else if (expr instanceof BinaryExpr)
            gen((BinaryExpr) expr, lval);
        else if (expr instanceof PrintExpr)
            gen((PrintExpr) expr);
        else
            throw new Error("ERROR: Cannot generate expression.");
    }

    /**
     * Generates one or more IR instructions for the given integer AST node.
     * 
     * @param expr An integer AST node.
     * @param result The address at which the value of the integer
     * is to be stored.
     */
    private void gen(IntegerExpr expr, Address result)
    {
        instrs.add(new CopyInst(result,
            Integer.parseInt(expr.token().rawText())));
    }

    /**
     * Generates one or more IR instructions for the given unary AST node.
     * 
     * @param expr An unary AST node.
     * @param result The address at which the result of the unary operation
     * is to be stored.
     */
    private void gen(UnaryExpr expr, Address result)
    {
        final UnaryOperator op = transUnaryOp(expr.token().rawText());
        final Address arg = newtemp();

        // generate the instructions for the operand
        gen((Expr) expr.first(), arg);

        instrs.add(new UnaryInst(op, arg, result));
    }

    /**
     * Generates one or more IR instructions for the given binary AST node.
     * 
     * @param expr An binary AST node.
     * @param result The address at which the result of the binary operation
     * is to be stored.
     */
    private void gen(BinaryExpr expr, Address result)
    {
        final BinaryOperator op = transBinaryOP(expr.token().rawText());

        // The results of the operands.
        final Address arg1 = newtemp();
        final Address arg2 = newtemp();

        // generate the instructions for both operands
        gen((Expr) expr.first(), arg1);
        gen((Expr) expr.second(), arg2);

        instrs.add(new BinaryInst(op, arg1, arg2, result));
    }

    /**
     * Generates one or more IR instructions for the given print AST node.
     * 
     * @param expr A print AST node.
     */
    private void gen(PrintExpr expr)
    {
        final List<Address> paramTemps = new ArrayList<>();
        
        // Generate the instructions for the parameters
        for (ASTNode child : expr.children())
        {
            final Address paramTemp = newtemp();
            gen((Expr) child, paramTemp);
            paramTemps.add(paramTemp);
        }

        // Generate the parameter instructions that
        // preceed the call instruction
        for (Address paramTemp : paramTemps)
            instrs.add(new ParamInst(paramTemp));
    
        final Address procName = new Address(AddressingMode.NAME, 
            expr.token().rawText());
        final Address numArgs = new Address(expr.children().size());

        instrs.add(new CallInst(procName, numArgs));
    }

    /*
     * Maps the raw text of an AST token to the equivalent
     * operator for an IR unary instruction.
     *  
     * @param rawText The raw text of the AST node's token.
     * @return The corresponding IR unary instruction operator type.
     */
    private UnaryOperator transUnaryOp(String rawText)
    {
        switch (rawText)
        {
            case "-": return UnaryOperator.MINUS;
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
    private BinaryOperator transBinaryOP(String rawText)
    {
        switch (rawText)
        {
            case "+": return BinaryOperator.ADD;
            case "-": return BinaryOperator.SUB;
            case "*": return BinaryOperator.MULT;
            case "/": return BinaryOperator.DIV;
            case "print":
            case "println":
                return BinaryOperator.CALL;
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
    private Address newtemp()
    {
        return new Address(String.format("t%d", tempCounter++));
    }
}
