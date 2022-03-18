package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.*;
import me.mtk.torrey.frontend.lexer.TokenType;

public final class IRIfInst extends Quadruple
{
  public IRIfInst(TokenType tokType, IRAddress arg1, IRAddress arg2,
    IRLabelAddress result)
  {
    super(OpType.getInvertedBinaryOpTypeFromTokenType(tokType),
      arg1, arg2, result);
  }

  public IRIfInst(IRConstAddress bool, IRLabelAddress result)
  {
    super(null, bool, null, result);
  }

  /**
   * The string representation of this instruction.
   *
   * @return The string of this instruction.
   */
  public String toString()
  {
    if (arg1 != null && arg2 != null && result != null && opType != null)
      return String.format("if %s %s %s goto %s", arg1,
        opType, arg2, result);
    return String.format("if %s goto %s", arg1, result);
  }
}
