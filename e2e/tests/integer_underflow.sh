# !/bin/bash

source _utils.sh

run_integer_underflow_tests () {
  echo "Tests for integer underflow"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report an integer underflow error when an integer literal is less than -2^63" \
    $1 \
    "
    ; -2^63 = -9223372036854775808
    (- 9223372036854775809)" \
    "Encountered one or more semantic errors during type checking:


Encountered integer underflow from operand '9223372036854775809' (2:8)

(- 9223372036854775809)
   ^^^^^^^^^^^^^^^^^^^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept integer literal -2^63" \
    $1 \
    "
    ; -2^63 = -9223372036854775808
    (print (- 9223372036854775808))" \
    "-9223372036854775808"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept an integer literal that is less than -2^31" \
    $1 \
    "
    ; -2^31 = -2147483648
    (print (- 2147483649))" \
    "-2147483649"

  assert_exec_stdout_equalto_with_stdin \
    "Should accept integer literal -2^31" \
    $1 \
    "
    ; -2^31 = -2147483648
    (print (- 2147483648))" \
    "-2147483648"

  echo -e "\n"
}
