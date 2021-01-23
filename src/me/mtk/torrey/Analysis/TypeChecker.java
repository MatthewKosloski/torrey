package me.mtk.torrey.Analysis;

import java.util.List;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.AST.ASTNode;
import me.mtk.torrey.AST.Expr;

public class TypeChecker {
    
    private Program unchecked;

    public TypeChecker(Program unchecked)
    {
        this.unchecked = unchecked;
    }

    public void check()
    {
        final TypeCheckerVisitor visitor = new TypeCheckerVisitor();
        visitor.visit(unchecked);
    }
}
