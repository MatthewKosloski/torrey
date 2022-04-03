#!/bin/bash

source _utils.sh

run_binary_add_expr_tests () {
  echo "Tests for \"(\" \"+\" expr expr \")\""

  assert_stderr \
    $1 \
    "Should report a syntax error if no left parenthesis and no operands" \
    "+)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '+' instead (1:1)

+)
^

1 Error"

  assert_stderr \
    $1 \
    "Should report a syntax error if no left parenthesis" \
    "+ 32 10)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '+' instead (1:1)

+ 32 10)
^

1 Error"

  assert_stderr \
    $1 \
    "Should report a syntax error if no operands" \
    "(+)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(+)
  ^

1 Error"

  assert_stderr \
    $1 \
    "Should report a syntax error if only one operand" \
    "(+ 32)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:6)

(+ 32)
     ^

1 Error"

  assert_stderr \
    $1 \
    "Should report a syntax error if no right parenthesis and no operands" \
    "(+" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(-)
  ^

1 Error"

  assert_stderr \
    $1 \
    "Should report a syntax error if no right parenthesis and only one operand" \
    "(+ 1" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(-)
  ^

1 Error"

  assert_stderr \
    $1 \
    "Should report a syntax error if no right parenthesis" \
    "(+ 4 5" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:6)

