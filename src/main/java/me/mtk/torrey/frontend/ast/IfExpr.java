package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.symbols.Env;
import me.mtk.torrey.frontend.symbols.Symbol;

public class IfExpr extends Expr
{
    public IfExpr(Token tok, Expr test, Expr consequent)
    {
        super(tok, DataType.NIL);
        addChild(test);
        addChild(consequent);
    }

    public Expr test()
    {
        return (Expr) get(0);
    }

    public Expr consequent()
    {
        return (Expr) get(1);
    }

    /**
     * Indicates whether the test condition has been evaluated at the current
     * time of compilation.
     * 
     * @return True if, at the current time of compilation, the truthiness of
     * the test condition is known; False otherwise.
     */
    public boolean hasEvaluatedTest(Env top)
    {
        final boolean testIsIdent = test() instanceof IdentifierExpr;
        final boolean testIsLet = test() instanceof LetExpr;
        final boolean testIsLetIdent = testIsLet &&
            test().last() instanceof IdentifierExpr;

        Symbol testIdentSym = null;
        if (testIsIdent)
        {
            // The test condition expression is an identifier, so
            // traverse the lexical scope chain to find the symbol
            // associated with the identifier.
            final String id = ((IdentifierExpr) test()).token().rawText();
            testIdentSym = top.get(id);
        }

        Symbol testLetIdentSym = null;
        if (testIsLetIdent)
        {
            // The test condition is a let expression that evaluates
            // to an identifier expression, so traverse the lexical scope
            // chain to find the symbol associated with the identifier.
            final LetExpr test = (LetExpr) test();
            final String id = test.last().token().rawText();
            top = test.environment();
            testLetIdentSym = top.get(id);
        }

        // Test condition is an identifier that evaluates to a primitive
        // (e.g., boolean, integer).
        final boolean testIsIdentPrim = testIdentSym != null && 
            testIdentSym.expr() instanceof PrimitiveExpr;
        
        // Test condition is an identifier that evaluates to a constant
        // (e.g., integer, unary).
        final boolean testIsIdentConst = testIdentSym != null && 
            testIdentSym.expr() instanceof ConstantConvertable;
        
        // Test condition evaluates to a constant (e.g., integer, unary).
        final boolean testIsConst = test() instanceof ConstantConvertable;
    
        // Test condition evaluates to a primitive (e.g., boolean, integer).
        final boolean testIsPrim = test() instanceof PrimitiveExpr;

        // Test condition is foldable and has a fold (e.g., IntegerExpr,
        // ArithmeticExpr, BinaryExpr).
        final boolean testIsFolded = test() instanceof Foldable &&
            ((Foldable) test()).hasFold();
        
        // Test condition is a let expression that evaluates to an
        // identifier that evaluates to a primitive.
        final boolean testIsLetIdentPrim = testLetIdentSym != null &&
            testLetIdentSym.expr() instanceof PrimitiveExpr;

        // Test condition is a let expression that evaluates to an
        // identifier that evaluates to a constant.
        final boolean testIsLetIdentConst = testLetIdentSym != null && 
            testLetIdentSym.expr() instanceof ConstantConvertable;

        // Test condition is a let expression that evaluates to
        // a constant (e.g., integer, unary).
        final boolean testIsLetConst = testIsLet &&
            ((LetExpr) test()).last() instanceof ConstantConvertable;
    
        // Test condition is a let expression that evaluates to
        // a primitive (e.g., boolean, integer).
        final boolean testIsLetPrim = testIsLet &&
            ((LetExpr) test()).last() instanceof PrimitiveExpr;

        // Test condition is a foldable let expression and has a fold
        // (e.g., IntegerExpr, ArithmeticExpr, BinaryExpr).
        final boolean testIsletFolded = test() instanceof Foldable &&
            ((Foldable) test()).hasFold();

        return testIsIdentPrim || testIsIdentConst || testIsConst || testIsPrim
            || testIsFolded || testIsLetIdentPrim || testIsLetIdentConst
            || testIsLetConst || testIsLetPrim || testIsletFolded;
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
