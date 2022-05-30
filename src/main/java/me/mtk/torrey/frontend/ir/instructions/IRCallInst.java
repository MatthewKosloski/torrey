package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.*;

public final class IRCallInst extends Quadruple
{
  /**
   * Instantiates a procedure call IR instruction.
   *
   * @param procName An name address containing the name of
   * the procedure.
   * @param numParams A constant address indicating the number
   * of parameters to the procedure.
   */
  public IRCallInst(IRNameAddress procName, IRConstAddress numParams)
  {
    super(OpType.CALL, procName, numParams);
  }

  /**
   * The string respresentation of the call instruction.
   */
  @Override
  public String toString()
  {
    return String.format("%s %s, %s", opType, arg1, arg2);
  }
}