(+ 4 5
     ^

1 Error"

  assert_stderr \
    $1 \
    "Should report a type error if both operands are boolean literals" \
    "(+ false true)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is a boolean literal" \
    "(+ true 4)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is a boolean literal" \
    "(+ 4 false)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if both operands are print expressions" \
    "(+ (print 1) (print 2))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is a print expression" \
    "(+ (print 1) 4)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is a print expression" \
    "(+ 4 (print 66))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if both operands are println expressions" \
    "(+ (println 1) (println 2))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is a println expression" \
    "(+ (println 1) 4)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is a println expression" \
    "(+ 4 (println 66))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if both operands are identifiers that do not evaluate to integers" \
    "(let [a true b false]
      (+ a b))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is an identifier that does not evaluate to an integer" \
    "(let [a true b 5]
      (+ a b))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is an identifier that does not evaluate to an integer" \
    "(let [a 5 b true]
      (+ a b))" \
    ""

  assert_stdout \
    $1 \
    "Should compute the sum of two integer literals" \
    "(print (+ 2 3))" \
    "5"

  assert_stdout \
    $1 \
    "Should compute the sum of an integer literal and an identifer that evaluates to an integer" \
    "(print (let [a 3] (+
      2
      a
    )))" \
    "5"

  assert_stdout \
    $1 \
    "Should compute the sum of an identifer that evaluates to an integer and an integer literal" \
    "(print (let [a 3] (+
      a
      2
    )))" \
    "5"

  assert_stdout \
    $1 \
    "Should compute the sum of an identifer that evaluates to an integer literal" \
    "(print (let [a 3] (+
      a
      a
    )))" \
    "6"

  assert_stdout \
    $1 \
    "Should compute the sum two identifiers that evaluate to integer literals" \
    "(print (let [a 3 b 2] (+
      b
      a
    )))" \
    "5"

  assert_stdout \
    $1 \
    "Should compute the sum of an integer literal and a unary expression" \
    "(print (+
       1
       (- 2)))" \
    "-1"

  assert_stdout \
    $1 \
    "Should compute the sum of a unary expression and an integer literal" \
    "(print (+
       (- 2)
       2))" \
    "0"

  assert_stdout \
    $1 \
    "Should compute the sum of two unary expressions" \
    "(print (+
       (- 10)
       (- 5)))" \
    "-15"

  assert_stdout \
    $1 \
    "Should compute the sum of an integer literal and a binary addition expression" \
    "(print (+
       1
       (+ 2 3)))" \
    "6"

  assert_stdout \
    $1 \
    "Should compute the sum of a binary addition expression and an integer literal" \
    "(print (+
       (+ (- 5) (- 5))
       5))" \
    "-5"

  assert_stdout \
    $1 \
    "Should compute the sum of two binary addition expressions" \
    "(print (+
       (+ 69 (- 0))
       (+ 0 (- 70))))" \
    "-1"

  assert_stdout \
    $1 \
    "Should compute the sum of an integer literal and a binary subtraction expression" \
    "(print (+
       1
       (- 2 3)))" \
    "0"

  assert_stdout \
    $1 \
    "Should compute the sum of a binary subtraction expression and an integer literal" \
    "(print (+
       (- (- 5) (- 5))
       5))" \
    "5"

  assert_stdout \
    $1 \
    "Should compute the sum of two binary subtraction expressions" \
    "(print (+
       (- 69 (- 0))
       (- 0 (- 70))))" \
    "139"

  assert_stdout \
    $1 \
    "Should compute the sum of an integer literal and a binary multiplication expression" \
    "(print (+
       1
       (* 2 3)))" \
    "7"

  assert_stdout \
    $1 \
    "Should compute the sum of a binary multiplication expression and an integer literal" \
    "(print (+
       (* (- 5) (- 5))
       5))" \
    "30"

  assert_stdout \
    $1 \
    "Should compute the sum of two binary multiplication expressions" \
    "(print (+
       (* 69 (- 0))
       (* 0 (- 70))))" \
    "0"

  assert_stdout \
    $1 \
    "Should compute the sum of an integer literal and a binary division expression" \
    "(print (+
       1
       (/ 2 3)))" \
    "1"

  assert_stdout \
    $1 \
    "Should compute the sum of a binary division expression and an integer literal" \
    "(print (+
       (/ (- 5) (- 5))
       5))" \
    "6"

  assert_stdout \
    $1 \
    "Should compute the sum of two binary division expressions" \
    "(print (+
       (/ 69 (- 1))
       (/ 0 (- 70))))" \
    "-69"

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is a relational equal to expression" \
    "(+ (== 2 3) 1)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is a relational equal to expression" \
    "(+ 1 (== 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if both operands are relational equal to expressions" \
    "(+ 1 (== 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is a relational less than expression" \
    "(+ (< 2 3) 1)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is a relational less than expression" \
    "(+ 1 (< 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if both operands are relational less than expressions" \
    "(+ 1 (< 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is a relational less than or equal to expression" \
    "(+ (<= 2 3) 1)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is a relational less than or equal to expression" \
    "(+ 1 (<= 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if both operands are relational less than or equal to expressions" \
    "(+ 1 (<= 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is a relational greater than expression" \
    "(+ (> 2 3) 1)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is a relational greater than expression" \
    "(+ 1 (> 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if both operands are relational greater than expressions" \
    "(+ 1 (> 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the first operand is a relational greater than or equal to expression" \
    "(+ (>= 2 3) 1)" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if the second operand is a relational greater than or equal to expression" \
    "(+ 1 (>= 2 3))" \
    ""

  assert_stderr \
    $1 \
    "Should report a type error if both operands are relational greater than or equal to expressions" \
    "(+ 1 (>= 2 3))" \
    ""

  assert_stdout \
    $1 \
    "Should compute the sum of an integer literal and a let expression that evaluates to an integer" \
    "(print (+
       1
       (let [] 0)))" \
    "1"

  assert_stdout \
    $1 \
    "Should compute the sum of a let expression that evaluates to an integer and an integer literal" \
    "(print (+
       (let [] 0)
       1)" \
    "1"

  assert_stdout \
    $1 \
    "Should compute the sum of two let expressions that evaluate to integers" \
    "(print (+
       (let [] 32)
       (let [] 10)))" \
    "42"

  assert_stdout \
    $1 \
    "Should compute the sum of an integer literal and a let expression that evaluates to an identifier that evaluates to an integer" \
    "(print (+
       1
       (let [a 0] a)))" \
    "1"

  assert_stdout \
    $1 \
    "Should compute the sum of a let expression that evaluates to an identifier that evaluates to an integer and an integer literal" \
    "(print (+
       (let [a 0] a)
       1)" \
    "1"

  assert_stdout \
    $1 \
    "Should compute the sum of two let expressions that evaluate to identiers that evaluate to integers" \
    "(print (+
       (let [a 32] a)
       (let [a 10] a)))" \
    "42"

  echo -e "\n"
}
