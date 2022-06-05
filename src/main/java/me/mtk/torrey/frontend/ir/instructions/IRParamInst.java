package me.mtk.torrey.frontend.ir.instructions;

import java.util.Objects;
import me.mtk.torrey.frontend.ir.addressing.IRAddress;

/**
 * Represents a parameter IR instruction.
 */
public final class IRParamInst extends Quadruple
{
  /**
   * Instantiates a new parameter instruction.
   *
   * @param addr The address at which the value of the parameter is located.
   */
  public IRParamInst(IRAddress addr)
  {
    super(OpType.PARAM, Objects.requireNonNull(addr), null, null);
  }

  /**
   * The string representation of this binary arithmetic instruction.
   *
   * @return The string of this instruction.
   */
  @Override
  public String toString()
  {
    return String.format("%s %s", opType, arg1);
  }
}
