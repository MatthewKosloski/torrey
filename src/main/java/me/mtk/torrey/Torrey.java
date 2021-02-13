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
import java.util.List;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import me.mtk.torrey.Lexer.Lexer;
import me.mtk.torrey.Lexer.Token;
import me.mtk.torrey.ErrorReporter.ErrorReporter;
import me.mtk.torrey.ErrorReporter.SemanticError;
import me.mtk.torrey.ErrorReporter.SyntaxError;
import me.mtk.torrey.Parser.Grammar;
import me.mtk.torrey.X86.X86Generator;
import me.mtk.torrey.X86.X86Program;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.Analysis.ConstantFolderVisitor;
import me.mtk.torrey.Analysis.TypeChecker;
import me.mtk.torrey.IR.IRGenVisitor;
import me.mtk.torrey.IR.IRProgram;

public class Torrey 
{
    @Parameter(
        names = {"--help", "-h"}, 
        description = "Display this information.", 
        help = true,
        order = 0)
    private boolean help;

    @Parameter(
        names = {"--debug", "-d"}, 
        description = "Show output from compilation stages.",
        order = 1)
    private boolean debug = false;

    @Parameter(
        names = {"--in", "-i"},
        description = "The path to the input file.", 
        order = 2)
    private String inPath;

    @Parameter(
        names = {"--out", "-o"}, 
        description = "Place the output into a file.",
        order = 3)
    private String outFileName;

    @Parameter(
        names = {"-L"}, 
        description = "Lex only; do not parse, compile, or assemble.",
        order = 4)
    private boolean stopAtLex = false;

    @Parameter(
        names = {"-p"}, 
        description = "Parse only; do not compile or assemble.",
        order = 5)
    private boolean stopAtParse = false;
    
    @Parameter(
        names = {"--no-hl-opt"}, 
        description = "Disable the high-level compiler optimizations.",
        order = 6)
    private boolean disableHlOpts = false;

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
        order = 8)
    private boolean keepSource = false;
    

    public boolean isHelp()
    {
        return help;
    }

    public String inPath()
    {
        return inPath;
    }

    public static void main(String ... argv)
    {    
        try
        {
            final Torrey torrey = new Torrey();
            final JCommander jct = JCommander.newBuilder()
                .addObject(torrey)
                .build();

            jct.setProgramName("java -jar torreyc.jar");
            jct.parse(argv);
    
            if (torrey.isHelp())
                jct.usage();

            if (System.in.available() != 0)
            {
                // We have bytes that can be read from stdin,
                // so use it as the source of the input program.

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
        
                // Run the compiler with the input from stdin.
                torrey.run(sb.toString());
            }
            else if (torrey.inPath() != null)
            {
                // Read the contents from the file at the
                // given path and run the compiler using that
                // as its input.
                torrey.run(read(torrey.inPath().trim()));
            }
            else if (!torrey.isHelp())
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

    public void run(String input)
    {
        try
        {
            // Lexical analysis (scanning)
            final Lexer lexer = new Lexer(
                    new ErrorReporter(input), input);
            final List<Token> tokens = lexer.lex();

            if (stopAtLex)
            {
                System.out.println(tokens);
                System.exit(0);
            }

            // Syntax analysis (parsing)
            final Grammar grammar = new Grammar(
                    new ErrorReporter(input), tokens);
            final Program program = grammar.parse();

            if (stopAtParse)
            {
                System.out.println(program);
                System.exit(0);
            }

            // Semantic analysis (type checking)
            final TypeChecker typeChecker = new TypeChecker(
                new ErrorReporter(input), program);
            typeChecker.check();

            // High-level optimizations (on AST)
            if (!disableHlOpts)
            {
                final ConstantFolderVisitor cfVistor = new ConstantFolderVisitor();
                cfVistor.visit(program);
            }

            // Intermediate code generation
            final IRGenVisitor irGen = new IRGenVisitor(program);
            final IRProgram irProgram = irGen.gen();

            if (stopAtIr)
            {
                System.out.println(irProgram);
                System.exit(0);
            }

            // Optimizations on the IR go here
            // ...

            // x86-64 code generation
            final X86Generator x86Gen = new X86Generator(irProgram);
            final X86Program x86Program = x86Gen.gen();

            if (stopAtCompile)
            {
                System.out.println(x86Program);
                System.exit(0);
            }

            // Assembly and Linking

            // Build the run-time object code.
            ProcessBuilder pb = new ProcessBuilder("gcc", 
                "-c", "runtime.c");
            Process p = pb.start();

            // before building the executable, wait for 
            // the run-time object code to be built.
            p.waitFor();

            // Write the assembly source code to disk.
            final BufferedWriter writer = 
                new BufferedWriter(new FileWriter("temp.s"));
            writer.write(x86Program.toString()); 
            writer.close();

            // Build the executable.
            pb = new ProcessBuilder("gcc", 
                "temp.s", "runtime.o", "-o", "program.out");
            p = pb.start();

            // Before, potentially deleting the assembly source,
            // wait for the executable to build.
            p.waitFor();

            if (!keepSource)
            {
                // Delete the previously written assembly source 
                // file from disk.
                Files.delete(Paths.get("temp.s"));
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
     * Reads the contents of the file at the given path, 
     * returning the contents of the file as a string object.
     * 
     * @param path Path to the file on the file system.
     * @return A string containing the contents of the file.
     * @throws IOException If an I/O error occurs.
     */
    public static String read(final String path) throws IOException
    {
        final byte[] bytes = Files.readAllBytes(Paths.get(path));   
        return new String(bytes, Charset.defaultCharset());
    }
}
