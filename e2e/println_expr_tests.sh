#!/bin/bash

source _utils.sh

run_println_expr_tests () {
  echo "Tests for \"(\" \"println\" expr+ \")\""

  assert_stderr \
    $1 \
    "Should be a syntax error if no left parenthesis and no operand" \
    "println)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'println' instead (1:1)

println)
^^^^^^^

1 Error" \

  assert_stderr \
    $1 \
    "Should be a syntax error if no left parenthesis" \
    "println 69)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'println' instead (1:1)

println 69)
^^^^^^^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no operand" \
    "(println)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:9)

(println)
        ^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no right parenthesis and no operand" \
    "(println" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:7)

(println)
        ^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no right parenthesis" \
    "(println 420" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:10)

(println 420
         ^^^

1 Error"

  assert_stdout \
    $1 \
    "Should accept an integer literal operand" \
    "(println 3)" \
    "3"

  assert_stdout \
    $1 \
    "Should accept more than one integer literal operands" \
    "(println 1 2 3)" \
    "1
2
3"

  assert_stdout \
    $1 \
    "Should accept an identifier operand" \
    "(let [a 4]
       (println a))" \
    "4"

  assert_stdout \
    $1 \
    "Should accept more than one identifier operands" \
    "(let [a 4 b 100]
       (println a b))" \
    "4
100"

  assert_stdout \
    $1 \
    "Should accept a unary operand" \
    "(println (- 1))" \
    "-1"

  assert_stdout \
    $1 \
    "Should accept more than one unary operands" \
    "(println (- 1) (- 10) (- 999))" \
    "-1
-10
-999"

  assert_stdout \
    $1 \
    "Should accept a binary addition operand" \
    "(println (+ 2 3))" \
    "5"

  assert_stdout \
    $1 \
    "Should accept more than one binary addition operands" \
    "(println (+ 1 3) (+ 2 (- 3)))" \
    "4
-1"

  assert_stdout \
    $1 \
    "Should accept a binary subtraction operand" \
    "(println (- 2 3))" \
    "-1"

  assert_stdout \
    $1 \
    "Should accept more than one binary subtraction operands" \
    "(println (- 2 3) (- 5 3))" \
    "-1
2"

  assert_stdout \
    $1 \
    "Should accept a binary multiplication operand" \
    "(println (* 100 (- 3)))" \
    "-300"

  assert_stdout \
    $1 \
    "Should accept more than one binary multiplication operands" \
    "(println (* 5 (- 9)) (* 8 3))" \
    "-45
24"

  assert_stdout \
    $1 \
    "Should accept a binary division operand" \
    "(println (/ 5 2))" \
    "2"

  assert_stdout \
    $1 \
    "Should accept more than one binary division operands" \
    "(println (/ 2 20) (/ (- 16) 4))" \
    "0
-4"

  assert_stderr \
    $1 \
    "Should be a semantic error if the operand is a print expression" \
    "(println (print 42))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'print' because it does not evaluate to a known type (1:11)

(println (print 42))
          ^^^^^

1 Error"

  assert_stderr \
    $1 \
    "Should be a semantic error if the operands are print expressions" \
    "(println (print 42) (print (- 42)))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'print' because it does not evaluate to a known type (1:11)

(println (print 42) (print (- 42)))
          ^^^^^

Cannot print operand 'print' because it does not evaluate to a known type (1:22)

(println (print 42) (print (- 42)))
                     ^^^^^

2 Errors"

  assert_stderr \
    $1 \
    "Should be a semantic error if the operand is a println expression" \
    "(println (println 42))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'println' because it does not evaluate to a known type (1:11)

(println (println 42))
          ^^^^^^^

1 Error"

  assert_stderr \
    $1 \
    "Should be a semantic error if the operands are println expressions" \
    "(println (println 42) (println (- 42)))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'println' because it does not evaluate to a known type (1:11)

(println (println 42) (println (- 42)))
          ^^^^^^^

Cannot print operand 'println' because it does not evaluate to a known type (1:24)

(println (println 42) (println (- 42)))
                       ^^^^^^^

2 Errors"

  assert_stdout \
    $1 \
    "Should accept a let expression" \
    "(println (let [] 42))" \
    "42"

  assert_stdout \
    $1 \
    "Should accept an if-then expression" \
    "(println (if true 69420))" \
    "69420"

  assert_stdout \
    $1 \
    "Should accept an if-then-else expression" \
    "(println (if false 0 1))" \
    "1"

  echo -e "\n"
}
