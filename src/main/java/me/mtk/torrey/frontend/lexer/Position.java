package me.mtk.torrey.frontend.lexer;

public final class Position
{
  // A line number.
  private final int line;

  // A column number.
  private final int col;

  /**
   * Constructs a new position object.
   *
   * @param line A line number.
   * @param col A column number.
   */
  public Position(int line, int col)
  {
    this.line = line;
    this.col = col;
  }

  /**
   * Returns the line number of this position object.
   *
   * @return The line number.
   */
  public int line()
  {
    return this.line;
  }

  /**
   * Returns the column number of this position object.
   *
   * @return The column number.
   */
  public int col()
  {
    return this.col;
  }

  /**
   * Returns a string representation of a Position object.
   *
   * @return The toString representation of this Position.
   */
  @Override
  public String toString()
  {
    return String.format("(%d:%d)", line, col);
  }
}
