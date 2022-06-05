# !/bin/bash

source _utils.sh

run_integer_overflow_tests () {
  echo "Tests for integer overflow"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an integer literal that is less than 2^31-1" \
    $1 \
    "
    ; 2^31-1 = 2147483647
    (print 2147483646)" \
    "2147483646"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept integer literal 2^31-1" \
    $1 \
    "
    ; 2^31-1 = 2147483647
    (print 2147483647)" \
    "2147483647"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an integer literal that is greater than 2^31-1" \
    $1 \
    "
    ; 2^31-1 = 2147483647
    (print 2147483648)" \
    "2147483648"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept integer literal 2^63-1" \
    $1 \
    "
    ; 9223372036854775807 = 2^63-1
    (print 9223372036854775807)" \
    "9223372036854775807"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report an integer overflow error when an integer literal is greater than 2^63-1" \
    $1 \
    "
    ; 9223372036854775808 = 2^63
    9223372036854775808" \
    "Encountered one or more semantic errors during type checking:


Encountered integer overflow from operand '9223372036854775808' (2:5)

9223372036854775808
^^^^^^^^^^^^^^^^^^^

1 Error"

  echo -e "\n"
}
