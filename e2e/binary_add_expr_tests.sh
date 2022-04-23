# !/bin/bash

source _utils.sh

run_binary_add_expr_tests () {
  echo "Tests for \"(\" \"+\" expr expr \")\""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis and no operands" \
    $1 \
    "+)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '+' instead (1:1)

+)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "+ 32 10)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found '+' instead (1:1)

+ 32 10)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no operands" \
    $1 \
    "(+)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:3)

(+)
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if only one operand" \
    $1 \
    "(+ 32)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:6)

(+ 32)
     ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and no operands" \
    $1 \
    "(+" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:3)

(+
  ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and only one operand" \
    $1 \
    "(+ 1" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:5)

(+ 1
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(+ 4 5" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:7)

(+ 4 5
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are boolean literals" \
    $1 \
    "(+ false true)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(+ false true)
   ^^^^^

Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:10)

(+ false true)
         ^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a boolean literal" \
    $1 \
    "(+ true 4)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:4)

(+ true 4)
   ^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a boolean literal" \
    $1 \
    "(+ 4 false)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:6)

(+ 4 false)
     ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are print expressions" \
    $1 \
    "(+ (print 1) (print 2))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(+ (print 1) (print 2))
    ^^^^^

Expected operand to operator '+' to be type 'INTEGER' but found type 'NIL' instead (1:15)

(+ (print 1) (print 2))
              ^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a print expression" \
    $1 \
    "(+ (print 1) 4)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(+ (print 1) 4)
    ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a print expression" \
    $1 \
    "(+ 4 (print 66))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(+ 4 (print 66))
      ^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are println expressions" \
    $1 \
    "(+ (println 1) (println 2))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(+ (println 1) (println 2))
    ^^^^^^^

Expected operand to operator '+' to be type 'INTEGER' but found type 'NIL' instead (1:17)

(+ (println 1) (println 2))
                ^^^^^^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a println expression" \
    $1 \
    "(+ (println 1) 4)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'NIL' instead (1:5)

(+ (println 1) 4)
    ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a println expression" \
    $1 \
    "(+ 4 (println 66))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'NIL' instead (1:7)

(+ 4 (println 66))
      ^^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are identifiers that do not evaluate to integers" \
    $1 \
    "(let [a true b false]
      (+ a b))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:10)

(+ a b))
   ^

Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:12)

(+ a b))
     ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is an identifier that does not evaluate to an integer" \
    $1 \
    "(let [a true b 5]
      (+ a b))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:10)

(+ a b))
   ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is an identifier that does not evaluate to an integer" \
    $1 \
    "(let [a 5 b true]
      (+ a b))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (2:12)

