package me.mtk.torrey.frontend.analysis;

import me.mtk.torrey.frontend.ast.ASTNode;
import me.mtk.torrey.frontend.ast.ASTNodeVisitor;
import me.mtk.torrey.frontend.ast.BinaryExpr;
import me.mtk.torrey.frontend.ast.BooleanExpr;
import me.mtk.torrey.frontend.ast.ConstantConvertable;
import me.mtk.torrey.frontend.ast.Program;
import me.mtk.torrey.frontend.ast.Expr;
import me.mtk.torrey.frontend.ast.Foldable;
import me.mtk.torrey.frontend.ast.IdentifierExpr;
import me.mtk.torrey.frontend.ast.IfExpr;
import me.mtk.torrey.frontend.ast.IntegerExpr;
import me.mtk.torrey.frontend.ast.UnaryExpr;
import me.mtk.torrey.frontend.symbols.Env;
import me.mtk.torrey.frontend.ast.LetExpr;
import me.mtk.torrey.frontend.ast.PrimitiveExpr;
import me.mtk.torrey.frontend.ast.PrintExpr;
import me.mtk.torrey.frontend.ast.LetBindings;
import me.mtk.torrey.frontend.ast.LetBinding;

/**
 * Constant folding is a type of high-level
 * compiler optimization that combines multiple
 * constants into a single constant. It performs
 * a post-order traversal of the AST. More generally,
 * this compiler pass reduces complex arithmetic and
 * logical expressions to their primitives.
 * 
 * Examples:
 *  (+ 2 3) -> 5
 *  (< 2 3) -> false
 */
public final class ConstantFolderVisitor implements ASTNodeVisitor<ASTNode>
{

    // The current environment.
    private Env top;

    public ConstantFolderVisitor()
    {
        top = new Env(null);
    }

    public ASTNode visit(Program program)
    {
        // Perform constant folding on each child node.
        for (ASTNode child : program.children())
            child.accept(this);

        return program;
    }

    public Expr visit(BinaryExpr expr)
    {
        // Perform constant folding on the two child nodes.
        final ASTNode firstFolded = fold(expr.first());
        final ASTNode secondFolded = fold(expr.second());

        if (firstFolded instanceof ConstantConvertable
            && secondFolded instanceof ConstantConvertable)
        {
            final int c1 = ((ConstantConvertable) firstFolded).toConstant();
            final int c2 = ((ConstantConvertable) secondFolded).toConstant();

            // If the folded expression is a unary expression
            // whose operand is a let expression, then don't attempt
            // constant fold.
            if ((firstFolded instanceof UnaryExpr 
                && firstFolded.first() instanceof LetExpr) ||
                (secondFolded instanceof UnaryExpr 
                && secondFolded.first() instanceof LetExpr))
                return expr;
            
            switch (expr.token().type())
            {
                case PLUS: return (Expr) Expr.makeConstantExpr(c1 + c2);
                case MINUS: return (Expr) Expr.makeConstantExpr(c1 - c2);
                case STAR: return (Expr) Expr.makeConstantExpr(c1 * c2);
                case SLASH: return (Expr) Expr.makeConstantExpr(c1 / c2);
                case LT: return new BooleanExpr(c1 < c2);
                case LTE: return new BooleanExpr(c1 <= c2);
                case GT: return new BooleanExpr(c1 > c2);
                case GTE: return new BooleanExpr(c1 >= c2);
                case EQUAL: return new BooleanExpr(c1 == c2);
                default: return expr;
            }
        }

        return expr;
    }

    public Expr visit(UnaryExpr expr) 
    {
        final Expr operand = (Expr) expr.first();

        // Don't perform folding if
        // the operand is an integer.
        if (!(operand instanceof IntegerExpr))
            fold(operand);

        return expr;
    }

    public Expr visit(PrintExpr expr) 
    {
        foldChildren(expr);
        return expr;
    }

    public Expr visit(LetExpr expr)
    {

        if (expr.children().size() != 0)
        {
            // The expression has one or more bindings and
            // zero or more expressions in its body.

            // Cache the previous environment and set
            // the current environment.
            final Env prevEnv = top;
            top = expr.environment();

            // Fold the child expressioms.
            foldChildren(expr);

            // Restore the previous environment.
            top = prevEnv;
        }
        
        return expr;
    }

    public ASTNode visit(LetBindings node)
    {
        foldChildren(node);
        return node;
    }

    public ASTNode visit(LetBinding node)
    {
        fold(node.second());
        return node;
    }

    public ASTNode visit(IfExpr expr)
    {
        foldChildren(expr);
        return expr;
    }

    public Expr visit(IdentifierExpr expr)
    {
        final String id = expr.token().rawText();
        final Expr boundedExpr = top.get(id).expr();

        fold(boundedExpr);
        
        if (boundedExpr instanceof Foldable)
            return ((Foldable) boundedExpr).getFold();
        else
            return boundedExpr;
    }

    public Expr visit(PrimitiveExpr expr)
    { 
        return expr; 
    }

    /*
     * Visits the given expression and potentially
     * sets its fold iff the expression is foldable.
     * 
     * @param expr An expression to (potentially)
     * @return The expression or a reference to
     * the expression to which it folds.
     */
    private ASTNode fold(ASTNode node)
    {
        final ASTNode fold = node.accept(this);
        // Only set fold if this node is foldable and
        // this node's fold is not just a reference to
        // itself.
        if (node instanceof Foldable && !fold.equals(node))
            ((Foldable) node).setFold((Expr) fold);
        return fold;
    }

    /**
     * Folds every child belonging to the parent expression.
     * 
     * @param parent A parent expression.
     */
    private void foldChildren(ASTNode parent)
    {
        for (ASTNode child : parent.children())
            fold(child);
    }
}
