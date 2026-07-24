#!/usr/bin/env bash
set -euo pipefail

python3 benchmarks/oopsla2026/collect_result.py --suite btree --write-raw
python3 benchmarks/oopsla2026/collect_result.py --suite quicksort --write-raw
python3 benchmarks/oopsla2026/collect_result.py --suite crafted --write-raw
python3 benchmarks/oopsla2026/collect_result.py --suite Collection-C --write-raw

python3 benchmarks/oopsla2026/summary.py \
  --rq compilation --suite btree \
  -o benchmarks/oopsla2026/final_results_btree.compilation.csv

python3 benchmarks/oopsla2026/summary.py \
  --rq compilation --suite quicksort \
  -o benchmarks/oopsla2026/final_results_quicksort.compilation.csv

python3 benchmarks/oopsla2026/summary.py \
  --rq compilation --suite crafted \
  -o benchmarks/oopsla2026/final_results_crafted.compilation.csv

python3 benchmarks/oopsla2026/summary.py \
  --rq compilation --suite Collection-C \
  -o benchmarks/oopsla2026/final_results_Collection-C.compilation.csv
