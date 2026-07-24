#!/usr/bin/env bash
set -euo pipefail

TIMEOUT=7200
RUNS=1
NO_COMPILE=0

usage() {
  cat <<'USAGE'
Usage: benchmarks/oopsla2026/run_collection_c_buggy.sh [--no-compile] [--runs N] [--timeout SECONDS]

Compile the generated Collection-C bug C++ files and run WASP on the
corresponding files under Collection-C/wasp-test-input/buggy.
USAGE
}

while [ "$#" -gt 0 ]; do
  case "$1" in
    --no-compile)
      NO_COMPILE=1
      shift
      ;;
    --runs)
      [ "$#" -ge 2 ] || { echo "Missing value for --runs" >&2; exit 2; }
      RUNS="$2"
      shift 2
      ;;
    --timeout)
      [ "$#" -ge 2 ] || { echo "Missing value for --timeout" >&2; exit 2; }
      TIMEOUT="$2"
      shift 2
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "Unknown argument: $1" >&2
      usage >&2
      exit 2
      ;;
  esac
done

if ! [[ "$RUNS" =~ ^[1-9][0-9]*$ ]]; then
  echo "Invalid --runs value: $RUNS" >&2
  exit 2
fi
if ! [[ "$TIMEOUT" =~ ^[0-9]+$ ]]; then
  echo "Invalid --timeout value: $TIMEOUT" >&2
  exit 2
fi

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPO_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
SUITE_ROOT="$REPO_ROOT/benchmarks/oopsla2026/Collection-C"
GENWASYM_INPUT="$SUITE_ROOT/genwasym-test-input/bugs"
GENWASYM_ARTIFACTS="$SUITE_ROOT/genwasym-test-artifacts/buggy"
WASP_INPUT="$SUITE_ROOT/wasp-test-input/buggy"
WASP_OUTPUT="$SUITE_ROOT/wasp-test-output/buggy"

wat_count=$(find "$GENWASYM_INPUT" -type f -name '*.wat' | wc -l)
if [ "$wat_count" -ne 2 ]; then
  echo "Expected 2 buggy GenWasym WAT files under $GENWASYM_INPUT; found $wat_count" >&2
  exit 1
fi

if [ "$NO_COMPILE" -eq 0 ]; then
  cd "$REPO_ROOT"
  echo "Compiling Collection-C buggy executables"
  python3 benchmarks/oopsla2026/compile.py --skip-newer "$GENWASYM_ARTIFACTS" -DBUG_FINDING
fi

echo "Running Collection-C buggy GenWasym executables: runs=${RUNS}"
found_artifact_dir=0
while IFS= read -r artifact_dir; do
  found_artifact_dir=1
  python3 benchmarks/oopsla2026/run_exe.py \
    "$artifact_dir" --run-all --runs "$RUNS"
done < <(find "$GENWASYM_ARTIFACTS" -mindepth 1 -maxdepth 1 -type d | sort)

if [ "$found_artifact_dir" -eq 0 ]; then
  echo "No compiled buggy GenWasym artifact directories found under $GENWASYM_ARTIFACTS" >&2
  exit 1
fi

if [ ! -d "$WASP_INPUT" ] || [ "$(find "$WASP_INPUT" -type f \( -name '*.wast' -o -name '*.wat' \) | wc -l)" -eq 0 ]; then
  echo "No WASP buggy inputs found under $WASP_INPUT" >&2
  echo "Add the two corresponding .wast files before running WASP." >&2
  exit 1
fi

cd "$REPO_ROOT"
echo "Running WASP on Collection-C buggy cases: timeout=${TIMEOUT}s runs=${RUNS}"
python3 benchmarks/oopsla2026/run.py \
  "$WASP_INPUT" \
  --workspace-dir "$WASP_OUTPUT" \
  --timeout "$TIMEOUT" \
  --runs "$RUNS"
