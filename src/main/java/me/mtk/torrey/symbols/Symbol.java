package me.mtk.torrey.symbols;

import java.util.Map;
import java.util.HashMap;
import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.ir.TempAddress;

public class Symbol 
{
    private String name;
    private String uniqueName;
    private DataType type;
    private SymCategory category;
    private TempAddress address;

    // Maps a name to the number of symbols
    // that have been generated for that name.
    // This helps ensure that every symbol is unique.
    private static Map<String, Integer> occurrences;

    static
    {
        occurrences = new HashMap<>();
    }

    public Symbol(String name, DataType type, SymCategory category)
    {
        this.name = name;
        this.type = type;
        this.category = category;
        this.uniqueName = uniqueName(name);
    }

    public String name()
    {
        return name;
    }

    public String uniqueName()
    {
        return uniqueName;
    }

    public DataType type()
    {
        return type;
    }

    public SymCategory category()
    {
        return category;
    }

    public void setAddress(TempAddress address)
    {
        this.address = address;
    }

    public TempAddress address()
    {
        return address;
    }

    private String uniqueName(String name)
    {
        // The number of occurrences of the 
        // identifier in the entire program.
        final Integer numOccurrences = occurrences.get(name);

        if (numOccurrences != null)
            occurrences.put(name, numOccurrences + 1);
        else
            occurrences.put(name, 1);

        return String.format("%s%d", name, occurrences.get(name));
    }
}
