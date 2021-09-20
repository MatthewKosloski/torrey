package me.mtk.torrey.frontend.ast;

public interface ASTNodeVisitor<T>
{
    // Root AST node.
    public T visit(Program program);

    // Regular AST nodes. One could argue that these
    // shouldn't be nodes on the AST, but rather fields
    // on the LetExpr node.
    public T visit(LetBinding binding);
    public T visit(LetBindings bindings);

    // Expressions.
    public T visit(ArithmeticExpr expr);
    public T visit(BooleanExpr expr);
    public T visit(CompareExpr expr);
    public T visit(IdentifierExpr expr);
    public T visit(IfExpr expr);
    public T visit(IfThenElseExpr expr);
    public T visit(IntegerExpr expr);
    public T visit(LetExpr expr);
    public T visit(PrintExpr expr);
    public T visit(UnaryExpr expr);
}