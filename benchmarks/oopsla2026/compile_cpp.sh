#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPO_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
cd "$REPO_ROOT"

case_name=""
mode="all"
while [ "$#" -gt 0 ]; do
  case "$1" in
    --all)
      [ "$mode" = "all" ] || { echo "--all cannot be combined with --case" >&2; exit 2; }
      shift
      ;;
    --case)
      [ "$#" -ge 2 ] || { echo "Missing value for --case" >&2; exit 2; }
      [ "$mode" = "all" ] || { echo "Only one --case is supported" >&2; exit 2; }
      case_name="$2"
      mode="case"
      shift 2
      ;;
    -h|--help)
      printf '%s\n' "Usage: $0 [--case CASE] [--all]" >&2
      printf '%s\n' "Cases: 2o1u, quicksort1.sym2.size20, parse_expr2000-8, array/array_test_add" >&2
      exit 0
      ;;
    *)
      echo "Unknown argument: $1" >&2
      echo "Usage: $0 [--case CASE] [--all]" >&2
      exit 2
      ;;
  esac
done

if [ "$mode" = "all" ]; then
  python3 benchmarks/oopsla2026/compile.py --skip-newer \
    benchmarks/oopsla2026/btree/genwasym-test-artifacts \
    benchmarks/oopsla2026/quicksort/genwasym-test-artifacts \
    benchmarks/oopsla2026/evaluator/genwasym-test-artifacts \
    benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts
else
  case "$case_name" in
    2o1u)
      cpp="benchmarks/oopsla2026/btree/genwasym-test-artifacts/2o1u.wat.cpp"
      ;;
    quicksort1.sym2.size20)
      cpp="benchmarks/oopsla2026/quicksort/genwasym-test-artifacts/quicksort1.sym2.size20.wat.cpp"
      ;;
    parse_expr2000-8)
      cpp="benchmarks/oopsla2026/evaluator/genwasym-test-artifacts/parse_expr2000-8.wat.cpp"
      ;;
    array/array_test_add)
      cpp="benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/normal/array/array_test_add.wat.cpp"
      ;;
    *)
      echo "Unknown case: $case_name" >&2
      echo "Cases: 2o1u, quicksort1.sym2.size20, parse_expr2000-8, array/array_test_add" >&2
      exit 2
      ;;
  esac
  python3 benchmarks/oopsla2026/compile.py --skip-newer "$cpp"
fi
