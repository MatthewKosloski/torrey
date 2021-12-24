package me.mtk.torrey;

import java.io.IOException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import me.mtk.torrey.frontend.ir.gen.IRProgram;
import me.mtk.torrey.frontend.CompilerFrontend;
import me.mtk.torrey.backend.CompilerBackend;
import me.mtk.torrey.backend.CompilerBackendFactory;
import me.mtk.torrey.backend.TargetProgram;
import me.mtk.torrey.backend.TargetRegistry;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.X8664PCLinuxTarget;

public final class Torrey
{
    // The semantic version number of the compiler.
    // WARNING: Do not change the below line as it will break the CI pipeline!
    public static String SEMANTIC_VERSION = "3.1.1";

    private static TargetRegistry targetRegistry;
    private static CompilerBackendFactory backendFactory;

    static
    {
        targetRegistry = new TargetRegistry();

        // Install the compiler backends.
        targetRegistry.add(new X8664PCLinuxTarget());

        // Initialize the backend factory with the target registry.
        backendFactory = new CompilerBackendFactory(targetRegistry);
    }

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
            final CompilerFrontend frontend = new CompilerFrontend();
            frontend.setConfig(new TorreyConfig(config));
            frontend.setInput(input);
            final IRProgram irProgram = frontend.run();
            
            CompilerBackend backend = Torrey.backendFactory
                .makeBackendFromTarget(config.target());

            if (backend == null)
            {
                System.err.format("'%s' is not a registered target."
                + " To view the registered targets, supply the"
                + " '--target-list' flag.\n", config.target());
                System.exit(1);
            }

            backend.setConfig(new TorreyConfig(config));
            backend.setInput(input);
            
            // Generate the target program from the intermediate representation
            // and then (optionally) assemble it into a native executable.
            final TargetProgram targetProgram = backend.generate(irProgram);
            backend.assemble(targetProgram);
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
        System.out.println("The Compiler for the Torrey Programming Language");
        System.out.println("https://github.com/MatthewKosloski/torrey/\n");
        System.out.println("The latest release of the software can be found at:");
        System.out.println("https://github.com/MatthewKosloski/torrey/releases/latest");
        System.exit(0);
    }

    public void showRegisteredTargets()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("Usage: --target <triple>,")
            .append("\n\twhere <triple> is of the form ")
            .append("<arch>-<vendor>-<sys>.\n\n");
        sb.append("Registered targets (triples):\n");
    
        for (String targetStr : Torrey.targetRegistry.getKeys())
            sb.append("\t").append(targetStr);

        System.out.println(sb.toString());
        System.exit(0);
    }
}
