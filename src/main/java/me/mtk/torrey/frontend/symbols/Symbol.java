package me.mtk.torrey.frontend.symbols;

import me.mtk.torrey.frontend.ast.Expr;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;

public class Symbol
{
  private String name;
  private SymCategory category;
  private IRTempAddress address;
  private Expr expr;

  public Symbol(String name, SymCategory category, Expr expr)
  {
    this.name = name;
    this.category = category;
    this.expr = expr;
  }

  public String name()
  {
    return name;
  }

  public SymCategory category()
  {
    return category;
  }

  public Expr expr()
  {
    return expr;
  }

  public void setAddress(IRTempAddress address)
  {
    this.address = address;
  }

  public IRTempAddress address()
  {
    return address;
  }
}
