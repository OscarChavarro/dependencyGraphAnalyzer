#!/bin/sh
set -e

./scripts/compile.sh
java -Xms200m -Xmx200m -classpath ./backend/build/classes/java/main:./backend/build/resources/main:./backend/build/runtime-libs/* entryPoints.SpringBootBackendApp "$@"
