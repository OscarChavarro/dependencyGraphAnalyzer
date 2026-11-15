#!/bin/sh
set -e

./scripts/compileBackend.sh
./backend/gradlew -q run -p backend -Papp=entryPoints.SpringBootBackendApp
