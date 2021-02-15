package me.mtk.torrey.analysis;

import me.mtk.torrey.error_reporter.ErrorMessages;
import me.mtk.torrey.error_reporter.ErrorReporter;
import me.mtk.torrey.error_reporter.SemanticError;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.ast.ExprVisitor;
import me.mtk.torrey.ast.ASTNode;
import me.mtk.torrey.ast.BinaryExpr;
import me.mtk.torrey.ast.IntegerExpr;
import me.mtk.torrey.ast.PrintExpr;
import me.mtk.torrey.ast.UnaryExpr;
import me.mtk.torrey.ast.Expr;
import me.mtk.torrey.ast.Program;
import me.mtk.torrey.ast.ProgramVisitor;

public final class TypeCheckerVisitor implements 
    ExprVisitor<DataType>, ProgramVisitor<DataType>
{

    private ErrorReporter reporter;

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
    }

    /**
     * Type checks the entire program, visiting every
     * child node.
     * 
     * @param program The program to be type checked.
     * @return The DataType of the program.
     */
    public DataType visit(Program program) throws SemanticError
    {
        for (ASTNode child : program.children())
            ((Expr) child).accept(this);

        reporter.reportSemanticErrors("Encountered one or more semantic"
            + " errors during type checking:");

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

        if (firstDataType != DataType.INTEGER)
        {
            // expected type DataType.INTEGER but got firstDataType
            reporter.error(first.token(), ErrorMessages.UnexpectedOperand, 
                operator.rawText(), DataType.INTEGER, firstDataType);
        } 

        if (secondDataType != DataType.INTEGER)
        {
            // expected type DataType.INTEGER but got secondDataType
            reporter.error(second.token(), ErrorMessages.UnexpectedOperand, 
                operator.rawText(), DataType.INTEGER, secondDataType);
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
            final DataType operandDataType = childExpr.accept(this);

            if (operandDataType != DataType.INTEGER)
            {
                // expected type DataType.INTEGER but got operandDataType
                reporter.error(childExpr.token(), ErrorMessages.UnexpectedOperand,
                    operator.rawText(), DataType.INTEGER, operandDataType);
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
        final DataType operandDataType = operand.accept(this);

        if (operandDataType != DataType.INTEGER)
        {
            // expected type DataType.INTEGER but got operandDataType
            reporter.error(operand.token(), ErrorMessages.UnexpectedOperand, 
                operator.rawText(), DataType.INTEGER, operandDataType);
        } 

        return DataType.INTEGER;
    }
}
