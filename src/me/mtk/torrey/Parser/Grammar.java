package me.mtk.torrey.Parser;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.Lexer.Token;
import me.mtk.torrey.Lexer.TokenType;
import me.mtk.torrey.AST.ExprNode;
import me.mtk.torrey.AST.IntegerExprNode;
import me.mtk.torrey.AST.PrintExprNode;
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
    public Grammar(List<Token> tokens)
    {
        super(tokens);
    }
   
    // program -> expression* ;
    public List<ExprNode> program()
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
    public ExprNode expression()
    {

        if (peek(TokenType.LPAREN))
        {
            // Predicted the beginning of a fully parenthesized expression.

            if (peekNext(TokenType.PLUS, TokenType.MINUS, 
            TokenType.STAR, TokenType.SLASH))
            {
                // expression -> binary | unary ;
                return binary();
            }
            else if (peekNext(TokenType.PRINT, TokenType.PRINTLN))
            {
                // expression -> print ;
                return print();
            }
        } else if (peek(TokenType.INTEGER))
        {
            // expression -> integer ;
            return integer();
        }

        final String err = String.format("Expected an integer, unary, binary, "
            + "or print expression but found %s instead.", peek().type());
        throw new Error(err);
    }

    // binary -> "(" binOp expression expression ")" ;
    public BinaryExprNode binary()
    {
        // consume "(".
        match(TokenType.LPAREN);
        
        // binOp -> "+" | "-" | "*" | "/" ;
        final Token binOp = nextToken();

        // build the operand subtrees.
        final ExprNode first = expression();
        final ExprNode second = expression();

        // consume ")".
        match(TokenType.RPAREN);

        return new BinaryExprNode(binOp, first, second);
    }

    // print -> "(" printOp exprlist ")" ;
    public PrintExprNode print()
    {
        // consume "(".
        match(TokenType.LPAREN);
 
        // printOp -> "print" | "println" ;
        final Token printOp = nextToken();

        final List<ExprNode> exprList = new ArrayList<>();

        do
        {
            exprList.add(expression());
        } while (!peek(TokenType.RPAREN, TokenType.EOF));

        // consume ")".
        match(TokenType.RPAREN);

        return new PrintExprNode(printOp, exprList);
    }

    // integer -> [0-9]+ ;
    public IntegerExprNode integer()
    {
        return new IntegerExprNode(nextToken());
    }
}
