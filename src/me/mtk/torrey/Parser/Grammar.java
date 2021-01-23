package me.mtk.torrey.Parser;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.Lexer.Token;
import me.mtk.torrey.Lexer.TokenType;
import me.mtk.torrey.AST.Expr;
import me.mtk.torrey.AST.IntegerExpr;
import me.mtk.torrey.AST.PrintExpr;
import me.mtk.torrey.AST.UnaryExpr;
import me.mtk.torrey.AST.BinaryExpr;
import me.mtk.torrey.AST.Program;

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
    public Program program() throws SyntaxError
    {
        final List<Expr> exprs = new ArrayList<>();

        while (hasTokens())
            exprs.add(expression());

        return new Program(exprs);
    }

    // expression   -> integer
    //              | unary
    //              | binary
    //              | print ;
    public Expr expression() throws SyntaxError
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
    public BinaryExpr binary() throws SyntaxError
    {
        consumeLeftParen();

        // binOp -> "+" | "*" | "/" ;
        final Token binOp = nextToken();
        
        // parse the two operands
        final Expr first = expression();
        final Expr second = expression();

        consumeRightParen();

        return new BinaryExpr(binOp, first, second);
    }

    public Expr binaryOrUnary() throws SyntaxError
    {
        consumeLeftParen();
        
        // "-"
        final Token operator = nextToken();

        // Either the operand to a unary expression
        // or the first operand to a binary expression
        final Expr first = expression();

        Expr second;
        Expr result;
        
        try
        {
            second = expression();
            result = new BinaryExpr(operator, first, second);
        }
        catch (SyntaxError e)
        {
            // Could not parse a second operand, so
            // we must have a unary expression.
            result = new UnaryExpr(operator, first);
        }
        finally
        {
            consumeRightParen();
        }

        return result;
    }

    // print -> "(" printOp exprlist ")" ;
    public PrintExpr print() throws SyntaxError
    {
        consumeLeftParen();
 
        // printOp -> "print" | "println" ;
        final Token printOp = nextToken();

        final List<Expr> exprList = new ArrayList<>();

        // exprlist -> expression+ ;
        do
        {
            exprList.add(expression());
        } while (!peek(TokenType.RPAREN, TokenType.EOF));

        consumeRightParen();

        return new PrintExpr(printOp, exprList);
    }

    // integer -> [0-9]+ ;
    public IntegerExpr integer()
    {
        return new IntegerExpr(nextToken());
    }

    private void consumeLeftParen() throws SyntaxError
    {
        if (!match(TokenType.LPAREN))
            error(peek(), ErrorMessages.ExpectedOpeningParen);
    }

    private void consumeRightParen() throws SyntaxError
    {
        if (!match(TokenType.RPAREN))
            error(lookahead(0), ErrorMessages.ExpectedClosingParen);
    }
}
