# The Torrey Programming Language
[![GitHub Actions CI](https://github.com/microsoft/TypeScript/workflows/CI/badge.svg)](https://github.com/matthewkosloski/torrey/actions?query=workflow%3AContinuous+Integration)

This is the source code of the compiler that implements the Torrey programming language.

## Docs

The documentation for the usage and implementation of the compiler can be found in the `/docs` directory.

## What is Torrey?

Torrey, named after [Torrey Pines State Natural Reserve](https://en.wikipedia.org/wiki/Torrey_Pines_State_Natural_Reserve), is a novel, "Lisp-like" programming language.  That is, its [syntax](https://en.wikipedia.org/wiki/Syntax_(programming_languages)) (form) and [semantics](https://en.wikipedia.org/wiki/Semantics_(computer_science)) (meaning)
are inspired by that of the Lisp languages (e.g., Common Lisp, Scheme, Racket, Clojure, etc.).

## Developing Locally

### Setup

Before working locally, from the project root directory, run `setup.sh` to run various setup procedures (e.g., configuring the conventional commit message validation):

```bash
$ bash setup.sh
```

### Dev Environment

> Note: If your host OS is Ubuntu, then you can skip this section.

The compiler's assembler invokes `gcc` to create executables. Moreover, the only supported back-end is `x86_64-pc-linux`.  Thus, in order to run the programs created by the compiler, one must be using Ubuntu.  Since running programs produced by the compiler is a common activity associated with the compiler's development, we need a way to create development environments that contain all necessary requirements (e.g., `gcc`, `mvn`, `java`, etc.).  We are leveraging [Vagrant](https://www.vagrantup.com/) for development environments.

Vagrant is a tool for building and managing virtual machine environments in a single workflow.  It isolates dependencies and their configuration within a single disposable, consistent environment, without sacrificing any of the tools you are used to working with.  You just need to checkout the code, run `vagrant up`, and everything is installed and configured for you to work (this is all specified within the `Vagrantfile`).

Whether you are working on Linux, Mac OS X, or Windows, everyone runs code in the same environment, against the same dependencies, all configured the same way. Say goodbye to "works on my machine" bugs.

#### Prerequisites

- Install the latest version of [Vagrant](https://www.vagrantup.com/docs/installation).
- Install [VirtualBox](https://www.virtualbox.org/).

Once you have downloaded the necessary prerequisites, from the root directory, run the following:

```
vagrant up && vagrant ssh
```

After you run the above command, you will have a fully running virtual machine in VirtualBox running Ubuntu 18.04 LTS 64-bit.  As a sanity check, run the e2e tests from within the Ubuntu VM to confirm that your development environment is working as intended.

### Maven Build

To build an executable (JAR file) via Maven, from the root directory, run:

```
mvn clean && mvn package
```

This will create a new `target` directory in the project root containing a `torreyc-x.x.x.jar` file, where `x.x.x` is the semantic version number for the compiler.  Additionally, this command will run the JUnit tests.

## Tests

### Unit and Integration Tests

Integration tests are used to verify that the different parts of the compiler (e.g., front-end, back-end, CLI, assembler, etc.) correctly work together. Unit tests verify that the smallest units of code work correctly in isolation.

To run the unit and integration tests, run the following:

```
mvn test
```

### End-to-End Tests

End-to-end (e2e) tests are used to test: the compiler runtime, the compiler's CLI, the standard output of executables creates by the compiler, and the standard error of the compiler.

The `e2e` directory contains the e2e tests, written in Bash, that assert on the standard output of executables produced by the compiler (via the `assert_stdout` utility function) and the standard error of the compiler (via the `assert_stderr` utility function).

To run the e2e tests, perform the following:

1. From the project root directory, build the compiler jarfile:

```sh
mvn package
```

2. Navigate to the `e2e` directory:

```sh
cd e2e
```

3. Run the test script:

```sh
bash _run_all_tests.sh
```
