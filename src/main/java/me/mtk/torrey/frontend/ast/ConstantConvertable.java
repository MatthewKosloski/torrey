package me.mtk.torrey.frontend.ast;

/**
 * If an ASTNode implements this interface,
 * then it can be converted to a constant integer
 * expression.
 */
public interface ConstantConvertable
{
  public int toConstant();
}
