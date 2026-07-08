#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NORMALIZE_PY="$SCRIPT_DIR/../normalize_wat.py"

mkdir -p "$SCRIPT_DIR/tests-denormalized"
mkdir -p "$SCRIPT_DIR/tests-first-normalized"
mkdir -p "$SCRIPT_DIR/tests-normalized"

shopt -s nullglob

# Find all .wast files in test-original and denormalize allocator calls first.
for wat_file in "$SCRIPT_DIR"/test-original/*.wast; do
    filename="$(basename "$wat_file")"
    output_file="$SCRIPT_DIR/tests-denormalized/$filename"
    python3 "$NORMALIZE_PY" --denormalize "$wat_file" "$output_file"
done

# Normalize all denormalized .wast files.
for denorm_wat_file in "$SCRIPT_DIR"/tests-denormalized/*.wast; do
    filename="$(basename "$denorm_wat_file")"
    output_file="$SCRIPT_DIR/tests-first-normalized/${filename%.wast}.norm.wat"
    python3 "$NORMALIZE_PY" "$denorm_wat_file" "$output_file"
done

# Call wat2wasm on all normalized .wat files.
for norm_wat_file in "$SCRIPT_DIR"/tests-first-normalized/*.norm.wat; do
    filename="$(basename "$norm_wat_file")"
    output_file="$SCRIPT_DIR/tests-normalized/${filename%.norm.wat}.wasm"
    wat2wasm "$norm_wat_file" -o "$output_file"
done

# Call wasm2wat on all .wasm files to get the final normalized .wat files.
for wasm_file in "$SCRIPT_DIR"/tests-normalized/*.wasm; do
    filename="$(basename "$wasm_file")"
    output_file="$SCRIPT_DIR/tests-normalized/${filename%.wasm}.wat"
    wasm2wat "$wasm_file" -o "$output_file"
done
