#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
DEFAULT_OUTPUT="$SCRIPT_DIR/dist/genwasym-source.tar.gz"
OUTPUT=${1:-$DEFAULT_OUTPUT}

for command in rsync tar sha256sum mktemp realpath; do
  if ! command -v "$command" >/dev/null 2>&1; then
    echo "$command is required to create the source archive." >&2
    exit 1
  fi
done

mkdir -p "$(dirname -- "$OUTPUT")"
OUTPUT=$(realpath -m "$OUTPUT")

STAGING_DIR=$(mktemp -d /tmp/genwasym-source-package.XXXXXX)
PACKAGE_ROOT="$STAGING_DIR/GenWasym"
DEMO_ROOT="$STAGING_DIR/genwasym-library-demo"

cleanup() {
  case "$STAGING_DIR" in
    /tmp/genwasym-source-package.*) rm -rf -- "$STAGING_DIR" ;;
    *) echo "Refusing to remove unexpected staging path: $STAGING_DIR" >&2 ;;
  esac
}
trap cleanup EXIT

mkdir -p "$PACKAGE_ROOT" "$DEMO_ROOT"
cd "$SCRIPT_DIR"

# Root build files and source-distribution documentation.
rsync -aR \
  .jvmopts \
  README.md \
  build.sbt \
  build-genwasym-release.sh \
  package-source.sh \
  project/build.properties \
  project/plugins.sbt \
  "$PACKAGE_ROOT/"
rsync -a distribution/ "$PACKAGE_ROOT/distribution/"

# GenWasym implementation, generated parser sources, tests, and C++ runtime.
rsync -a src/ "$PACKAGE_ROOT/src/"
rsync -a headers/ "$PACKAGE_ROOT/headers/"

# Grammar sources only; generated grammar build caches and bundled tool JARs
# are not needed because generated Java parser sources are already under src/.
mkdir -p "$PACKAGE_ROOT/grammar"
rsync -a \
  grammar/README.md \
  grammar/Makefile \
  grammar/*.g4 \
  grammar/gen-llvm-parser.sh \
  "$PACKAGE_ROOT/grammar/"

# lms-clean is the source-level sbt project dependency required by build.sbt.
mkdir -p \
  "$PACKAGE_ROOT/third-party/lms-clean/project" \
  "$PACKAGE_ROOT/third-party/lms-clean/src/main"
rsync -a \
  third-party/lms-clean/LICENSE \
  third-party/lms-clean/README.md \
  third-party/lms-clean/build.sbt \
  "$PACKAGE_ROOT/third-party/lms-clean/"
rsync -a third-party/lms-clean/project/build.properties \
  "$PACKAGE_ROOT/third-party/lms-clean/project/build.properties"
rsync -a third-party/lms-clean/src/main/ \
  "$PACKAGE_ROOT/third-party/lms-clean/src/main/"

# Immer is header-only and is used when compiling generated C++.
mkdir -p "$PACKAGE_ROOT/third-party/immer"
rsync -a third-party/immer/immer/ "$PACKAGE_ROOT/third-party/immer/immer/"
for file in LICENSE README.rst README.md; do
  if [ -f "third-party/immer/$file" ]; then
    rsync -a "third-party/immer/$file" "$PACKAGE_ROOT/third-party/immer/$file"
  fi
done

# Reuse documentation and Wasm benchmark inputs.
mkdir -p "$PACKAGE_ROOT/benchmarks/oopsla2026"
rsync -a \
  benchmarks/oopsla2026/Reusability.md \
  benchmarks/oopsla2026/compile.py \
  benchmarks/oopsla2026/run_exe.py \
  "$PACKAGE_ROOT/benchmarks/oopsla2026/"
mkdir -p "$PACKAGE_ROOT/benchmarks/wasm"
rsync -a \
  --include '*/' \
  --include '*.wat' \
  --exclude '*' \
  benchmarks/wasm/ \
  "$PACKAGE_ROOT/benchmarks/wasm/"

# Independent Scala project demonstrating library integration.
rsync -a \
  --exclude target/ \
  --exclude '*.jar' \
  --exclude '*.log' \
  genwasym-library-demo/ \
  "$DEMO_ROOT/"

(
  cd "$PACKAGE_ROOT"
  find . -type f -print | LC_ALL=C sort > SOURCE_MANIFEST.txt
)

SOURCE_DATE_EPOCH=${SOURCE_DATE_EPOCH:-0}
tar \
  --sort=name \
  --mtime="@$SOURCE_DATE_EPOCH" \
  --owner=0 \
  --group=0 \
  --numeric-owner \
  -C "$STAGING_DIR" \
  -czf "$OUTPUT" \
  GenWasym \
  genwasym-library-demo

(
  cd "$(dirname -- "$OUTPUT")"
  sha256sum "$(basename -- "$OUTPUT")" > "$(basename -- "$OUTPUT").sha256"
)

echo "Created source archive: $OUTPUT"
echo "Created checksum: $OUTPUT.sha256"
