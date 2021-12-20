#!/bin/bash
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$1 \
    && mvn --update-snapshots package