# !/bin/bash
source program_tests.sh
source print_expr_tests.sh
source println_expr_tests.sh
source unary_expr_tests.sh
source binary_add_expr_tests.sh
source let_expr_tests.sh
source if_expr_tests.sh
source if_else_expr_tests.sh
source cli_tests.sh

count_total=0
count_passed=0
count_failed=0

compiler_file_name="torreyc-3.1.2.jar"
compiler_target_path="../target/$compiler_file_name"
runtime_source_path="../src/runtime/runtime.c"

# Check if gcc dependency is installed.
if [ "$(type -t gcc)" != "file" ]; then
  >&2 echo -e "gcc is not installed\nExiting"
  exit 1
fi

# Check if java dependency is installed.
if [ "$(type -t java)" != "file" ]; then
  >&2 echo -e "java is not installed\nExiting"
  exit 1
fi

# Check if the runtime source file exists.
if [ ! -f "$runtime_source_path" ]; then
  >&2 echo -e "Runtime source file does not exist at $runtime_source_path\nExiting"
  exit 1
fi

# Check if the compiler jarfile exists.
if [ ! -f "$compiler_target_path" ]; then
  >&2 echo -e "Compiler jarfile does not exist at $compiler_target_path. Please run 'mvn package' from the project root directory\nExiting"
  exit 1
fi

# Copy the compiler jar into this directory
cp $compiler_target_path .

# Build the runtime and write the object file into this directory
gcc -c $runtime_source_path -o ./runtime.o

VERBOSITY_QUIET='VERBOSITY_QUIET'
VERBOSITY_LOUD='VERBOSITY_LOUD'
if [[ "$1" == "--quiet" ]]; then
  verbosity=VERBOSITY_QUIET
else
  verbosity=VERBOSITY_LOUD
fi

# At this point, we can now run all e2e tests.
# Run tests, passing in the path to the compiler jar as the first argument
run_program_tests ./$compiler_file_name
run_print_expr_tests ./$compiler_file_name
run_println_expr_tests ./$compiler_file_name
run_unary_expr_tests ./$compiler_file_name
run_binary_add_expr_tests ./$compiler_file_name
run_let_expr_tests ./$compiler_file_name
run_if_expr_tests ./$compiler_file_name
run_if_else_expr_tests ./$compiler_file_name
run_cli_tests ./$compiler_file_name

# Display statistics
echo -e "Ran $count_total tests\n Passed: $count_passed"
# If awk is available to us, calculate percentage of tests that passed
if [[ "$(type -t awk)" == "file" && $count_total -ne 0 ]]; then
  percent_passed=$(echo - | awk "{print $count_passed / $count_total * 100}")
  echo "  ($percent_passed%)"
fi
echo -e " Failed: $count_failed"

# Cleanup
if [ -f "a.out" ]; then rm a.out; fi
if [ -f "runtime.o" ]; then rm runtime.o; fi
if [ -f "$compiler_file_name" ]; then rm $compiler_file_name; fi

# Exit
if [ "$count_failed" -eq "0" ]; then
  exit 0
else
  # At least one e2e test failed, so exit with non-success status
  exit 1
fi
