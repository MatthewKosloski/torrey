# !/bin/bash

source _utils.sh

run_binary_div_expr_tests () {
  echo "Tests for \"(\" \"/\" expr expr \")\""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis and no operands" \
    $1 \
    "/)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '/' instead (1:1)

/)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "/ 32 10)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '/' instead (1:1)

/ 32 10)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no operands" \
    $1 \
    "(/)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(/)
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if only one operand" \
    $1 \
    "(/ 32)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:6)

(/ 32)
     ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and no operands" \
    $1 \
    "(/" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:3)

(/
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and only one operand" \
    $1 \
    "(/ 1" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:5)

(/ 1
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(/ 4 5" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:7)

(/ 4 5
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are boolean literals" \
    $1 \
    "(/ false true)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(/ false true)
   ^^^^^

Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:10)

(/ false true)
         ^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a boolean literal" \
    $1 \
    "(/ true 4)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(/ true 4)
   ^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a boolean literal" \
    $1 \
    "(/ 4 false)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:6)

(/ 4 false)
     ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are print expressions" \
    $1 \
    "(/ (print 1) (print 2))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(/ (print 1) (print 2))
    ^^^^^

Expected operand to operator '/' to be type 'INTEGER' but found type 'NIL' instead (1:15)

(/ (print 1) (print 2))
              ^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a print expression" \
    $1 \
    "(/ (print 1) 4)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(/ (print 1) 4)
    ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a print expression" \
    $1 \
    "(/ 4 (print 66))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(/ 4 (print 66))
      ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are println expressions" \
    $1 \
    "(/ (println 1) (println 2))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(/ (println 1) (println 2))
    ^^^^^^^

Expected operand to operator '/' to be type 'INTEGER' but found type 'NIL' instead (1:17)

(/ (println 1) (println 2))
                ^^^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a println expression" \
    $1 \
    "(/ (println 1) 4)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(/ (println 1) 4)
    ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a println expression" \
    $1 \
    "(/ 4 (println 66))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(/ 4 (println 66))
      ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are identifiers that do not evaluate to integers" \
    $1 \
    "(let [a true b false]
      (/ a b))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:10)

(/ a b))
   ^

Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:12)

(/ a b))
     ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an identifier that does not evaluate to an integer" \
    $1 \
    "(let [a true b 5]
      (/ a b))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:10)

(/ a b))
   ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an identifier that does not evaluate to an integer" \
    $1 \
    "(let [a 5 b true]
      (/ a b))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:12)

