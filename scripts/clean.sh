#!/bin/sh
set -eu

ROOT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
cd "$ROOT_DIR"

remove_if_exists() {
  target="$1"
  if [ -e "$target" ]; then
    rm -rf "$target"
    echo "Removed: $target"
  fi
}

# Gradle artifacts (root + Java subprojects)
remove_if_exists ".gradle"
remove_if_exists "build"
remove_if_exists "backend/.gradle"
remove_if_exists "backend/build"
remove_if_exists "graphBuilderPlugins/.gradle"
remove_if_exists "graphBuilderPlugins/build"

# Frontend artifacts
remove_if_exists "frontend/node_modules"
remove_if_exists "frontend/dist"
remove_if_exists "frontend/.angular"
remove_if_exists "frontend/coverage"

# Generated runtime/output artifacts
remove_if_exists "output"
remove_if_exists "e"

# Recreate expected output directory for run scripts
mkdir -p output

echo "Clean complete. Preserved data folders (etc, u, v) and cache text files (*cache*.txt)."
