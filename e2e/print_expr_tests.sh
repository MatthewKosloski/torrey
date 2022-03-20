#!/bin/bash

source _utils.sh

run_print_expr_tests () {
  echo "Tests for \"(\" \"print\" expr+ \")\""

  assert_stderr \
    $1 \
    "Should be a syntax error if no left parenthesis and no operand" \
    "print)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'print' instead (1:1)

print)
^^^^^

1 Error" \

  assert_stderr \
    $1 \
    "Should be a syntax error if no left parenthesis" \
    "print 69)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'print' instead (1:1)

print 69)
^^^^^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no operand" \
    "(print)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:7)

(print)
      ^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no right parenthesis and no operand" \
    "(print" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:7)

(print)
      ^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no right parenthesis" \
    "(print 420" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:8)

(print 420
       ^^^

1 Error"

  assert_stdout \
    $1 \
    "Should accept an integer literal operand" \
    "(print 3)" \
    "3"

  assert_stdout \
    $1 \
    "Should accept more than one integer literal operands" \
    "(print 1 2 3)" \
    "123"

  assert_stdout \
    $1 \
    "Should accept an identifier operand" \
    "(let [a 4]
       (print a))" \
    "4"

  assert_stdout \
    $1 \
    "Should accept more than one identifier operands" \
    "(let [a 4 b 100]
       (print a b))" \
    "4100"

  assert_stdout \
    $1 \
    "Should accept a unary operand" \
    "(print (- 1))" \
    "-1"

  assert_stdout \
    $1 \
    "Should accept more than one unary operands" \
    "(print (- 1) (- 10) (- 999))" \
    "-1-10-999"

  assert_stdout \
    $1 \
    "Should accept a binary addition operand" \
    "(print (+ 2 3))" \
    "5"

  assert_stdout \
    $1 \
    "Should accept more than one binary addition operands" \
    "(print (+ 1 3) (+ 2 (- 3)))" \
    "4-1"

  assert_stdout \
    $1 \
    "Should accept a binary subtraction operand" \
    "(print (- 2 3))" \
    "-1"

  assert_stdout \
    $1 \
    "Should accept more than one binary subtraction operands" \
    "(print (- 2 3) (- 5 3))" \
    "-12"

  assert_stdout \
    $1 \
    "Should accept a binary multiplication operand" \
    "(print (* 100 (- 3)))" \
    "-300"

  assert_stdout \
    $1 \
    "Should accept more than one binary multiplication operands" \
    "(print (* 5 (- 9)) (* 8 3))" \
    "-4524"

  assert_stdout \
    $1 \
    "Should accept a binary division operand" \
    "(print (/ 5 2))" \
    "2"

  assert_stdout \
    $1 \
    "Should accept more than one binary division operands" \
    "(print (/ 2 20) (/ (- 16) 4))" \
    "0-4"

  assert_stderr \
    $1 \
    "Should be a semantic error if the operand is a print expression" \
    "(print (print 42))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'print' because it does not evaluate to a known type (1:9)

(print (print 42))
        ^^^^^

1 Error"

  assert_stderr \
    $1 \
    "Should be a semantic error if the operands are print expressions" \
    "(print (print 42) (print (- 42)))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'print' because it does not evaluate to a known type (1:9)

(print (print 42) (print (- 42)))
        ^^^^^

Cannot print operand 'print' because it does not evaluate to a known type (1:20)

(print (print 42) (print (- 42)))
                   ^^^^^

2 Errors"

  assert_stderr \
    $1 \
    "Should be a semantic error if the operand is a println expression" \
    "(print (println 42))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'println' because it does not evaluate to a known type (1:9)

(print (println 42))
        ^^^^^^^

1 Error"

  assert_stderr \
    $1 \
    "Should be a semantic error if the operands are println expressions" \
    "(print (println 42) (println (- 42)))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'println' because it does not evaluate to a known type (1:9)

(print (println 42) (println (- 42)))
        ^^^^^^^

Cannot print operand 'println' because it does not evaluate to a known type (1:22)

(print (println 42) (println (- 42)))
                     ^^^^^^^

2 Errors"

  assert_stdout \
    $1 \
    "Should accept a let expression" \
    "(print (let [] 42))" \
    "42"

  assert_stdout \
    $1 \
    "Should accept an if-then expression" \
    "(print (if true 69420))" \
    "69420"

  assert_stdout \
    $1 \
    "Should accept an if-then-else expression" \
    "(print (if false 0 1))" \
    "1"

  echo -e "\n"
}
