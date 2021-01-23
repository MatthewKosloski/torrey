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
    public DataType visit(Program program)
    {
        final List<ASTNode> children = program.children();
        for (ASTNode child : children)
        {
            final Expr expr = (Expr) child;
            expr.accept(this);
        }

        return DataType.UNDEFINED;
    }

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

    public DataType visit(IntegerExpr expr)
    {
        return DataType.INTEGER;
    }

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
