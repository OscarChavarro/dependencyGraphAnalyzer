#!/bin/sh
set -e

./backend/gradlew -p backend -q classes copyRuntimeDeps
