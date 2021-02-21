package me.mtk.torrey.ast;

import me.mtk.torrey.ir.TempAddress;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.lexer.TokenType;

public class IntegerExpr extends Expr implements ConstantConvertable
{
    public IntegerExpr(Token t) 
    { 
        super(t); 
    }

    public IntegerExpr(int constant)
    {
        super(new Token(TokenType.INTEGER, constant + ""));
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor)
    {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(ASTNodeIRVisitor<T> visitor, TempAddress result)
    {
        return visitor.visit(this, result);
    }

    public int toConstant()
    {
        return Integer.parseInt(token().rawText());
    }
}
