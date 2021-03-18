package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.analysis.DataType;
import me.mtk.torrey.frontend.lexer.Token;

public class UnaryExpr extends Expr implements ConstantConvertable
{
    public UnaryExpr(Token unaryOp, Expr operand) 
    { 
        // "-"
        super(unaryOp, DataType.INTEGER); 

        addChild(operand);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
    
    public int toConstant()
    {
        return Integer.parseInt(first().token().rawText()) * -1;
    }

}
