#!/bin/bash
NEXT_RELEASE=$1
echo "starting to execute prepare-semantic-release with next version $NEXT_RELEASE"
ls \
	&& mvn --batch-mode --update-snapshots --no-transfer-progress versions:set -DgenerateBackupPoms=false -DnewVersion=$NEXT_RELEASE \
	&& sed -i "s/public static String SEMANTIC_VERSION = \".*\"/public static String SEMANTIC_VERSION = \"${NEXT_RELEASE}\"/" ./src/main/java/me/mtk/torrey/Torrey.java \
	&& sed -i "s/compiler_version=\".*\"/compiler_version=\"${NEXT_RELEASE}\"/" ./e2e/_run_all_tests.sh \
	&& mvn --batch-mode --update-snapshots test \
	&& mvn --batch-mode --update-snapshots --no-transfer-progress package \
	&& gcc -c ./src/runtime/runtime.c -o runtime.o \
  && cd e2e && bash _run_all_tests.sh && cd .. \
	&& mkdir release \
	&& mv ./runtime.o ./target/torreyc-$NEXT_RELEASE.jar ./release \
	&& zip -r release.zip release
