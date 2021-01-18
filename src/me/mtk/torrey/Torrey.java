package me.mtk.torrey;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import me.mtk.torrey.Lexer.TorreyLexer;
import me.mtk.torrey.Lexer.Token;

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
            final TorreyLexer lexer = new TorreyLexer(input);
            List<Token> tokens = lexer.start();
            System.out.println(tokens);
            System.out.println();
        }
        catch (IOException e)
        {
            System.err.println("Encountered an I/O error.");
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
