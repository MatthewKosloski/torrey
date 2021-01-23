package me.mtk.torrey.AST;

import java.util.List;

import me.mtk.torrey.Lexer.TokenType;
import me.mtk.torrey.Lexer.Token;

/**
 * The top-level AST node returned by the parser.
 */
public class Program extends ASTNode
{
    public Program(List<Expr> exprList) 
    {
        // Create "empty" token to satisfy inheritance requirement.
        super(new Token(TokenType.UNIDENTIFIED, "", -1, -1, -1, null, null));

        for (Expr expr : exprList)
            addChild(expr);
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        for (ASTNode child : children())
        {
            sb.append("\n");
            sb.append(child.toString());
        }

        return sb.toString();
    }
}
