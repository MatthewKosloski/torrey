# The Torrey Programming Language

This is the first compiler of the Torrey programming language.

## Grammar

The parser of this compiler implements the following grammar:

```
program       -> expression* ;

expression    -> integer
               | unary
               | binary
               | print ;

unary         -> "(" unaryOp expression ")" ;
binary        -> "(" binOp expression expression ")" ;
print         -> "(" printOp exprlist ")" ;

exprlist      -> expression+ ;

integer       -> [0-9]+ ;
unaryOp       -> "-" ;
binOp         -> "+" | "-" | "*" | "/" ;
printOp       -> "print" | "println" ;
```