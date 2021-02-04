package me.mtk.torrey.IR;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.AST.ProgramIRVisitor;
import me.mtk.torrey.AST.ASTNode;
import me.mtk.torrey.AST.BinaryExpr;
import me.mtk.torrey.AST.Expr;
import me.mtk.torrey.AST.ExprIRVisitor;
import me.mtk.torrey.AST.IntegerExpr;
import me.mtk.torrey.AST.PrintExpr;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.AST.UnaryExpr;

public final class IRGenVisitor extends IRGenerator implements 
    ExprIRVisitor<Void>, ProgramIRVisitor<Void>
{
    /**
     * Traverses the given AST, generating intermediate code.
     * 
     * @param Program The root AST node.
     */
    public Void visit(Program program)
    {
        for (ASTNode child : program.children())
            ((Expr) child).accept(this, newTemp());
    
        return null;
    }

    /**
     * Generates one or more IR instructions for the given integer AST node.
     * 
     * @param expr An integer AST node.
     * @param result The address at which the value of the integer
     * is to be stored.
     */
    public Void visit(IntegerExpr expr, TempAddress result)
    {
        final ConstAddress constant = new ConstAddress(
            Integer.parseInt(expr.token().rawText()));
        quads.add(new CopyInst(result, constant));

        return null;
    }

    /**
     * Generates one or more IR instructions for the given unary AST node.
     * 
     * @param expr An unary AST node.
     * @param result The address at which the result of the unary operation
     * is to be stored.
     */
    public Void visit(UnaryExpr expr, TempAddress result)
    {
        final UnaryOperator op = transUnaryOp(expr.token().rawText());
        final TempAddress arg = newTemp();

        // generate the instructions for the operand
        ((Expr) expr.first()).accept(this, arg);

        quads.add(new UnaryInst(op, arg, result));

        return null;
    }

    /**
     * Generates one or more IR instructions for the given binary AST node.
     * 
     * @param expr An binary AST node.
     * @param result The address at which the result of the binary operation
     * is to be stored.
     */
    public Void visit(BinaryExpr expr, TempAddress result)
    {
        final BinaryOperator op = transBinaryOp(expr.token().rawText());

        // The results of the operands.
        final TempAddress arg1 = newTemp();
        final TempAddress arg2 = newTemp();

        // generate the instructions for both operands
        ((Expr) expr.first()).accept(this, arg1);
        ((Expr) expr.second()).accept(this, arg2);

        quads.add(new BinaryInst(op, arg1, arg2, result));

        return null;
    }

    /**
     * Generates one or more IR instructions for the given print AST node.
     * 
     * @param expr A print AST node.
     */
    public Void visit(PrintExpr expr)
    {
        final List<Address> paramTemps = new ArrayList<>();
        
        // Generate the instructions for the parameters.
        for (ASTNode child : expr.children())
        {
            final TempAddress paramTemp = newTemp();
            ((Expr) child).accept(this, paramTemp);
            paramTemps.add(paramTemp);
        }

        // Generate the parameter instructions that
        // precede the call instruction.
        for (Address paramTemp : paramTemps)
            quads.add(new ParamInst(paramTemp));
    
        final NameAddress procName = new NameAddress(expr.token().rawText());
        final ConstAddress numParams = new ConstAddress(expr.children().size());

        quads.add(new CallInst(procName, numParams));

        return null;
    }
}