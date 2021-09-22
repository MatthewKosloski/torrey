package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.frontend.ir.instructions.IROpType;

public enum X86OpType 
{
    // Arithmetic
    ADDQ ("addq"),
    SUBQ ("subq"),
    IMULQ ("imulq"),
    IDIVQ ("idivq"),
    CQO ("cqo"),
    NEGQ ("negq"),

    // Control flow
    CMP ("cmp"),
    JMP ("jmp"),
    JL ("jl"),
    JLE ("jle"),
    JG ("jg"),
    JGE ("jge"),
    JE ("je"),
    JNE ("jne"),
    LABEL ("label"),
    CALLQ ("callq"),
    RETQ ("retq"),

    // Memory
    MOVQ ("movq"),
    POPQ ("popq"),
    PUSHQ ("pushq");

    private final String terminalSymbol;

    X86OpType (String terminalSymbol)
    {
        this.terminalSymbol = terminalSymbol;
    }

    /**
     * Translates an IR binary operator to an x86 condition code.
     * 
     * @param irOpType An IR binary operator type.
     * @return The corresponding condition code in x86.
     */
    public static X86OpType getConditionCodeFromIROpType(IROpType irOpType)
    {
        switch (irOpType)
        {
            case LT: return JL;
            case LTE: return JLE;
            case GT: return JG;
            case GTE: return JGE;
            case EQUAL: return JE;
            case NEQUAL: return JNE;

            default:
                throw new Error(String.format(
                    "Unexpected IR op type %s",irOpType));
        }
    }

    public String toString()
    {
        return terminalSymbol;
    }
}
