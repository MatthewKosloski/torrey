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
import me.mtk.torrey.ast.IntegerExpr;
import me.mtk.torrey.ast.PrintExpr;
import me.mtk.torrey.ast.UnaryExpr;
import me.mtk.torrey.ast.Expr;
import me.mtk.torrey.ast.Program;
import me.mtk.torrey.ast.LetExpr;
import me.mtk.torrey.ast.LetBinding;
import me.mtk.torrey.ast.LetBindings;
import me.mtk.torrey.symbols.Env;
import me.mtk.torrey.symbols.Symbol;

public final class TypeCheckerVisitor implements ASTNodeVisitor<DataType>
{

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
        }

        // A Program doesn't have a data type,
        // but we must return one so we will
        // return UNDEFINED.
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

        final DataType firstDataType = first.accept(this);
        final DataType secondDataType = second.accept(this);

        final Token operator = expr.token();

        if (firstDataType != DataType.INTEGER && firstDataType != DataType.UNDEFINED)
        {
            // expected type DataType.INTEGER but got firstDataType
            reporter.error(first.token(), ErrorMessages.UnexpectedOperand, 
                operator.rawText(), DataType.INTEGER, firstDataType);
        } 

        if (secondDataType != DataType.INTEGER && secondDataType != DataType.UNDEFINED)
        {
            // expected type DataType.INTEGER but got secondDataType
            reporter.error(second.token(), ErrorMessages.UnexpectedOperand, 
                operator.rawText(), DataType.INTEGER, secondDataType);
        }

        if (firstDataType == DataType.INTEGER && 
            secondDataType == DataType.INTEGER && 
            operator.type() == TokenType.SLASH &&
            Integer.parseInt(second.token().rawText()) == 0)
        {
            // Both operands are integers to a division operator
            // and the denominator is 0.

            reporter.error(second.token(), ErrorMessages.DivisionByZero, 
            operator.rawText());   
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
            final DataType type = childExpr.accept(this);

            if (type != DataType.INTEGER && type != DataType.UNDEFINED)
            {
                // expected type DataType.INTEGER
                reporter.error(childExpr.token(), 
                    ErrorMessages.UnexpectedOperand,
                    operator.rawText(), 
                    DataType.INTEGER, 
                    type);
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
        final Token operator = expr.token();
        final Expr operand = (Expr) expr.first();
        final DataType type = operand.accept(this);

        if (type != DataType.INTEGER && type != DataType.UNDEFINED)
        {
            // expected type DataType.INTEGER
            reporter.error(operand.token(), ErrorMessages.UnexpectedOperand, 
                operator.rawText(), DataType.INTEGER, type);
        } 

        return DataType.INTEGER;
    }

    public DataType visit(IdentifierExpr expr)
    {
        final String id = expr.token().rawText();
        final Symbol sym = top.get(id);

        if (sym != null)
            // The identifier is bounded to a symbol
            // in the lexical scope chain.
            return sym.type();
        else
        {
            // The identifier isn't bounded to a symbol
            reporter.error(expr.token(), ErrorMessages.UndefinedId, id);
            return DataType.UNDEFINED;
        }
    }

    public DataType visit(LetExpr expr)
    {

        if (expr.children().size() == 0)
        {
            // The expression has no bindings or expressions
            // in its body (e.g., (let []) ).
            return DataType.UNDEFINED;
        }
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

            // Restore the previous environment.
            top = prevEnv;

            return DataType.UNDEFINED;
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
            
            // Type check the last expression.
            final DataType lastExprType = ((Expr) expr.last()).accept(this);

            // Restore the previous environment.
            top = prevEnv;

            // The type of this let expression is the same as the type
            // of its last expression.
            return lastExprType;
        }
    }

    public DataType visit(LetBindings bindings)
    {
        // Call visit(LetBinding) to type check
        // all the bindings in this AST node.
        for (ASTNode n : bindings.children())
            ((LetBinding) n).accept(this);

        // A LetBindings AST node has no data type as it's
        // simply a container for bindings.
        return DataType.UNDEFINED;
    }

    public DataType visit(LetBinding binding)
    {
        final LetBinding letBinding = (LetBinding) binding;
        final IdentifierExpr idExpr = (IdentifierExpr) letBinding.first();
        final Expr boundedExpr = (Expr) letBinding.second();

        // The data type of the expression bounded to the identifier
        final DataType type = boundedExpr.accept(this);
        
        final String identifier = idExpr.token().rawText();
        final Symbol sym = new Symbol(identifier, type);
        top.put(identifier, sym);

        return type;
    }
}
