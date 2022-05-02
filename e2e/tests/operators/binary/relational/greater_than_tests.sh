# !/bin/bash

source _utils.sh

run_binary_relational_greater_than_expr_tests () {
  echo "Tests for \"(\" \">\" expr expr \")\""

  assert_exec_stdout_equalto_with_stdin \
    "Should allow zero or more white space characters between the opening parenthesis and the > operator" \
    $1 \
    "(if (> 1 0) (print 1))
    (if ( > 1 0) (print 1))
    (if (  > 1 0) (print 1))
    (if (
> 1 0) (print 1))
    (if (

> 1 0) (print 1))" \
    "11111"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow zero or more white space characters between the > operator and the first operand" \
    $1 \
    "(if (>1 0) (print 1))
    (if (> 1 0) (print 1))
    (if (>  1 0) (print 1))
    (if (>
1 0) (print 1))
    (if (>

1 0) (print 1))" \
    "11111"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow zero or more white space characters between the last operand and the closing parenthesis" \
    $1 \
    "(if (> 1 0) (print 1))
    (if (> 1 0 ) (print 1))
    (if (> 1 0  ) (print 1))
    (if (> 1 0
) (print 1))
    (if (> 1 0

) (print 1))" \
    "11111"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis and no operands" \
    $1 \
    ">)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '>' instead (1:1)

>)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "> 32 10)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '>' instead (1:1)

> 32 10)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no operands" \
    $1 \
    "(>)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(>)
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if only one operand" \
    $1 \
    "(> 32)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:6)

(> 32)
     ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and no operands" \
    $1 \
    "(>" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:3)

(>
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and only one operand" \
    $1 \
    "(> 1" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:5)

(> 1
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(> 4 5" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:7)

(> 4 5
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is boolean literal true" \
    $1 \
    "(> true 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(> true 1)
   ^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is boolean literal true" \
    $1 \
    "(> 1 true)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:6)

(> 1 true)
     ^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are boolean literal true" \
    $1 \
    "(> true true)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(> true true)
   ^^^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:9)

(> true true)
        ^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is boolean literal false" \
    $1 \
    "(> false 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(> false 1)
   ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is boolean literal false" \
    $1 \
    "(> 1 false)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:6)

(> 1 false)
     ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are boolean literal false" \
    $1 \
    "(> false false)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(> false false)
   ^^^^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:10)

(> false false)
         ^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if the first operand is boolean literal true and the second is boolean literal false" \
    $1 \
    "(> true false)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(> true false)
   ^^^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:9)

(> true false)
        ^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if the first operand is boolean literal false and the second is boolean literal true" \
    $1 \
    "(> false true)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(> false true)
   ^^^^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:10)

(> false true)
         ^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational equal to expression" \
    $1 \
    "(> (== 1 0) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (== 1 0) 0)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational equal to expression" \
    $1 \
    "(> 0 (== 1 0))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (== 1 0))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational equal to expressions" \
    $1 \
    "(> (== true false) (== 1 0))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (== true false) (== 1 0))
    ^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:21)

(> (== true false) (== 1 0))
                    ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a let expression that evaluates to a boolean" \
    $1 \
    "(> (let [] true) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (let [] true) 0)
    ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a let expression that evaluates to a boolean" \
    $1 \
    "(> 0 (let [] false))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (let [] false))
      ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are let expressions that evaluate to booleans" \
    $1 \
    "(> (let [] true) (let [] false))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (let [] true) (let [] false))
    ^^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:19)

(> (let [] true) (let [] false))
                  ^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a let expression that evaluates to nil" \
    $1 \
    "(> (let [] (print 1)) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (let [] (print 1)) 0)
    ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a let expression that evaluates to nil" \
    $1 \
    "(> 0 (let [] (print 1)))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(> 0 (let [] (print 1)))
      ^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are let expressions that evaluate to nil" \
    $1 \
    "(> (let [] (print 1)) (let [] (let [])))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (let [] (print 1)) (let [] (let [])))
    ^^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:24)

(> (let [] (print 1)) (let [] (let [])))
                       ^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an if-then expression that evaluates to a boolean" \
    $1 \
    "(> (if 0 true) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (if 0 true) 0)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an if-then expression that evaluates to a boolean" \
    $1 \
    "(> 0 (if 1 false))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (if 1 false))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are if-then expressions that evaluate to booleans" \
    $1 \
    "(> (if 1 true) (if 0 false))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (if 1 true) (if 0 false))
    ^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:17)

