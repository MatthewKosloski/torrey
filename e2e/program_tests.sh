# !/bin/bash

source _utils.sh

run_program_tests () {
  echo "Tests for program"

  assert_torreyc_stderr_empty_with_stdin \
    "Should accept an empty program without error" \
    $1 \
    ""

  assert_torreyc_stderr_empty_with_stdin \
    "Should accept a white space program without error" \
    $1 \
    "



      " \
    ""

  assert_torreyc_stderr_empty_with_stdin \
    "Should accept an integer literal without error" \
    $1 \
    "42"

  assert_torreyc_stderr_empty_with_stdin \
    "Should accept boolean literal true without error" \
    $1 \
    "true"

  assert_torreyc_stderr_empty_with_stdin \
    "Should accept boolean literal false without error" \
    $1 \
    "false"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error when given an identifier that is not scoped to an environment" \
    $1 \
    "foobar" \
    "Encountered one or more semantic errors during environment building:


Identifier 'foobar' is not defined in this environment (1:1)

foobar
^^^^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error when program is a left parenthesis" \
    $1 \
    "(" \
    "Encountered one or more syntax errors during parse:


Expected a unary, binary, or print expression but found '' instead (1:2)

(
 ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error when program is a right parenthesis" \
    $1 \
    ")" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ')' instead (1:1)

)
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error when program is an unexpected character" \
    $1 \
    "@" \
    "Encountered one or more syntax errors during lexing:


Unexpected character '@' (1:1)

@
^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error when there is an unexpected character after an expression" \
    $1 \
    "(let [x 42]
       (print x))%" \
    "Encountered one or more syntax errors during lexing:


Unexpected character '%' (2:18)

(print x))%
          ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept exactly one expression" \
    $1 \
    "(println 42)" \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept more than one expression" \
    $1 \
    "(let [a 5]
       (+ 2 a))
     (println
       (let [a 2]
         (- 2)))
     (if false
       (print 1)
       (print 0))" \
    "-2
0"

  echo -e "\n"
}
