#!/bin/bash

# -------------------
# Assertion functions
# -------------------

# Helpers for asserting against the compiler's standard output

assert_torreyc_stdout_empty () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=$3
  _assert_torreyc_stdout \
    "$test_title" $compiler_path "$cli_args" "" 0
}

assert_torreyc_stdout_equalto () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=$3
  local expected_stdout=$4
  _assert_torreyc_stdout \
    "$test_title" $compiler_path "$cli_args" "$expected_stdout" 0
}

assert_torreyc_stdout_equalto_with_stdin () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=""
  local stdin=$3
  local expected_stdout=$4
  assert_torreyc_stdout_equalto_with_stdin_and_cli_args \
    "$test_title" $compiler_path "$cli_args" "$stdin" "$expected_stdout"
}

assert_torreyc_stdout_equalto_with_stdin_and_cli_args () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=$3
  local stdin=$4
  local expected_stdout=$5
  _pipe_stdin_to_torreyc_and_assert_torreyc_stdout \
    "$test_title" $compiler_path "$cli_args" "$stdin" "$expected_stdout" 0
}

assert_torreyc_stdout_contains () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=$3
  local expected_stdout=$4
  _assert_torreyc_stdout \
    "$test_title" $compiler_path "$cli_args" "$expected_stdout" 1
}

assert_torreyc_stdout_contains_with_stdin () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=$3
  local stdin=$4
  local expected_stdout=$5
  _pipe_stdin_to_torreyc_and_assert_torreyc_stdout \
    "$test_title" $compiler_path "$cli_args" "$stdin" "$expected_stdout" 1
}

# Helpers for asserting against the compiler's standard error

assert_torreyc_stderr_empty () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=$3
  local expected_stderr=""
  _assert_torreyc_stderr_equalto \
    "$test_title" $compiler_path "$cli_args" "$expected_stderr"
}

assert_torreyc_stderr_equalto () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=$3
  local expected_stderr=$4
  _assert_torreyc_stderr_equalto \
    "$test_title" $compiler_path "$cli_args" "$expected_stderr"
}

assert_torreyc_stderr_empty_with_stdin () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=""
  local stdin=$3
  local expected_stderr=""
  assert_torreyc_stderr_equalto_with_stdin_and_cli_args \
    "$test_title" $compiler_path "$cli_args" "$stdin" "$expected_stderr"
}

assert_torreyc_stderr_equalto_with_stdin () {
  local test_title=$1
  local compiler_path=$2
  local cli_args=""
  local stdin=$3
  local expected_stderr=$4
  assert_torreyc_stderr_equalto_with_stdin_and_cli_args \
    "$test_title" $compiler_path "$cli_args" "$stdin" "$expected_stderr"
}

assert_torreyc_stderr_equalto_with_stdin_and_cli_args () {
  local stderr_file="torreyc_stderr.txt"

  local compiler_path=$2
  local cli_args=$3
  local stdin=$4
  echo "$stdin" | java -jar $compiler_path $cli_args 2> $stderr_file

  # Performs side-effects and cleanup
  local test_title=$1
  local expected_stderr=$5
  _log_and_record_result_for_stderr_assertion $stderr_file "$test_title" "$expected_stderr"
}

# Helpers for asserting against the standard output of executables

assert_exec_stdout_equalto_with_stdin () {
  local exec_path="a.out"
  local cli_args="-o $exec_path"
  local test_title=$1
  local compiler_path=$2
  local stdin=$3
  local expected_stdout=$4
  assert_exec_stdout_equalto_with_stdin_and_cli_args \
    "$test_title" $compiler_path "$cli_args" "$stdin" "$exec_path" "$expected_stdout"
}

assert_exec_stdout_equalto_with_stdin_and_cli_args () {
  local stdout_file="torreyc_stdout.txt"
  local stderr_file="torreyc_stderr.txt"

  local compiler_path=$2
  local cli_args=$3
  local stdin=$4
  local exec_path=$5
  echo "$stdin" | java -jar $compiler_path $cli_args 2> $stderr_file \
    && ./$exec_path > $stdout_file

  # Performs side-effects and cleanup
  local test_title=$1
  local expected_stdout=$6
  _log_and_record_result_for_stdout_assertion \
    $stdout_file $stderr_file "$test_title" "$expected_stdout" 0
}

