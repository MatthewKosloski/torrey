# !/bin/bash

source _utils.sh

run_let_expr_tests () {
  echo "Tests for \"(\" \"let\" \"[\" (identifier expr)* \"]\" expr* \")\""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis and no bindings list" \
    $1 \
    "let)" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'let' instead (1:1)

let)
^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left parenthesis" \
    $1 \
    "let [])" \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found 'let' instead (1:1)

let [])
^^^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no bindings list" \
    $1 \
    "(let)" \
    "Encountered one or more syntax errors during parse:


Expected an opening bracket '[' (1:5)

(let)
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error when there is no identifier after the opening bracket" \
    $1 \
    "(let [)" \
    "Encountered one or more syntax errors during parse:


Expected a closing bracket ']' or the start of an identifier but found ')' instead (1:7)

(let [)
      ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no left bracket" \
    $1 \
    "(let" \
    "Encountered one or more syntax errors during parse:


Expected an opening bracket '[' (1:5)

(let
    ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right bracket when there is a binding" \
    $1 \
    "(let [x 1)" \
    "Encountered one or more syntax errors during parse:


Expected a closing bracket ']' or the start of an identifier but found ')' instead (1:10)

(let [x 1)
         ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis when there are no bindings" \
    $1 \
    "(let []" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:8)

(let []
       ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if no right parenthesis when there is a binding" \
    $1 \
    "(let [x 500]" \
    "Encountered one or more syntax errors during parse:


Expected a closing parenthesis ')' (1:13)

(let [x 500]
            ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should allow an empty bindings list with no body expression" \
    $1 \
    "(let [])" \
    ""

  assert_torreyc_stderr_equalto_with_stdin \
    "Should allow an empty bindings list with a body expression" \
    $1 \
    "(let [] true)" \
    ""

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to start with lowercase letters" \
    $1 \
    "(print
       (let [a 1] a)
       (let [ba 2] ba)
       (let [zm 3] zm))" \
    "123"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to start with uppercase letters" \
    $1 \
    "(print
       (let [A 1] A)
       (let [Ba 2] Ba)
       (let [Zm 3] Zm))" \
    "123"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to start with an underscore character" \
    $1 \
    "(print
       (let [_foo 1] _foo)
       (let [__bar 2] __bar)
       (let [___baz 3] ___baz))" \
    "123"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to contain underscore characters" \
    $1 \
    '(print
       (let [f_oo 1] f_oo)
       (let [ba_r 2] ba_r)
       (let [baz_ 3] baz_)
       (let [bax__ 4] bax__)
       (let [fo_o__bar___ 5] fo_o__bar___))' \
    "12345"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to start with a dollar sign character" \
    $1 \
    '(print
       (let [$foo 1] $foo)
       (let [$$bar 2] $$bar)
       (let [$$$baz 3] $$$baz))' \
    "123"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to contain dollar sign characters" \
    $1 \
    '(print
       (let [f$oo 1] f$oo)
       (let [ba$r 2] ba$r)
       (let [baz$ 3] baz$)
       (let [bax$$ 4] bax$$)
       (let [fo$o$$bar$$$ 5] fo$o$$bar$$$))' \
    "12345"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if identifiers start with exclamation characters" \
    $1 \
    '(let [!foo])' \
    "Encountered one or more syntax errors during lexing:


Unexpected character '!' (1:7)

(let [!foo])
      ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to contain exclamation characters" \
    $1 \
    '(print
       (let [f!oo 1] f!oo)
       (let [ba!r 2] ba!r)
       (let [baz! 3] baz!)
       (let [bax!! 4] bax!!)
       (let [fo!o!!bar!!! 5] fo!o!!bar!!!))' \
    "12345"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if an identifier starts with a question mark character" \
    $1 \
    '(let [?foo])' \
    "Encountered one or more syntax errors during lexing:


Unexpected character '?' (1:7)

(let [?foo])
      ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to contain question mark characters" \
    $1 \
    '(print
       (let [f?oo 1] f?oo)
       (let [ba?r 2] ba?r)
       (let [baz? 3] baz?)
       (let [bax?? 4] bax??)
       (let [fo?o??bar??? 5] fo?o??bar???))' \
    "12345"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if an identifier starts with a hyphen character" \
    $1 \
    '(let [-foo] -foo)' \
    "Encountered one or more syntax errors during parse:


Expected a closing bracket ']' or the start of an identifier but found '-' instead (1:7)

(let [-foo] -foo)
      ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to contain hyphen characters" \
    $1 \
    '(print
       (let [f-oo 1] f-oo)
       (let [ba-r 2] ba-r)
       (let [baz- 3] baz-)
       (let [bax-- 4] bax--)
       (let [fo-o--bar--- 5] fo-o--bar---))' \
    "12345"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if an identifier starts with an integer" \
    $1 \
    '(let [1foo] 1foo)' \
    "Encountered one or more syntax errors during parse:


Expected a closing bracket ']' or the start of an identifier but found '1' instead (1:7)

(let [1foo] 1foo)
      ^

1 Error"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to contain integers" \
    $1 \
    '(print
       (let [f1oo 1] f1oo)
       (let [ba2r 2] ba2r)
       (let [baz3 3] baz3)
       (let [bax42 4] bax42)
       (let [fo1o23bar456 5] fo1o23bar456))' \
    "12345"

  assert_exec_stdout_equalto_with_stdin \
    "Should use case sensitivity when evaluating identifiers" \
    $1 \
    '(let [a 1 A 2] (print A a))' \
    "21"

  assert_exec_stdout_equalto_with_stdin \
    "Should traverse the lexical scope chain" \
    $1 \
    '(let [a 1 b 2 c 3]
       (println a b c)
       (let [a 11 d 4]
         (println a b c d)
         (let [d 44 e 5]
            (println a b c d e))))' \
    "1
2
3
11
2
3
4
11
2
3
44
5"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if an identifier cannot be found in the lexical environment" \
    $1 \
    '(let [a 1]
        (println b)
        (let [b 2]
          (println b)))' \
    "Encountered one or more semantic errors during environment building:


Identifier 'b' is not defined in this environment (2:18)

(println b)
         ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if an identifier has already been assigned a value" \
    $1 \
    '(let [a 1 a 2])' \
    "Encountered one or more semantic errors during environment building:


Identifier 'a' has already been declared in this scope (1:11)

(let [a 1 a 2])
          ^

1 Error"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if an identifier has not been bounded to an expression" \
    $1 \
    '(let [a])' \
    "Encountered one or more syntax errors during parse:


Expected an integer, unary, binary, print, let, or identifier expression but found ']' instead (1:8)

(let [a])
       ^

1 Error"


  assert_exec_stdout_equalto_with_stdin \
    "Should allow the expressions bounded to identifiers to reference previously defined identifiers" \
    $1 \
    '(let [a 1 b (+ 2 a)]
        (println a b)
        (let [a 11 c (* a b) d (+ a c)]
          (println a b c d)))' \
    "1
