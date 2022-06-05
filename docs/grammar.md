 Grammar

The following grammar describes the syntax of a syntactically valid Torrey program. Note that _syntactic validity_ does not imply _semantic validity_. That is, a Torrey program can look right while also have no meaning.  For example, the Torrey program `(+ (print 3) 5)` is _syntactically_ valid because it can be derived from applying zero or more grammar rules.  However, it is _semantically_ invalid as the operands to the `+` operator must be integers.

The following grammar, expressed using [EBNF](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form), is implemented by the compiler's parser.

```
program       -> expr* ;

expr          -> primitive
               | identifier
               | unary
               | binary
               | print
               | let
               | not
               | and
               | or
               | if ;

boolean       -> "true" | "false" ;
integer       -> [0-9]+ ;
identifier    -> [a-zA-Z_$]+ [a-zA-Z0-9_$!?-]* ;
primitive     -> integer | boolean ;
unary         -> "(" "-" expr ")" ;
binary        -> "(" ("+" | "-" | "*" | "/" | "==" | "<" | "<=" | ">" | ">=") expr expr ")" ;
print         -> "(" ("print" | "println") expr+ ")" ;
let           -> "(" "let" "[" (identifier expr)* "]" expr* ")" ;
not           -> "(" "not" expr ")" ;
and           -> "(" "and" expr expr+ ")" ;
or            -> "(" "or" expr expr+ ")" ;
if            -> "(" "if" expr expr expr? ")" ;
```
