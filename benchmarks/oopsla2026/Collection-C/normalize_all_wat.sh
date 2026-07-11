#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NORMALIZE_PY="$SCRIPT_DIR/../normalize_wat.py"
SOURCE_DIR="$SCRIPT_DIR/wasp-test-input"
ARTIFACTS_DIR="$SCRIPT_DIR/artifacts"
DENORMALIZED_DIR="$ARTIFACTS_DIR/tests-denormalized"
FIRST_NORMALIZED_DIR="$ARTIFACTS_DIR/tests-first-normalized"
NORMALIZED_DIR="$SCRIPT_DIR/genwasym-test-input"

mkdir -p "$DENORMALIZED_DIR"
mkdir -p "$FIRST_NORMALIZED_DIR"
mkdir -p "$NORMALIZED_DIR"

shopt -s globstar nullglob

# Denormalize allocator instructions while preserving the input directory layout.
for wat_file in "$SOURCE_DIR"/**/*.wat; do
    relative_path="${wat_file#"$SOURCE_DIR"/}"
    output_file="$DENORMALIZED_DIR/$relative_path"
    mkdir -p "$(dirname "$output_file")"
    python3 "$NORMALIZE_PY" --denormalize "$wat_file" "$output_file"
done

# Normalize the denormalized WAT files and keep them in a separate tree.
for denorm_wat_file in "$DENORMALIZED_DIR"/**/*.wat; do
    relative_path="${denorm_wat_file#"$DENORMALIZED_DIR"/}"
    output_file="$FIRST_NORMALIZED_DIR/${relative_path%.wat}.norm.wat"
    mkdir -p "$(dirname "$output_file")"
    python3 "$NORMALIZE_PY" "$denorm_wat_file" "$output_file"
done

# Lower normalized WAT files to wasm.
for norm_wat_file in "$FIRST_NORMALIZED_DIR"/**/*.norm.wat; do
    relative_path="${norm_wat_file#"$FIRST_NORMALIZED_DIR"/}"
    output_file="$NORMALIZED_DIR/${relative_path%.norm.wat}.wasm"
    mkdir -p "$(dirname "$output_file")"
    wat2wasm "$norm_wat_file" -o "$output_file"
done

# Round-trip wasm back to wat in the final normalized tree.
for wasm_file in "$NORMALIZED_DIR"/**/*.wasm; do
    output_file="${wasm_file%.wasm}.wat"
    wasm2wat "$wasm_file" -o "$output_file"
done
