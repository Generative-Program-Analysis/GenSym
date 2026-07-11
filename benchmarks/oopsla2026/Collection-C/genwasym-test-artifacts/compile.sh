#!/usr/bin/env sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
COMPILER="$SCRIPT_DIR/../../compile.py"
TARGET_PATH=

compile_target() {
  target=$1
  case "$target" in
    "$SCRIPT_DIR"/bugs/*)
      GENSYM_USE_SOFT_ASSERT=0 python3 "$COMPILER" "$target" "$@"
      ;;
    *)
      python3 "$COMPILER" "$target" "$@"
      ;;
  esac
}

# Forward help directly to the shared compiler entrypoint.
if [ "${1-}" = "-h" ] || [ "${1-}" = "--help" ]; then
  exec python3 "$COMPILER" --help
fi

# If the first argument is not an option, treat it as a relative or absolute
# path to a specific testcase directory or .cpp file under genwasym-test-artifacts.
if [ "${1-}" != "" ] && [ "${1#-}" = "$1" ]; then
  TARGET_PATH=$1
  shift
fi

if [ -n "$TARGET_PATH" ]; then
  case "$TARGET_PATH" in
    /*) RESOLVED_TARGET=$TARGET_PATH ;;
    *) RESOLVED_TARGET=$SCRIPT_DIR/$TARGET_PATH ;;
  esac
  case "$RESOLVED_TARGET" in
    "$SCRIPT_DIR"/bugs/*)
      exec env GENSYM_USE_SOFT_ASSERT=0 python3 "$COMPILER" "$RESOLVED_TARGET" "$@"
      ;;
    *)
      exec python3 "$COMPILER" "$RESOLVED_TARGET" "$@"
      ;;
  esac
fi

# Otherwise compile every testcase directory directly under normal/ and bugs/.
find "$SCRIPT_DIR" -mindepth 2 -maxdepth 2 -type d | sort | while IFS= read -r dir; do
  compile_target "$dir" "$@"
done
