package me.mtk.torrey.Analysis;

import java.util.List;
import me.mtk.torrey.AST.ExprVisitor;
import me.mtk.torrey.AST.ASTNode;
import me.mtk.torrey.AST.BinaryExpr;
import me.mtk.torrey.AST.IntegerExpr;
import me.mtk.torrey.AST.PrintExpr;
import me.mtk.torrey.AST.UnaryExpr;
import me.mtk.torrey.AST.Expr;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.AST.ProgramVisitor;

public final class TypeCheckerVisitor implements 
    ExprVisitor<DataType>, ProgramVisitor<DataType>
{

    /**
     * Type checks the entire program, visiting every
     * child node.
     * 
     * @param program The program to be type checked.
     * @return The DataType of the program.
     */
    public DataType visit(Program program)
    {
        final List<ASTNode> children = program.children();
        for (ASTNode child : children)
        {
            final Expr expr = (Expr) child;
            expr.accept(this);
        }

        // A Program doesn't have a data type,
        // but we must return one so we will
        // return UNDEFINED.
        return DataType.UNDEFINED;
    }

    /**
     * Type checks a binary expression, ensuring that the operands
     * are of type integer.
     * 
     * @param expr The binary expression to be type checked.
     * @return DataType INTEGER.
     */
    public DataType visit(BinaryExpr expr)
    {
        final Expr first = (Expr) expr.first();
        final Expr second = (Expr) expr.second();
        final String opRawText = expr.token().rawText();

        if (first.accept(this) != DataType.INTEGER)
        {
            final String err = String.format("First operand to binary operator '%s' must be of type integer",
                opRawText);
            throw new Error(err);
        } 
        if (second.accept(this) != DataType.INTEGER)
        {
            final String err = String.format("Second operand to binary operator '%s' must be of type integer",
                opRawText);
            throw new Error(err);
        }
        return DataType.INTEGER;
    }

    /**
     * Type checks an integer literal expression by simply returning
     * the integer data type.
     * 
     * @param expr The integer literal expression to be type checked.
     * @return DataType INTEGER.
     */
    public DataType visit(IntegerExpr expr)
    {
        return DataType.INTEGER;
    }

    /**
     * Type checks an integer literal expression by simply returning
     * the integer data type.
     * 
     * @param expr The integer literal expression to be type checked.
     * @return DataType INTEGER.
     */
    public DataType visit(PrintExpr expr)
    {
        final List<ASTNode> children = expr.children();
        for (ASTNode child : children)
        {
            final Expr childExpr = (Expr) child;
            if (childExpr.accept(this) != DataType.INTEGER)
            {
                final String err = "Operand to print expression must be of type integer";
                throw new Error(err);
            } 

        }
        return DataType.PRINT;
    }

    /**
     * Type checks a unary expression, ensuring that the 
     * operand is of type integer.
     * 
     * @param expr The unary expression to be type checked.
     * @return DataType INTEGER.
     */
    public DataType visit(UnaryExpr expr)
    {
        final Expr operand = (Expr) expr.first();
        final String opRawText = expr.token().rawText();
        if (operand.accept(this) != DataType.INTEGER)
        {
            final String err = String.format("Operand to unary operator '%s' must be of type integer",
                opRawText);
            throw new Error(err);
        } 

        return DataType.INTEGER;
    }
}
