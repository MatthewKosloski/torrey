#!/bin/bash
all_args=("$@")
program=$1
version=$2
path_prefix=$3
rest_args=("${all_args[@]:3}")
compiler_path="$path_prefix/torreyc-$version.jar"

if [ ! -f "$compiler_path" ]; then
 >&2 echo "Cannot find compiler at $compiler_path."
fi

if [ "${#rest_args[@]}" -ne "0" ]; then
    echo "$program" | java -jar $compiler_path $rest_args

else
    echo "$program" | java -jar $compiler_path -S > $path_prefix/temp.s

	if [ $? -eq 0 ]; then
	    # The compiler terminated normally,
		# so let's try to assemble and run.
	    gcc $path_prefix/temp.s $path_prefix/runtime.o -o $path_prefix/a.out \
            && $path_prefix/a.out \
			&& rm $path_prefix/temp.s $path_prefix/a.out
        exit 0
	else
        exit 1
    fi
fi