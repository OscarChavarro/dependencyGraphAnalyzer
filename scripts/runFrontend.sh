#!/bin/sh
set -eu

ROOT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"

"$ROOT_DIR/scripts/compileFrontend.sh"
cd "$ROOT_DIR/frontend"
npm run start
