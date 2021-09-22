package me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing;

public final class Global extends X86Address
{
    public enum RuntimeProcedure
    {
        PRINT_INT ("print_int"),
        PRINT_NL ("print_nl");

        private final String terminalSymbol;

        RuntimeProcedure(String terminalSymbol)
        {
            this.terminalSymbol = terminalSymbol;
        }
    }

    public Global(String name)
    {
        super(AddressingMode.GLOBAL, name);
    }

    public Global(RuntimeProcedure proc)
    {
        this(proc.terminalSymbol);
    }
}
