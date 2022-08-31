#!/bin/bash

# Run this script in a folder with *.bc file for KLEE

# The srcipt
# - runs klee with .bc for N time, keeps raw log of each run,
# - then extracts statistics from `klee-stats` into a single file for each run,
# - finally takes the final result of each statistics and combines all
#   N final results into a single CSV file.

for filename in *.bc; do
  for ((i=0; i<5; i++)); do
    echo "Running numactl -N1 -m1 klee --solver-backend=z3 --output-dir=$filename-$i $filename"
    numactl -N1 -m1 klee --solver-backend=z3 --output-dir="$filename-$i" "$filename" > "$filename-$i_raw.log" 2>&1
    klee-stats --to-csv --print-all "$filename-$i" > "$filename-$i.log"
  done
done

for filename in *.bc; do
  head -n1 "$filename-0.log" > "$filename.csv"
  for ((i=0; i<5; i++)); do
    tail -q -n 1 "$filename-$i.log" >> "$filename.csv"
  done
done

