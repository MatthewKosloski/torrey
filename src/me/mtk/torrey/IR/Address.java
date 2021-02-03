package me.mtk.torrey.IR;

/**
 * An address can be a name, a constant, or a 
 * compiler-generated temporary.
 */
public class Address 
{
    // The type of address name, temp, etc.
    private AddressType type;

    // The value of the address.
    private String value;

    public Address(AddressType type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public Address(int contant)
    {
        type = AddressType.CONSTANT;
        value = String.valueOf(contant);
    }

    public AddressType type()
    {
        return type;
    }

    public String value()
    {
        return value;
    }

    public String toString()
    {
        return String.format("%s", value);
    }
}
