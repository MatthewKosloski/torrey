package me.mtk.torrey.Analysis;

import me.mtk.torrey.AST.ASTNode;
import me.mtk.torrey.AST.BinaryExpr;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.AST.Expr;
import me.mtk.torrey.AST.ExprVisitor;
import me.mtk.torrey.AST.IntegerExpr;
import me.mtk.torrey.AST.PrintExpr;
import me.mtk.torrey.AST.ProgramVisitor;
import me.mtk.torrey.AST.UnaryExpr;
import me.mtk.torrey.Lexer.Token;
import me.mtk.torrey.Lexer.TokenType;

/**
 * Constant folding is a type of high-level
 * compiler optimization that combines multiple
 * constants into a single constant. It performs
 * a post-order traversal of the AST, visiting
 * every binary expression. If both operands
 * to the binary expression are constants, then
 * the binary expression node can be reduced to
 * a single integer expression node.
 */
public final class ConstantFolderVisitor implements 
    ExprVisitor<Expr>, ProgramVisitor<ASTNode>
{
    public ASTNode visit(Program program)
    {
        for (ASTNode child : program.children())
            ((Expr) child).accept(this);
        return program;
    }

    public Expr visit(BinaryExpr expr)
    {
        final Expr first = (Expr) expr.first();
        final Expr second = (Expr) expr.second();

        // Perform constant folding on the two children.
        final Expr firstFolded = first.accept(this);
        final Expr secondFolded = second.accept(this);

        expr.children().set(0, firstFolded);
        expr.children().set(1, secondFolded);

        if (firstFolded instanceof IntegerExpr && 
        secondFolded instanceof IntegerExpr)
        {
            // Both folded children are integers, so we can 
            // reduce this binary expression to an integer.
            switch (expr.token().type())
            {
                case MINUS:
                    final int difference = 
                        Integer.parseInt(firstFolded.token().rawText()) -
                        Integer.parseInt(secondFolded.token().rawText());
                    return createIntExpr(difference);
                case STAR:
                    final int product = 
                        Integer.parseInt(firstFolded.token().rawText()) * 
                        Integer.parseInt(secondFolded.token().rawText());
                    return createIntExpr(product);
                case SLASH:
                    final int divisor = 
                        Integer.parseInt(firstFolded.token().rawText()) / 
                        Integer.parseInt(secondFolded.token().rawText());
                    return createIntExpr(divisor);
                case PLUS:
                    final int sum = 
                        Integer.parseInt(firstFolded.token().rawText()) + 
                        Integer.parseInt(secondFolded.token().rawText());
                    return createIntExpr(sum);
                default:
                    return null;
            }
        }
        else
            return expr;
    }

    public Expr visit(UnaryExpr expr) 
    {
        final Expr foldedChild = ((Expr) expr.first()).accept(this);
        expr.children().set(0, foldedChild);
        return expr;
    }

    public Expr visit(PrintExpr expr) 
    {
        for (int i = 0; i < expr.children().size(); i++)
        {
            final Expr child  = (Expr) expr.children().get(i);
            final Expr newExpr = child.accept(this);
            expr.children().set(i, newExpr);
        }
        return expr;
    }

    public Expr visit(IntegerExpr expr) 
    { 
        return expr; 
    }

    /*
     * Helper method for easy construction of integer
     * expressions.
     * 
     * @param constant The constant value to be stored
     * in this integer expresison.
     * @return An integer expression.
     */
    private IntegerExpr createIntExpr(int constant)
    {
        return new IntegerExpr(new Token(TokenType.INTEGER, 
            constant + ""));
    }
}
