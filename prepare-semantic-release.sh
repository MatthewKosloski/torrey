#!/bin/bash
echo "starting to execute prepare-semantic-release with next version $1"
mvn --batch-mode versions:set -DgenerateBackupPoms=false -DnewVersion=$1 \
    && mvn --batch-mode --update-snapshots package