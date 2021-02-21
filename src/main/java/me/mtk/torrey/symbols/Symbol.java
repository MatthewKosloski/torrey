package me.mtk.torrey.symbols;

import me.mtk.torrey.analysis.DataType;

public class Symbol 
{
    private String name;
    private DataType type;
    private SymCategory category;

    public Symbol(String name, DataType type, SymCategory category)
    {
        this.name = name;
        this.type = type;
        this.category = category;
    }

    public String name()
    {
        return name;
    }

    public DataType type()
    {
        return type;
    }

    public SymCategory category()
    {
        return category;
    }
}
