package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.*;

public final class IRCallInst extends Quadruple
{
  /**
   * Instantiates a procedure call IR instruction,
   * storing the result of the procedure in the
   * specified result address.
   *
   * @param result The temp address at which the result of the
   * procedure is to be stored.
   * @param procName A name address containing the name of
   * the procedure.
   * @param numParams A constant address indicating the number
   * of parameters to the procedure.
   */
  public IRCallInst(IRTempAddress result, IRNameAddress procName,
      IRConstAddress numParams)
  {
    super(OpType.CALL, procName, numParams, result);
  }

  /**
   * Instantiates a procedure call IR instruction
   * without a result address.
   *
   * @param procName An name address containing the name of
   * the procedure.
   * @param numParams A constant address indicating the number
   * of parameters to the procedure.
   */
  public IRCallInst(IRNameAddress procName, IRConstAddress numParams)
  {
    this(null, procName, numParams);
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
