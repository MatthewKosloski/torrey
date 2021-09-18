package me.mtk.torrey.frontend.analysis;

import me.mtk.torrey.frontend.ast.*;
import me.mtk.torrey.frontend.error_reporter.*;
import me.mtk.torrey.frontend.lexer.*;
import me.mtk.torrey.frontend.symbols.*;

/**
 * Traverses the AST, checking the types of expressions and 
 * the operands to operators. Also, determines the evaluation
 * types of identifiers, let expressions, and if expressions.
 */
public final class TypeCheckerVisitor implements ASTNodeVisitor<DataType>
{
    // A reference to the error reporter that will
    // report any semantic errors during type checking.
    private ErrorReporter reporter;

    // The current environment.
    private Env top;

    /**
     * Constructs a new TypeCheckerVisitor that walks an
     * AST starting at the Program node checking for type
     * errors.
     * 
     * @param reporter The reporter reponsible for accumulating
     * and throwing SemanticErrors.
     */
    public TypeCheckerVisitor(ErrorReporter reporter)
    {
        this.reporter = reporter;
        top = new Env(null);
    }

    /**
     * Type checks the entire program, visiting every
     * child node.
     * 
     * @param program The program to be type checked.
     * @return The DataType of the program.
     */
    public DataType visit(Program program)
    {
        try
        {
            for (ASTNode child : program.children())
                child.accept(this);

            reporter.reportSemanticErrors("Encountered one or more semantic"
                + " errors during type checking:");
        }
        catch (SemanticError e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        // A Program AST does not evaluate to a 
        // data type as it's not an expression.
        return DataType.UNDEFINED;
    }

    /**
     * Type checks a binary expression, ensuring that both operands
     * are of type integer.
     * 
     * @param expr The binary expression to be type checked.
     * @return DataType INTEGER.
     */
    public DataType visit(BinaryExpr expr)
    {
        final Expr first = (Expr) expr.first();
        final Expr second = (Expr) expr.second();

        // Type check the operands.
        first.accept(this);
        second.accept(this);

        final Token operator = expr.token();

        if (expr instanceof ArithmeticExpr)
        {
            if (first.evalType() != DataType.INTEGER)
            {
                // expected type DataType.INTEGER
                reporter.error(first.token(), ErrorMessages.UnexpectedOperandToBe, 
                    operator.rawText(), DataType.INTEGER, first.evalType());
            } 
    
            if (second.evalType() != DataType.INTEGER)
            {
                // expected type DataType.INTEGER
                reporter.error(second.token(), ErrorMessages.UnexpectedOperandToBe, 
                    operator.rawText(), DataType.INTEGER, second.evalType());
            }
    
            if (first instanceof IntegerExpr && 
                second instanceof IntegerExpr && 
                operator.type() == TokenType.SLASH &&
                Integer.parseInt(second.token().rawText()) == 0)
            {
                // Both operands are primitives and integers to 
                // a division operator and the denominator is 0.
    
                reporter.error(second.token(), ErrorMessages.DivisionByZero, 
                operator.rawText());   
            }
    
        }
        else if (expr instanceof CompareExpr)
        {
            final boolean areInts = first.evalType() == DataType.INTEGER
                && second.evalType() == DataType.INTEGER;
            final boolean areBools = first.evalType() == DataType.BOOLEAN
                && second.evalType() == DataType.BOOLEAN;
            final boolean onlyFirstIsInt = first.evalType() == DataType.INTEGER 
                && second.evalType() != DataType.INTEGER;
            final boolean onlyFirstIsBool = first.evalType() == DataType.BOOLEAN
                && second.evalType() != DataType.BOOLEAN;

            if (onlyFirstIsInt)
            {
                // The first operand is an integer. Expected the second 
                // operand to also be an integer.
                reporter.error(
                    second.token(), 
                    ErrorMessages.UnexpectedOperandToBe, 
                    operator.rawText(), 
                    DataType.INTEGER, 
                    second.evalType());
            }
            else if (onlyFirstIsBool)
            {
                // The first operand is a boolean. Expected the second 
                // operand to also be a boolean.
                reporter.error(second.token(), 
                ErrorMessages.UnexpectedOperandToBe,
                    operator.rawText(), 
                    DataType.BOOLEAN, 
                    second.evalType());
            }
            else if (!(areInts || areBools))
            {
                // Either both operands are not integers or
                // both operands are not booleans.
                reporter.error(first.token(), 
                    ErrorMessages.UnexpectedOperandToBeEither, 
                    operator.rawText(), 
                    DataType.INTEGER, 
                    DataType.BOOLEAN, 
                    first.evalType());
            }
        }
        else
        {
            throw new Error("TypeCheckerVisitor.visit(BinaryExpr):"
                + " Unhandled expr case.");
        }


        return expr.evalType();
    }

    /**
     * Type checks primitive literal expressions by simply returning
     * the eval type of the expression.
     * 
     * @param expr The primitive literal expression to be type checked.
     * @return The data type of the primitive.
     */
    public DataType visit(PrimitiveExpr expr)
    {
        return expr.evalType();
    }

    /**
     * Type checks a print expression, ensuring that each
     * operand is of type integer.
     * 
     * @param expr The print expression to be type checked.
     * @return DataType PRINT.
     */
    public DataType visit(PrintExpr expr)
    {
        for (ASTNode child : expr.children())
        {
            final Expr childExpr = (Expr) child;

            // Type check child expression.
            childExpr.accept(this);

            if (childExpr.evalType() == DataType.UNDEFINED)
                reporter.error(
                    childExpr.token(), 
                    ErrorMessages.UndefinedOperandToPrint, 
                    childExpr.token().rawText());

        }
        
        return DataType.UNDEFINED;
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
        final Token operator = expr.token();
        final Expr operand = (Expr) expr.first();
        
        // Type check the operand.
        operand.accept(this);

        if (operand.evalType() != DataType.INTEGER)
        {
            // expected type DataType.INTEGER
            reporter.error(
                operand.token(), 
                ErrorMessages.UnexpectedOperandToBe,
                operator.rawText(), 
                DataType.INTEGER, 
                operand.evalType());
        } 

        return expr.evalType();
    }

    public DataType visit(IdentifierExpr expr)
    {
        final String id = expr.token().rawText();
        final Symbol sym = top.get(id);
        expr.setEvalType(sym.type());
        return expr.evalType();
    }

    public DataType visit(LetExpr expr)
    {
        if (expr.children().size() > 1)
        {
            // Cache the previous environment and create
            // a new environment.
            final Env prevEnv = top;
            top = expr.environment();

            // Type-check the child expressions.
            for (ASTNode child : expr.children())
                child.accept(this);

            // Restore the previous environment.
            top = prevEnv;

            // The type of this let expression is the same 
            // as the type of its last expression.
            final Expr lastExpr = (Expr) expr.last();
            expr.setEvalType(lastExpr.evalType());
            return expr.evalType();
        }

        return expr.evalType();
    }

    public DataType visit(LetBindings bindings)
    {
        // Call visit(LetBinding) to type check
        // all the bindings in this AST node.
        for (ASTNode binding : bindings.children())
            binding.accept(this);

        // A LetBindings AST does not evaluate to a 
        // data type as it's not an expression.
        return DataType.UNDEFINED;
    }

    public DataType visit(LetBinding binding)
    {
        final IdentifierExpr idExpr = (IdentifierExpr) binding.first();
        final Expr boundedExpr = (Expr) binding.second();

        // Type check the bounded expression.
        boundedExpr.accept(this);

        if (boundedExpr.evalType() == DataType.UNDEFINED)
        {
            reporter.error(idExpr.token(), 
                ErrorMessages.UnexpectedBoundedExprType,
                idExpr.token().rawText(),
                boundedExpr.evalType());
        }
        
        // A LetBinding AST does not evaluate to a 
        // data type as it's not an expression.
        return DataType.UNDEFINED;
    }

    public DataType visit(IfExpr expr)
    {
        // Type-check the test condition
        // and its child nodes.
        expr.test().accept(this);
        
        // Type-check the consequent and
        // its child nodes.
        expr.consequent().accept(this);

        // If we have an alternative expression,
        // then type check the alternative and
        // its child nodes.
        if (expr.alternative() != null)
            expr.alternative().accept(this);

        // The test condition will either be a 
        // BooleanExpr or a CompareExpr that folds
        // to a BooleanExpr.
        BooleanExpr bool;
        if (expr.test() instanceof Foldable)
            bool = (BooleanExpr) ((Foldable) expr.test()).getFold();
        else
            bool = (BooleanExpr) expr.test();

        if (bool.token().type() == TokenType.TRUE)
            // The test condition is true, so the type of
            // this if expression is the type of the consequent.
            expr.setEvalType(expr.consequent().evalType());
        else if (expr.alternative() != null)
            // The test condition is false and we have an alternative,
            // so the type of this if expression is the type of
            // the alternative.
            expr.setEvalType(expr.alternative().evalType());
        else
            // The test condition is false and we have no alternative,
            // so the type of this if expression is undefined.
            expr.setEvalType(DataType.UNDEFINED);

        return expr.evalType();
    }

}
