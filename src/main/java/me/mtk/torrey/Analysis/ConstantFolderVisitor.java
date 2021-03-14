package me.mtk.torrey.analysis;

import me.mtk.torrey.ast.ASTNode;
import me.mtk.torrey.ast.ASTNodeVisitor;
import me.mtk.torrey.ast.BinaryExpr;
import me.mtk.torrey.ast.ConstantConvertable;
import me.mtk.torrey.ast.Program;
import me.mtk.torrey.ast.Expr;
import me.mtk.torrey.ast.IdentifierExpr;
import me.mtk.torrey.ast.IntegerExpr;
import me.mtk.torrey.ast.PrintExpr;
import me.mtk.torrey.ast.UnaryExpr;
import me.mtk.torrey.ast.LetExpr;
import me.mtk.torrey.ast.PrimitiveExpr;
import me.mtk.torrey.ast.LetBindings;
import me.mtk.torrey.ast.LetBinding;

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
public final class ConstantFolderVisitor implements ASTNodeVisitor<ASTNode>
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
        final Expr firstFolded = (Expr) first.accept(this);
        final Expr secondFolded = (Expr) second.accept(this);

        expr.children().set(0, firstFolded);
        expr.children().set(1, secondFolded);

        if (firstFolded instanceof ConstantConvertable 
            && secondFolded instanceof ConstantConvertable)
        {

            // If the folded expression is a unary expression
            // whose operand is a let expression, then don't attempt
            // constant fold.
            if (firstFolded instanceof UnaryExpr 
                && firstFolded.first() instanceof LetExpr)
                return expr;

            if (secondFolded instanceof UnaryExpr 
                && secondFolded.first() instanceof LetExpr)
                return expr;


            // Both folded children can be converted to constants,
            // so we can reduce this binary expression to an integer expression.

            // Get the constants of the operands to the
            // arthmetic expression.
            final int c1 = ((ConstantConvertable) firstFolded).toConstant();
            final int c2 = ((ConstantConvertable) secondFolded).toConstant();

            switch (expr.token().type())
            {
                case PLUS: return new IntegerExpr(c1 + c2);
                case MINUS: return new IntegerExpr(c1 - c2);
                case STAR: return new IntegerExpr(c1 * c2);
                case SLASH: return new IntegerExpr(c1 / c2);
                default: return null;
            }
        }
        else
            return expr;
    }

    public Expr visit(UnaryExpr expr) 
    {
        final Expr foldedChild = (Expr) ((Expr) expr.first()).accept(this);
        expr.children().set(0, foldedChild);
        return expr;
    }

    public Expr visit(PrintExpr expr) 
    {
        for (int i = 0; i < expr.children().size(); i++)
        {
            final Expr child  = (Expr) expr.children().get(i);
            final Expr newExpr = (Expr) child.accept(this);
            expr.children().set(i, newExpr);
        }
        return expr;
    }

    public Expr visit(LetExpr expr)
    {

        if (expr.children().size() != 0)
        {
            // The expression has one or more bindings and
            // zero or more expressions in its body.
            ((LetBindings) expr.first()).accept(this);
        }
        
        return expr;
    }

    public ASTNode visit(LetBindings expr)
    {
        for (int i = 0; i < expr.children().size(); i++)
        {
            final LetBinding child = (LetBinding) expr.children().get(i);
            final LetBinding foldedChild = (LetBinding) child.accept(this);
            expr.children().set(i, foldedChild);
        }
        return expr;
    }

    public ASTNode visit(LetBinding expr)
    {
        final IdentifierExpr id = (IdentifierExpr) expr.first();
        final Expr boundedExpr = (Expr) expr.second();

        final IdentifierExpr foldedId = (IdentifierExpr) id.accept(this);
        final Expr foldedExpr = (Expr) boundedExpr.accept(this);
        
        expr.children().set(0, foldedId);
        expr.children().set(1, foldedExpr);

        return expr;
    }

    public Expr visit(IdentifierExpr expr)
    {
        return expr;
    }

    public Expr visit(PrimitiveExpr expr)
    { 
        return expr; 
    }
}
