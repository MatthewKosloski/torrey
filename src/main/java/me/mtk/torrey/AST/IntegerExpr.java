package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.lexer.TokenType;

public class IntegerExpr extends Expr implements ConstantConvertable
{
    public IntegerExpr(Token t)
    {
        super(t, DataType.INTEGER);
    }

    public IntegerExpr(int constant)
    {
        this(new Token(TokenType.INTEGER, constant + ""));
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }

    public int toConstant()
    {
        return Integer.parseInt(token().rawText());
    }
}
