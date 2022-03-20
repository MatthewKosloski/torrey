#!/bin/bash

source _utils.sh

run_program_tests () {
  echo "Tests for program"

  assert_stdout \
    $1 \
    "Should accept an empty program" \
    "" \
    ""

  assert_stdout \
    $1 \
    "Should accept a white space program" \
    "



      " \
    ""

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
         (- 2)))" \
    "-2"

  echo -e "\n"
}
