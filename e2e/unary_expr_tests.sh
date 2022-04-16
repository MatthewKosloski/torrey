# !/bin/bash

source _utils.sh

run_unary_expr_tests () {
  echo "Tests for \"(\" \"-\" expr \")\""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis and no operand" \
    $1 \
    "-)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '-' instead (1:1)

-)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "- 69)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '-' instead (1:1)

- 69)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no operand" \
    $1 \
    "(-)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(-)
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and no operand" \
    $1 \
    "(-" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:3)

(-
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(- 420" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:4)

(- 420
   ^^^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to zero if the operand is literal 0" \
    $1 \
    "(print (- 0))" \
    "0"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is literal true" \
    $1 \
    "(- true)" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:4)

(- 420
   ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is literal false" \
    $1 \
    "(- false)" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:4)

(- 420
   ^^^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to a positive integer if the operand is an integer literal nested within a unary expression" \
    $1 \
    "(println
       (- (- 0))
       (- (- 1)))" \
    "0
1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to a negative integer if the operand is an integer literal nested within a double unary expression" \
    $1 \
    "(print (- (- (- 25))))" \
    "-25"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to zero if the operand is 0 nested in a double unary expression" \
    $1 \
    "(print (- (- (- 0))))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate unary expressions of arbitrary depth" \
    $1 \
    "(println (- (- (- (- (- (- (- 3141519))))))))
     (println (- (- (- (- (- (- (- (- (- (- (- (- 6942069)))))))))))))
     (println (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- 99999999))))))))))))))))))))" \
    "-3141519
6942069
-99999999"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an integer literal operand" \
    $1 \
    "(print
       (- 42))" \
    "-42"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an identifier operand" \
    $1 \
    "(print
       (let [a 400]
         (- a)))" \
    "-400"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a binary addition expression operand" \
    $1 \
    "(print
       (-
         (+ 3 (- 5))))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a binary subtraction expression operand" \
    $1 \
    "(print
       (-
         (-
           420
           69)))" \
    "-351"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a binary multiplication expression operand" \
    $1 \
    "(print (- (* 9 0)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a binary division expression operand" \
    $1 \
    "(print
       (- (/ 12 (- 5))))" \
    "2"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is a print expression" \
    $1 \
    "(print
       (- (print 42)))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is a println expression" \
    $1 \
    "(print
       (- (println 42)))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should accept a let expression that evaluates to an integer" \
    $1 \
    "(print
       (- (let [] 42)))" \
    "-42"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is a let expression that does not evaluate to an integer" \
    $1 \
    "(- (let [] true))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an if-then expression that evaluates to an integer" \
    $1 \
    "(print
       (- (if true 69420)))" \
    "-69420"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is an if-then expression that does not evaluate to an integer" \
    $1 \
    "(- (if true (== 2 3)))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an if-then-else expression that evaluates to an integer" \
    $1 \
    "(print
       (- (if false 0 1)))" \
    "-1"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is an if-then-else expression that does not evaluate to an integer" \
    $1 \
    "(- (if
         false
         (>= 1 2)
         (< 5 6)))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is a relational equal to expression" \
    $1 \
    "(- (== 32 10))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is a relational less than expression" \
    $1 \
    "(- (< 32 10))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is a relational less than or equal to expression" \
    $1 \
    "(- (<= 32 10))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is a relational greater than expression" \
    $1 \
    "(- (> 32 10))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the operand is a relational greater than or equal to expression" \
    $1 \
    "(- (>= 32 10))" \
    ""

  echo -e "\n"
}
