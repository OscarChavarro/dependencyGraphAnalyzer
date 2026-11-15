#!/bin/sh
set -eu

ROOT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
FRONTEND_DIR="frontend"

cd "$ROOT_DIR/$FRONTEND_DIR"
npm install
npm run build
