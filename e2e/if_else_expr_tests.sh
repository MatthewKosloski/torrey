# !/bin/bash

source _utils.sh

run_if_else_expr_tests () {
  echo "Tests for \"(\" \"if\" expr expr expr \")\""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "if false 0 1)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'if' instead (1:1)

if false 0 1)
^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(if false 0 1" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:14)

(if false 0 1
             ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the test is a print expression" \
    $1 \
    "(if (print 1) 1 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (print 1) 1 0)
     ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the test is a println expression" \
    $1 \
    "(if (println 1) 1 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (println 1) 1 0)
     ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the test is an empty let expression" \
    $1 \
    "(if (let []) 1 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (let []) 1 0)
     ^^^

1 Error"

# Should report a type error if the branches evaluate to different types

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a print expression and the second branch is a boolean literal" \
    $1 \
    "(if true (print 0) false)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (print 0) false)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a print expression and the second branch is a relational equal to expression" \
    $1 \
    "(if true (print 0) (== 0 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (print 0) (== 0 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a print expression and the second branch is a relational less than expression" \
    $1 \
    "(if true (print 0) (< 1 0))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (print 0) (< 1 0))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a print expression and the second branch is a relational less than or equal to expression" \
    $1 \
    "(if true (print 0) (<= 1 0))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (print 0) (<= 1 0))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a print expression and the second branch is a relational greater than expression" \
    $1 \
    "(if true (print 0) (> 0 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (print 0) (> 0 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a print expression and the second branch is a relational greater than or equal to expression" \
    $1 \
    "(if true (print 0) (>= 0 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (print 0) (>= 0 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is an empty let expression and the second branch is an integer literal" \
    $1 \
    "(if true (let []) 1)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (let []) 1)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a boolean literal and the second branch is a print expression" \
    $1 \
    "(if true true (print 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true true (print 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational equal to expression and the second branch is a print expression" \
    $1 \
    "(if true (== 0 0) (print 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (== 0 0) (print 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational less than expression and the second branch is a print expression" \
    $1 \
    "(if true (< 0 1) (print 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (< 0 1) (print 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational less than or equal to expression and the second branch is a print expression" \
    $1 \
    "(if true (<= 0 1) (print 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (<= 0 1) (print 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational greater than expression and the second branch is a print expression" \
    $1 \
    "(if true (> 1 0) (print 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (> 1 0) (print 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational greater than or equal to expression and the second branch is a print expression" \
    $1 \
    "(if true (>= 1 0) (print 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (>= 1 0) (print 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a boolean literal and the second branch is an integer literal" \
    $1 \
    "(if false false 0)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false false 0)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational equal to expression and the second branch is an integer literal" \
    $1 \
    "(if false (== 0 1) 0)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false (== 0 1) 0)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational less than expression and the second branch is an integer literal" \
    $1 \
    "(if false (< 1 0) 0)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false (< 1 0) 0)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational less than or equal to expression and the second branch is an integer literal" \
    $1 \
    "(if false (<= 1 0) 0)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false (<= 1 0) 0)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational greater than expression and the second branch is an integer literal" \
    $1 \
    "(if false (> 0 1) 0)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false (> 0 1) 0)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is a relational greater than or equal to expression and the second branch is an integer literal" \
    $1 \
    "(if false (>= 0 1) 0)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false (>= 0 1) 0)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is an integer literal and the second branch is an empty let expression" \
    $1 \
    "(if false 0 (let []))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false 0 (let []))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is an integer literal and the second branch is a boolean literal" \
    $1 \
    "(if false 1 true)" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false 1 true)
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is an integer literal and the second branch is a relational equal to expression" \
    $1 \
    "(if false 1 (== 0 0))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false 1 (== 0 0))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is an integer literal and the second branch is a relational less than expression" \
    $1 \
    "(if false 1 (< 0 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false 1 (< 0 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is an integer literal and the second branch is a relational less than or equal to expression" \
    $1 \
    "(if false 1 (<= 0 1))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false 1 (<= 0 1))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is an integer literal and the second branch is a relational greater than expression" \
    $1 \
    "(if false 1 (> 1 0))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false 1 (> 1 0))
 ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first branch is an integer literal and the second branch is a relational greater than or equal to expression" \
    $1 \
    "(if false 1 (>= 1 0))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if false 1 (>= 1 0))
 ^^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should not report a type error if the branches are print(ln) expressions" \
    $1 \
    "(if true (print 1) (print 0))
     (if true (print 1) (println 0))
     (if true (println 1) (print 0))
     (if true (println 1) (println 0))" \
    "111
1"

  assert_exec_stdout_equalto_with_stdin \
    "Should not report a type error if the branches are empty let and print expressions" \
    $1 \
    "(if true (let []) (print 0))
     (if true (print 1) (let []))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should not report a type error if the branches are empty let and println expressions" \
    $1 \
    "(if true (let []) (println 0))
     (if true (println 1) (let []))" \
    "1"

  # Should take the else branch if the test evaluates to zero

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is literal 0" \
    $1 \
    "(if 0
        (print 1)
        (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is an identifier that evaluates to literal zero" \
    $1 \
    "(let [x 0]
       (if x
         (print 1)
         (print 0)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a unary expression whose operand is literal zero" \
    $1 \
    "(if (- 0)
         (print 1)
         (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a binary add expression that evaluates to zero" \
    $1 \
    "(if (+ 0 0)
         (print 1)
         (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a binary sub expression that evaluates to zero" \
    $1 \
    "(if (- 1 1)
         (print 1)
         (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a binary multiple expression that evaluates to zero" \
    $1 \
    "(if (* 1 0)
         (print 1)
         (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a binary division expression that evaluates to zero" \
    $1 \
    "(if (/ 0 1)
         (print 1)
         (print 0))" \
    "0"

  # Should take the else branch if the test evaluates to false

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test evalutes to literal false" \
    $1 \
    "(if false
        (print 1)
        (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is an identifier that evaluates to literal false" \
    $1 \
    "(let [a false]
       (if a
         (print 1)
         (print 0)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a relational equal to expression that evaluates to false" \
    $1 \
    "(if (== 1 0)
        (print 1)
        (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a relational less than expression that evaluates to false" \
    $1 \
    "(if (< 1 0)
       (print 1)
       (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a relational less than or equal to expression that evaluates to false" \
    $1 \
    "(if (<= 10 1)
       (print 1)
       (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a relational greater than expression that evaluates to false" \
    $1 \
    "(if (> 1 5)
       (print 1)
       (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is a relational greater than or equal to expression that evaluates to false" \
    $1 \
    "(if (>= 10 11)
       (print 1)
       (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is an identifier that evaluates to a relational equal to expression that evaluates to false" \
    $1 \
    "(let [x (== 1 0)]
       (if x
         (print 1)
         (print 0)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is an identifier that evaluates to a relational less than expression that evaluates to false" \
    $1 \
    "(let [x (< 1 0)]
       (if x
         (print 1)
         (print 0)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is an identifier that evaluates to a relational less than or equal to expression that evaluates to false" \
    $1 \
    "(let [x (<= 10 1)]
       (if x
         (print 1)
         (print 0)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is an identifier that evaluates to a relational greater than expression that evaluates to false" \
    $1 \
    "(let [x (> 1 5)]
       (if x
         (print 1)
         (print 0)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the else branch if the test is an identifier that evaluates to a relational greater than or equal to expression that evaluates to false" \
    $1 \
    "(let [x (>= 10 11)]
       (if x
         (print 1)
         (print 0)))" \
    "0"

  # Should take the then branch if the test evaluates to a negative integer

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a negative integer" \
    $1 \
    "(if (- 1)
        (print 1)
        (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to an identifier that evaluates to a negative integer" \
    $1 \
    "(let [x (- 2)]
       (if x
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a binary add expression that evaluates to a negative integer" \
    $1 \
    "(if (+ 1 (- 2))
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a binary sub expression that evaluates to a negative integer" \
    $1 \
    "(if (- 1 10)
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a binary multiplication expression that evaluates to a negative integer" \
    $1 \
    "(if (* (- 1) 2)
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a binary division expression that evaluates to a negative integer" \
    $1 \
    "(if (/ 16 (- 4))
       (print 1)
       (print 0))" \
    "1"

  # Should take the then branch if the test evaluates to a positive integer

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a positive integer literal" \
    $1 \
    "(if 1
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to an identifier that evaluates to a positive integer literal" \
    $1 \
    "(let [x 2]
       (if x
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a unary minus expression that evaluates to a positive integer" \
    $1 \
    "(if (- (- 3))
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a binary add expression that evaluates to a positive integer" \
    $1 \
    "(if (+ 0 4)
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a binary sub expression that evaluates to a positive integer" \
    $1 \
    "(if (- 10 5)
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a binary multiplication expression that evaluates to a positive integer" \
    $1 \
    "(if (* 6 1)
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a binary division expression that evaluates to a positive integer" \
    $1 \
    "(if (/ 100 2)
       (print 1)
       (print 0))" \
    "1"

# Should take the then branch if the test evaluates to true

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to literal true" \
    $1 \
    "(if true
       (print 1)
       (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to an identifier that evaluates to literal true" \
    $1 \
    "(let [x true]
       (if x
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is a relational equal to expression that evaluates to true" \
    $1 \
    "(if (== 1 1)
      (print 1)
      (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is a relational less than expression that evaluates to true" \
    $1 \
    "(if (< 0 1)
      (print 1)
      (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is a relational less than or equal to expression that evaluates to true" \
    $1 \
    "(if (<= 1 10)
      (print 1)
      (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is a relational greater than expression that evaluates to true" \
    $1 \
    "(if (> 5 1)
      (print 1)
      (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is a relational greater than or equal to expression that evaluates to true" \
    $1 \
    "(if (>= 11 10)
      (print 1)
      (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is an identifier that evaluates to a relational equal to expression that evaluates to true" \
    $1 \
    "(let [x (== 1 1)]
       (if x
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is an identifier that evaluates to a relational less than expression that evaluates to true" \
    $1 \
    "(let [x (< 0 1)]
       (if x
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is an identifier that evaluates to a relational less than or equal to expression that evaluates to true" \
    $1 \
    "(let [x (<= 1 10)]
       (if x
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is an identifier that evaluates to a relational greater than expression that evaluates to true" \
    $1 \
    "(let [x (> 5 1)]
       (if x
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test is an identifier that evaluates to a relational greater than or equal to expression that evaluates to true" \
    $1 \
    "(let [x (>= 11 10)]
       (if x
         (print 1)
         (print 0)))" \
    "1"

  echo -e "\n"
}
