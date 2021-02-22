package me.mtk.torrey;

import java.lang.ProcessBuilder;
import java.lang.Process;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import me.mtk.torrey.error_reporter.ErrorReporter;
import me.mtk.torrey.error_reporter.SemanticError;
import me.mtk.torrey.error_reporter.SyntaxError;
import me.mtk.torrey.lexer.Lexer;
import me.mtk.torrey.lexer.TokenList;
import me.mtk.torrey.parser.Grammar;
import me.mtk.torrey.x86.X86Generator;
import me.mtk.torrey.x86.X86Program;
import me.mtk.torrey.ast.PrettyPrinterVisitor;
import me.mtk.torrey.ast.Program;
import me.mtk.torrey.analysis.ConstantFolderVisitor;
import me.mtk.torrey.analysis.TypeChecker;
import me.mtk.torrey.ir.IRGenVisitor;
import me.mtk.torrey.ir.IRProgram;

public class Torrey 
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

    // The semantic version number of this software.
    private static String SEMANTIC_VERSION = "1.0.0"; 

    @Parameter(
        names = {"--help", "-h"}, 
        description = "Display this information.", 
        help = true,
        order = 0)
    private boolean help;

    @Parameter(
        names = {"--version", "-v"}, 
        description = "Display compiler version information.", 
        order = 1)
    private boolean version = false;

    @Parameter(
        names = {"--debug", "-d"}, 
        description = "Show output from compilation stages.",
        order = 2)
    private boolean debug = false;

    @Parameter(
        names = {"--in", "-i"},
        description = "The path to the input file.", 
        order = 3)
    private String inPath;

    @Parameter(
        names = {"--out", "-o"}, 
        description = "Place the output into a file.",
        order = 4)
    private String outFileName;

    @Parameter(
        names = {"-L"}, 
        description = "Lex only; do not parse, compile, or assemble.",
        order = 5)
    private boolean stopAtLex = false;

    @Parameter(
        names = {"-p"}, 
        description = "Parse only; do not compile or assemble.",
        order = 6)
    private boolean stopAtParse = false;

    @Parameter(
        names = {"-ir"}, 
        description = "Generate intermediate code only; do not compile or assemble.",
        order = 7)
    private boolean stopAtIr = false;

    @Parameter(
        names = {"-S"}, 
        description = "Compile only; do not assemble.",
        order = 8)
    private boolean stopAtCompile = false;

    @Parameter(
        names = {"--keep-source"}, 
        description = "Keep the assembly source file after assembly.",
        order = 9)
    private boolean keepSource = false;

    @Parameter(
        names = {"--no-hl-opt"}, 
        description = "Disable high-level compiler optimizations.",
        order = 10)
    private boolean disableHlOpts = false;
    
    @Parameter(
        names = {"--no-stdout"}, 
        description = "Suppress all output to the standard output stream.",
        order = 11)
    private boolean noStdOut = false;

    public static void main(String ... argv)
    {
        try
        {
            final Torrey torrey = new Torrey();
            final JCommander jct = JCommander.newBuilder()
                .addObject(torrey)
                .build();

            jct.setProgramName(String.format("java -jar torreyc-%s.jar", 
                SEMANTIC_VERSION));
            jct.parse(argv);

            if (torrey.version)
                torrey.showVersionInfo();
    
            if (torrey.help)
                jct.usage();

            if (System.in.available() != 0)
            {
                // We have bytes that can be read from stdin,
                // so use it as the source of the input program.
                torrey.run(torrey.readFromStdin());
            }
            else if (torrey.inPath != null)
            {
                // Read the contents from the file at the
                // given path and run the compiler using that
                // as its input.
                torrey.run(torrey.read(torrey.inPath.trim()));
            }
            else if (!torrey.help)
            {
                // No input from stdin was detected and no
                // path to an input file was provided, so
                // show compiler usage information.
                jct.usage();
            }
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
     * Runs the Torrey compiler with a given input program.
     * 
     * @param input An input program.
     */
    public void run(String input)
    {
        try
        {
            if (debug)
            {
                stdout("");
                stdout("DEBUG: Program (input to compiler):");
                stdout(input);
                stdout("");
            }

            // Lexical analysis (scanning)
            final Lexer lexer = new Lexer(
                    new ErrorReporter(input), input);
            final TokenList tokens = lexer.lex();

            if (debug)
            {
                stdout("DEBUG: Tokens (output from Lexer):");
                stdout(tokens.toString());
                stdout("");
            }

            if (stopAtLex)
            {
                if (hasOutFile())
                    write(tokens.toString(), outFileName);
                else
                    stdout(tokens.toString());
                System.exit(0);
            }


            // Syntax analysis (parsing)
            final Grammar grammar = new Grammar(
                    new ErrorReporter(input), tokens.tokens());
            final Program program = grammar.parse();
            final PrettyPrinterVisitor ppVisitor = new PrettyPrinterVisitor();

            if (debug)
            {
                stdout("DEBUG: AST (output from Grammar):");
                stdout(ppVisitor.visit(program));
                stdout("");
            }

            if (stopAtParse)
            {
                if (hasOutFile())
                    write(program.toString(), outFileName);
                else
                    stdout(program.toString());
                System.exit(0);
            }

            // Semantic analysis

            // Type checks operands to expressions, decorates
            // the AST with type information, and creates 
            // environments for let expressions.
            final TypeChecker typeChecker = new TypeChecker(
                new ErrorReporter(input), program);
            typeChecker.check();

            if (debug)
            {
                stdout("DEBUG: AST (output from TypeChecker):");
                stdout(ppVisitor.visit(program));
                stdout("");
            }

            // High-level optimizations (on AST)
            if (!disableHlOpts)
            {
                final ConstantFolderVisitor cfVistor = 
                    new ConstantFolderVisitor();
                cfVistor.visit(program);
            }

            if (debug && !disableHlOpts)
            {
                stdout("DEBUG: Optimized AST (output from ConstantFolderVisitor):");
                stdout(ppVisitor.visit(program));
                stdout("");
            }

            // Intermediate code generation
            final IRGenVisitor irVisitor = new IRGenVisitor(program);
            final IRProgram irProgram = irVisitor.gen();

            if (debug)
            {
                stdout("DEBUG: IR program (output from IRGenVisitor):");
                stdout(irProgram.toString());
                stdout("");
            }

            if (stopAtIr)
            {
                if (hasOutFile())
                    write(irProgram.toString(), outFileName);
                else
                    stdout(irProgram.toString());
                System.exit(0);
            }

            // Optimizations on the IR go here
            // ...

            // x86-64 code generation
            final X86Generator x86Gen = new X86Generator(irProgram);
            final X86Program x86Program = x86Gen.gen();

            if (debug)
            {
                stdout("DEBUG: x86-64 program (output from X86Generator):");
                stdout(x86Program.toString());
                stdout("");
            }


            if (stopAtCompile)
            {
                if (hasOutFile())
                    write(x86Program.toString(), outFileName);
                else
                    stdout(x86Program.toString());
                System.exit(0);
            }

            // Assembly and Linking

            if (debug)
            {
                stdout("DEBUG: Starting assembly and linking");
                stdout("DEBUG: Building the run-time object code; running gcc\n"
                    + " with the following command-line arguments:");
                stdout(String.format("\t%s, %s", "-c", RUNTIME_CCODE_NAME));
            }

            // Build the run-time object code.
            ProcessBuilder pb = new ProcessBuilder(
                "gcc", 
                "-c", 
                RUNTIME_CCODE_NAME);
            Process p = pb.start();

            // before building the executable, wait for 
            // the run-time object code to be built.
            p.waitFor();

            // Write the assembly source code to disk.
            write(x86Program.toString(), DEFAULT_SOURCE_ASM_NAME);

            if (debug)
            {
                stdout(String.format("DEBUG: Wrote assembly code"
                + " to file %s in current directory", DEFAULT_SOURCE_ASM_NAME));
            }

            if (debug)
            {
                stdout(String.format("DEBUG: Building the executable and"
                    + " linking it with object code file\n %s; running gcc with"
                    + " the following command-line arguments:", RUNTIME_OCODE_NAME));
                stdout(String.format("\t%s, %s, %s, %s", DEFAULT_SOURCE_ASM_NAME, 
                RUNTIME_OCODE_NAME, "-o", 
                hasOutFile() ? outFileName : DEFAULT_EXECUTABLE_NAME));
            }

            // Build the executable.
            pb = new ProcessBuilder(
                "gcc", 
                DEFAULT_SOURCE_ASM_NAME, 
                RUNTIME_OCODE_NAME,
                "-o", 
                hasOutFile() ? outFileName : DEFAULT_EXECUTABLE_NAME);
            p = pb.start();

            // Before, potentially deleting the assembly source,
            // wait for the executable to build.
            p.waitFor();

            if (debug)
            {
                stdout(String.format("DEBUG: Built executable %s"
                    + " and saved it to current directory", 
                    hasOutFile() ? outFileName : DEFAULT_EXECUTABLE_NAME));
            }

            if (!keepSource)
            {
                // Delete the previously written assembly source 
                // file from disk.
                Files.delete(Paths.get(DEFAULT_SOURCE_ASM_NAME));
            }

            if (debug)
            {
                stdout(String.format("DEBUG: Deleted assembly file"
                    + " %s from current directory", DEFAULT_SOURCE_ASM_NAME));
            }
            
        }
        catch (InterruptedException e)
        {
            System.err.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        catch (SyntaxError e)
        {
            System.err.println(e.getMessage());
        }
        catch (SemanticError e)
        {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Prints compiler version information to the standard output stream.
     */
    public void showVersionInfo()
    {
        stdout(String.format("torreyc %s", SEMANTIC_VERSION));
        stdout("This is free and open source software available at:");
        stdout("https://github.com/MatthewKosloski/torrey/");
        System.exit(0);
    }

    /*
     * Writes the provided output string to a file with the
     * provided file name.
     * 
     * @param output The output string to be written to the file.
     * @param fileName The name of the output file.
     * @throws IOException
     */
    private void write(final String output, final String fileName)
    throws IOException
    {
        final BufferedWriter writer = 
            new BufferedWriter(new FileWriter(fileName));
        writer.write(output);
        writer.close();
    }

    /*
     * Reads the contents of the file at the given path, 
     * returning the contents of the file as a string object.
     * 
     * @param path Path to the file on the file system.
     * @return A string containing the contents of the file.
     * @throws IOException If an I/O error occurs.
     */
    private String read(final String path) throws IOException
    {
        final byte[] bytes = Files.readAllBytes(Paths.get(path));   
        return new String(bytes, Charset.defaultCharset());
    }

    /*
     * Reads from standard input, returning it
     * as a string.
     * 
     * @return A string of the standard input.
     * @throws IOException
     */
    private String readFromStdin() throws IOException
    {
        // System.in is a byte stream, so we wrap it in an 
        // InputStreamReader to convert it to a character 
        // stream. We then buffer the input to reduce the 
        // cost of every read() from the byte stream.
        final BufferedReader in = 
            new BufferedReader(new InputStreamReader(System.in));
        
        // Read characters from stdin until EOF.
        final StringBuffer sb = new StringBuffer();
        int ascii;
        while ((ascii = in.read()) != -1)
            sb.append((char) ascii);
        
        return sb.toString();
    }

    /**
     * Sends the string to standard output unless the user specifies 
     * otherwise via a command-line argument.
     * 
     * @param str The string to be sent to standard ouput.
     */
    private void stdout(String str)
    {
        if (!noStdOut)
            System.out.println(str);
    }

    /*
     * Indicates whether the user specified a name of an output file.
     * 
     * @return True if the user specified the name of an output file;
     * False otherwise.
     */
    private boolean hasOutFile()
    {
        if (outFileName != null && outFileName.trim().length() > 0)
            return true;

        return false;
    }
}
