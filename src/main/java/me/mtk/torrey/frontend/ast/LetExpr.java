package me.mtk.torrey.frontend.ast;

import java.util.List;
import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.symbols.Env;

public class LetExpr extends Expr
{
  private Env environment;

  public LetExpr(Token letTok, LetBindings bindings, List<Expr> exprList)
  {
    // "let"
    super(letTok, DataType.NIL);

    // The environment doesn't get created until semantic analysis.
    environment = null;

    // (identifier expr)*
    addChild(bindings);

    // expr*
    for (Expr expr : exprList)
      addChild(expr);
  }

  @Override
  public void accept(ASTNodeVisitor visitor)
  {
    visitor.visit(this);
  }

  public void setEnv(Env e)
  {
    environment = e;
  }

  public Env environment()
  {
    return environment;
  }
}
