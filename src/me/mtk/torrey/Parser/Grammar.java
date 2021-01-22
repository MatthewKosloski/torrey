package me.mtk.torrey.Parser;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.Lexer.Token;
import me.mtk.torrey.Lexer.TokenType;
import me.mtk.torrey.AST.ExprNode;
import me.mtk.torrey.AST.IntegerExprNode;
import me.mtk.torrey.AST.PrintExprNode;
import me.mtk.torrey.AST.UnaryExprNode;
import me.mtk.torrey.AST.BinaryExprNode;

/**
 * Translates the context-free grammar to a 
 * LL(k) recursive-descent parser that produces
 * an abstract syntax tree (AST).
 * 
 * For each rule, r, defined in the context-free grammar,
 * we build a method of the same name. For rules with
 * alternatives, we use appropriate control flow.
 */
public class Grammar extends Parser
{
    public Grammar(List<Token> tokens, String input)
    {
        super(tokens, input);
    }
   
    // program -> expression* ;
    public List<ExprNode> program() throws SyntaxError
    {
        final List<ExprNode> exprs = new ArrayList<>();

        while (hasTokens())
            exprs.add(expression());

        return exprs;
    }

    // expression   -> integer
    //              | unary
    //              | binary
    //              | print ;
    public ExprNode expression() throws SyntaxError
    {

        if (peek(TokenType.LPAREN))
        {
            // Predicted the beginning of a fully parenthesized expression.

            if (peekNext(TokenType.PLUS, TokenType.STAR, TokenType.SLASH))
            {
                // expression -> binary ;
                return binary();
            }
            else if (peekNext(TokenType.MINUS))
            {
                // Predicted a binary subtraction or 
                // a unary negation expression.

                // expression -> binary | unary ;
                return binaryOrUnary();
            }
            else if (peekNext(TokenType.PRINT, TokenType.PRINTLN))
            {
                // expression -> print ;
                return print();
            }
            else
            {
                error(ErrorMessages.ExpectedUnaryBinaryPrint, 
                    peek().rawText());
            }
        } 
        else if (peek(TokenType.INTEGER))
        {
            // expression -> integer ;
            return integer();
        }

        error(ErrorMessages.ExpectedIntUnaryBinaryPrint, peek().rawText());

        return null;
    }

    // binary -> "(" binOp expression expression ")" ;
    public BinaryExprNode binary() throws SyntaxError
    {
        // consume "(".
        match(TokenType.LPAREN, ErrorMessages.ExpectedOpeningParen);

        // binOp -> "+" | "*" | "/" ;
        final Token binOp = nextToken();
        
        // parse the two operands
        final ExprNode first = expression();
        final ExprNode second = expression();

        // consume ")".
        match(TokenType.RPAREN, ErrorMessages.ExpectedClosingParen);

        return new BinaryExprNode(binOp, first, second);
    }

    public ExprNode binaryOrUnary() throws SyntaxError
    {
        // consume "(".
        match(TokenType.LPAREN, ErrorMessages.ExpectedOpeningParen);
        
        // "-"
        final Token operator = nextToken();

        // Either the operand to a unary expression
        // or the first operand to a binary expression
        final ExprNode first = expression();

        ExprNode second;
        ExprNode result;
        
        try
        {
            second = expression();
            result = new BinaryExprNode(operator, first, second);
        }
        catch (SyntaxError e)
        {
            // Could not parse a second operand, so
            // we must have a unary expression.
            result = new UnaryExprNode(operator, first);
        }
        finally
        {
            // consume ")".
            match(TokenType.RPAREN, ErrorMessages.ExpectedClosingParen);
        }

        return result;
    }

    // print -> "(" printOp exprlist ")" ;
    public PrintExprNode print() throws SyntaxError
    {
        // consume "(".
        match(TokenType.LPAREN, ErrorMessages.ExpectedOpeningParen);
 
        // printOp -> "print" | "println" ;
        final Token printOp = nextToken();

        final List<ExprNode> exprList = new ArrayList<>();

        // exprlist -> expression+ ;
        do
        {
            exprList.add(expression());
        } while (!peek(TokenType.RPAREN, TokenType.EOF));

        // consume ")".
        match(TokenType.RPAREN, ErrorMessages.ExpectedClosingParen);

        return new PrintExprNode(printOp, exprList);
    }

    // integer -> [0-9]+ ;
    public IntegerExprNode integer()
    {
        return new IntegerExprNode(nextToken());
    }
}
