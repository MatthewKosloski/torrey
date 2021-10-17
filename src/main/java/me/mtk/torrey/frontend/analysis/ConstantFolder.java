package me.mtk.torrey.frontend.analysis;

import me.mtk.torrey.frontend.ast.*;
import me.mtk.torrey.frontend.symbols.Env;

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
public final class ConstantFolder implements ASTNodeVisitor<ASTNode>
{

    // The current environment.
    private Env top;

    public ConstantFolder()
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

    public Expr visit(ArithmeticExpr expr)
    {
        final Expr foldedExpr = foldBinaryExpr(expr);
        expr.setFold(foldedExpr);
        return expr;
    }

    public Expr visit(CompareExpr expr)
    {
        final Expr foldedExpr = foldBinaryExpr(expr);
        expr.setFold(foldedExpr);
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

            // Fold the child expressions.
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

    public ASTNode visit(IfThenElseExpr expr)
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

    public Expr visit(IntegerExpr expr)
    { 
        // Cannot be simplified, so just return the expression.
        return expr; 
    }

    public Expr visit(BooleanExpr expr)
    { 
        // Cannot be simplified, so just return the expression.
        return expr; 
    }

    private Expr foldBinaryExpr(BinaryExpr expr)
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

            Expr foldedExpr;
            
            switch (expr.token().type())
            {
                case PLUS:
                    foldedExpr = (Expr) Expr.makeConstantExpr(c1 + c2);
                    if (c1 + c2 == 0)
                        foldedExpr.makeFalsy();
                    break;
                case MINUS:
                    foldedExpr = (Expr) Expr.makeConstantExpr(c1 - c2);
                    if (c1 - c2 == 0)
                        foldedExpr.makeFalsy();
                    break;
                case STAR:
                    foldedExpr = (Expr) Expr.makeConstantExpr(c1 * c2);
                    if (c1 * c2 == 0)
                        foldedExpr.makeFalsy();
                    break;
                case SLASH:
                    foldedExpr = (Expr) Expr.makeConstantExpr(c1 / c2);
                    if (c1 / c2 == 0)
                        foldedExpr.makeFalsy();
                    break;
                case LT:
                    foldedExpr = new BooleanExpr(c1 < c2);
                    if (!(c1 < c2))
                        foldedExpr.makeFalsy();
                    break;
                case LTE:
                    foldedExpr = new BooleanExpr(c1 <= c2);
                    if (!(c1 <= c2))
                        foldedExpr.makeFalsy();
                    break;
                case GT:
                    foldedExpr = new BooleanExpr(c1 > c2);
                    if (!(c1 > c2))
                        foldedExpr.makeFalsy();
                    break;
                case GTE:
                    foldedExpr = new BooleanExpr(c1 >= c2);
                    if (!(c1 >= c2))
                        foldedExpr.makeFalsy();
                    break;
                case EQUAL:
                    foldedExpr = new BooleanExpr(c1 == c2);
                    if (!(c1 == c2))
                        foldedExpr.makeFalsy();
                    break;
                default:
                    foldedExpr = expr;
            }

            return foldedExpr;
        }

        return expr;
    }

    /*
     * Visits the given expression and potentially
     * sets its fold iff the expression is foldable.
     * 
     * @param expr An expression to (potentially) fold.
     * @return The expression or a reference to
     * the expression to which it folds.
     */
    private ASTNode fold(ASTNode node)
    {
        // Recurse through children.
        final ASTNode fold = node.accept(this);

        if (node instanceof Foldable)
        {
            // The expression can be folded, so assign the fold
            // to this expression if it doesn't already have a fold.
            final Foldable foldableExpr = ((Foldable) node);
            foldableExpr.setFold((Expr) fold);
            return foldableExpr.getFold();
        }

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
