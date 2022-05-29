package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRLabelAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents an intermediate language instruction.
 */
public abstract class Quadruple
{

  public enum OpType
  {
    // IR binary instruction operators
    ADD ("+"),
    SUB ("-"),
    MULT ("*"),
    DIV ("/"),
    EQUAL ("=="),
    NEQUAL ("!="),
    LT ("<"),
    LTE ("<="),
    GT (">"),
    GTE (">="),

    // IR call instruction operator
    CALL ("call"),

    // IR copy instruction operator
    COPY ("="),

    // IR goto instruction operator
    GOTO ("goto"),

    // IR label instruction operator
    LABEL ("label"),

    // IR param instruction operator
    PARAM ("param"),

    // IR unary instruction operator
    MINUS ("-");

    private final String terminalSymbol;

    private OpType(String terminalSymbol)
    {
      this.terminalSymbol = terminalSymbol;
    }

    /**
     * Gets the binary IR operator type corresponding
     * to the given Torrey token type.
     *
     * @param tokType A Torrey token type.
     * @return The corresponding binary IR operator type.
     */
    public static OpType getBinaryOpTypeFromTokenType(TokenType tokType)
    {
      switch (tokType)
      {
          case PLUS: return OpType.ADD;
          case MINUS: return OpType.SUB;
          case STAR: return OpType.MULT;
          case SLASH: return OpType.DIV;
          case EQUAL: return OpType.EQUAL;
          case NOT: return OpType.NEQUAL;
          case LT: return OpType.LT;
          case LTE: return OpType.LTE;
          case GT: return OpType.GT;
          case GTE: return OpType.GTE;
          default:
            throw new Error(String.format(
              "Unexpected token type %s", tokType));
      }
    }

    /**
     * Gets the IR unary operator type corresponding
     * to the given Torrey token type.
     *
     * @param tokType A Torrey token type.
     * @return The corresponding unary IR operator type.
     */
    public static OpType getUnaryOpTypeFromTokenType(TokenType tokType)
    {
      switch (tokType)
      {
        case MINUS: return OpType.MINUS;
        default:
          throw new Error(String.format(
            "Unexpected token type %s", tokType));
      }
    }

    /**
     * Returns the inverse of the binary IR operator type corresponding
     * to the given Torrey token type.
     *
     * @param tokType A Torrey token type.
     * @return The inverse IR operator type.
     */
    public static OpType getInvertedBinaryOpTypeFromTokenType(TokenType tokenType)
    {
      return getInvertedBinaryOpType(getBinaryOpTypeFromTokenType(tokenType));
    }

    /**
     * Returns the inverse of the given binary IR operator type.
     *
     * @param opType A binary IR operator type.
     * @return The inverse IR operator type.
     */
    public static OpType getInvertedBinaryOpType(OpType opType)
    {
      switch (opType)
      {
        case ADD: return OpType.SUB;
        case SUB: return OpType.ADD;
        case MULT: return OpType.DIV;
        case DIV: return OpType.MULT;
        case EQUAL: return OpType.NEQUAL;
        case NEQUAL: return OpType.EQUAL;
        case LT: return OpType.GTE;
        case LTE: return OpType.GT;
        case GT: return OpType.LTE;
        case GTE: return OpType.LT;
        default:
          throw new Error(String.format(
            "Unexpected op type %s", opType));
      }
    }

    @Override
    public String toString()
    {
      return terminalSymbol;
    }
  }

  // The operator type of the instruction.
  OpType opType;

  // The first argument of the instruction.
  IRAddress arg1;

  // The second argument of the instruction.
  IRAddress arg2;

  // The address at which the result of the instruction
  // is to be stored.
  IRAddress result;

  /**
   * Instantiates a new quadruple to hold the properties
   * of an intermediate language instruction.
   *
   * @param opType The operator type of the instruction.
   * @param arg1 The address at which the first argument is located.
   * @param arg2 The address at which the second argument is located.
   * @param result A temporary address to store the result of the
   * instruction.
   */
  public Quadruple(OpType opType, IRAddress arg1, IRAddress arg2, IRTempAddress result)
  {
    this.opType = opType;
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.result = result;
  }

  public Quadruple(OpType opType, IRAddress arg1, IRAddress arg2, IRLabelAddress result)
  {
    this.opType = opType;
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.result = result;
  }

  public Quadruple(OpType opType, IRAddress arg)
  {
    this.opType = opType;
    this.arg1 = arg;
    this.arg2 = null;
    this.result = null;
  }

  public OpType opType()
  {
    return opType;
  }

  public IRAddress arg1()
  {
    return arg1;
  }

  public IRAddress arg2()
  {
    return arg2;
  }

  public IRAddress result()
  {
    return result;
  }

  public void setResult(IRAddress newResult)
  {
    result = newResult;
  }
}
