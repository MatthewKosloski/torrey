# !/bin/bash

source _utils.sh

run_if_then_expr_tests () {
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
    "Should report a syntax error if no right parenthesis and no test expression and no branch" \
    $1 \
    "(if" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:4)

(if
   ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis and no branch" \
    $1 \
    "(if true" \
    "Encountered one or more syntax errors during parse:


Expected the start of an expression but found nothing instead (1:9)

(if true
        ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis" \
    $1 \
    "(if true 1" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:11)

(if true 1
          ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no branch" \
    $1 \
    "(if true)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:9)

(if true)
        ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an arbitrarily nested if-then expression that is truthy" \
    $1 \
    "(if
      (if true 999)
      (print 1))
    (if
      (if
        (if true 999)
        999)
      (print 2))
    (if
      (if
        (if
          (if true 999) 998)
        997)
      (print 3))" \
    "123"

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an arbitrarily nested if-then expression that is falsy" \
    $1 \
    "(if
      (if true 0)
      (print 1))
    (if
      (if
        (if true 999)
        0)
      (print 2))
    (if
      (if
        (if
          (if true 999) 998)
        0)
      (print 3))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an arbitrarily nested if-then-else expression that is truthy" \
    $1 \
    "(if
      (if false 0 400)
      (print 1))
    (if
      (if
        (if
          false
          500
          0)
        0
        501)
      (print 2))
    (if
      (if
        (if
          (if false
            0
            602)
          601
          0)
        600
        0)
      (print 3))" \
    "123"

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an arbitrarily nested if-then-else expression that is falsy" \
    $1 \
    "(if
      (if false 400 0)
      (print 1))
    (if
      (if
        (if
          false
          500
          0)
        501
        0)
      (print 2))
    (if
      (if
        (if
          (if false
            0
            602)
          601
          0)
        0
        600)
      (print 3))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a print expression" \
    $1 \
    "(if (print 42)
      (print 1))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a println expression" \
    $1 \
    "(if (println 42)
      (print 1))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an empty let expression" \
    $1 \
    "(if (let [])
      (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an if-then expression that does not evaluate to a type" \
    $1 \
    "(if
      (if true (let []))
      (print 1))
    (if
      (if false (let []))
      (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an if-then expression that is truthy" \
    $1 \
    "(if (if true 1)
      (print 42))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an if-then expression that is falsy" \
    $1 \
    "(if (if true 0)
      (print 42))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an if-then-else expression that does not evaluate to a type" \
    $1 \
    "(if (if true (let []) (print 777))
      (print 1))
     (if (if false (print 555) (let []))
      (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an if-then-else expression that is truthy" \
    $1 \
    "(if (if true 1 0)
      (print 42))" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an if-then-else expression that is falsy" \
    $1 \
    "(if (if true 0 1)
      (print 42))" \
    ""

    # Should not take the branch if the test evaluates to zero

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is literal 0" \
    $1 \
    "(if 0
        (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an identifier that evaluates to literal zero" \
    $1 \
    "(let [x 0]
       (if x
         (print 1)))" \
    ""
  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a unary expression whose operand is literal zero" \
    $1 \
    "(if (- 0)
       (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a binary add expression that evaluates to zero" \
    $1 \
    "(if (+ 0 0)
         (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a binary sub expression that evaluates to zero" \
    $1 \
    "(if (- 1 1)
         (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a binary multiple expression that evaluates to zero" \
    $1 \
    "(if (* 1 0)
         (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a binary division expression that evaluates to zero" \
    $1 \
    "(if (/ 0 1)
         (print 1))" \
    ""

  # Should not take the branch if the test evaluates to false

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test evalutes to literal false" \
    $1 \
    "(if false
        (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an identifier that evaluates to literal false" \
    $1 \
    "(let [a false]
       (if a
         (print 1)))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a relational equal to expression that evaluates to false" \
    $1 \
    "(if (== 1 0)
        (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a relational less than expression that evaluates to false" \
    $1 \
    "(if (< 1 0)
       (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a relational less than or equal to expression that evaluates to false" \
    $1 \
    "(if (<= 10 1)
       (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a relational greater than expression that evaluates to false" \
    $1 \
    "(if (> 1 5)
       (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is a relational greater than or equal to expression that evaluates to false" \
    $1 \
    "(if (>= 10 11)
       (print 1))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an identifier that evaluates to a relational equal to expression that evaluates to false" \
    $1 \
    "(let [x (== 1 0)]
       (if x
         (print 1)))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an identifier that evaluates to a relational less than expression that evaluates to false" \
    $1 \
    "(let [x (< 1 0)]
       (if x
         (print 1)))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an identifier that evaluates to a relational less than or equal to expression that evaluates to false" \
    $1 \
    "(let [x (<= 10 1)]
       (if x
         (print 1)))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an identifier that evaluates to a relational greater than expression that evaluates to false" \
    $1 \
    "(let [x (> 1 5)]
       (if x
         (print 1)))" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should not take the branch if the test is an identifier that evaluates to a relational greater than or equal to expression that evaluates to false" \
    $1 \
    "(let [x (>= 10 11)]
       (if x
         (print 1)))" \
    ""

  # Should take the branch if the test evaluates to a negative integer

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a negative integer" \
    $1 \
    "(if (- 1)
        (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to an identifier that evaluates to a negative integer" \
    $1 \
    "(let [x (- 2)]
       (if x
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a binary add expression that evaluates to a negative integer" \
    $1 \
    "(if (+ 1 (- 2))
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a binary sub expression that evaluates to a negative integer" \
    $1 \
    "(if (- 1 10)
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a binary multiplication expression that evaluates to a negative integer" \
    $1 \
    "(if (* (- 1) 2)
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a binary division expression that evaluates to a negative integer" \
    $1 \
    "(if (/ 16 (- 4))
       (print 1))" \
    "1"

  # Should take the branch if the test evaluates to a positive integer

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a positive integer literal" \
    $1 \
    "(if 1
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to an identifier that evaluates to a positive integer literal" \
    $1 \
    "(let [x 2]
       (if x
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a unary minus expression that evaluates to a positive integer" \
    $1 \
    "(if (- (- 3))
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a binary add expression that evaluates to a positive integer" \
    $1 \
    "(if (+ 0 4)
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a binary sub expression that evaluates to a positive integer" \
    $1 \
    "(if (- 10 5)
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a binary multiplication expression that evaluates to a positive integer" \
    $1 \
    "(if (* 6 1)
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to a binary division expression that evaluates to a positive integer" \
    $1 \
    "(if (/ 100 2)
       (print 1))" \
    "1"

# Should take the branch if the test evaluates to true

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to literal true" \
    $1 \
    "(if true
       (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test evaluates to an identifier that evaluates to literal true" \
    $1 \
    "(let [x true]
       (if x
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is a relational equal to expression that evaluates to true" \
    $1 \
    "(if (== 1 1)
      (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is a relational less than expression that evaluates to true" \
    $1 \
    "(if (< 0 1)
      (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is a relational less than or equal to expression that evaluates to true" \
    $1 \
    "(if (<= 1 10)
      (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is a relational greater than expression that evaluates to true" \
    $1 \
    "(if (> 5 1)
      (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is a relational greater than or equal to expression that evaluates to true" \
    $1 \
    "(if (>= 11 10)
      (print 1))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an identifier that evaluates to a relational equal to expression that evaluates to true" \
    $1 \
    "(let [x (== 1 1)]
       (if x
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an identifier that evaluates to a relational less than expression that evaluates to true" \
    $1 \
    "(let [x (< 0 1)]
       (if x
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an identifier that evaluates to a relational less than or equal to expression that evaluates to true" \
    $1 \
    "(let [x (<= 1 10)]
       (if x
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an identifier that evaluates to a relational greater than expression that evaluates to true" \
    $1 \
    "(let [x (> 5 1)]
       (if x
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should take the branch if the test is an identifier that evaluates to a relational greater than or equal to expression that evaluates to true" \
    $1 \
    "(let [x (>= 11 10)]
       (if x
         (print 1)))" \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should implicitly evaluate to zero when the branch evaluates to an integer and is not taken" \
    $1 \
    "(print
       (if false 42))" \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should implicitly evaluate to false when the branch evaluates to a boolean and is not taken" \
    $1 \
    "(println
       (if
         (==
           ; This if-then expression should implicitly
           ; evaluate to false
           (if false true)
           false)
         1))" \
    "1"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should implicitly evaluate to nil when the branch evaluates to nil and is not taken" \
    $1 \
    "(print
      ; This if-then expression should implicitly
      ; evaluate to nil
      (if false (let [])))" \
    "Encountered one or more semantic errors during type checking:


Cannot print operand 'if' because it does not evaluate to a known type (2:8)

(if false (let [])))
 ^^

1 Error"


  echo -e "\n"
}
