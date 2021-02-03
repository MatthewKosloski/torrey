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

public class IRGenerator
{
    // current temp var number
    private int tempCounter;
    // accumulated list of instructions
    private List<IRInst> instrs;


    public IRGenerator()
    {
        instrs = new ArrayList<>();
    }

    public List<IRInst> gen(Program program)
    {
        for (ASTNode child : program.children())
            gen((Expr)child, newtemp());
        
        return instrs;
    }

    public void gen(Expr expr, Address temp)
    {
        // A switch statement on token type would probably
        // be better, but we need a way to differentiate
        // binary subtraction from unary negation
        if (expr instanceof IntegerExpr)
            gen((IntegerExpr)expr, temp);
        else if (expr instanceof UnaryExpr)
            gen((UnaryExpr)expr, temp);
        else if (expr instanceof BinaryExpr)
            gen((BinaryExpr)expr, temp);
        else if (expr instanceof PrintExpr)
            gen((PrintExpr)expr, temp);
        else
            // TODO: better error message here
            throw new Error("ERROR: Cannot generate expression.");
    }

    public void gen(IntegerExpr expr, Address temp)
    {
        instrs.add(new IntegerInst(temp, 
            Integer.parseInt(expr.token().rawText())));
    }

    public void gen(UnaryExpr expr, Address temp)
    {
        final UnaryOpType op = transUnaryOp(expr.token().rawText());
        final Address operandTemp = newtemp();

        // generate the instructions for the operand
        gen((Expr)expr.first(), operandTemp);

        instrs.add(new UnaryInst(temp, op, operandTemp));
    }

    public void gen(BinaryExpr expr, Address temp)
    {
        final BinaryOpType op = transBinaryOP(expr.token().rawText());
        final Address firstOpTemp = newtemp();
        final Address secondOpTemp = newtemp();

        // generate the instructions for both operands
        gen((Expr)expr.first(), firstOpTemp);
        gen((Expr)expr.second(), secondOpTemp);

        instrs.add(new BinaryInst(temp, op, firstOpTemp, secondOpTemp));
    }

    public void gen(PrintExpr expr, Address temp)
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
    
        instrs.add(new CallInst(temp, expr.token().rawText(), 
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
        return new Address(AddressType.TEMP, 
            String.format("t%d", tempCounter++));
    }
}
