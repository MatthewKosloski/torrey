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
import me.mtk.torrey.frontend.lexer.TokenType;
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
 * logical expression to their primitives.
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
        for (ASTNode child : program.children())
            child.accept(this);
        return program;
    }

    public Expr visit(BinaryExpr expr)
    {
        final Expr first = (Expr) expr.first();
        final Expr second = (Expr) expr.second();

        final ASTNode firstFolded = fold(first);
        final ASTNode secondFolded = fold(second);

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

            final Expr lastExpr = (Expr) expr.last();

            // Set the expression to which this let expression
            // evaluates (i.e., the last expression in the body).
            expr.setEval(lastExpr);

            // The let expression evaluates to whatever the type
            // of the last expression is.
            expr.setEvalType(lastExpr.evalType());

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

        // We've reduced the test condition to a primitive,
        // so we know which branch will be taken and thus
        // the type of value this expression evaluates to.
        if (expr.test().token().type() == TokenType.TRUE)
        {
            expr.setEvalType(expr.consequent().evalType());
            expr.setEval(expr.consequent());
        }
        else
        {
            expr.setEvalType(expr.alternative().evalType());
            expr.setEval(expr.alternative());
        }

        return expr;
    }

    public Expr visit(IdentifierExpr expr)
    {
        final String id = expr.token().rawText();
        final Expr boundedExpr = top.get(id).expr();
        
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
        if (node instanceof Foldable)
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
