package me.mtk.torrey;

import com.beust.jcommander.Parameter;

public class TorreyConfig 
{
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
        description = "Generate intermediate code only;"
            + " do not compile or assemble.",
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

    public boolean help() { return help; }
    public boolean version() { return version; }
    public boolean debug() { return debug; }
    public String inPath() { return inPath; }
    public String outFileName() { return outFileName; }
    public boolean stopAtLex() { return stopAtLex; }
    public boolean stopAtParse() { return stopAtParse; }
    public boolean stopAtIr() { return stopAtIr; }
    public boolean stopAtCompile() { return stopAtCompile; }
    public boolean keepSource() { return keepSource; }
    public boolean disableHlOpts() { return disableHlOpts; }
    public boolean noStdOut() { return noStdOut; }

    public TorreyConfig()
    {

    }

    public TorreyConfig(TorreyConfig original)
    {
        help = original.help;
        version = original.version;
        debug = original.debug;
        stopAtLex = original.stopAtLex;
        stopAtParse = original.stopAtParse;
        stopAtCompile = original.stopAtCompile;
        keepSource = original.keepSource;
        disableHlOpts = original.disableHlOpts;
        noStdOut = original.noStdOut;
        inPath = new String(original.inPath);
        outFileName = original.outFileName != null 
            ? new String(original.outFileName)
            : null;
    }
}