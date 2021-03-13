package me.mtk.torrey.analysis;

import me.mtk.torrey.error_reporter.ErrorMessages;
import me.mtk.torrey.error_reporter.ErrorReporter;
import me.mtk.torrey.error_reporter.SemanticError;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.lexer.TokenType;
import me.mtk.torrey.ast.IdentifierExpr;
import me.mtk.torrey.ast.ASTNode;
import me.mtk.torrey.ast.ASTNodeVisitor;
import me.mtk.torrey.ast.BinaryExpr;
import me.mtk.torrey.ast.CompareExpr;
import me.mtk.torrey.ast.IntegerExpr;
import me.mtk.torrey.ast.PrintExpr;
import me.mtk.torrey.ast.UnaryExpr;
import me.mtk.torrey.ast.Expr;
import me.mtk.torrey.ast.Program;
import me.mtk.torrey.ast.LetExpr;
import me.mtk.torrey.ast.LetBinding;
import me.mtk.torrey.ast.LetBindings;
import me.mtk.torrey.symbols.Env;
import me.mtk.torrey.symbols.SymCategory;
import me.mtk.torrey.symbols.Symbol;

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
                ((Expr) child).accept(this);

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

        return expr.evalType();
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
        final Token operator = expr.token();

        for (ASTNode child : expr.children())
        {
            final Expr childExpr = (Expr) child;

            // Type check child expression.
            childExpr.accept(this);

            if (childExpr.evalType() != DataType.INTEGER 
                && childExpr.evalType() != DataType.UNDEFINED)
            {
                // expected type DataType.INTEGER
                reporter.error(childExpr.token(), 
                    ErrorMessages.UnexpectedOperandToBe,
                    operator.rawText(), 
                    DataType.INTEGER, 
                    childExpr.evalType());
            } 
        }
        
        return expr.evalType();
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
            reporter.error(operand.token(), ErrorMessages.UnexpectedOperandToBe,
                operator.rawText(), DataType.INTEGER, operand.evalType());
        } 

        return expr.evalType();
    }

    public DataType visit(IdentifierExpr expr)
    {
        final String id = expr.token().rawText();
        final Symbol sym = top.get(id);

        if (sym != null)
            // The identifier is bounded to a symbol
            // in the lexical scope chain.
            expr.setEvalType(sym.type());
        else
            // The identifier isn't bounded to a symbol
            reporter.error(expr.token(), ErrorMessages.UndefinedId, id);

        return expr.evalType();
    }

    public DataType visit(LetExpr expr)
    {

        if (expr.children().size() == 0)
            // The expression has no bindings or expressions
            // in its body (e.g., (let []) ).
            return expr.evalType();
        else if (expr.children().size() == 1)
        {
            // The expression has one or more bindings but
            // no expressions in its body (e.g., (let [x 42]) ).

            // Cache the previous environment and create
            // a new environment.
            final Env prevEnv = top;
            top = new Env(top);

            // Type check all bindings and store them
            // in an environment.
            ((LetBindings) expr.first()).accept(this);

            // Save the environment in the AST.
            expr.setEnv(top);

            // Restore the previous environment.
            top = prevEnv;

            return expr.evalType();
        }
        else
        {
            // The expression has one or more bindings and
            // one or more expressions in its body
            // (e.g., (let [x 42] (print x)) ).
            
            // Cache the previous environment and create
            // a new environment.
            final Env prevEnv = top;
            top = new Env(top);

            // Type check all bindings and store them
            // in an environment.
            ((LetBindings) expr.first()).accept(this);

            // Type check all expressions in the body
            // and record the types in the AST.
            // (body expressions start at index 1).
            for (int i = 1; i < expr.children().size(); i++)
            {
                final Expr bodyExpr = (Expr) expr.children().get(i);
                bodyExpr.setEvalType(bodyExpr.accept(this));
            }

            // Save the environment in the AST.
            expr.setEnv(top);

            // Restore the previous environment.
            top = prevEnv;

            // The type of this let expression is the same 
            // as the type of its last expression.
            expr.setEvalType(((Expr) expr.last()).evalType());
            return expr.evalType();
        }
    }

    public DataType visit(LetBindings bindings)
    {
        // Call visit(LetBinding) to type check
        // all the bindings in this AST node.
        for (ASTNode n : bindings.children())
            ((LetBinding) n).accept(this);

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
        
        final String id = idExpr.token().rawText();
        final Symbol sym = new Symbol(id, boundedExpr.evalType(),
            SymCategory.VARIABLE);

        if (!top.has(id))
            top.put(id, sym);
        else
            // The identifier id has already been declared in this scope.
            reporter.error(idExpr.token(), 
                ErrorMessages.AlreadyDeclared, id);

        // A LetBinding AST does not evaluate to a 
        // data type as it's not an expression.
        return DataType.UNDEFINED;
    }

    public DataType visit(CompareExpr expr)
    {
        final Expr first = (Expr) expr.first();
        final Expr second = (Expr) expr.second();

        // Type check the operands.
        first.accept(this);
        second.accept(this);

        final Token operator = expr.token();

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
            reporter.error(second.token(), ErrorMessages.UnexpectedOperandToBe, 
                operator.rawText(), DataType.INTEGER, second.evalType());
        }
        else if (onlyFirstIsBool)
        {
            // The first operand is a boolean. Expected the second 
            // operand to also be a boolean.
            reporter.error(second.token(), ErrorMessages.UnexpectedOperandToBe,
                operator.rawText(), DataType.BOOLEAN, second.evalType());
        }
        else if (!(areInts || areBools))
        {
            // Either both operands are not integers or
            // both operands are not booleans.
            reporter.error(first.token(), 
                ErrorMessages.UnexpectedOperandToBeEither, 
                operator.rawText(), DataType.INTEGER, 
                DataType.BOOLEAN, first.evalType());
        }

        return expr.evalType();
    }
}
