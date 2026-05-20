#!/bin/sh
set -e

rm -rf ./backend/build/runtime-libs
./backend/gradlew -p backend -q classes copyRuntimeDeps
