#!/bin/bash

source _utils.sh

run_unary_expr_tests () {
  echo "Tests for \"(\" \"-\" expr \")\""

  assert_stderr \
    $1 \
    "Should be a syntax error if no left parenthesis and no operand" \
    "-)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '-' instead (1:1)

-)
^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no left parenthesis" \
    "- 69)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '-' instead (1:1)

- 69)
^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no operand" \
    "(-)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(-)
  ^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no right parenthesis and no operand" \
    "(-" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(-)
  ^

1 Error"

  assert_stderr \
    $1 \
    "Should be a syntax error if no right parenthesis" \
    "(- 420" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:4)

(- 420
   ^^^

1 Error"

  assert_stdout \
    $1 \
    "Should evaluate to zero if operand is literal 0" \
    "(print (- 0))" \
    "0"

  assert_stderr \
    $1 \
    "Should be a semantic error if operand is literal true" \
    "(- true)" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:4)

(- 420
   ^^^

1 Error"

  assert_stderr \
    $1 \
    "Should be a semantic error if operand is literal false" \
    "(- false)" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:4)

(- 420
   ^^^

1 Error"

  assert_stdout \
    $1 \
    "Should evaluate to a positive integer if operand is an integer literal nested within a unary expression" \
    "(println
       (- (- 0))
       (- (- 1)))" \
    "0
1"

  assert_stdout \
    $1 \
    "Should evaluate to a negative integer if operand is an integer literal nested within a double unary expression" \
    "(print (- (- (- 25))))" \
    "-25"

  assert_stdout \
    $1 \
    "Should evaluate to zero if operand is 0 nested in a double unary expression" \
    "(print (- (- (- 0))))" \
    "0"

  assert_stdout \
    $1 \
    "Should evaluate unary expressions of arbitrary depth" \
    "(println (- (- (- (- (- (- (- 3141519))))))))
     (println (- (- (- (- (- (- (- (- (- (- (- (- 6942069)))))))))))))
     (println (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- 99999999))))))))))))))))))))" \
    "-3141519
6942069
-99999999"

  assert_stdout \
    $1 \
    "Should accept an integer literal operand" \
    "(print
       (- 42))" \
    "-42"

  assert_stdout \
    $1 \
    "Should accept an identifier operand" \
    "(print
       (let [a 400]
         (- a)))" \
    "-400"

  assert_stdout \
    $1 \
    "Should accept a binary addition expression operand" \
    "(print
       (-
         (+ 3 (- 5))))" \
    "2"

  assert_stdout \
    $1 \
    "Should accept a binary subtraction expression operand" \
    "(print
       (-
         (-
           420
           69)))" \
    "-351"

  assert_stdout \
    $1 \
    "Should accept a binary multiplication expression operand" \
    "(print (- (* 9 0)))" \
    "0"

  assert_stdout \
    $1 \
    "Should accept a binary division expression operand" \
    "(print
       (- (/ 12 (- 5))))" \
    "2"

  assert_stderr \
    $1 \
    "Should be a semantic error if operand is a print expression" \
    "(print
       (- (print 42)))" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:4)

(- 420
   ^^^

1 Error"

  assert_stderr \
    $1 \
    "Should be a semantic error if operand is a println expression" \
    "(print
       (- (println 42)))" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:4)

(- 420
   ^^^

1 Error"

  assert_stdout \
    $1 \
    "Should accept a let expression" \
    "(print
       (- (let [] 42)))" \
    "-42"

  assert_stdout \
    $1 \
    "Should accept an if-then expression" \
    "(print
       (- (if true 69420)))" \
    "-69420"

  assert_stdout \
    $1 \
    "Should accept an if-then-else expression" \
    "(print
       (- (if false 0 1)))" \
    "-1"

  echo -e "\n"
}
