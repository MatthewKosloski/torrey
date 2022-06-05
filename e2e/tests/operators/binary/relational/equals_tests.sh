# !/bin/bash

source _utils.sh

run_binary_relational_equals_expr_tests () {
  echo "Tests for \"(\" \"==\" expr expr \")\""

  assert_exec_stdout_equalto_with_stdin \
    "Should allow zero or more white space characters between the opening parenthesis and the == operator" \
    $1 \
    "(if (== 0 0) (print 1))
    (if ( == 0 0) (print 1))
    (if (  == 0 0) (print 1))
    (if (
== 0 0) (print 1))
    (if (

== 0 0) (print 1))" \
    "11111"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow zero or more white space characters between the == operator and the first operand" \
    $1 \
    "(if (==0 0) (print 1))
    (if (== 0 0) (print 1))
    (if (==  0 0) (print 1))
    (if (==
0 0) (print 1))
    (if (==

0 0) (print 1))" \
    "11111"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow zero or more white space characters between the last operand and the closing parenthesis" \
    $1 \
    "(if (== 0 0) (print 1))
    (if (== 0 0 ) (print 1))
    (if (== 0 0  ) (print 1))
    (if (== 0 0
) (print 1))
    (if (== 0 0

) (print 1))" \
    "11111"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis and no operands" \
    $1 \
    "==)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '==' instead (1:1)

==)
^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "== 32 10)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '==' instead (1:1)

== 32 10)
^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no operands" \
    $1 \
    "(==)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:4)

(==)
   ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if only one operand" \
    $1 \
    "(== 32)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:7)