3
11
3
33
44"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to be bounded to integer literals" \
    $1 \
    '(let [a 1 b 200]
        (print a b))' \
    "1200"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to be bounded to boolean literal true" \
    $1 \
    '(println
       (let [a true]
         (if a 1 0)))' \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to be bounded to boolean literal false" \
    $1 \
    '(println
       (let [a false]
         (if a 1 0)))' \
    "0"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow identifiers to be bounded to previously defined identifiers" \
    $1 \
    '(println
       (let [a 42 b a] b))' \
    "42"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a unary expression" \
    $1 \
    '(let [a (- 1) b (- a)]
       (print a b))' \
    "-11"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a binary addition expression" \
    $1 \
    '(let [a (+ 1 2) b (+ 1 a)]
       (print a b))' \
    "34"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a binary subtraction expression" \
    $1 \
    '(let [a (- 1 2) b (- 1 a)]
       (print a b))' \
    "-12"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a binary multiplication expression" \
    $1 \
    '(let [a (* 1 2) b (* 1 a)]
       (print a b))' \
    "22"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a binary division expression" \
    $1 \
    '(let [a (/ 1 2) b (/ a 1)]
       (print a b))' \
    "00"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a relational equal to expression" \
    $1 \
    '(let [a (== true true) b (== a false)]
       (if a (print 1)))' \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a relational less than expression" \
    $1 \
    '(print
       (let [a (< 0 1)]
         (if a 1)))' \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a relational less than or equal to expression" \
    $1 \
    '(print
       (let [a (<= 0 1)]
         (if a 1)))' \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a relational greater than expression" \
    $1 \
    '(print
       (let [a (> 0 1)]
         (if a 0 1)))' \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a relational greater than or equal to expression" \
    $1 \
    '(print
       (let [a (>= 0 1)]
         (if a 0 1)))' \
    "1"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a print expression" \
    $1 \
    '(let [a (print 2)])' \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to a println expression" \
    $1 \
    '(let [a (println 2)])' \
    "2"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to an if expression" \
    $1 \
    '(print
       (let [a (if true 69)]
         a))' \
    "69"

  assert_exec_stdout_equalto_with_stdin \
    "Should allow an identifier to be bounded to an if-else expression" \
    $1 \
    '(print
       (let [a (if false 0 1)]
         a))' \
    "1"

  assert_torreyc_stderr_equalto_with_stdin \
    "Should report a syntax error if an identifier is bounded to an empty let expression" \
    $1 \
    '(let [a (let [])])' \
    "69"

  echo -e "\n"
}
