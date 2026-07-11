#!/usr/bin/env sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
RUNNER="$SCRIPT_DIR/../../run_exe.py"
TARGET_PATH=

# Forward help directly to the shared executable runner.
if [ "${1-}" = "-h" ] || [ "${1-}" = "--help" ]; then
  exec python3 "$RUNNER" --help
fi

# If the first argument is not an option, treat it as a relative or absolute
# path to a specific testcase directory under genwasym-test-artifacts.
if [ "${1-}" != "" ] && [ "${1#-}" = "$1" ]; then
  TARGET_PATH=$1
  shift
fi

if [ -n "$TARGET_PATH" ]; then
  case "$TARGET_PATH" in
    /*) RESOLVED_TARGET=$TARGET_PATH ;;
    *) RESOLVED_TARGET=$SCRIPT_DIR/$TARGET_PATH ;;
  esac
  exec python3 "$RUNNER" "$RESOLVED_TARGET" "$@"
fi

# Otherwise run every testcase directory directly under normal/ and bugs/.
find "$SCRIPT_DIR" -mindepth 2 -maxdepth 2 -type d | sort | while IFS= read -r dir; do
  python3 "$RUNNER" "$dir" "$@"
done
