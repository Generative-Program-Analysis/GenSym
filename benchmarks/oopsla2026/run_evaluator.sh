#!/usr/bin/env bash
set -euo pipefail

CASE="parse_expr2000-8"
TOOL="all"
WASP_TIMEOUT="7200"
QUICK=0
RUNS=5
COMPILE=0

usage() {
  cat <<'USAGE'
Usage: benchmarks/oopsla2026/run_evaluator.sh [--quick] [--compile] [--case CASE] [--runs N] [--tool all|genwasym|wasp] [--timeout SECONDS]

Runs the evaluator parse-expression benchmark through GenWasym, WASP, or both.
USAGE
}

while [ "$#" -gt 0 ]; do
  case "$1" in
    --case) CASE="$2"; shift 2 ;;
    --tool) TOOL="$2"; shift 2 ;;
    --runs) RUNS="$2"; shift 2 ;;
    --timeout) WASP_TIMEOUT="$2"; shift 2 ;;
    --quick) QUICK=1; shift ;;
    --compile) COMPILE=1; shift ;;
    -h|--help) usage; exit 0 ;;
    *) echo "Unknown argument: $1" >&2; usage >&2; exit 2 ;;
  esac
done

if ! [[ "$RUNS" =~ ^[1-9][0-9]*$ ]]; then
  echo "Invalid --runs value: $RUNS" >&2
  exit 2
fi
if [ "$QUICK" -eq 1 ] && [ -z "$CASE" ]; then
  echo "--quick requires --case CASE" >&2
  exit 2
fi

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPO_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
SUITE_ROOT="$REPO_ROOT/benchmarks/oopsla2026/evaluator"
GENWASYM_ARTIFACTS="$SUITE_ROOT/genwasym-test-artifacts"
WASP_INPUT="$SUITE_ROOT/wasp-test-input"
WASP_OUTPUT="$SUITE_ROOT/wasp-test-output"

case_file() {
  case "$1" in
    *.wat|*.wast) printf '%s\n' "$1" ;;
    *) printf '%s.wat\n' "$1" ;;
  esac
}

run_genwasym() {
  cd "$REPO_ROOT"
  if [ "$QUICK" -eq 1 ]; then
    local wat
    wat=$(case_file "$CASE")
    local cpp="$GENWASYM_ARTIFACTS/$wat.cpp"
    if [ ! -f "$cpp" ]; then
      echo "Missing generated C++ file: $cpp (use --compile to generate it)" >&2
      exit 1
    fi
    if [ "$COMPILE" -eq 1 ]; then
      python3 benchmarks/oopsla2026/compile.py --skip-newer "$cpp"
    fi
    python3 benchmarks/oopsla2026/run_exe.py \
      "$GENWASYM_ARTIFACTS" --run-all --case "$wat" --clean
    python3 benchmarks/oopsla2026/run_exe.py \
      "$GENWASYM_ARTIFACTS" --run-all --case "$wat" --runs "$RUNS"
  else
    if [ "$COMPILE" -eq 1 ]; then
      python3 benchmarks/oopsla2026/compile.py --skip-newer "$GENWASYM_ARTIFACTS"
    fi
    python3 benchmarks/oopsla2026/run_exe.py \
      "$GENWASYM_ARTIFACTS" --run-all --runs "$RUNS"
  fi
}

run_wasp() {
  cd "$REPO_ROOT"
  local wast
  wast=$(case_file "$CASE")
  wast="${wast%.wat}.wast"
  if [ "$QUICK" -eq 1 ]; then
    python3 benchmarks/oopsla2026/run.py \
      "$WASP_INPUT" --workspace-dir "$WASP_OUTPUT" --case "$wast" --clean
    python3 benchmarks/oopsla2026/run.py \
      "$WASP_INPUT" --workspace-dir "$WASP_OUTPUT" --case "$wast" \
      --timeout "$WASP_TIMEOUT" --runs "$RUNS"
  else
    python3 benchmarks/oopsla2026/run.py \
      "$WASP_INPUT" --workspace-dir "$WASP_OUTPUT" \
      --timeout "$WASP_TIMEOUT" --runs "$RUNS"
  fi
}

case "$TOOL" in
  all) run_genwasym; run_wasp ;;
  genwasym) run_genwasym ;;
  wasp) run_wasp ;;
  *) echo "Unknown tool: $TOOL" >&2; usage >&2; exit 2 ;;
esac
