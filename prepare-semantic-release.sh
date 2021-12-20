#!/bin/bash
echo "starting to execute prepare-semantic-release with next version $1"
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$1 \
    && mvn --update-snapshots package