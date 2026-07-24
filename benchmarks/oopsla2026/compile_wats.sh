#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPO_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
cd "$REPO_ROOT"

sbt \
  'testOnly gensym.wasm.TestBenchmark -- -z compile-btree-benchmarks' \
  'testOnly gensym.wasm.TestBenchmark -- -z compile-quicksort-benchmark' \
  'testOnly gensym.wasm.TestBenchmark -- -z compile-evaluator-benchmarks' \
  'testOnly gensym.wasm.TestBenchmark -- -z compile-collection-c-normal-benchmarks'
