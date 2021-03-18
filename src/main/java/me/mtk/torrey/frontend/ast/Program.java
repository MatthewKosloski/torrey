package me.mtk.torrey.frontend.ast;

import java.util.List;

/**
 * The top-level AST node returned by the parser.
 */
public class Program extends ASTNode
{
    public Program(List<Expr> exprList) 
    {
        super(null);

        for (Expr expr : exprList)
            addChild(expr);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
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
