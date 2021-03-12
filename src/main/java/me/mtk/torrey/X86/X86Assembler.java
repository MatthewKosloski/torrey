package me.mtk.torrey.x86;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import me.mtk.torrey.TorreyCompiler;
import me.mtk.torrey.TorreyConfig;
import me.mtk.torrey.TorreyIOUtils;

/**
 * Uses the gcc process to build an executable
 * by first building the dependent run-time object
 * code and linking it with the generated x86-64
 * assembly.
 */
public final class X86Assembler extends TorreyCompiler
{
    // If the user does not provide an output file name for the 
    // executable via the command-line, use this as a default.
    private static String DEFAULT_EXECUTABLE_NAME = "a.out";

    // If the user does not provide an output file name for
    // the assembly output, use this as a default.
    private static String DEFAULT_SOURCE_ASM_NAME = "temp.s";

    // The name of the run time dependency.
    private static String RUNTIME_CCODE_NAME = "runtime.c";

    // The name to be given to the run time object code.
    private static String RUNTIME_OCODE_NAME = "runtime.o";

    // The name of gcc on the PATH.
    private static String GCC_PATH_NAME = "gcc";

    private X86Program x86;

    public X86Assembler(TorreyConfig config, String input, 
        X86Program x86Program)
    {
        super(config, input);
        x86 = x86Program;
    }

    /**
     * Runs the assembler.
     */
    public Void run()
    {
        debug("Starting assembly and linking");
        buildRunTime();
        writeAsm();
        buildExec();

        if (!config.keepSource())
        {
            try
            {
                // Delete the previously written assembly source 
                // file from disk.
                Files.delete(Paths.get(DEFAULT_SOURCE_ASM_NAME));

                debug("Deleted asm file '%s' from current directory.", 
                    DEFAULT_SOURCE_ASM_NAME);
            }
            catch(IOException e)
            {
                System.err.format("Encountered an I/O error while"
                    + " attempting to delete file '%s' from the"
                    + " current directory.", DEFAULT_SOURCE_ASM_NAME);
            }
        }

        return null;
    }

    /*
     * Builds the run-time object code to be linked
     * with the assembly. 
     */
    private void buildRunTime()
    {
        debug("Building run-time object code by running:\n\t%s %s %s",
            GCC_PATH_NAME, "-c", RUNTIME_CCODE_NAME);

        try
        {
            // Build the run-time object code.
            new ProcessBuilder(
                GCC_PATH_NAME, 
                "-c",
                RUNTIME_CCODE_NAME)
                .start()
                // Wait for the gcc process to terminate 
                // before exiting this method.
                .waitFor();
        }
        catch(IOException e)
        {
            System.err.format("gcc encountered an I/O error while"
                + " building the run-time object code from '%s'.", 
                RUNTIME_CCODE_NAME);
        }
        catch(InterruptedException e)
        {
            System.err.format("The gcc process was interrupted"
                + " while building the run-time object code from '%s'.",
                RUNTIME_CCODE_NAME);
        }
    }

    /*
     * Writes the x86-64 program to the file system.
     */
    private void writeAsm()
    {
        try
        {
            TorreyIOUtils.write(x86.toString(), DEFAULT_SOURCE_ASM_NAME);
            debug("Wrote assembly code to file '%s' in current directory", 
                DEFAULT_SOURCE_ASM_NAME);
        }
        catch(IOException e)
        {
            System.err.format("Encountered an I/O error while attempting"
                + " to write to file '%s'.", DEFAULT_SOURCE_ASM_NAME);
        }
    }

    /*
     * Build the executable.
     */
    private void buildExec()
    {
        final String outFileName = hasOutFile() 
            ? config.outFileName() 
            : DEFAULT_EXECUTABLE_NAME;

        debug("Building an executable by running:\n\t%s %s %s %s %s",
            GCC_PATH_NAME, DEFAULT_SOURCE_ASM_NAME, RUNTIME_OCODE_NAME, 
            "-o", outFileName);

        try
        {
            new ProcessBuilder(
                GCC_PATH_NAME, 
                DEFAULT_SOURCE_ASM_NAME, 
                RUNTIME_OCODE_NAME,
                "-o",
                outFileName)
                .start()
                // Wait for the gcc process to terminate 
                // before exiting this method.
                .waitFor();
    
            debug("Built executable '%s' and saved it to"
                + " current directory.", outFileName);
        }
        catch(IOException e)
        {
            System.err.format("gcc encountered an I/O error while"
                + " building the executable '%s'.", 
                outFileName);
        }
        catch(InterruptedException e)
        {
            System.err.format("The gcc process was interrupted"
                + " while executable file '%s'.",
                outFileName);
        }
    }
}