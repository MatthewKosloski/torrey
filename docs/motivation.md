# Motivation

Algorithms that are employed to solve computational problems must be implemented in some
_high-level programming language_. That is, a language that abstracts away the many low-level
details of the computer (e.g., registers, memory addresses, the stack, calling conventions, etc.),
thereby making it loosely coupled to the hardware. These high-level languages contrast greatly
to low-level languages (e.g., assembly languages), which are very tightly coupled to a computer
and its architecture. While this abstraction has facilitated accessibility and the speed at which
programming can take place, it has simultaneously (by design) “shielded” programmers, many
of whom take the abstraction for granted.

There are several different ways of implementing a
high-level programming language, one of which is through _compilation_. A _compiler_ is a computer program that translates a program written in a source language to an equivalent program in a target
language.

In an attempt to better my understanding of the low-level details that high-level languages abstract away, I am implementing my own novel programming language by constructing a compiler to translate the Lisp-like high-level language to an equivalent program in x86-64 assembly code.