(/ a b))
     ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two integer literals" \
    $1 \
    "(print (/ 2 3))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and an identifer that evaluates to an integer" \
    $1 \
    "(print (let [a 3] (/
      2
      a
    )))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an identifer that evaluates to an integer and an integer literal" \
    $1 \
    "(print (let [a 3] (/
      a
      2
    )))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an identifer that evaluates to an integer literal" \
    $1 \
    "(print (let [a 3] (/
      a
      a
    )))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two identifiers that evaluate to integer literals" \
    $1 \
    "(print
       (let [a 3 b 2]
         (/ b a)
       ))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and a unary expression" \
    $1 \
    "(print (/
       1
       (- 2)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of a unary expression and an integer literal" \
    $1 \
    "(print (/
       (- 2)
       2))" \
    "-1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two unary expressions" \
    $1 \
    "(print (/
       (- 10)
       (- 5)))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and a binary addition expression" \
    $1 \
    "(print (/
       1
       (+ 2 3)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of a binary addition expression and an integer literal" \
    $1 \
    "(print (/
       (+ (- 5) (- 5))
       5))" \
    "-2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two binary addition expressions" \
    $1 \
    "(print (/
       (+ 10 32)
       (+ 3 3)))" \
    "7"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and a binary subtraction expression" \
    $1 \
    "(print (/
       16
       (- 8 4)))" \
    "4"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of a binary subtraction expression and an integer literal" \
    $1 \
    "(print (/
       (- 20 30)
       5))" \
    "-2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two binary subtraction expressions" \
    $1 \
    "(print (/
       (- 8 2)
       (- 4 2)))" \
    "3"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and a binary multiplication expression" \
    $1 \
    "(print (/
       1
       (* 2 3)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of a binary multiplication expression and an integer literal" \
    $1 \
    "(print (/
       (* 5 6)
       6))" \
    "5"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two binary multiplication expressions" \
    $1 \
    "(print (/
       (* 8 (- 9))
       (* 4 3)))" \
    "-6"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and a binary division expression" \
    $1 \
    "(print (/
       1
       (/ 3 2)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of a binary division expression and an integer literal" \
    $1 \
    "(print (/
       (/ 10 2)
       5))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two binary division expressions" \
    $1 \
    "(print (/
       (/ 30 4)
       (/ 11 11)))" \
    "7"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational equal to expression" \
    $1 \
    "(/ (== 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (== 2 3) 1)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational equal to expression" \
    $1 \
    "(/ 1 (== 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(/ 1 (== 2 3))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational equal to expressions" \
    $1 \
    "(/ (== 3 2) (== 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (== 3 2) (== 2 3))
    ^^

Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(/ (== 3 2) (== 2 3))
             ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational less than expression" \
    $1 \
    "(/ (< 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (< 2 3) 1)
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational less than expression" \
    $1 \
    "(/ 1 (< 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(/ 1 (< 2 3))
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational less than expressions" \
    $1 \
    "(/ (< 3 2) (< 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (< 3 2) (< 2 3))
    ^

Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:13)

(/ (< 3 2) (< 2 3))
            ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational less than or equal to expression" \
    $1 \
    "(/ (<= 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (<= 2 3) 1)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational less than or equal to expression" \
    $1 \
    "(/ 1 (<= 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(/ 1 (<= 2 3))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational less than or equal to expressions" \
    $1 \
    "(/ (<= 3 2) (<= 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (<= 3 2) (<= 2 3))
    ^^

Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(/ (<= 3 2) (<= 2 3))
             ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational greater than expression" \
    $1 \
    "(/ (> 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (> 2 3) 1)
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational greater than expression" \
    $1 \
    "(/ 1 (> 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(/ 1 (> 2 3))
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational greater than expressions" \
    $1 \
    "(/ (> 3 2) (> 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (> 3 2) (> 2 3))
    ^

Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:13)

(/ (> 3 2) (> 2 3))
            ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational greater than or equal to expression" \
    $1 \
    "(/ (>= 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (>= 2 3) 1)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational greater than or equal to expression" \
    $1 \
    "(/ 1 (>= 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(/ 1 (>= 2 3))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational greater than or equal to expressions" \
    $1 \
    "(/ (>= 3 2) (>= 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(/ (>= 3 2) (>= 2 3))
    ^^

Expected operand to operator '/' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(/ (>= 3 2) (>= 2 3))
             ^^

2 Errors"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and a let expression that evaluates to an integer" \
    $1 \
    "(print (/
       12
       (let [] 6)))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of a let expression that evaluates to an integer and an integer literal" \
    $1 \
    "(print (/
       (let [] 12)
       6))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two let expressions that evaluate to integers" \
    $1 \
    "(print (/
       (let [] 12)
       (let [] 6)))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and a let expression that evaluates to an identifier that evaluates to an integer" \
    $1 \
    "(print (/
       12
       (let [a 6] a)))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of a let expression that evaluates to an identifier that evaluates to an integer and an integer literal" \
    $1 \
    "(print (/
       (let [a 12] a)
       2))" \
    "6"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two let expressions that evaluate to identifiers that evaluate to integers" \
    $1 \
    "(print (/
       (let [a 12] a)
       (let [a 6] a)))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and an if expression that evaluates to an integer" \
    $1 \
    "(print (/
       12
       (if true 3)))" \
    "4"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an if expression that evaluates to an integer and an integer literal" \
    $1 \
    "(print (/
       (if true 12)
       6))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two if expressions that evaluate to integers" \
    $1 \
    "(print (/
       (if true 12)
       (if true 6)))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an integer literal and an if-else expression that evaluates to an integer" \
    $1 \
    "(print (/
       12
       (if false 0 6)))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of an if-else expression that evaluates to an integer and an integer literal" \
    $1 \
    "(print (/
       (if false 0 12)
       6))" \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the quotient of two if-else expressions that evaluate to integers" \
    $1 \
    "(print (/
       (if false 0 12)
       (if false 0 6)))" \
    "2"

  echo -e "\n"
}
