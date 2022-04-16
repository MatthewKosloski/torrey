#!/bin/bash

source _utils.sh

run_println_expr_tests () {
  echo "Tests for \"(\" \"println\" expr+ \")\""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis and no operand" \
    $1 \
    "println)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'println' instead (1:1)

println)
^^^^^^^

1 Error" \

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "println 69)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'println' instead (1:1)

println 69)
^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no operand" \
    $1 \
    "(println)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:9)

(println)
        ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and no operand" \
    $1 \
    "(println" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:7)

(println)
        ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(println 420" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:10)

(println 420
         ^^^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an integer literal operand" \
    $1 \
    "(println 3)" \
    "3"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept more than one integer literal operands" \
    $1 \
    "(println 1 2 3)" \
    "1
2
3"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an identifier operand" \
    $1 \
    "(let [a 4]
       (println a))" \
    "4"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept more than one identifier operands" \
    $1 \
    "(let [a 4 b 100]
       (println a b))" \
    "4
100"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a unary operand" \
    $1 \
    "(println (- 1))" \
    "-1"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept more than one unary operands" \
    $1 \
    "(println (- 1) (- 10) (- 999))" \
    "-1
-10
-999"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a binary addition operand" \
    $1 \
    "(println (+ 2 3))" \
    "5"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept more than one binary addition operands" \
    $1 \
    "(println (+ 1 3) (+ 2 (- 3)))" \
    "4
-1"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a binary subtraction operand" \
    $1 \
    "(println (- 2 3))" \
    "-1"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept more than one binary subtraction operands" \
    $1 \
    "(println (- 2 3) (- 5 3))" \
    "-1
2"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a binary multiplication operand" \
    $1 \
    "(println (* 100 (- 3)))" \
    "-300"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept more than one binary multiplication operands" \
    $1 \
    "(println (* 5 (- 9)) (* 8 3))" \
    "-45
24"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a binary division operand" \
    $1 \
    "(println (/ 5 2))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept more than one binary division operands" \
    $1 \
    "(println (/ 2 20) (/ (- 16) 4))" \
    "0
-4"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a semantic error if the operand is a print expression" \
    $1 \
    "(println (print 42))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'print' because it does not evaluate to a known type (1:11)

(println (print 42))
          ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a semantic error if the operands are print expressions" \
    $1 \
    "(println (print 42) (print (- 42)))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'print' because it does not evaluate to a known type (1:11)

(println (print 42) (print (- 42)))
          ^^^^^

Cannot print operand 'print' because it does not evaluate to a known type (1:22)

(println (print 42) (print (- 42)))
                     ^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a semantic error if the operand is a println expression" \
    $1 \
    "(println (println 42))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'println' because it does not evaluate to a known type (1:11)

(println (println 42))
          ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a semantic error if the operands are println expressions" \
    $1 \
    "(println (println 42) (println (- 42)))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'println' because it does not evaluate to a known type (1:11)

(println (println 42) (println (- 42)))
          ^^^^^^^

Cannot print operand 'println' because it does not evaluate to a known type (1:24)

(println (println 42) (println (- 42)))
                       ^^^^^^^

2 Errors"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a let expression" \
    $1 \
    "(println (let [] 42))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an if-then expression" \
    $1 \
    "(println (if true 69420))" \
    "69420"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an if-then-else expression" \
    $1 \
    "(println (if false 0 1))" \
    "1"

  echo -e "\n"
}