assert_exec_stdout_equalto_with_infile () {
  local stdout_file="torreyc_stdout.txt"
  local stderr_file="torreyc_stderr.txt"

  local compiler_path=$2
  local cli_args=$3
  local program=$4
  local exec_path=$5
  local infile_path=$6

  echo "$program" > $infile_path
  java -jar $compiler_path $cli_args 2> $stderr_file \
    && ./$exec_path > $stdout_file

  # Performs side-effects and cleanup
  local test_title=$1
  local expected_stdout=$7
  _log_and_record_result_for_stdout_assertion \
    $stdout_file $stderr_file "$test_title" "$expected_stdout" 0

  # Delete input file
  rm $infile_path
}

assert_outfile_contents_equalto () {
  local stderr_file="torreyc_stderr.txt"

  local compiler_path=$2
  local cli_args=$3
  local program=$4

  echo "$program" | java -jar $compiler_path $cli_args 2> $stderr_file

  # Performs side-effects and cleanup
  local test_title=$1
  local outfile_file=$5
  local expected_outfile_contents=$6
  _log_and_record_result_for_stdout_assertion \
    $outfile_file $stderr_file "$test_title" "$expected_outfile_contents" 0
}

# ----------------
# Helper functions
# ----------------

_assert_torreyc_stdout () {
  local stdout_file="torreyc_stdout.txt"
  local stderr_file="torreyc_stderr.txt"

  local compiler_path=$2
  local cli_args=$3
  java -jar $compiler_path $cli_args 2> $stderr_file > $stdout_file

  # Performs side-effects
  local test_title=$1
  local expected_stdout=$4
  local equalto_or_contains=$5
  _log_and_record_result_for_stdout_assertion \
    $stdout_file $stderr_file "$test_title" "$expected_stdout" $equalto_or_contains
}

_assert_torreyc_stderr_equalto () {
  local stderr_file="torreyc_stderr.txt"

  local compiler_path=$2
  local cli_args=$3

  java -jar $compiler_path $cli_args 2> $stderr_file

  # Performs side-effects and cleanup
  local test_title=$1
  local expected_stderr=$4
  _log_and_record_result_for_stderr_assertion \
    $stderr_file "$test_title" "$expected_stderr"
}

_pipe_stdin_to_torreyc_and_assert_torreyc_stdout () {
  local stdout_file="torreyc_stdout.txt"
  local stderr_file="torreyc_stderr.txt"

  local compiler_path=$2
  local cli_args=$3
  local stdin=$4
  echo "$stdin" | java -jar $compiler_path $cli_args 2> $stderr_file > $stdout_file

  # Performs side-effects and cleanup
  local test_title=$1
  local expected_stdout=$5
  local equalto_or_contains=$6
  _log_and_record_result_for_stdout_assertion \
    $stdout_file $stderr_file "$test_title" "$expected_stdout" $equalto_or_contains
}

# Preconditions:
#   Stdout is written to file $1; Stderr is written to file $2.
#   If this precondition is not true, then the script
#   will exit with failure.
#
# Postconditions:
#   Files $1 and $2 are removed.
_log_and_record_result_for_stdout_assertion () {
  local stdout_file=$1
  local stderr_file=$2
  local test_title=$3
  local expected_stdout=$4
  local equalto_or_contains=$5

  # Ensure stderr file exists on fs
  if [ -f "$stderr_file" ]; then
    local actual_stderr=$(cat $stderr_file)
  fi

  # Ensure stdout file exists on fs
  if [ -f "$stdout_file" ]; then
    local actual_stdout=$(cat $stdout_file)
  fi

  # Performs side-effects
  _log_result_for_stdout_assertion \
    "$test_title" "$expected_stdout" "$actual_stdout" "$actual_stderr" "$equalto_or_contains"
  # Performs side-effects
  _record_result_for_stdout_assertion \
    "$expected_stdout" "$actual_stdout" "$actual_stderr"

  # Cleanup temp files
  if [ -f "$stdout_file" ]; then rm $stdout_file; fi
  if [ -f "$stderr_file" ]; then rm $stderr_file; fi
}

