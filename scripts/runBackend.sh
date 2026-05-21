#!/bin/sh
set -e

./scripts/compile.sh
./backend/gradlew -q run -p backend -Papp=entryPoints.SpringBootBackendApp
