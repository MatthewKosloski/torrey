package me.mtk.torrey.ir;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents an unary operator.
 */
public final class UnaryOperator extends Operator
{
    // Maps an enum prop to its op text
    private static final Map<UnaryOpType, String> store;

    static
    {
        store = new HashMap<>();
        store.put(UnaryOpType.MINUS, "-");
    }

    public UnaryOperator(UnaryOpType op) {
        super(store.get(op));
    }
}