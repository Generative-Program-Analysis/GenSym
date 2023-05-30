#!/bin/sh
# TODO: use clang? zig compiles stdlib to wasm
# also figure out how to compile to freestanding?

zig cc -target wasm32-wasi -O3 -o "$2.temp" $1
wasm2wat "$2.temp" > "$2"
rm "$2.temp"
