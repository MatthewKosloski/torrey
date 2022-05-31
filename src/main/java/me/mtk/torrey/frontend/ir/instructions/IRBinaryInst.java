package me.mtk.torrey.frontend.ir.instructions;

import java.util.Objects;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents a binary IR instruction.
 */
public class IRBinaryInst extends Quadruple
{
  /**
   * Instantiates a new binary IR instruction.
   *
   * @param tokType The token type of the Torrey expression from
   * which this IR instruction is derived.
   * @param arg1 The address at which the first operand is located.
   * @param arg2 The address at which the second operand is located.
   * @param result The address at which the result of the operation
   * is to be stored.
   */
  public IRBinaryInst(TokenType tokType, IRAddress arg1, IRAddress arg2,
    IRAddress result)
  {
    super(
      OpType.getBinaryOpTypeFromTokenType(tokType),
      Objects.requireNonNull(arg1),
      Objects.requireNonNull(arg2),
      requireTemp(Objects.requireNonNull(result)));
  }

  /**
   * The string representation of this binary instruction.
   *
   * @return The string of this instruction.
   */
  @Override
  public String toString()
  {
    return String.format("%s = %s %s %s", result, arg1, opType, arg2);
  }
}
