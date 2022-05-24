package me.mtk.torrey.frontend.ast;

public interface ASTNodeVisitor
{
  // Root AST node.
  public void visit(Program program);

  // Regular AST nodes. One could argue that these
  // shouldn't be nodes on the AST, but rather fields
  // on the LetExpr node.
  public void visit(LetBinding binding);
  public void visit(LetBindings bindings);

  // Expressions.
  public void visit(ArithmeticExpr expr);
  public void visit(BooleanExpr expr);
  public void visit(CompareExpr expr);
  public void visit(IdentifierExpr expr);
  public void visit(IfExpr expr);
  public void visit(IfThenElseExpr expr);
  public void visit(IntegerExpr expr);
  public void visit(LetExpr expr);
  public void visit(PrintExpr expr);
  public void visit(UnaryExpr expr);
}