(> (if 1 true) (if 0 false))
                ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an if-then expression that evaluates to nil" \
    $1 \
    "(> (if 0 (let [])) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (if 0 (let [])) 0)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an if-then expression that evaluates to nil" \
    $1 \
    "(> 0 (if 1 (let [])))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(> 0 (if 1 (let [])))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are if-then expressions that evaluate to nil" \
    $1 \
    "(> (if 1 (let [])) (if 0 (print 1)))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (if 1 (let [])) (if 0 (print 1)))
    ^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:21)

(> (if 1 (let [])) (if 0 (print 1)))
                    ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an if-then-else expression that evaluates to a boolean" \
    $1 \
    "(> (if 0 true false) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (if 0 true false) 0)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an if-then-else expression that evaluates to a boolean" \
    $1 \
    "(> 0 (if 1 false true))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (if 1 false true))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are if-then-else expressions that evaluate to booleans" \
    $1 \
    "(> (if 1 true false) (if 0 false true))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (if 1 true false) (if 0 false true))
    ^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:23)

(> (if 1 true false) (if 0 false true))
                      ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an if-then-else expression that evaluates to nil" \
    $1 \
    "(> (if 0 (let []) (let [])) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (if 0 (let []) (let [])) 0)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an if-then-else expression that evaluates to nil" \
    $1 \
    "(> 0 (if 1 (print 1) (print 1)))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(> 0 (if 1 (print 1) (print 1)))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are if-then-else expressions that evaluate to nil" \
    $1 \
    "(> (if 1 (print 1) (print 1)) (if 0 (let []) (let [])))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (if 1 (print 1) (print 1)) (if 0 (let []) (let [])))
    ^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:32)

(> (if 1 (print 1) (print 1)) (if 0 (let []) (let [])))
                               ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a print expression" \
    $1 \
    "(> (print 1) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (print 1) 0)
    ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a print expression" \
    $1 \
    "(> 0 (print 0))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(> 0 (print 0))
      ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are if-then-else expressions that evaluate to booleans" \
    $1 \
    "(> (print 1) (print 2))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (print 1) (print 2))
    ^^^^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:15)

(> (print 1) (print 2))
              ^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a println expression" \
    $1 \
    "(> (println 1) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (println 1) 0)
    ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a println expression" \
    $1 \
    "(> 0 (println 0))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(> 0 (println 0))
      ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are if-then-else expressions that evaluate to booleans" \
    $1 \
    "(> (println 1) (println 2))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(> (println 1) (println 2))
    ^^^^^^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (1:17)

(> (println 1) (println 2))
                ^^^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an identifier that evaluates to a boolean" \
    $1 \
    "(let [a true]
       (> a 0))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:11)

(> a 0))
   ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an identifier that evaluates to a boolean" \
    $1 \
    "(let [a false]
       (> 0 a))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:13)

(> 0 a))
     ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are identifiers that evaluate to booleans" \
    $1 \
    "(let [a true b false]
       (> a b))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:11)

(> a b))
   ^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:13)

(> a b))
     ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if the first operand is an identifier that evaluates to nil" \
    $1 \
    "(let [a (let [])]
       (> a 0))" \
    "Encountered one or more semantic errors during type checking:


The expression bounded to identifier 'a' cannot be of type 'NIL' (1:7)

(let [a (let [])]
      ^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (2:11)

(> a 0))
   ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if the second operand is an identifier that evaluates to nil" \
    $1 \
    "(let [a (print 1)]
       (> 0 a))" \
    "Encountered one or more semantic errors during type checking:


The expression bounded to identifier 'a' cannot be of type 'NIL' (1:7)

(let [a (print 1)]
      ^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (2:13)

(> 0 a))
     ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report four type errors if both operands are identifiers that evaluate to nil" \
    $1 \
    "(let [a (println 2) b (println 3)]
       (> a b))" \
    "Encountered one or more semantic errors during type checking:


The expression bounded to identifier 'a' cannot be of type 'NIL' (1:7)

