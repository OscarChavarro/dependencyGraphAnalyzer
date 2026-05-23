#!/bin/sh
set -eu

ROOT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
cd "$ROOT_DIR"

TS_ANALYZER_DIR="graphBuilderPlugins/src/main/resources/typescript-analyzer"
FRONTEND_DIR="frontend"

cd "$TS_ANALYZER_DIR"
npm install
npm run build

cd "$ROOT_DIR/$FRONTEND_DIR"
npm install
npm run build

cd "$ROOT_DIR"
rm -rf ./backend/build/runtime-libs
./backend/gradlew -p backend -q classes copyRuntimeDeps
