package me.mtk.torrey.symbols;

import me.mtk.torrey.analysis.DataType;

public class Symbol 
{
    private String identifier;
    private String uniqueId;
    private DataType type;

    public Symbol(String identifier, String uniqueId, DataType type)
    {
        this.identifier = identifier;
        this.uniqueId = uniqueId;
        this.type = type;
    }

    public String id()
    {
        return identifier;
    }

    public String uniqueId()
    {
        return uniqueId;
    }

    public DataType type()
    {
        return type;
    }    
}
