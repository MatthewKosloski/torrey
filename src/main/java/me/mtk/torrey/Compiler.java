package me.mtk.torrey;

import java.io.IOException;

public abstract class Compiler
{
  // The config file that holds the command-line arguments.
  protected TorreyConfig config;

  // The input program.
  protected String input;

  /**
   * Sets the config.
   *
   * @param config A config.
   */
  public void setConfig(TorreyConfig config)
  {
    this.config = config;
  }

  /**
   * Sets the input program.
   *
   * @param input The input program.
   */
  public void setInput(String input)
  {
    this.input = input;
  }

  /**
   * Sends the string to standard output unless the user specifies
   * otherwise via a command-line argument.
   *
   * @param str The string to be sent to standard ouput.
   */
  protected void stdout(String str)
  {
    if (!config.noStdOut())
      System.out.println(str);
  }

  /**
   * Sends the given string to the standard output stream
   * if and only if the compiler's debug flag is provided.
   *
   * @param str A string.
   */
  protected void debug(String str)
  {
    if (config.debug())
      stdout(String.format("\nDEBUG :: %s\n", str));
  }

  /**
   * Applies the given arguments to the format string, sending
   * the resulting string to the standard output stream if
   * and only if the compiler's debug flag is provided.
   *
   * @param formatString A format string.
   * @param args Arguments referenced by the format specifiers
   * in the given format string.
   */
  protected void debug(String formatString, Object... args)
  {
    debug(String.format(formatString, args));
  }


  /*
    * Writes the given string to either an output file or
    * to the standard output stream (depending on what the
    * user specified via the command-line arguments). If
    * an IOException is encountered while attempting to
    * write to the file system, then an error message is sent
    * to the standard error stream and the output string is
    * sent to the standard output stream. Exits the JVM,
    * indicating abnormal process termination.
    *
    * @param outStr The string to be outputted.
    */
  protected void writeAndExit(String outStr)
  {
    if (hasOutFile())
    {
      try
      {
        TorreyIOUtils.write(outStr.toString(),
          config.outFileName());
      }
      catch (IOException e)
      {
        // Couldn't write to the file, so just
        // send it to stdout.
        System.err.println(String.format("Encountered an I/O"
          + " error while attempting to write the following"
          + " text to file '%s':", config.outFileName()));
          stdout(outStr.toString());
      }
    }
    else
      stdout(outStr.toString());

    // Exit, indicating abnormal termination.
    System.exit(0);
  }

  /**
   * Indicates whether the user specified a name of an output file.
   *
   * @return True if the user specified the name of an output file;
   * False otherwise.
   */
  protected boolean hasOutFile()
  {
    final String fname = config.outFileName();

    if (fname != null && fname.trim().length() > 0)
      return true;

    return false;
  }
}
