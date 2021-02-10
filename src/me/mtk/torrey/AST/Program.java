package me.mtk.torrey.AST;

import java.util.List;
import me.mtk.torrey.ErrorReporter.SemanticError;
import me.mtk.torrey.Lexer.Token;

/**
 * The top-level AST node returned by the parser.
 */
public class Program extends ASTNode
{
    public Program(List<Expr> exprList) 
    {
        // Create "empty" token to satisfy inheritance requirement.
        super(new Token());

        for (Expr expr : exprList)
            addChild(expr);
    }

    public <T> T accept(ProgramVisitor<T> visitor) throws SemanticError
    {
        return visitor.visit(this);
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < children().size(); i++)
            sb.append(children().get(i).toString())
                .append(i == children().size() - 1 ? "" : "\n");

        return sb.toString();
    }
}
