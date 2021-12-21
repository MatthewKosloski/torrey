#!/bin/bash
echo "starting to execute prepare-semantic-release with next version $1"
ls \
	&& mvn --batch-mode --update-snapshots --no-transfer-progress versions:set -DgenerateBackupPoms=false -DnewVersion=$1 \
	&& mvn --batch-mode --update-snapshots test \
	&& mvn --batch-mode --update-snapshots --no-transfer-progress package \
	&& gcc -c ./src/runtime/runtime.c -o runtime.o \
	&& mkdir release \
	&& mv ./runtime.o ./target/torreyc-$1.jar ./release \
	&& zip -r release.zip release