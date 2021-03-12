package me.mtk.torrey;

import java.io.IOException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import me.mtk.torrey.x86.X86Backend;
import me.mtk.torrey.ir.IRProgram;

public final class Torrey
{
    // The semantic version number of the compiler.
    public static String SEMANTIC_VERSION = "3.0.0";

    public static void main(String ... args)
    {
        try
        {
            final Torrey torrey = new Torrey();
            final TorreyConfig config = new TorreyConfig();
            final JCommander jcmdr = JCommander.newBuilder()
                .addObject(config)
                .build();
            jcmdr.parse(args);
                
            jcmdr.setProgramName(String.format(
                "java -jar torreyc-%s.jar", 
                SEMANTIC_VERSION));

            if (config.version())
                torrey.showVersionInfo();
    
            if (config.help())
                jcmdr.usage();

            String input = ""; 
            if (System.in.available() != 0)
            {
                // We have bytes that can be read from stdin,
                // so use it as the source of the input program.
                input = TorreyIOUtils.readFromStdin();
            }
            else if (config.inPath() != null)
            {
                // Read the contents from the file at the
                // given path and run the compiler using that
                // as its input.
                input = TorreyIOUtils.read(config.inPath().trim());
            }
            else if (!config.help())
            {
                // No input from stdin was detected, no
                // path to an input file was provided, and
                // the user didn't ask for help, so
                // show compiler usage information.
                jcmdr.usage();
                System.exit(0);
            }

            // The compiler front-end.
            final TorreyFrontEnd fe = new TorreyFrontEnd(config, input);
            final IRProgram irProgram = fe.run();
            
            // The compiler back-end. This can be swapped
            // out with another back-end very easily.
            final X86Backend be = new X86Backend(config, input, irProgram);
            
            // Generate x86 assembly code.
            be.run();

            // Build the object code and link it with the x86 assembly code,
            // producing an executable.
            be.assemble();
        }
        catch (IOException e)
        {
            System.err.println("Torrey: An error occurred while reading"
                + " from the standard input stream.");
            System.exit(1);
        }
        catch (ParameterException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Prints compiler version information to the standard output stream.
     */
    public void showVersionInfo()
    {
        System.out.format("torreyc %s\n", SEMANTIC_VERSION);
        System.out.println("This is free and open source software available at:");
        System.out.println("https://github.com/MatthewKosloski/torrey/");
        System.exit(0);
    }
}
