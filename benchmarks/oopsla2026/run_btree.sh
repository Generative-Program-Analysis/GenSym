#!/usr/bin/env bash
set -euo pipefail

CASE="2o1u"
TOOL="all"
WASP_TIMEOUT="900"
QUICK=0
FULL_RUNS=5

usage() {
  cat <<'USAGE'
Usage: benchmarks/oopsla2026/run_btree.sh [--quick] [--case CASE] [--tool all|genwasym|wasp] [--timeout SECONDS]

Runs the btree benchmark:
  genwasym: generate, compile, and run tests-normalized/*.wat.cpp
  wasp:     run wasp_btree/*.wast through wasp

By default, runs the whole benchmark with 5 runs per executable/testcase.
With --quick, runs one testcase selected by --case once.
USAGE
}

while [ "$#" -gt 0 ]; do
  case "$1" in
    --case)
      CASE="$2"
      shift 2
      ;;
    --tool)
      TOOL="$2"
      shift 2
      ;;
    --timeout)
      WASP_TIMEOUT="$2"
      shift 2
      ;;
    --quick)
      QUICK=1
      shift
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

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPO_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
BTREE_DIR="$REPO_ROOT/benchmarks/oopsla2026/btree/tests-normalized"

log() {
  printf '[%s] %s\n' "$(date '+%Y-%m-%d %H:%M:%S')" "$*"
}

run_genwasym() {
  local wat="$BTREE_DIR/$CASE.wat"
  local cpp="$wat.cpp"

  cd "$REPO_ROOT"
  if [ "$QUICK" -eq 1 ]; then
    log "GenWasym quick run: case=$CASE"
    if [ ! -f "$wat" ]; then
      echo "Missing GenWasym btree input: $wat" >&2
      exit 1
    fi

    if [ -f "$cpp" ]; then
      log "Skipping C++ generation; already exists: $cpp"
    else
      log "Generating C++ from $wat"
      INPUT="$wat" MAIN=main sbt "testOnly genwasym.TestBenchmark -- -z compile-a-single-file"
    fi
    log "Compiling generated C++: $cpp"
    python3 benchmarks/oopsla2026/compile.py --skip-newer "$cpp"
    log "Cleaning previous executable outputs for $CASE"
    python3 benchmarks/oopsla2026/run_exe.py btree/tests-normalized --run-all --case "$CASE.wat" --clean
    log "Running generated executables for $CASE"
    python3 benchmarks/oopsla2026/run_exe.py btree/tests-normalized --run-all --case "$CASE.wat"
  else
    log "Compiling all generated btree C++ files"
    python3 benchmarks/oopsla2026/compile.py --skip-newer benchmarks/oopsla2026/btree/tests-normalized
    log "Running all generated btree executables ${FULL_RUNS} times"
    python3 benchmarks/oopsla2026/run_exe.py btree/tests-normalized --run-all --runs "$FULL_RUNS"
  fi
}

run_wasp() {
  cd "$REPO_ROOT"
  if [ "$QUICK" -eq 1 ]; then
    log "WASP quick run: case=$CASE timeout=${WASP_TIMEOUT}s"
    python3 benchmarks/oopsla2026/run.py wasp_btree --case "$CASE" --clean
    python3 benchmarks/oopsla2026/run.py wasp_btree --case "$CASE" --timeout "$WASP_TIMEOUT"
  else
    log "WASP full run: timeout=${WASP_TIMEOUT}s runs=${FULL_RUNS}"
    python3 benchmarks/oopsla2026/run.py wasp_btree --timeout "$WASP_TIMEOUT" --runs "$FULL_RUNS"
  fi
}

case "$TOOL" in
  all)
    log "Starting btree benchmark: tool=all quick=$QUICK"
    run_genwasym
    run_wasp
    ;;
  genwasym)
    log "Starting btree benchmark: tool=genwasym quick=$QUICK"
    run_genwasym
    ;;
  wasp)
    log "Starting btree benchmark: tool=wasp quick=$QUICK"
    run_wasp
    ;;
  *)
    echo "Unknown tool: $TOOL" >&2
    usage >&2
    exit 2
    ;;
esac
