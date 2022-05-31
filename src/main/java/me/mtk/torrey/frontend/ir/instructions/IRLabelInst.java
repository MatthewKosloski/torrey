package me.mtk.torrey.frontend.ir.instructions;

import java.util.Objects;
import me.mtk.torrey.frontend.ir.addressing.IRAddress;

public final class IRLabelInst extends Quadruple
{
  /**
   * Instantiates a new label instruction.
   *
   * @param label The name of the label.
   */
  public IRLabelInst(IRAddress label)
  {
    super(
      OpType.LABEL,
      requireLabel(Objects.requireNonNull(label)),
      null,
      null);
  }

  /**
   * The string representation of this binary arithmetic instruction.
   *
   * @return The string of this instruction.
   */
  @Override
  public String toString()
  {
    return String.format("%s %s:", opType, arg1);
  }
}
