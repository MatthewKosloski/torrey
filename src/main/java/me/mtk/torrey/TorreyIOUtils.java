package me.mtk.torrey;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Container class for all general-purpose,
 * input/output functionality used by the compiler.
 */
public final class TorreyIOUtils 
{
    /**
     * Writes the provided output string to a file with the
     * provided file name.
     * 
     * @param output The output string to be written to the file.
     * @param fileName The name of the output file.
     * @throws IOException
     */
    public static void write(final String output, final String fileName)
    throws IOException
    {
        final BufferedWriter writer = 
            new BufferedWriter(new FileWriter(fileName));
        writer.write(output);
        writer.close();
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

    /**
     * Reads from standard input, returning it
     * as a string.
     * 
     * @return A string of the standard input.
     * @throws IOException
     */
    public static String readFromStdin() throws IOException
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
}