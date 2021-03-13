package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

// TODO: This class is very similar to the BinaryExpr,
// so perhaps we can use inheritance in the future
// to take advantage of the code reuse.
public class CompareExpr extends Expr
{
    public CompareExpr(Token tok, Expr first, Expr second)
    {
        // "==" | "<" | "<=" | ">" | ">=" ;
        super(tok, DataType.BOOLEAN);
        
        addChild(first);
        addChild(second);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}