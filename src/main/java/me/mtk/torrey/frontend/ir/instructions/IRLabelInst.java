package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRLabelAddress;

public final class IRLabelInst extends Quadruple
{
  /**
   * Instantiates a new label instruction.
   *
   * @param label The name of the label.
   */
  public IRLabelInst(IRLabelAddress label)
  {
    super(OpType.LABEL, label);
  }

  /**
   * The string representation of this binary arithmetic instruction.
   *
   * @return The string of this instruction.
   */
  public String toString()
  {
    return String.format("%s %s:", opType, arg1);
  }
}
