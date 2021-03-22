package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public abstract class Stmt extends ASTNode 
{
    public Stmt(Token t)
    {
        super(t);
    }    
}
