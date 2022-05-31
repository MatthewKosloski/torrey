package me.mtk.torrey.frontend.ir.instructions;

import java.util.Objects;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;

public final class IRGotoInst extends Quadruple
{
  /**
   * Instantiates a new goto instruction.
   *
   * @param label The name of the label.
   */
  public IRGotoInst(IRAddress label)
  {
    super(
      OpType.GOTO,
      null,
      null,
      requireLabel(Objects.requireNonNull(label)));
  }

  /**
   * The string representation of this binary arithmetic instruction.
   *
   * @return The string of this instruction.
   */
  @Override
  public String toString()
  {
    return String.format("%s %s", opType, result);
  }
}
