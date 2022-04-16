#!/bin/bash

source _utils.sh

run_if_expr_tests () {
  echo "Tests for \"(\" \"if\" expr expr \")\""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis and no test expression" \
    $1 \
    "if)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'if' instead (1:1)

if)
^^

1 Error"


  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "if true)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'if' instead (1:1)

if true)
^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no test expression" \
    $1 \
    "(if)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:4)

(if)
   ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and no test expression" \
    $1 \
    "(if" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(-)
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(if true" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:7)

(let []
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no then expression" \
    $1 \
    "(if true)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:9)

(if true)
        ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the test is a print expression" \
    $1 \
    "(if (print 1) 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (print 1) 0)
     ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the test is a println expression" \
    $1 \
    "(if (println 1) 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (println 1) 0)
     ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the test is an empty let expression" \
    $1 \
    "(if (let []) 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (let []) 0)
     ^^^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the then branch if the test evaluates to zero" \
    $1 \
    "(if 0
        (print 42))
     (let [x 0]
       (if x
         (print 42)))
     (if (- 0)
         (print 42))
     (if (+ 0 0)
         (print 42))
     (if (- 1 1)
         (print 42))
     (if (* 1 0)
         (print 42))
     (if (/ 0 1)
         (print 42))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the then branch if the test evaluates to false" \
    $1 \
    "(if false
        (print 42))
     (let [a false b (== 1 0) c (< 1 0) d (<= 10 1) e (> 1 5) f (>= 10 11)]
       (if a
         (print 42))
       (if b
         (print 42))
       (if c
         (print 42))
       (if d
         (print 42))
       (if e
         (print 42))
       (if f
         (print 42)))
     (if (== 1 0)
         (print 42))
     (if (< 1 0)
         (print 42))
     (if (<= 10 1)
         (print 42))
     (if (> 1 5)
         (print 42))
     (if (>= 10 11)
         (print 42))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a negative integer" \
    $1 \
    "(if (- 1)
        (print 42))
     (let [x (- 2)]
       (if x
         (print 42)))
     (if (+ 1 (- 2))
         (print 42))
     (if (- 1 10)
         (print 42))
     (if (* (- 1) 2)
         (print 42))
     (if (/ 16 (- 4))
         (print 42))" \
    "424242424242"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to a positive integer" \
    $1 \
    "(if 1
        (print 42))
     (let [x 2]
       (if x
         (print 42)))
     (if (- (- 3))
         (print 42))
     (if (+ 0 4)
         (print 42))
     (if (- 10 5)
         (print 42))
     (if (* 6 1)
         (print 42))
     (if (/ 100 2)
         (print 42))" \
    "42424242424242"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the then branch if the test evaluates to true" \
    $1 \
    "(if true
        (print 42))
     (let [a true b (== 1 1) c (< 0 1) d (<= 1 10) e (> 5 1) f (>= 11 10)]
       (if a
         (print 42))
       (if b
         (print 42))
       (if c
         (print 42))
       (if d
         (print 42))
       (if e
         (print 42))
       (if f
         (print 42)))
     (if (== 1 1)
         (print 42))
     (if (< 0 1)
         (print 42))
     (if (<= 1 10)
         (print 42))
     (if (> 5 1)
         (print 42))
     (if (>= 11 10)
         (print 42))" \
    "424242424242424242424242"

  echo -e "\n"
}
