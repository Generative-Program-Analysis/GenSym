#!/bin/sh
# TODO: use clang? zig compiles stdlib to wasm
# also figure out how to compile to freestanding?

#zig cc -target wasm32-wasi -O3 -o "$2.temp" $1

clang --target=wasm32 --no-standard-libraries -Wl,--export-all -Wl,--no-entry -o add.wasm add.c

wasm2wat "$2.temp" > "$2"
rm "$2.temp"
