#!/bin/bash
echo "starting to execute prepare-semantic-release with next version $1"
mvn --batch-mode --no-transfer-progress versions:set -DgenerateBackupPoms=false -DnewVersion=$1 \
    && mvn --batch-mode --no-transfer-progress --update-snapshots package