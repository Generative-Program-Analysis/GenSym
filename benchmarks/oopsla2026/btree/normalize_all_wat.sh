#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NORMALIZE_PY="$SCRIPT_DIR/../normalize_wat.py"

GENWASYM_TESTS_DIR="$SCRIPT_DIR/genwasym-test-output"
WORK_DIR="$SCRIPT_DIR/artifacts"
DENORMALIZED_DIR="$WORK_DIR/denormalized"
FIRST_NORMALIZED_DIR="$WORK_DIR/first-normalized"
WASM_DIR="$WORK_DIR/wasm"
NORMALIZED_WAT_DIR="$SCRIPT_DIR/genwasym-test-input"

mkdir -p "$DENORMALIZED_DIR"
mkdir -p "$FIRST_NORMALIZED_DIR"
mkdir -p "$WASM_DIR"
mkdir -p "$GENWASYM_TESTS_DIR"
mkdir -p "$NORMALIZED_WAT_DIR"

shopt -s nullglob

# Find all .wast files in wasp-test-input and denormalize allocator calls first.
for wat_file in "$SCRIPT_DIR"/wasp-test-input/*.wast; do
    filename="$(basename "$wat_file")"
    output_file="$DENORMALIZED_DIR/$filename"
    python3 "$NORMALIZE_PY" --denormalize "$wat_file" "$output_file"
done

# Normalize all denormalized .wast files.
for denorm_wat_file in "$DENORMALIZED_DIR"/*.wast; do
    filename="$(basename "$denorm_wat_file")"
    output_file="$FIRST_NORMALIZED_DIR/${filename%.wast}.norm.wat"
    python3 "$NORMALIZE_PY" "$denorm_wat_file" "$output_file"
done

# Call wat2wasm on all normalized .wat files.
for norm_wat_file in "$FIRST_NORMALIZED_DIR"/*.norm.wat; do
    filename="$(basename "$norm_wat_file")"
    output_file="$WASM_DIR/${filename%.norm.wat}.wasm"
    wat2wasm "$norm_wat_file" -o "$output_file"
done

# Call wasm2wat on all .wasm files to get the final normalized .wat files.
for wasm_file in "$WASM_DIR"/*.wasm; do
    filename="$(basename "$wasm_file")"
    output_file="$NORMALIZED_WAT_DIR/${filename%.wasm}.wat"
    wasm2wat "$wasm_file" -o "$output_file"
done
