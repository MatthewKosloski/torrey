#!/bin/bash
all_args=("$@")
program=$1
version=$2
rest_args=("${all_args[@]:2}")

compiler_file_name="torreyc-$version.jar"
runtime_source_file_name="runtime.c"
runtime_build_file_name="runtime.o"
assembly_file_name="temp.s"
executable_file_name="a.out"

build_dir="./utils/run/build"
src_dir="../src"
runtime_src_dir="$src_dir/runtime"
target_dir="../target"

compiler_target_path="$target_dir/$compiler_file_name"
compiler_build_path="$build_dir/$compiler_file_name"
runtime_source_path="$runtime_src_dir/$runtime_source_file_name"
runtime_build_path="$build_dir/$runtime_build_file_name"
executable_path="$build_dir/$executable_file_name"
assembly_path="$build_dir/$assembly_file_name"

if [ ! -f "$runtime_source_path" ]; then
    >&2 echo "Runtime source file does not exist at $runtime_source_path."
    exit 64
fi

if [ ! -f "$compiler_target_path" ]; then
    >&2 echo "Target compiler not found at $compiler_target_path. Please run the Maven package command."
    exit 64
fi

if [ ! -d "$build_dir" ]; then
    # Build directory does not exist, so make the directory
    mkdir -p $build_dir

    # Build the runtime object file
    gcc -c $runtime_source_path -o $runtime_build_path

    # Copy the compiler into the build directory
    cp $compiler_target_path $compiler_build_path
fi

if [ "${#rest_args[@]}" -ne "0" ]; then
    # We've been given compiler arguments, so run it with those arguments.
    echo "$program" | java -jar $compiler_build_path $rest_args
else
    # We've been given no compiler arguments, so compile and execute a program.
    echo "$program" | java -jar $compiler_build_path -S > $assembly_path

	if [ $? -eq 0 ]; then
	    # The compiler terminated normally, so let's try to assemble and run.

        # This command will:
        #   1. Assemble and link runtime object file with assembly code
        #      to produce an executable
        #   2. Execute the executable
        #   3. Delete the temporary assembly file and executable
	    gcc $assembly_path $runtime_build_path -o $executable_path \
            && $executable_path \
			&& rm $assembly_path $executable_path
        exit 0
	else
        # Delete the temporary assembly file and exit with non-zero
        # exit code to indicate compiler error.
        rm $assembly_path
        exit 1
    fi
fi
