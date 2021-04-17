package me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing;

/**
 * The different types of x86 addressing
 * modes that we are using.
 */
public enum AddressingMode
{
    // A constant value indicated by a dollar sign.
    // (e.g., $1, $-42, etc.)
    IMMEDIATE,

    // A register (%rax, %rsp, %r11, %rbx, etc.)
    REGISTER,
    
    // Not a real x86 addressing mode,
    // but we use it in our "pseudo-x86"
    // program that contains an infinite
    // number of temporaries. Later on in compilation,
    // these temporaries are replaced by either registers
    // or stack locations.
    TEMP,

    // A global value in the .data segment of memory (e.g., 'x')
    // or global name of a procedure (e.g., 'printf').
    // The assembler translates this into an absolute address
    // or an address computation.
    GLOBAL,

    // A base-relative value, obtained by adding a signed
    // integer (an offset) to the name of a register (base)
    // (e.g., -8(%rbp)).
    BASEREL
}