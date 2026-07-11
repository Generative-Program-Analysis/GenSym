#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
RUNNER="$SCRIPT_DIR/../../run.py"
INPUT_DIR="$SCRIPT_DIR/../wasp-test-input"
INPUT_ROOT_REL="Collection-C/wasp-test-input"
OUTPUT_ROOT_REL="Collection-C/wasp-test-output"

if [ "${1-}" = "-h" ] || [ "${1-}" = "--help" ]; then
  exec python3 "$RUNNER" --help
fi

mapfile -t DIRS < <(find "$INPUT_DIR" -mindepth 2 -maxdepth 2 -type d | sort)

if [ "${#DIRS[@]}" -eq 0 ]; then
  echo "No test directories found under $INPUT_DIR" >&2
  exit 1
fi

for dir in "${DIRS[@]}"; do
  rel_dir="${dir#"$INPUT_DIR"/}"
  python3 "$RUNNER" "$INPUT_ROOT_REL/$rel_dir" --workspace-dir "$OUTPUT_ROOT_REL/$rel_dir" "$@"
done
