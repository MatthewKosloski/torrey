# Command Line Interface

## Usage

The input to the compiler is a sequence of zero or more characters, a string.  Ideally, this input string is a syntactically and semantically valid program written in the Torrey programming language (see the below grammar for what a syntactially valid Torrey program looks like). If the input string is either syntactically or semantically invalid, then the compiler will inform the user by way of a helpful error message.

### Input

The input string must come from exactly one of two places: the standard input stream or the file system.

####  Input from Standard Input

To run the compiler with input from standard input, pipe the input into `java` like so:

```
$ echo "(println 42 (* (/ 12 2) (+ 3 4)) (- 42))" | java -jar torreyc-x.x.x.jar && ./a.out
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
$ java -jar torreyc-x.x.x.jar -i foo.torrey && ./a.out
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
