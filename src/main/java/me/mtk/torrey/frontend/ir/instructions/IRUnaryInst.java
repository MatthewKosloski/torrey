package me.mtk.torrey.frontend.ir.instructions;

import java.util.Objects;
import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents a unary IR instruction.
 */
public class IRUnaryInst extends Quadruple
{
  /**
   * Instantiates a new unary IR instruction.
   *
   * @param tokType The token type of the Torrey expression from
   * which this IR instruction is derived.
   * @param arg The address at which the operand is located.
   * @param result The temp address at which the result of the operation
   * is to be stored.
   */
  public IRUnaryInst(TokenType tokType, IRAddress arg, IRAddress result)
  {
    super(
      OpType.getUnaryOpTypeFromTokenType(Objects.requireNonNull(tokType)),
      Objects.requireNonNull(arg),
      null,
      requireTemp(Objects.requireNonNull(result)));
  }

  /**
   * The string representation of this unary instruction.
   *
   * @return The string of this instruction.
   */
  @Override
  public String toString()
  {
    return String.format("%s = %s %s", result, opType, arg1);
  }
}
