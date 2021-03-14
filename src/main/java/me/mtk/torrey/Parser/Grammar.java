package me.mtk.torrey.parser;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.ast.Expr;
import me.mtk.torrey.ast.IdentifierExpr;
import me.mtk.torrey.ast.IntegerExpr;
import me.mtk.torrey.ast.LetExpr;
import me.mtk.torrey.ast.PrintExpr;
import me.mtk.torrey.ast.UnaryExpr;
import me.mtk.torrey.ast.ArithmeticExpr;
import me.mtk.torrey.ast.BinaryExpr;
import me.mtk.torrey.ast.CompareExpr;
import me.mtk.torrey.ast.LetBindings;
import me.mtk.torrey.ast.LetBinding;
import me.mtk.torrey.ast.Program;
import me.mtk.torrey.error_reporter.ErrorReporter;
import me.mtk.torrey.error_reporter.SyntaxError;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.lexer.TokenType;
import me.mtk.torrey.error_reporter.ErrorMessages;

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
    public Grammar(ErrorReporter reporter,  List<Token> tokens)
    {
        super(reporter, tokens);
    }
   
    // program -> expression* ;
    protected Program program()
    {
        final List<Expr> exprs = new ArrayList<>();

        while (hasTokens())
        {
            try
            {
                exprs.add(expression());
            }
            catch (SyntaxError e)
            {
                synchronize();
            }
        }

        return new Program(exprs);
    }

    // expression   -> integer
    //              | identifier
    //              | unary
    //              | binary
    //              | print 
    //              | let 
    //              | compare ;
    private Expr expression() throws SyntaxError
    {

        if (peek(TokenType.LPAREN))
        {
            // Predicted the beginning of a fully parenthesized expression.

            if (peekNext(TokenType.PLUS, TokenType.STAR, TokenType.SLASH, 
            TokenType.EQUAL, TokenType.LT, TokenType.LTE, TokenType.GT, 
            TokenType.GTE))
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
            else if (peekNext(TokenType.LET))
            {
                // expression -> let ;
                return let();
            }
            else
            {
                reporter.throwSyntaxError(peekNext(), 
                    ErrorMessages.ExpectedUnaryBinaryPrint,
                    peekNext().rawText());
            }
        } 
        else if (peek(TokenType.INTEGER))
        {
            // expression -> integer ;
            return integer();
        }
        else if (peek(TokenType.IDENTIFIER))
        {
            // expression -> identifier ;
            return identifier();
        }

        reporter.throwSyntaxError(peek(), ErrorMessages.ExpectedExpr,
            peek().rawText());
        
        return null;
    }

    private Expr binaryOrUnary() throws SyntaxError
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
            result = new ArithmeticExpr(operator, first, second);
        }
        catch (SyntaxError e)
        {
            // Could not parse a second operand, so
            // we must have a unary expression.
            result = new UnaryExpr(operator, first);

            // don't report this syntax error
            reporter.pop();
        }
        finally
        {
            consumeRightParen();
        }

        return result;
    }

    // print -> "(" printOp exprlist ")" ;
    private PrintExpr print() throws SyntaxError
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

    // let -> "(" "let" "[" (identifier expr)* "]" expr* ")" ;
    private LetExpr let() throws SyntaxError
    {
        // "("
        consumeLeftParen();
 
        // "let"
        final Token tok = nextToken();

        // "["
        consumeLeftBracket();

        final List<LetBinding> bindings = new ArrayList<>();

        if (!peek(TokenType.RBRACK))
        {
            // The next token is not a closing bracket,
            // so try to parse (identifier expr)*.

            while (!peek(TokenType.RBRACK, TokenType.EOF))
            {
                // Initialize the identifier with an empty token
                IdentifierExpr id = new IdentifierExpr(new Token());

                // identifier
                if (!peek(TokenType.IDENTIFIER))
                {
                    reporter.throwSyntaxError(peek(), 
                        ErrorMessages.ExpectedIdentifier,
                        peek().rawText());
                }
                else
                    id = new IdentifierExpr(nextToken());

                final Expr expr = expression();
                bindings.add(new LetBinding(id, expr));
            }
        }

        // "]"
        consumeRightBracket();

        // expr*
        final List<Expr> exprList = new ArrayList<>();
        while (!peek(TokenType.RPAREN, TokenType.EOF))
            exprList.add(expression());

        // ")"
        consumeRightParen();

        return new LetExpr(tok, new LetBindings(bindings), exprList);
    }

    // binary -> "(" ("+" | "-" | "*" | "/" | "==" 
    //        | "<" | "<=" | ">" | ">=") expr expr ")" ;
    private BinaryExpr binary() throws SyntaxError
    {
        // "("
        consumeLeftParen();
 
        // "==" | "<" | ... | ">="
        final Token tok = nextToken();

        // expr
        final Expr first = expression();

        // expr
        final Expr second = expression();

        // ")"
        consumeRightParen();

        if (tok.isType(TokenType.PLUS, TokenType.STAR, TokenType.SLASH))
            return new ArithmeticExpr(tok, first, second);
        else
            return new CompareExpr(tok, first, second);
    }

    // integer -> [0-9]+ ;
    private IntegerExpr integer()
    {
        return new IntegerExpr(nextToken());
    }

    // identifier -> [a-zA-Z_$]+ [a-zA-Z0-9_$!?-]* ;
    private IdentifierExpr identifier()
    {
        return new IdentifierExpr(nextToken());
    }

    private void consumeLeftParen() throws SyntaxError
    {
        if (!match(TokenType.LPAREN))
            reporter.throwSyntaxError(peek(), 
                ErrorMessages.ExpectedOpeningParen);
    }

    private void consumeRightParen() throws SyntaxError
    {
        if (!match(TokenType.RPAREN))
            reporter.throwSyntaxError(lookahead(0), 
                ErrorMessages.ExpectedClosingParen);
    }

    private void consumeLeftBracket() throws SyntaxError
    {
        if (!match(TokenType.LBRACK))
            reporter.throwSyntaxError(peek(), 
                ErrorMessages.ExpectedOpeningBracket);
    }

    private void consumeRightBracket() throws SyntaxError
    {
        if (!match(TokenType.RBRACK))
            reporter.throwSyntaxError(peek(), 
                ErrorMessages.ExpectedClosingBracket);
    }
}
