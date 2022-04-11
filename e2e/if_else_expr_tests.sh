#!/bin/bash

source _utils.sh

run_if_else_expr_tests () {
  echo "Tests for \"(\" \"if\" expr expr expr \")\""

  assert_stderr \
    $1 \
    "Should report a syntax error if no left parenthesis" \
    "if false 0 1)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'if' instead (1:1)

if false 0 1)
^^

1 Error"

  assert_stderr \
    $1 \
    "Should report a syntax error if no right parenthesis" \
    "(if false 0 1" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:13)

(if false 0 1
            ^

1 Error"

  assert_stderr \
    $1 \
    "Should report a type error if the test is a print expression" \
    "(if (print 1) 1 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (print 1) 1 0)
     ^^^^^

1 Error"

  assert_stderr \
    $1 \
    "Should report a type error if the test is a println expression" \
    "(if (println 1) 1 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (println 1) 1 0)
     ^^^^^^^

1 Error"

  assert_stderr \
    $1 \
    "Should report a type error if the test is an empty let expression" \
    "(if (let []) 1 0)" \
    "Encountered one or more semantic errors during type checking:


An expression of type 'NIL' cannot be tested for truthiness (1:6)

(if (let []) 1 0)
     ^^^

1 Error"

  assert_stderr \
    $1 \
    "Should report a type error if the branches evaluate to different types" \
    "(if true (print 0) false)
     (if true (print 0) (== 0 1))
     (if true (print 0) (< 1 0))
     (if true (print 0) (<= 1 0))
     (if true (print 0) (> 0 1))
     (if true (print 0) (>= 0 1))

     (if true (let []) 1)

     (if true true (print 1))
     (if true (== 0 0) (print 1))
     (if true (< 0 1) (print 1))
     (if true (<= 0 1) (print 1))
     (if true (> 1 0) (print 1))
     (if true (>= 1 0) (print 1))

     (if false false 0)
     (if false (== 0 1) 0)
     (if false (< 1 0) 0)
     (if false (<= 1 0) 0)
     (if false (> 0 1) 0)
     (if false (>= 0 1) 0)

     (if false 0 (let []))

     (if false 1 true)
     (if false 1 (== 0 0))
     (if false 1 (== 0 0))
     (if false 1 (< 0 1))
     (if false 1 (<= 0 1))
     (if false 1 (> 1 0))
     (if false 1 (>= 1 0))" \
    "Encountered one or more semantic errors during type checking:


Both branches to an if expression must evaluate to the same types (1:2)

(if true (print 0) false)
 ^^

Both branches to an if expression must evaluate to the same types (2:7)

(if true (print 0) (== 0 1))
 ^^

Both branches to an if expression must evaluate to the same types (3:7)

(if true (print 0) (< 1 0))
 ^^

Both branches to an if expression must evaluate to the same types (4:7)

(if true (print 0) (<= 1 0))
 ^^

Both branches to an if expression must evaluate to the same types (5:7)

(if true (print 0) (> 0 1))
 ^^

Both branches to an if expression must evaluate to the same types (6:7)

(if true (print 0) (>= 0 1))
 ^^

Both branches to an if expression must evaluate to the same types (8:7)

(if true (let []) 1)
 ^^

Both branches to an if expression must evaluate to the same types (10:7)

(if true true (print 1))
 ^^

Both branches to an if expression must evaluate to the same types (11:7)

(if true (== 0 0) (print 1))
 ^^

Both branches to an if expression must evaluate to the same types (12:7)

(if true (< 0 1) (print 1))
 ^^

Both branches to an if expression must evaluate to the same types (13:7)

(if true (<= 0 1) (print 1))
 ^^

Both branches to an if expression must evaluate to the same types (14:7)

(if true (> 1 0) (print 1))
 ^^

Both branches to an if expression must evaluate to the same types (15:7)

(if true (>= 1 0) (print 1))
 ^^

Both branches to an if expression must evaluate to the same types (17:7)

(if false false 0)
 ^^

Both branches to an if expression must evaluate to the same types (18:7)

(if false (== 0 1) 0)
 ^^

Both branches to an if expression must evaluate to the same types (19:7)

(if false (< 1 0) 0)
 ^^

Both branches to an if expression must evaluate to the same types (20:7)

(if false (<= 1 0) 0)
 ^^

Both branches to an if expression must evaluate to the same types (21:7)

(if false (> 0 1) 0)
 ^^

Both branches to an if expression must evaluate to the same types (22:7)

(if false (>= 0 1) 0)
 ^^

Both branches to an if expression must evaluate to the same types (24:7)

(if false 0 (let []))
 ^^

Both branches to an if expression must evaluate to the same types (26:7)

(if false 1 true)
 ^^

Both branches to an if expression must evaluate to the same types (27:7)

(if false 1 (== 0 0))
 ^^

Both branches to an if expression must evaluate to the same types (28:7)

(if false 1 (== 0 0))
 ^^

Both branches to an if expression must evaluate to the same types (29:7)

(if false 1 (< 0 1))
 ^^

Both branches to an if expression must evaluate to the same types (30:7)

(if false 1 (<= 0 1))
 ^^

Both branches to an if expression must evaluate to the same types (31:7)

(if false 1 (> 1 0))
 ^^

Both branches to an if expression must evaluate to the same types (32:7)

(if false 1 (>= 1 0))
 ^^

27 Errors"

  assert_stdout \
    $1 \
    "Should not report a type error if the branches are print(ln) expressions" \
    "(if true (print 1) (print 0))
     (if true (print 1) (println 0))
     (if true (println 1) (print 0))
     (if true (println 1) (println 0))" \
    "111
1"

  assert_stdout \
    $1 \
    "Should not report a type error if the branches are empty let and print expressions" \
    "(if true (let []) (print 0))
     (if true (print 1) (let []))" \
    "1"

  assert_stdout \
    $1 \
    "Should not report a type error if the branches are empty let and println expressions" \
    "(if true (let []) (println 0))
     (if true (println 1) (let []))" \
    "1"

  assert_stdout \
    $1 \
    "Should take the else branch if the test evaluates to zero" \
    "(if 0
        (print 1)
        (print 0))
     (let [x 0]
       (if x
         (print 1)
         (print 0)))
     (if (- 0)
         (print 1)
         (print 0))
     (if (+ 0 0)
         (print 1)
         (print 0))
     (if (- 1 1)
         (print 1)
         (print 0))
     (if (* 1 0)
         (print 1)
         (print 0))
     (if (/ 0 1)
         (print 1)
         (print 0))" \
    "0000000"

  assert_stdout \
    $1 \
    "Should take the else branch if the test evaluates to false" \
    "(if false
        (print 1)
        (print 0))
     (let [a false b (== 1 0) c (< 1 0) d (<= 10 1) e (> 1 5) f (>= 10 11)]
       (if a
         (print 1)
         (print 0))
       (if b
         (print 1)
         (print 0))
       (if c
         (print 1)
         (print 0))
       (if d
         (print 1)
         (print 0))
       (if e
         (print 1)
         (print 0))
       (if f
         (print 1)
         (print 0)))
     (if (== 1 0)
         (print 1)
         (print 0))
     (if (< 1 0)
         (print 1)
         (print 0))
     (if (<= 10 1)
         (print 1)
         (print 0))
     (if (> 1 5)
         (print 1)
         (print 0))
     (if (>= 10 11)
         (print 1)
         (print 0))" \
    "000000000000"

  assert_stdout \
    $1 \
    "Should take the then branch if the test evaluates to a negative integer" \
    "(if (- 1)
        (print 1)
        (print 0))
     (let [x (- 2)]
       (if x
         (print 1)
         (print 0)))
     (if (+ 1 (- 2))
         (print 1)
         (print 0))
     (if (- 1 10)
         (print 1)
         (print 0))
     (if (* (- 1) 2)
         (print 1)
         (print 0))
     (if (/ 16 (- 4))
         (print 1)
         (print 0))" \
    "111111"

  assert_stdout \
    $1 \
    "Should take the then branch if the test evaluates to a positive integer" \
    "(if 1
        (print 1)
        (print 0))
     (let [x 2]
       (if x
         (print 1)
         (print 0)))
     (if (- (- 3))
         (print 1)
         (print 0))
     (if (+ 0 4)
         (print 1)
         (print 0))
     (if (- 10 5)
         (print 1)
         (print 0))
     (if (* 6 1)
         (print 1)
         (print 0))
     (if (/ 100 2)
         (print 1)
         (print 0))" \
    "1111111"

  assert_stdout \
    $1 \
    "Should take the then branch if the test evaluates to true" \
    "(if true
        (print 1)
        (print 0))
     (let [a true b (== 1 1) c (< 0 1) d (<= 1 10) e (> 5 1) f (>= 11 10)]
       (if a
         (print 1)
         (print 0))
       (if b
         (print 1)
         (print 0))
       (if c
         (print 1)
         (print 0))
       (if d
         (print 1)
         (print 0))
       (if e
         (print 1)
         (print 0))
       (if f
         (print 1)
         (print 0)))
     (if (== 1 1)
         (print 1)
         (print 0))
     (if (< 0 1)
         (print 1)
         (print 0))
     (if (<= 1 10)
         (print 1)
         (print 0))
     (if (> 5 1)
         (print 1)
         (print 0))
     (if (>= 11 10)
         (print 1)
         (print 0))" \
    "111111111111"

  echo -e "\n"
}
