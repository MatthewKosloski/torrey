package me.mtk.torrey.frontend.ir.instructions;

import java.util.Objects;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;

/**
 * Represents a simple "x = y" copy instruction,
 * where the contents at address y are stored in
 * the temp address x.
 */
public final class IRCopyInst extends Quadruple
{
  /**
   * Instantiates a new copy IR instruction.
   *
   * @param lhs The left-hand side of the copy, that is,
   * the destination. This must be a temporary address.
   * @param rhs The right-hand side of the copy, that is,
   * the source.
   */
  public IRCopyInst(IRAddress lhs, IRAddress rhs)
  {
    super(
      OpType.COPY,
      Objects.requireNonNull(rhs),
      null,
      requireTemp(Objects.requireNonNull(lhs)));
  }

  @Override
  public String toString()
  {
    return String.format("%s %s %s", result, opType, arg1);
  }
}
