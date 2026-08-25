#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
ASSEMBLY_JAR="$SCRIPT_DIR/target/scala-2.12/genwasym.jar"
DIST_DIR="$SCRIPT_DIR/dist"
RELEASE_DIR="$DIST_DIR/genwasym"

if ! command -v sbt >/dev/null 2>&1; then
  echo "sbt is required but was not found in PATH." >&2
  exit 1
fi

mkdir -p "$DIST_DIR"
(
  cd "$SCRIPT_DIR"
  sbt assembly
)

if [ ! -s "$ASSEMBLY_JAR" ]; then
  echo "Assembly completed without producing $ASSEMBLY_JAR" >&2
  exit 1
fi

STAGING_DIR=$(mktemp -d "$DIST_DIR/.genwasym-release.XXXXXX")
cleanup() {
  case "$STAGING_DIR" in
    "$DIST_DIR"/.genwasym-release.*) rm -rf -- "$STAGING_DIR" ;;
    *) echo "Refusing to remove unexpected staging path: $STAGING_DIR" >&2 ;;
  esac
}
trap cleanup EXIT

mkdir -p \
  "$STAGING_DIR/bin" \
  "$STAGING_DIR/lib" \
  "$STAGING_DIR/include/immer"

install -m 0755 "$SCRIPT_DIR/distribution/bin/genwasym" "$STAGING_DIR/bin/genwasym"
install -m 0644 "$ASSEMBLY_JAR" "$STAGING_DIR/lib/genwasym.jar"
install -m 0644 "$SCRIPT_DIR/distribution/README.md" "$STAGING_DIR/README.md"
install -m 0644 "$SCRIPT_DIR/distribution/VERSION" "$STAGING_DIR/VERSION"
install -m 0644 "$SCRIPT_DIR/headers/gensym.hpp" "$STAGING_DIR/include/gensym.hpp"
install -m 0644 "$SCRIPT_DIR/headers/wasm.hpp" "$STAGING_DIR/include/wasm.hpp"
cp -R "$SCRIPT_DIR/headers/wasm" "$STAGING_DIR/include/wasm"
cp -R "$SCRIPT_DIR/third-party/immer/immer/." "$STAGING_DIR/include/immer/"

if [ -e "$RELEASE_DIR" ]; then
  rm -rf -- "$RELEASE_DIR"
fi
mv "$STAGING_DIR" "$RELEASE_DIR"
trap - EXIT

echo "Built GenWasym CLI distribution: $RELEASE_DIR"
echo "Run: $RELEASE_DIR/bin/genwasym --help"
