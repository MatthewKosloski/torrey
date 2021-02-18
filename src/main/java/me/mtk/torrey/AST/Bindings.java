package me.mtk.torrey.ast;

import java.util.List;

/**
 * Stores zero or more let bindings, where
 * each binding is an identifier expression
 * mapped to an expression.
 */
public class Bindings extends ASTNode
{
    public Bindings(List<Binding> bindings) 
    {
        super(null);

        for (Binding binding : bindings)
            addChild(binding);
    }
}
