#!/bin/bash

source _utils.sh

run_cli_tests () {
  local compiler_path=$1
  echo "Tests for CLI"

  assert_torreyc_stdout_equalto \
    "Should output version information when given the -v flag" \
    $compiler_path \
    "-v" \
    "torreyc 3.1.2
The Compiler for the Torrey Programming Language
https://github.com/MatthewKosloski/torrey/

The latest release of the software can be found at:
https://github.com/MatthewKosloski/torrey/releases/latest"

  assert_torreyc_stdout_equalto \
    "Should output version information when given the --version flag" \
    $compiler_path \
    "--version" \
    "torreyc 3.1.2
The Compiler for the Torrey Programming Language
https://github.com/MatthewKosloski/torrey/

The latest release of the software can be found at:
https://github.com/MatthewKosloski/torrey/releases/latest"

  assert_torreyc_stdout_equalto \
    "Should output usage information when given the -h flag" \
    $compiler_path \
    "-h" \
    "Usage: java -jar torreyc-3.1.2.jar [options]
  Options:
    --help, -h
      Display this information.
    --version, -v
      Display compiler version information.
      Default: false
    --debug, -d
      Show output from compilation stages.
      Default: false
    --target, -t
      Generate code for the given target.
      Default: x86_64-pc-linux
    --target-list
      Show a list of available targets.
      Default: false
    --in, -i
      The path to the input file.
    --out, -o
      Place the output into a file.
    -L
      Lex only; do not parse, compile, or assemble.
      Default: false
    -p
      Parse only; do not compile or assemble.
      Default: false
    -ir
      Generate intermediate code only; do not compile or assemble.
      Default: false
    -S
      Compile only; do not assemble.
      Default: false
    --keep-source
      Keep the assembly source file after assembly.
      Default: false
    --no-stdout
      Suppress all output to the standard output stream.
      Default: false"

  assert_torreyc_stdout_equalto \
    "Should output usage information when given the --help flag" \
    $compiler_path \
    "--help" \
    "Usage: java -jar torreyc-3.1.2.jar [options]
  Options:
    --help, -h
      Display this information.
    --version, -v
      Display compiler version information.
      Default: false
    --debug, -d
      Show output from compilation stages.
      Default: false
    --target, -t
      Generate code for the given target.
      Default: x86_64-pc-linux
    --target-list
      Show a list of available targets.
      Default: false
    --in, -i
      The path to the input file.
    --out, -o
      Place the output into a file.
    -L
      Lex only; do not parse, compile, or assemble.
      Default: false
    -p
      Parse only; do not compile or assemble.
      Default: false
    -ir
      Generate intermediate code only; do not compile or assemble.
      Default: false
    -S
      Compile only; do not assemble.
      Default: false
    --keep-source
      Keep the assembly source file after assembly.
      Default: false
    --no-stdout
      Suppress all output to the standard output stream.
      Default: false"

  assert_torreyc_stdout_equalto \
    "Should output usage information when given no flags" \
    $compiler_path \
    "" \
    "Usage: java -jar torreyc-3.1.2.jar [options]
  Options:
    --help, -h
      Display this information.
    --version, -v
      Display compiler version information.
      Default: false
    --debug, -d
      Show output from compilation stages.
      Default: false
    --target, -t
      Generate code for the given target.
      Default: x86_64-pc-linux
    --target-list
      Show a list of available targets.
      Default: false
    --in, -i
      The path to the input file.
    --out, -o
      Place the output into a file.
    -L
      Lex only; do not parse, compile, or assemble.
      Default: false
    -p
      Parse only; do not compile or assemble.
      Default: false
    -ir
      Generate intermediate code only; do not compile or assemble.
      Default: false
    -S
      Compile only; do not assemble.
      Default: false
    --keep-source
      Keep the assembly source file after assembly.
      Default: false
    --no-stdout
      Suppress all output to the standard output stream.
      Default: false"

  assert_torreyc_stdout_equalto \
    "Should output a list of available targets when given the --target-list flag" \
    $compiler_path \
    "--target-list" \
    "Usage: --target <triple>,
  where <triple> is of the form <arch>-<vendor>-<sys>

Registered targets (triples):
  x86_64-pc-linux"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report error if invalid target passed to --target flag" \
    $compiler_path \
    "--target invalid-target" \
    "(print 42)" \
    "'invalid-target' is not a registered target. To view the registered targets, supply the '--target-list' flag."

  assert_torreyc_stderr_equalto \
    "Should report error when given an invalid flag" \
    $compiler_path \
    "--not-a-valid-flag" \
    "Was passed main parameter '--not-a-valid-flag' but no main parameter was defined in your arg class"

  assert_torreyc_stdout_equalto_with_stdin \
    "Smoke: Should output x86-64 when x86_64-pc-linux is passed to the --target flag" \
    $compiler_path \
    "--target x86_64-pc-linux -S" \
    "(print 42)" \
    '.text
  .globl main
main:
  pushq %rbp
  movq %rsp, %rbp
  subq $16, %rsp
  jmp start
start:
  movq $42, -8(%rbp)
  movq -8(%rbp), %rdi
  callq print_int
  jmp conclusion
conclusion:
  addq $16, %rsp
  popq %rbp
  movq'

  assert_torreyc_stdout_equalto_with_stdin \
    "Smoke: Should use the x86_64-pc-linux backend when no --target provided" \
    $compiler_path \
    "-S" \
    "(print 42)" \
    '.text
  .globl main
main:
  pushq %rbp
  movq %rsp, %rbp
  subq $16, %rsp
  jmp start
start:
  movq $42, -8(%rbp)
  movq -8(%rbp), %rdi
  callq print_int
  jmp conclusion
conclusion:
  addq $16, %rsp
  popq %rbp
  movq'

  assert_torreyc_stdout_equalto_with_stdin \
    "Smoke: Should ouput a sequence of tokens when given the -L flag" \
    $compiler_path \
    "-L" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Smoke: Should ouput an AST when given the -p flag" \
    $compiler_path \
    "-p" \
    "(- 1)" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "fold": "null",
    "node_type": "UnaryExpr",
    "children": [{
      "evalType": "INTEGER",
      "node_type": "IntegerExpr",
      "token": {
        "rawText": "1",
        "endIndex": 4,
        "beginIndex": 3,
        "type": "INTEGER",
        "beginLineIndex": 0,
        "startPos": {
          "col": 4,
          "line": 1
        },
        "endPos": {
          "col": 5,
          "line": 1
        }
      }
    }],
    "token": {
      "rawText": "-",
      "endIndex": 2,
      "beginIndex": 1,
      "type": "MINUS",
      "beginLineIndex": 0,
      "startPos": {
        "col": 2,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_torreyc_stdout_equalto_with_stdin \
    "Smoke: Should ouput the intermediate representation when given the -ir flag" \
    $compiler_path \
    "-ir" \
    "(print 1)" \
    "t0 = 1
param t0
call print, 1"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout -h flags" \
    $compiler_path \
    "--no-stdout -h"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout --help flags" \
    $compiler_path \
    "--no-stdout --help"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout -v flags" \
    $compiler_path \
    "--no-stdout -v"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout --version flags" \
    $compiler_path \
    "--no-stdout --version"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout -d flags" \
    $compiler_path \
    "--no-stdout -d"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout --debug flags" \
    $compiler_path \
    "--no-stdout --debug"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout -L flags" \
    $compiler_path \
    "--no-stdout -L"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout -p flags" \
    $compiler_path \
    "--no-stdout -p"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout -ir flags" \
    $compiler_path \
    "--no-stdout -ir"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout -S flags" \
    $compiler_path \
    "--no-stdout -S"

  assert_torreyc_stdout_empty \
    "Should not output anything when given the --no-stdout -S flags" \
    $compiler_path \
    ""

  assert_exec_stdout_equalto_with_infile \
    "Should accept input from a local file via the -i flag" \
    $compiler_path \
    "-i in.torrey" \
    "(println 42)" \
    "a.out" \
    "in.torrey" \
    "42"

  assert_exec_stdout_equalto_with_infile \
    "Should accept input from a local file via the --in flag" \
    $compiler_path \
    "--in in.torrey" \
    "(println 42)" \
    "a.out" \
    "in.torrey" \
    "42"

  assert_torreyc_stderr_equalto \
    "Should report an error when the -i flag is given a filename that does not exist" \
    $compiler_path \
    "-i not_in.torrey" \
    "Torrey: An error occurred while reading from the standard input stream." \

  assert_torreyc_stderr_equalto \
    "Should report an error when the --in flag is given a filename that does not exist" \
    $compiler_path \
    "--in not_in.torrey" \
    "Torrey: An error occurred while reading from the standard input stream." \

  # Should accept a file from a distant directory via the -i flag
  # Should accept a file from a distant directory via the --in flag

  # Smoke: Should default to writing an a.out executable to the current directory
  # Should write an executable to the current directory via the -o flag
  # Should write an executable to the current directory via the --out flag
  # Should write an executable to a distant directory via the -o flag
  # Should write an executable to a distant directory via the --out flag
  # Should override an existing executable via the -o flag
  # Should override an existing executable via the --out flag

  assert_outfile_contents_equalto \
    "Should be able to write the result of -L to disk via the -o flag" \
    $compiler_path \
    "-L -o out.txt" \
    "(print 42)" \
    "out.txt" \
    "<'(',LPAREN,1:1,1:2>
<'print',PRINT,1:2,1:7>
<'42',INTEGER,1:8,1:10>
<')',RPAREN,1:10,1:11>
<'',EOF,-1:-1,-1:-1>"

  assert_outfile_contents_equalto \
    "Should be able to write the result of -L to disk via the --out flag" \
    $compiler_path \
    "-L --out out.txt" \
    "(print 42)" \
    "out.txt" \
    "<'(',LPAREN,1:1,1:2>
<'print',PRINT,1:2,1:7>
<'42',INTEGER,1:8,1:10>
<')',RPAREN,1:10,1:11>
<'',EOF,-1:-1,-1:-1>"

  assert_outfile_contents_equalto \
    "Should be able to write the result of -p to disk via the -o flag" \
    $compiler_path \
    "-p -o out.txt" \
    "99" \
    "out.txt" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "node_type": "IntegerExpr",
    "token": {
      "rawText": "99",
      "endIndex": 2,
      "beginIndex": 0,
      "type": "INTEGER",
      "beginLineIndex": 0,
      "startPos": {
        "col": 1,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_outfile_contents_equalto \
    "Should be able to write the result of -p to disk via the --out flag" \
    $compiler_path \
    "-p --out out.txt" \
    "99" \
    "out.txt" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "node_type": "IntegerExpr",
    "token": {
      "rawText": "99",
      "endIndex": 2,
      "beginIndex": 0,
      "type": "INTEGER",
      "beginLineIndex": 0,
      "startPos": {
        "col": 1,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_outfile_contents_equalto \
    "Should be able to write the result of -ir to disk via the -o flag" \
    $compiler_path \
    "-ir -o out.txt" \
    "(let [x 1] (println x))" \
    "out.txt" \
    "t0 = 1
param t0
call println, 1"

  assert_outfile_contents_equalto \
    "Should be able to write the result of -ir to disk via the --out flag" \
    $compiler_path \
    "-ir --out out.txt" \
    "(let [x 1] (println x))" \
    "out.txt" \
    "t0 = 1
param t0
call println, 1"

  assert_outfile_contents_equalto \
    "Should be able to write the result of -S to disk via the -o flag" \
    $compiler_path \
    "-S -o out.txt" \
    "(let [x 1] (println x))" \
    "out.txt" \
    '.text
  .globl main
main:
  pushq %rbp
  movq %rsp, %rbp
  subq $16, %rsp
  jmp start
start:
  movq $1, -8(%rbp)
  movq -8(%rbp), %rdi
  callq print_int
  callq print_nl
  jmp conclusion
conclusion:
  addq $16, %rsp
  popq %rbp
  movq $0, %rax
  retq'

  assert_outfile_contents_equalto \
    "Should be able to write the result of -S to disk via the --out flag" \
    $compiler_path \
    "-S --out out.txt" \
    "(let [x 1] (println x))" \
    "out.txt" \
    '.text
  .globl main
main:
  pushq %rbp
  movq %rsp, %rbp
  subq $16, %rsp
  jmp start
start:
  movq $1, -8(%rbp)
  movq -8(%rbp), %rdi
  callq print_int
  callq print_nl
  jmp conclusion
conclusion:
  addq $16, %rsp
  popq %rbp
  movq $0, %rax
  retq'

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -L -p -ir and -S flags, in that order" \
    $compiler_path \
    "-L -p -ir -S" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -L -p -S and -ir flags, in that order" \
    $compiler_path \
    "-L -p -S -ir" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -L -ir -S and -p flags, in that order" \
    $compiler_path \
    "-L -ir -S -p" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -L -ir -p and -S flags, in that order" \
    $compiler_path \
    "-L -ir -p -S" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -L -S -p and -ir flags, in that order" \
    $compiler_path \
    "-L -S -p -ir" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -L -S -ir and -p flags, in that order" \
    $compiler_path \
    "-L -S -ir -p" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -p -ir -S -L flags, in that order" \
    $compiler_path \
    "-p -ir -S -L" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -p -ir -L -S flags, in that order" \
    $compiler_path \
    "-p -ir -L -S" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -p -L -ir -S flags, in that order" \
    $compiler_path \
    "-p -L -ir -S" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -p -L -S -ir flags, in that order" \
    $compiler_path \
    "-p -L -S -ir" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -p -S -L -ir flags, in that order" \
    $compiler_path \
    "-p -S -L -ir" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -p -S -ir -L flags, in that order" \
    $compiler_path \
    "-p -S -ir -L" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -ir -p -L -S flags, in that order" \
    $compiler_path \
    "-ir -p -L -S" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -ir -p -S -L flags, in that order" \
    $compiler_path \
    "-ir -p -S -L" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -ir -S -L -p flags, in that order" \
    $compiler_path \
    "-ir -S -L -p" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -ir -S -p -L flags, in that order" \
    $compiler_path \
    "-ir -S -p -L" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -ir -L -S -p flags, in that order" \
    $compiler_path \
    "-ir -L -S -p" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -ir -L -p -S flags, in that order" \
    $compiler_path \
    "-ir -L -p -S" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -S -p -ir -L flags, in that order" \
    $compiler_path \
    "-S -p -ir -L" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -S -p -L -ir flags, in that order" \
    $compiler_path \
    "-S -p -L -ir" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -S -ir -L -p flags, in that order" \
    $compiler_path \
    "-S -ir -L -p" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -S -ir -p -L flags, in that order" \
    $compiler_path \
    "-S -ir -p -L" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -S -L -ir -p flags, in that order" \
    $compiler_path \
    "-S -L -ir -p" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should lex when given the -S -L -p -ir flags, in that order" \
    $compiler_path \
    "-S -L -p -ir" \
    "42" \
    "<'42',INTEGER,1:1,1:3>
<'',EOF,-1:-1,-1:-1>"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should parse when given the -p -ir -S flags, in that order" \
    $compiler_path \
    "-p -ir -S" \
    "42" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "node_type": "IntegerExpr",
    "token": {
      "rawText": "42",
      "endIndex": 2,
      "beginIndex": 0,
      "type": "INTEGER",
      "beginLineIndex": 0,
      "startPos": {
        "col": 1,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_torreyc_stdout_equalto_with_stdin \
    "Should parse when given the -p -S -ir flags, in that order" \
    $compiler_path \
    "-p -S -ir" \
    "42" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "node_type": "IntegerExpr",
    "token": {
      "rawText": "42",
      "endIndex": 2,
      "beginIndex": 0,
      "type": "INTEGER",
      "beginLineIndex": 0,
      "startPos": {
        "col": 1,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_torreyc_stdout_equalto_with_stdin \
    "Should parse when given the -ir -S -p flags, in that order" \
    $compiler_path \
    "-ir -S -p" \
    "42" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "node_type": "IntegerExpr",
    "token": {
      "rawText": "42",
      "endIndex": 2,
      "beginIndex": 0,
      "type": "INTEGER",
      "beginLineIndex": 0,
      "startPos": {
        "col": 1,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_torreyc_stdout_equalto_with_stdin \
    "Should parse when given the -ir -p -S flags, in that order" \
    $compiler_path \
    "-ir -p -S" \
    "42" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "node_type": "IntegerExpr",
    "token": {
      "rawText": "42",
      "endIndex": 2,
      "beginIndex": 0,
      "type": "INTEGER",
      "beginLineIndex": 0,
      "startPos": {
        "col": 1,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_torreyc_stdout_equalto_with_stdin \
    "Should parse when given the -S -p -ir flags, in that order" \
    $compiler_path \
    "-S -p -ir" \
    "42" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "node_type": "IntegerExpr",
    "token": {
      "rawText": "42",
      "endIndex": 2,
      "beginIndex": 0,
      "type": "INTEGER",
      "beginLineIndex": 0,
      "startPos": {
        "col": 1,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_torreyc_stdout_equalto_with_stdin \
    "Should parse when given the -S -ir -p flags, in that order" \
    $compiler_path \
    "-S -ir -p" \
    "42" \
    '{
  "children": [{
    "evalType": "INTEGER",
    "node_type": "IntegerExpr",
    "token": {
      "rawText": "42",
      "endIndex": 2,
      "beginIndex": 0,
      "type": "INTEGER",
      "beginLineIndex": 0,
      "startPos": {
        "col": 1,
        "line": 1
      },
      "endPos": {
        "col": 3,
        "line": 1
      }
    }
  }],
  "token": "null"
}'

  assert_torreyc_stdout_equalto_with_stdin \
    "Should generate IR when given the -ir -S flags, in that order" \
    $compiler_path \
    "-ir -S" \
    "42" \
    "t0 = 42"

  assert_torreyc_stdout_equalto_with_stdin \
    "Should generate IR when given the -S -ir flags, in that order" \
    $compiler_path \
    "-S -ir" \
    "42" \
    "t0 = 42"

  echo -e "\n"
}