(+ a b))
     ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the identity property of addition when a is negative" \
    $1 \
    "
    ; Zero is called the identity element of addition
    (let [a (- 1)]
       (if (== (+ a 0) (+ 0 a))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the identity property of addition when a is zero" \
    $1 \
    "
     ; Zero is called the identity element of addition
     (let [a 0]
       (if (== (+ a 0) (+ 0 a))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the identity property of addition when a is positive" \
    $1 \
    "
     ; Zero is called the identity element of addition
     (let [a 1]
       (if (== (+ a 0) (+ 0 a))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the commutative property of addition when a and b are negative" \
    $1 \
    "
    ; The order in which you add two numbers
    ; does not change the result
     (let [a (- 1) b (- 2)]
       (if (== (+ a b) (+ b a))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the commutative property of addition when a and b are zero" \
    $1 \
    "
    ; The order in which you add two numbers
    ; does not change the result
    (let [a 0 b a]
       (if (== (+ a b) (+ b a))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the commutative property of addition when a and b are positive" \
    $1 \
    "
     ; The order in which you add two numbers
     ; does not change the result
     (let [a 1 b 2]
       (if (== (+ a b) (+ b a))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the associative property of addition when a, b, and c are negative" \
    $1 \
    "
    ; When you add three real numbers, the grouping
    ; (or association) of the numbers does not change
    ; the result
    (let [a (- 1) b (- 2) c (- 3)]
       (if (== (+ c (+ a b)) (+ a (+ b c)))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the associative property of addition when a, b, and c are zero" \
    $1 \
    "
    ; When you add three real numbers, the grouping
    ; (or association) of the numbers does not change
    ; the result
    (let [a 0 b a c a]
       (if (== (+ c (+ a b)) (+ a (+ b c)))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the associative property of addition when a, b, and c are positive" \
    $1 \
    "
    ; When you add three real numbers, the grouping
    ; (or association) of the numbers does not change
    ; the result
    (let [a 1 b 2 c 3]
       (if (== (+ c (+ a b)) (+ a (+ b c)))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the property of opposites of addition when a is negative" \
    $1 \
    "
    ; A number and its opposite are additive inverses
    ; of each other because their sum is zero
    (let [a (- 1)]
       (if (== (+ a (- a)) 0)
         (print 1)
         (print 0))
      (if (== (+ (- a) a) 0)
         (print 1)
         (print 0)))" \
    "11"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the property of opposites of addition when a is zero" \
    $1 \
    "
    ; A number and its opposite are additive inverses
    ; of each other because their sum is zero
    (let [a 0]
       (if (== (+ a (- a)) 0)
         (print 1)
         (print 0))
      (if (== (+ (- a) a) 0)
         (print 1)
         (print 0)))" \
    "11"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the property of opposites of addition when a is positive" \
    $1 \
    "
    ; A number and its opposite are additive inverses
    ; of each other because their sum is zero
    (let [a 1]
       (if (== (+ a (- a)) 0)
         (print 1)
         (print 0))
      (if (== (+ (- a) a) 0)
         (print 1)
         (print 0)))" \
    "11"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the property of opposites of a sum when a and b are negative" \
    $1 \
    "
    ; The opposite of a sum of real numbers
    ; is equal to the sum of the opposites
    (let [a (- 1) b (- 2)]
       (if (== (- (+ a b)) (+ (- a) (- b)))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the property of opposites of a sum when a and b are zero" \
    $1 \
    "
    ; The opposite of a sum of real numbers
    ; is equal to the sum of the opposites
    (let [a 0 b a]
       (if (== (- (+ a b)) (+ (- a) (- b)))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should respect the property of opposites of a sum when a and b are positive" \
    $1 \
    "
    ; The opposite of a sum of real numbers
    ; is equal to the sum of the opposites
    (let [a 1 b 2]
       (if (== (- (+ a b)) (+ (- a) (- b)))
         (print 1)
         (print 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two integer literals" \
    $1 \
    "(print (+ 2 3))" \
    "5"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and an identifer that evaluates to an integer" \
    $1 \
    "(print (let [a 3] (+
      2
      a
    )))" \
    "5"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an identifer that evaluates to an integer and an integer literal" \
    $1 \
    "(print (let [a 3] (+
      a
      2
    )))" \
    "5"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an identifer that evaluates to an integer literal" \
    $1 \
    "(print (let [a 3] (+
      a
      a
    )))" \
    "6"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two identifiers that evaluate to integer literals" \
    $1 \
    "(print
       (let [a 3 b 2]
         (+ b a)
       ))" \
    "5"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and a unary expression" \
    $1 \
    "(print (+
       1
       (- 2)))" \
    "-1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of a unary expression and an integer literal" \
    $1 \
    "(print (+
       (- 2)
       2))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two unary expressions" \
    $1 \
    "(print (+
       (- 10)
       (- 5)))" \
    "-15"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and a binary addition expression" \
    $1 \
    "(print (+
       1
       (+ 2 3)))" \
    "6"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of a binary addition expression and an integer literal" \
    $1 \
    "(print (+
       (+ (- 5) (- 5))
       5))" \
    "-5"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two binary addition expressions" \
    $1 \
    "(print (+
       (+ 69 (- 0))
       (+ 0 (- 70))))" \
    "-1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and a binary subtraction expression" \
    $1 \
    "(print (+
       1
       (- 2 3)))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of a binary subtraction expression and an integer literal" \
    $1 \
    "(print (+
       (- (- 5) (- 5))
       5))" \
    "5"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two binary subtraction expressions" \
    $1 \
    "(print (+
       (- 69 (- 0))
       (- 0 (- 70))))" \
    "139"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and a binary multiplication expression" \
    $1 \
    "(print (+
       1
       (* 2 3)))" \
    "7"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of a binary multiplication expression and an integer literal" \
    $1 \
    "(print (+
       (* (- 5) (- 5))
       5))" \
    "30"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two binary multiplication expressions" \
    $1 \
    "(print (+
       (* 69 (- 0))
       (* 0 (- 70))))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and a binary division expression" \
    $1 \
    "(print (+
       1
       (/ 2 3)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of a binary division expression and an integer literal" \
    $1 \
    "(print (+
       (/ (- 5) (- 5))
       5))" \
    "6"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two binary division expressions" \
    $1 \
    "(print (+
       (/ 69 (- 1))
       (/ 0 (- 70))))" \
    "-69"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational equal to expression" \
    $1 \
    "(+ (== 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (== 2 3) 1)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational equal to expression" \
    $1 \
    "(+ 1 (== 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(+ 1 (== 2 3))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational equal to expressions" \
    $1 \
    "(+ (== 3 2) (== 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (== 3 2) (== 2 3))
    ^^

Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(+ (== 3 2) (== 2 3))
             ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational less than expression" \
    $1 \
    "(+ (< 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (< 2 3) 1)
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational less than expression" \
    $1 \
    "(+ 1 (< 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(+ 1 (< 2 3))
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational less than expressions" \
    $1 \
    "(+ (< 3 2) (< 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (< 3 2) (< 2 3))
    ^

Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:13)

(+ (< 3 2) (< 2 3))
            ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational less than or equal to expression" \
    $1 \
    "(+ (<= 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (<= 2 3) 1)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational less than or equal to expression" \
    $1 \
    "(+ 1 (<= 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(+ 1 (<= 2 3))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational less than or equal to expressions" \
    $1 \
    "(+ (<= 3 2) (<= 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (<= 3 2) (<= 2 3))
    ^^

Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(+ (<= 3 2) (<= 2 3))
             ^^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational greater than expression" \
    $1 \
    "(+ (> 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (> 2 3) 1)
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational greater than expression" \
    $1 \
    "(+ 1 (> 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(+ 1 (> 2 3))
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational greater than expressions" \
    $1 \
    "(+ (> 3 2) (> 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (> 3 2) (> 2 3))
    ^

Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:13)

(+ (> 3 2) (> 2 3))
            ^

2 Errors"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the first operand is a relational greater than or equal to expression" \
    $1 \
    "(+ (>= 2 3) 1)" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (>= 2 3) 1)
    ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a type error if the second operand is a relational greater than or equal to expression" \
    $1 \
    "(+ 1 (>= 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:7)

(+ 1 (>= 2 3))
      ^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report two type errors if both operands are relational greater than or equal to expressions" \
    $1 \
    "(+ (>= 3 2) (>= 2 3))" \
    "Encountered one or more semantic errors during type checking:


Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:5)

(+ (>= 3 2) (>= 2 3))
    ^^

Expected operand to operator '+' to be type 'INTEGER' but found type 'BOOLEAN' instead (1:14)

(+ (>= 3 2) (>= 2 3))
             ^^

2 Errors"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and a let expression that evaluates to an integer" \
    $1 \
    "(print (+
       1
       (let [] 0)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of a let expression that evaluates to an integer and an integer literal" \
    $1 \
    "(print (+
       (let [] 0)
       1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two let expressions that evaluate to integers" \
    $1 \
    "(print (+
       (let [] 32)
       (let [] 10)))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and a let expression that evaluates to an identifier that evaluates to an integer" \
    $1 \
    "(print (+
       1
       (let [a 0] a)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of a let expression that evaluates to an identifier that evaluates to an integer and an integer literal" \
    $1 \
    "(print (+
       (let [a 0] a)
       1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two let expressions that evaluate to identifiers that evaluate to integers" \
    $1 \
    "(print (+
       (let [a 32] a)
       (let [a 10] a)))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and an if expression that evaluates to an integer" \
    $1 \
    "(print (+
       1
       (if true 41)))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an if expression that evaluates to an integer and an integer literal" \
    $1 \
    "(print (+
       (if true 41)
       1))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two if expressions that evaluate to integers" \
    $1 \
    "(print (+
       (if true 32)
       (if true 10)))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an integer literal and an if-else expression that evaluates to an integer" \
    $1 \
    "(print (+
       1
       (if false 0 41)))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of an if-else expression that evaluates to an integer and an integer literal" \
    $1 \
    "(print (+
       (if false 0 41)
       1))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should compute the sum of two if-else expressions that evaluate to integers" \
    $1 \
    "(print (+
       (if false 0 32)
       (if false 0 10)))" \
    "42"

  echo -e "\n"
}
