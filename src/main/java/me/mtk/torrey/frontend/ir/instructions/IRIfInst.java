package me.mtk.torrey.frontend.ir.instructions;

import java.util.Objects;
import me.mtk.torrey.frontend.ir.addressing.*;
import me.mtk.torrey.frontend.lexer.TokenType;

public final class IRIfInst extends Quadruple
{
  public IRIfInst(TokenType tokType, IRAddress arg1, IRAddress arg2,
    IRAddress result)
  {
    super(
      OpType.getInvertedBinaryOpTypeFromTokenType(tokType),
      Objects.requireNonNull(arg1),
      Objects.requireNonNull(arg2),
      requireLabel(Objects.requireNonNull(result)));
  }

  /**
   * The string representation of this instruction.
   *
   * @return The string of this instruction.
   */
  @Override
  public String toString()
  {
    return String.format("if %s %s %s goto %s", arg1, opType, arg2, result);
  }
}