(== 32)
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and no operands" \
    $1 \
    "(==" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:4)

(==
   ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and only one operand" \
    $1 \
    "(== 1" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:6)

(== 1
     ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(== 4 5" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:8)

(== 4 5
       ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are boolean literal true" \
    $1 \
    "(if (== true true) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if the first operand is boolean literal true and the second operand is boolean literal false" \
    $1 \
    "(if (== true false) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if the first operand is boolean literal false and the second operand is boolean literal true" \
    $1 \
    "(if (== false true) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are boolean literal false" \
    $1 \
    "(if (== false false) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are integer literal zero" \
    $1 \
    "(if (== 0 0) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are the same positive integer literal" \
    $1 \
    "(if (== 1 1) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are not the same positive integer literal" \
    $1 \
    "(if (== 0 1) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are the same negative integer literal" \
    $1 \
    "(if (== (- 1) (- 1)) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are not the same negative integer literal" \
    $1 \
    "(if (== (- 1) (- 2)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are binary addition expressions that evaluate to the same integer value" \
    $1 \
    "(if (== (+ 1 1) (+ 10 (- 8))) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are binary addition expressions that do not evaluate to the same integer value" \
    $1 \
    "(if (== (+ 2 3) (+ 2 4)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are binary subtraction expressions that evaluate to the same integer value" \
    $1 \
    "(if (== (- 1 1) (- 5 5)) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are binary subtraction expressions that do not evaluate to the same integer value" \
    $1 \
    "(if (== (- 2 3) (- 2 4)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are binary multiplication expressions that evaluate to the same integer value" \
    $1 \
    "(if (== (* 4 5) (* 2 10)) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are binary multiplication expressions that do not evaluate to the same integer value" \
    $1 \
    "(if (== (* 3 6) (* 4 5)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are binary division expressions that evaluate to the same integer value" \
    $1 \
    "(if (== (/ 16 4) (/ 8 2)) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are binary division expressions that do not evaluate to the same integer value" \
    $1 \
    "(if (== (/ 6 3) (/ 16 4)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are relational equal to expressions that evaluate to the same boolean value" \
    $1 \
    "(if (== (== 0 0) (== (- 1) (- 1))) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are relational equal to expressions that do not evaluate to the same boolean value" \
    $1 \
    "(if (== (== 1 (- 1)) (== (- 1) (- 1))) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are relational less than expressions that evaluate to the same boolean value" \
    $1 \
    "(if (== (< 1 2) (< (- 2) 2)) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are relational less than expressions that do not evaluate to the same boolean value" \
    $1 \
    "(if (== (< 10 5) (< (- 2) 2)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are relational less than or equal to expressions that evaluate to the same boolean value" \
    $1 \
    "(if (== (<= 2 2) (<= 1 2)) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are relational less than or equal to expressions that do not evaluate to the same boolean value" \
    $1 \
    "(if (== (<= 10 5) (<= (- 2) 2)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are greater than expressions that evaluate to the same boolean value" \
    $1 \
    "(if (== (> 2 1) (> 10 (- 1))) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are greater than expressions that do not evaluate to the same boolean value" \
    $1 \
    "(if (== (> 1 2) (> 10 (- 10))) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are greater than or equal to expressions that evaluate to the same boolean value" \
    $1 \
    "(if (== (>= 10 10) (>= (- 1) (- 2))) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are greater than or equal to expressions that do not evaluate to the same boolean value" \
    $1 \
    "(if (== (>= 0 0) (>= (- 10) 1)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are let expressions that evaluate to the same integer value" \
    $1 \
    "(if (== (let [] 1) (let [] (- (- 1)))) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are let expressions that do not evaluate to the same integer value" \
    $1 \
    "(if (== (let [] 1) (let [] (- 1))) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are let expressions that evaluate to the same boolean value" \
    $1 \
    "(if (== (let [] true) (let [] true)) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are let expressions that do not evaluate to the same boolean value" \
    $1 \
    "(if (== (let [] true) (let [] false)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are if-then expressions that evaluate to the same boolean value" \
    $1 \
    "(if (== (if 1 true) (if 1 true)) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are if-then expressions that do not evaluate to the same boolean value" \
    $1 \
    "(if (== (if 1 true) (if 1 false)) (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are if-then expressions that evaluate to the same integer value" \
    $1 \
    "(if (== (if 1 42) (if 1 (- (- 42)))) (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are if-then expressions that do not evaluate to the same integer value" \
    $1 \
    "(if (== (if 1 42) (if 1 43)) (print 1))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report type errors if an operand is an if-then expression that evaluates to nil" \
    $1 \
    "
    (if (== (if true (let [])) true) 1)
    (if (== false (if false (let []))) 1)
    " \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be type 'BOOLEAN' but found type 'NIL' instead (1:10)

(if (== (if true (let [])) true) 1)
         ^^

Expected operand to operator '==' to be type 'BOOLEAN' but found type 'NIL' instead (2:20)

(if (== false (if false (let []))) 1)
               ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an integer but the second operand is a boolean" \
    $1 \
    "(if (== 1 true) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:11)

(if (== 1 true) 1)
          ^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an integer but the second operand is nil" \
    $1 \
    "(if (== 1 (let [])) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be type 'INTEGER' but found type 'NIL' instead (1:12)

(if (== 1 (let [])) 1)
           ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a boolean but the second operand is an integer" \
    $1 \
    "(if (== true 1) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be type 'BOOLEAN' but found type 'INTEGER' instead (1:14)

(if (== true 1) 1)
             ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a boolean but the second operand is nil" \
    $1 \
    "(if (== true (let [])) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be type 'BOOLEAN' but found type 'NIL' instead (1:15)

(if (== true (let [])) 1)
              ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an integer but the first operand is nil" \
    $1 \
    "(if (== (let []) 1) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be type 'INTEGER' but found type 'NIL' instead (1:10)

(if (== (let []) 1) 1)
         ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a boolean but the first operand is nil" \
    $1 \
    "(if (== (let []) true) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be type 'BOOLEAN' but found type 'NIL' instead (1:10)

(if (== (let []) true) 1)
         ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are nil" \
    $1 \
    "(if (== (let []) (print 1)) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be of type 'INTEGER' or 'BOOLEAN' but found type 'NIL' instead (1:10)

(if (== (let []) (print 1)) 1)
         ^^^

Expected operand to operator '==' to be of type 'INTEGER' or 'BOOLEAN' but found type 'NIL' instead (1:19)

(if (== (let []) (print 1)) 1)
                  ^^^^^

2 Errors"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are if-then-else expressions that evaluate to the same integer value" \
    $1 \
    "(if
       (== (if false 0 1) (if true 1 0))
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are if-then-else expressions that do not evaluate to the same integer value" \
    $1 \
    "(if
       (== (if false 0 0) (if true 1 0))
       (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are if-then-else expressions that evaluate to the same boolean value" \
    $1 \
    "(if
       (== (if false false true) (if true true false))
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are if-then-else expressions that do not evaluate to the same boolean value" \
    $1 \
    "(if
       (== (if false false false) (if true true false))
       (print 1))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report type errors if the operand is an if-then-else expression that evaluates to nil" \
    $1 \
    "(==
       (if 0 false false)
       (if 1 (let []) (print 1)))
     (==
       (if 0 (print 1) (let []))
       (if 1 false true))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '==' to be type 'BOOLEAN' but found type 'NIL' instead (3:9)

(if 1 (let []) (print 1)))
 ^^

Expected operand to operator '==' to be type 'BOOLEAN' but found type 'NIL' instead (5:9)

(if 0 (print 1) (let []))
 ^^

2 Errors"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are identifiers that evaluate to the same boolean value" \
    $1 \
    "(let [a true b true]
       (if
         (== a b)
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are identifiers that do not evaluate to the same boolean value" \
    $1 \
    "(let [a true b false]
       (if
         (== a b)
         (print 1)))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are identifiers that evaluate to the same integer value" \
    $1 \
    "(let [a 1 b a]
       (if
         (== a b)
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are identifiers that do not evaluate to the same integer value" \
    $1 \
    "(let [a 1 b 2]
       (if
         (== a b)
         (print 1)))" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if an identifier evaluates to nil" \
    $1 \
    "(let [a true b (let [])]
       (if
         (== a b)
         (print 1)))" \
    "Encountered one or more semantic errors during type checking:


The expression bounded to identifier 'b' cannot be of type 'NIL' (1:14)

(let [a true b (let [])]
             ^

Expected operand to operator '==' to be type 'BOOLEAN' but found type 'NIL' instead (3:16)

(== a b)
      ^

2 Errors"



  echo -e "\n"
}