# Preconditions:
#   Stderr is written to file $1.
#   If this precondition is not true, then the script will
#   exit with failure.
#
# Postconditions:
#   File $1 is removed.
_log_and_record_result_for_stderr_assertion () {
  local stderr_file=$1
  local test_title=$2
  local expected_stderr=$3

  if [ -f "$stderr_file" ]; then
    local actual_stderr=$(cat $stderr_file)
  fi

  # Performs side-effects
  _log_result_for_stderr_assertion "$test_title" "$expected_stderr" "$actual_stderr"
  # Performs side-effects
  _record_result_for_stderr_assertion "$expected_stderr" "$actual_stderr"

  if [ -f "$stderr_file" ]; then rm $stderr_file; fi
}

_log_result_for_stdout_assertion () {
  local test_title=$1
  local expected_stdout=$2
  local actual_stdout=$3
  local actual_stderr=$4
  local equalto_or_contains=$5

  local failed_prefix="  \033[31m[FAILED]\033[0m $test_title"
  local passed_prefix="  \033[32m[PASSED]\033[0m $test_title"

  if [[ "$actual_stderr" != "" ]]; then
    # We expect stdout but we got stderr, fail test
    echo -e "$failed_prefix"
    if [[ "$verbosity" != "$VERBOSITY_QUIET" ]]; then
      echo -e "\n\tExpected stdout: \"$expected_stdout\"\n\tActual stderr: \"$actual_stderr\""
    fi
  elif [[ "$equalto_or_contains" -eq 0 && "$actual_stdout" != "$expected_stdout" ]]; then
    # Actual stdout does not equal expected, fail test
    echo -e "$failed_prefix"
    if [[ "$verbosity" != "$VERBOSITY_QUIET" ]]; then
      echo -e "\n\tExpected stdout: \"$expected_stdout\"\n\tActual stdout: \"$actual_stdout\""
    fi
  elif [[ "$equalto_or_contains" -eq 1 && "$actual_stdout" != *"$expected_stdout"* ]]; then
    # Actual stdout does not contain expected, fail test
    echo -e "$failed_prefix"
    if [[ "$verbosity" != "$VERBOSITY_QUIET" ]]; then
      echo -e "\n\tExpected stdout: \"$expected_stdout\"\n\tActual stdout: \"$actual_stdout\""
    fi
  else
    # We did not get stderr and actual stdout equals expected, pass test
    echo -e "$passed_prefix"
  fi
}

_log_result_for_stderr_assertion () {
  local test_title=$1
  local expected_stderr=$2
  local actual_stderr=$3

  local failed_prefix="  \033[31m[FAILED]\033[0m $test_title"
  local passed_prefix="  \033[32m[PASSED]\033[0m $test_title"

  if [[ "$actual_stderr" != "$expected_stderr" ]]; then
    # Actual stderr does not equal expected, fail test
    echo -e "$failed_prefix"
    if [[ "$verbosity" != "$VERBOSITY_QUIET" ]]; then
      echo -e "\n\tExpected stderr: \"$expected_stderr\"\n\tActual stderr: \"$actual_stderr\""
    fi
  else
    # Actual stderr equals expected, pass test
    echo -e "$passed_prefix"
  fi
}

# Postconditions:
#   If $3 is not empty OR $1 is not equal to $2, then
#   count_failed global variable is incremented; else,
#   count_passed global variabled is incremented.
#   The count_total global variable is incremented.
_record_result_for_stdout_assertion () {
  local expected_stdout=$1
  local actual_stdout=$2
  local actual_stderr=$3

  if [[ $actual_stderr != "" ]]; then
    # We expect stdout but we got stderr, record failure
    count_failed=$(($count_failed+1))
  elif [[ $actual_stdout != $expected_stdout ]]; then
    # Actual stdout does not equal expected, record failure
    count_failed=$(($count_failed+1))
  else
    # We did not get stderr and actual stdout equals expected, record pass
    count_passed=$(($count_passed+1))
  fi

  # Increment total count of tests run
  count_total=$(($count_total+1))
}

# Postconditions:
#   If $1 is not equal to $2, then count_failed global
#   variable is incremented; else, count_passed global
#   variabled is incremented.
#   The count_total global variable is incremented.
_record_result_for_stderr_assertion () {
  local expected_stderr=$1
  local actual_stderr=$2

  if [[ "$actual_stderr" != "$expected_stderr" ]]; then
    # Actual stderr does not equal expected, record failure
    count_failed=$(($count_failed+1))
  else
    # Actual stderr equals expected, record pass
    count_passed=$(($count_passed+1))
  fi

  # Increment total count of tests run
  count_total=$(($count_total+1))
}
