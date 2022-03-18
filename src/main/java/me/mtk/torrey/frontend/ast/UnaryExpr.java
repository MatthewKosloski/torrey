package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public final class UnaryExpr extends Expr implements ConstantConvertable, Foldable
{

  // The expression that this expression reduces to.
  private Expr fold;

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
    String rawText;

    if (first() instanceof Foldable)
      rawText = ((Foldable) first()).getFold().token().rawText();
    else
      rawText = first().token().rawText();

    return Integer.parseInt(rawText) * -1;
  }

  public void setFold(Expr fold)
  {
    // If the fold is equal to ourselves, then don't
    // assign it as there's no point. Also, only assign
    // the fold to us if we don't already have one (this
    // prevents the overriding of folds).
    if (!fold.equals(this) && this.fold == null)
      this.fold = fold;
  }

  public Expr getFold()
  {
    return fold;
  }

}
