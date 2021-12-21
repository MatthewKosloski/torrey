#!/bin/bash

# Setup conventional commit message validation
echo $(cat .githooks/commit-msg) > .git/hooks/commit-msg
