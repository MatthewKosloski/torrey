#!/bin/bash

assert_stdout () {
  echo "$3" | java -jar $1 -o a.out 2> stderr.txt && ./a.out > stdout.txt

  if [ -f "stderr.txt" ]; then
    local actual_stderr=$(cat stderr.txt)
  fi

  if [ -f "stdout.txt" ]; then
    local actual_stdout=$(cat stdout.txt)
  fi

  if [[ $actual_stderr != "" ]]; then
    echo -e "  \033[31m[FAILED]\033[0m $2"
    echo -e "    Expected stdout: \"$4\"\n    Actual stderr: \"$actual_stderr\""
    count_failed=$(($count_failed+1))
  elif [[ $actual_stdout != $4 ]]; then
    echo -e "  \033[31m[FAILED]\033[0m $2"
    echo -e "    Expected stdout: \"$4\"\n    Actual stdout: \"$actual_stdout\""
    count_failed=$(($count_failed+1))
  else
    echo -e "  \033[32m[PASSED]\033[0m $2"
    count_passed=$(($count_passed+1))
  fi

  if [ -f "stderr.txt" ]; then
    rm stderr.txt
  fi

  if [ -f "stdout.txt" ]; then
    rm stdout.txt
  fi

  count_total=$(($count_total+1))
}

assert_stderr () {
  echo "$3" | java -jar $1 -o a.out 2> stderr.txt

  if [ -f "stderr.txt" ]; then
    local actual_stderr=$(cat stderr.txt)
  fi

  if [[ $actual_stderr != $4 ]]; then
    echo -e "  \033[31m[FAILED]\033[0m $2"
    echo -e "    Expected stderr: \"$4\"\n    Actual stderr: \"$actual_stderr\""
    count_failed=$(($count_failed+1))
  else
    echo -e "  \033[32m[PASSED]\033[0m $2"
    count_passed=$(($count_passed+1))
  fi

  if [ -f "stderr.txt" ]; then
    rm stderr.txt
  fi

  count_total=$(($count_total+1))
}
