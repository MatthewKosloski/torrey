package me.mtk.torrey.ast;

import java.util.List;

/**
 * Stores zero or more let bindings, where
 * each binding is an identifier expression
 * mapped to an expression.
 */
public class LetBindings extends ASTNode
{
    public LetBindings(List<LetBinding> bindings) 
    {
        super(null);

        for (LetBinding binding : bindings)
            addChild(binding);
    }
}
