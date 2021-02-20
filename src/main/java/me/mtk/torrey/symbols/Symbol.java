package me.mtk.torrey.symbols;

import me.mtk.torrey.analysis.DataType;

public class Symbol 
{
    private String identifier;
    private DataType type;

    public Symbol(String identifier, DataType type)
    {
        this.identifier = identifier;
        this.type = type;
    }

    public String id()
    {
        return identifier;
    }

    public DataType type()
    {
        return type;
    }    
}
