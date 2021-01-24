package me.mtk.torrey;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import me.mtk.torrey.Lexer.Lexer;
import me.mtk.torrey.Lexer.Token;
import me.mtk.torrey.ErrorReporter.ErrorReporter;
import me.mtk.torrey.ErrorReporter.SemanticError;
import me.mtk.torrey.ErrorReporter.SyntaxError;
import me.mtk.torrey.Parser.Grammar;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.Analysis.TypeChecker;

public class Torrey 
{
    public static void main(String[] args)
    {
        if (args.length > 1) 
        {
            System.out.println("Usage: Torrey [script]");
            System.exit(64);
        }

        try
        {
            final String input = read(args[0]);
            final Lexer lexer = new Lexer(
                    new ErrorReporter(input), input);
            final List<Token> tokens = lexer.lex();
            final Grammar grammar = new Grammar(
                    new ErrorReporter(input), tokens);
            final Program program = grammar.parse();

            final TypeChecker typeChecker = new TypeChecker(
                new ErrorReporter(input), program);
            typeChecker.check();
            
            System.out.println(tokens);
            System.out.println(program);
        }
        catch (IOException e)
        {
            System.err.println("Encountered an I/O error.");
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
