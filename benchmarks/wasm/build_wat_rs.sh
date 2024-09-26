#!/bin/sh

# c has a lot of extra stuff for WASI I think
# also ran into a case where the wasm2wat output was incorrect
# clang -O1 -target wasm32 -nostdlib -Wl,--no-entry -Wl,--export-all -Wl,--allow-undefined -o "$2.temp" "$1"

# zig isn't making wasm files with build-lib
# zig build-exe -target wasm32-freestanding --name "$2" "$1" -lc
# zig build-exe -target wasm32-wasi --name "$2" "$1" -lc

# rust makes a clean wat file
rustc -C opt-level=1 --crate-type cdylib "$1" -o "$2.temp" --target wasm32-unknown-unknown

# wasm-tools print will print all the dwarf info
wasm2wat "$2.temp" > "$2"
rm "$2.temp"
