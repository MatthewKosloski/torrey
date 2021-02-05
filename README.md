# The Torrey Programming Language

This is the first compiler of the Torrey programming language.

## Grammar

The parser of this compiler implements the following grammar:

```
program       -> expression* ;

expression    -> integer
               | unary
               | binary
               | print ;

integer       -> [0-9]+ ;
unary         -> "(" "-" expression ")" ;
binary        -> "(" ("+" | "-" | "*" | "/") expression expression ")" ;
print         -> "(" ("print" | "println") expression+ ")" ;
```

## Intermediate Representation (IR) Grammar

This is the grammar of the intermediate language.  This intermediate language, or intermediate representation (IR), is used as a stepping-stone between the high-level Torrey language and the very low-level x86 assembly language.

Although this intermediate language is not necessary for compilation to be possible, it is advantageous. The advantages of using such an intermediate representation are:

- The linear structure of the intermediate language more closely resembles the flow of control of an assembly program.
- If we plan on compiling down to more than one assembly language, then only one translation from the high-level language to the intermediate language is necessary.  Only the translation from the intermediate language to assembly is needed.
- If we want to support more than one high-level language, then only the translation from that high-level language to intermediate code needs to be written for each language.  All the high-level languages can share the same compiler backend that translates the intermediate language to assembly code.
- If we wanted to write an interpreter for our language, we could write an interpreter that simply interprets the intermediate language.

The intermediate language is a type of compiler IR, specifically, a *linear* IR.  Such a linear IR contrasts greatly to the non-linear, tree structure of the abstract syntax tree used by the compiler's syntax and semantic analyzers.  Instructions in the intermediate language are known as *three-address instructions*.  Together, one or more three-address instructions make up *three-address code*.  Three-address code is a linearized representation of the abstract syntax tree.  In a three-address instruction, there is at most one operator on the right side of an instruction and, including the left-hand side, there can be at most three operands.  Typically, the left-hand side is a *compiler-generated temporary name*.

In the intermediate language, a program is a linear sequence of zero or more quadruples of the form `(op, arg1, arg2, result)`.  A *quadruple* has at most four fields and is one way of representing a three-address instruction as a data structure.

The operands, or args, of a quadruple are addresses.  In this intermediate language, there are three types of address: temp, name, and constant.  A *temp* is like a virtual CPU register.  A *name* can be an identifier that appears in the input program.  Finally, a *constant* is an integer.

These types of addresses somewhat correspond to the different *addressing modes* of x86-64 assembly language.  For example, a *temp* corresponds to the *register* addressing mode, whereas a *constant* corresponds to the *immediate* addressing mode.  The *name* address type does not correspond to an x86-64 addressing mode.  There will be a compiler pass that replaces all name addresses with register names or base-relative addresses on the stack.

```
program      -> quadruple* ;

quadruple    -> copy 
              | unary 
              | binary 
              | param 
              | call ;

copy         -> temp "=" addr ;
unary        -> temp "=" "-" addr ;
binary       -> temp "=" addr ("+" | "-" | "*" | "/") addr ;
param        -> "param" addr ;
call         -> "call" name "," constant ;

addr         -> temp | name | constant ;
```