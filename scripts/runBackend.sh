#!/bin/sh
set -e

./scripts/compile.sh
cd backend
./gradlew -q run -Papp=entryPoints.SpringBootBackendApp
cd ..
