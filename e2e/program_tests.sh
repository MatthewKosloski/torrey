#!/bin/bash

source _utils.sh

run_program_tests () {
  echo "Tests for program"

  assert_stderr \
    $1 \
    "Should accept an empty program without error" \
    "" \
    ""

  assert_stderr \
    $1 \
    "Should accept a white space program without error" \
    "



      " \
    ""

  assert_stderr \
    $1 \
    "Should accept an integer literal without error" \
    "42" \
    ""

  assert_stderr \
    $1 \
    "Should accept boolean literal true without error" \
    "true" \
    ""

  assert_stderr \
    $1 \
    "Should accept boolean literal false without error" \
    "false" \
    ""

  assert_stderr \
    $1 \
    "Should report a syntax error when given an identifier" \
    "foobar" \
    "Encountered one or more semantic errors during environment building:


Identifier 'foobar' is not defined in this environment (1:1)

foobar
^^^^^^

1 Error"

  assert_stdout \
    $1 \
    "Should accept exactly one expression" \
    "(println 42)" \
    "42"

  assert_stdout \
    $1 \
    "Should accept more than one expression" \
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
