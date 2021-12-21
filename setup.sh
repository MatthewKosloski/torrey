#!/bin/bash

# Setup conventional commit message validation
cp .githooks/commit-msg .git/hooks/commit-msg.sample \
  && mv .git/hooks/commit-msg.sample .git/hooks/commit-msg
