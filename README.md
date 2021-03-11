# The Torrey Programming Language

This is the source code of the third compiler of the Torrey programming language.  [Here](https://github.com/MatthewKosloski/torrey/tree/c02) is the source code of the second compiler.

## What is Torrey?

Torrey, named after [Torrey Pines State Natural Reserve](https://en.wikipedia.org/wiki/Torrey_Pines_State_Natural_Reserve), is a novel, "Lisp-like" programming language.  That is, its [syntax](https://en.wikipedia.org/wiki/Syntax_(programming_languages)) (form) and [semantics](https://en.wikipedia.org/wiki/Semantics_(computer_science)) (meaning)
are inspired by that of the Lisp languages (e.g., Common Lisp, Scheme, Racket, Clojure, etc.).

## Motivation

Algorithms that are employed to solve computational problems must be implemented in some
_high-level programming language_. That is, a language that abstracts away the many low-level
details of the computer (e.g., registers, memory addresses, the stack, calling conventions, etc.),
thereby making it loosely coupled to the hardware. These high-level languages contrast greatly
to low-level languages (e.g., assembly languages), which are very tightly coupled to a computer
and its architecture. While this abstraction has facilitated accessibility and the speed at which
programming can take place, it has simultaneously (by design) “shielded” programmers, many
of whom take the abstraction for granted. 

There are several different ways of implementing a
high-level programming language, one of which is through _compilation_. A _compiler_ is a computer program that translates a program written in a source language to an equivalent program in a target
language. 

In an attempt to better my understanding of the low-level details that high-level languages abstract away, I am implementing my own novel programming language by constructing a compiler to translate the Lisp-like high-level language to an equivalent program in x86-64 assembly code. 


## Maven Build

To build an executable (JAR file) via Maven, run:

```
mvn package
```

This will create a new `target` directory in the project root containing a `torreyc-x.x.x.jar` file, where `x.x.x` is the semantic version number for the compiler.

## Usage

The input to the compiler is a sequence of zero or more characters, a string.  Ideally, this input string is a syntactically and semantically valid program written in the Torrey programming language (see the below grammar for what a syntactially valid Torrey program looks like). If the input string is either syntactically or semantically invalid, then the compiler will inform the user by way of a helpful error message.

### Input

The input string must come from exactly one of two places: the standard input stream or the file system.

####  Input from Standard Input

To run the compiler with input from standard input, pipe the input into `java` like so:

```
$ echo "(println 42 (* (/ 12 2) (+ 3 4)) (- 42))" | java -jar torreyc-3.0.0.jar && ./a.out
$ 42
$ 42
$ -42
```

The above command compiles the Torrey program and outputs an executable `a.out`. This method is convenient for quick tests of a few lines, but is impractical for sophisticated, multi-line programs.  To input decently sized Torrey programs, use the `-i` or `--in` flag as demonstrated below.

####  Input from the File System

To run the compiler with a file on the file system, provide the `-i` or `--in` flag like so:

```
; foo.torrey
(let [foo (+ 2 3) bar (let [baz 9] (* foo baz))]
  (println foo bar))
```

```
$ java -jar torreyc-3.0.0.jar -i foo.torrey && ./a.out
$ 5
$ 45
```

The above command compiles `foo.torrey`, which is in the same directory as the compiler jar, and outputs an executable `a.out`.

###  Output

By default, the compiler: (1) compiles the input Torrey program into an equivalent 64-bit x86 assembly code program, (2) assembles and links the assembly with the dependent run-time object code, and (3) builds an executable `a.out` in the current directory.

To override this default behavior, supply the compiler with additional command-line flags:

| Flag            | Description |
| --------------- | ----------- |
| `-o <filename>` | Place the output of the compiler into a file called `<filename>` in the current directory.       |
| `-L`   | Lex only; do not parse, compile, or assemble. |
| `-p`   | Parse only; do not compile or assemble. |
| `-ir`  | Generate intermediate code only; do not compile or assemble. |
| `-S` | Compile only; do not assemble. |

*Tip: To view all available command-line arguments, run the compiler with the `--help` flag.*

Only one of `-L`, `-p`, `-ir`, or `-S` can be supplied, and the output can either go to the standard output stream or the file system.

#### Output to Standard Output Stream

When using flags `-L`, `-p`, `-ir`, or `-S`, to send to the standard output stream, simply omit the `-o` flag.

#### Output to File System

When using flags `-L`, `-p`, `-ir`, or `-S`, to send the compiler output to the file system, simply provide the `-o` flag.

## Grammar

The following grammar describes the syntax of a syntactically valid Torrey program. Note that _syntactic validity_ does not imply _semantic validity_. That is, a Torrey program can look right while also have no meaning.  For example, the Torrey program `(+ (print 3) 5)` is _syntactically_ valid because it can be derived from applying zero or more grammar rules.  However, it is _semantically_ invalid as the operands to the `+` operator must be integers.  See [TypeCheckerVisitor.java](https://github.com/MatthewKosloski/torrey/blob/c02/src/main/java/me/mtk/torrey/Analysis/TypeCheckerVisitor.java) for the implementation of such a semantic check.

The following grammar is implemented by the compiler's parser.  See [Grammar.java](https://github.com/MatthewKosloski/torrey/blob/c02/src/main/java/me/mtk/torrey/Parser/Grammar.java) for the implementation of this grammar.

```
program       -> expr* ;

expr          -> integer
               | identifier
               | unary
               | binary
               | print
               | let ;

integer       -> [0-9]+ ;
identifier    -> [a-zA-Z_$]+ [a-zA-Z0-9_$!?-]* ;
unary         -> "(" "-" expr ")" ;
binary        -> "(" ("+" | "-" | "*" | "/") expr expr ")" ;
print         -> "(" ("print" | "println") expr+ ")" ;
let           -> "(" "let" "[" (identifier expr)* "]" expr* ")" ;
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
irprogram    -> quadruple* ;

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