package me.mtk.torrey;

import java.io.IOException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import me.mtk.torrey.targets.x86_64.pc.linux.X86Backend;
import me.mtk.torrey.ir.IRProgram;
import me.mtk.torrey.targets.Targets;

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

            if (config.targetList())
                torrey.showRegisteredTargets();
    
            if (config.help())
                jcmdr.usage();

            if (config.target() == null)
            {
                throw new Error(String.format("%s is not a registered target."
                    + " To view the registered targets, supply the"
                    + " '--target-list' flag.\n", config.target()));
            }

            // Check stdin first.
            String input = TorreyIOUtils.readFromStdin();

            if (input == null && config.inPath() != null)
            {
                // No bytes were read from stdin and the user
                // supplied a path to a file on the file system,
                // so try to read from that file.
                input = TorreyIOUtils.read(config.inPath());
            }
            else if (input == null && !config.help())
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

    public void showRegisteredTargets()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("Usage: --target=<triple>,")
            .append("\n\twhere <triple> is of the form ")
            .append("<arch>-<vendor>-<sys>.\n");
        sb.append("Registered targets (triples):\n");
        Targets.registry.forEach((k, v) -> sb.append("\t").append(k));
        System.out.println(sb.toString());
        System.exit(0);
    }
}