(let [a (println 2) b (println 3)]
      ^

The expression bounded to identifier 'b' cannot be of type 'NIL' (1:21)

(let [a (println 2) b (println 3)]
                    ^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (2:11)

(> a b))
   ^

Expected operand to operator '>' to be type 'INTEGER' but found type 'NIL' instead (2:13)

(> a b))
     ^

4 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a less than expression" \
    $1 \
    "(> (< 1 2) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (< 1 2) 0)
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a less than expression" \
    $1 \
    "(> 0 (< 2 1))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (< 2 1))
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are less than expressions" \
    $1 \
    "(> (< 1 2) (< 3 4))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (< 1 2) (< 3 4))
    ^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:13)

(> (< 1 2) (< 3 4))
            ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a less than or equal to expression" \
    $1 \
    "(> (<= 1 2) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (<= 1 2) 0)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a less than or equal to expression" \
    $1 \
    "(> 0 (<= 2 1))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (<= 2 1))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are less than or equal to expressions" \
    $1 \
    "(> (<= 1 2) (<= 3 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (<= 1 2) (<= 3 3))
    ^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(> (<= 1 2) (<= 3 3))
             ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a greater than expression" \
    $1 \
    "(> (> 1 2) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (> 1 2) 0)
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a greater than expression" \
    $1 \
    "(> 0 (> 2 1))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (> 2 1))
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are greater than expressions" \
    $1 \
    "(> (> 1 2) (> 5 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (> 1 2) (> 5 3))
    ^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:13)

(> (> 1 2) (> 5 3))
            ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a greater than or equal to expression" \
    $1 \
    "(> (>= 1 2) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (>= 1 2) 0)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a greater than or equal to expression" \
    $1 \
    "(> 0 (>= 2 1))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (>= 2 1))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are greater than or equal to expressions" \
    $1 \
    "(> (>= 1 2) (>= 3 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (>= 1 2) (>= 3 3))
    ^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(> (>= 1 2) (>= 3 3))
             ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an equal to expression" \
    $1 \
    "(> (== 1 2) 0)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (== 1 2) 0)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an equal to expression" \
    $1 \
    "(> 0 (== 2 1))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(> 0 (== 2 1))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are equal to expressions" \
    $1 \
    "(> (== 1 2) (== 3 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(> (== 1 2) (== 3 3))
    ^^

Expected operand to operator '>' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(> (== 1 2) (== 3 3))
             ^^

2 Errors"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are integer literal zero" \
    $1 \
    "(if (> 0 0) (print 1) (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are negative integers and the first is greater than the second" \
    $1 \
    "(if (> (- 1) (- 2)) (print 1) (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are negative integers and the second is greater than the first" \
    $1 \
    "(if (> (- 2) (- 1)) (print 1) (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are positive integers and the first is greater than the second" \
    $1 \
    "(if (> 2 1) (print 1) (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are positive integers and the second is greater than the first" \
    $1 \
    "(if (> 1 2) (print 1) (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are addition expressions and the first is greater than the second" \
    $1 \
    "(if (> (+ 2 3) (+ 0 1)) (print 1) (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are addition expressions and the second is greater than the first" \
    $1 \
    "(if (> (+ 0 1) (+ 2 3)) (print 1) (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are subtraction expressions and the first is greater than the second" \
    $1 \
    "(if (> (- 2 3) (- 1 999)) (print 1) (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are subtraction expressions and the second is greater than the first" \
    $1 \
    "(if (> (- 1 999) (- 2 3)) (print 1) (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are multiplication expressions and the first is greater than the second" \
    $1 \
    "(if (> (* 2 3) (* 0 1)) (print 1) (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are multiplication expressions and the second is greater than the first" \
    $1 \
    "(if (> (* 0 1) (* 2 3)) (print 1) (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands are division expressions and the first is greater than the second" \
    $1 \
    "(if (> (/ 16 4) (/ 0 1)) (print 1) (print 0))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands are division expressions and the second is greater than the first" \
    $1 \
    "(if (> (/ 0 1) (/ 16 4)) (print 1) (print 0))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to true if both operands evaluate to identifiers and the first is greater than the second" \
    $1 \
    "(let [a 2 b 1]
       (if (> a b) (print 1) (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should evaluate to false if both operands evaluate to identifiers and the second is greater than the first" \
    $1 \
    "(let [a 1 b 2]
       (if (> a b) (print 1) (print 0)))" \
    "0"

  echo -e "\n"
}
