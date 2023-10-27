#!/bin/sh
# TODO: use clang? zig compiles stdlib to wasm
# also figure out how to compile to freestanding?

# zig cc -target wasm32-wasi -O3 -o "$2.temp" $1
# zig cc -target wasm32-freestanding -O3 -o "$2.temp" $1

sudo docker run --rm \
    --volume "$(pwd):/home/wasp/tmp" \
    -w "/home/wasp/tmp" \
    ghcr.io/wasp-platform/wasp:latest \
    clang --target=wasm32 --no-standard-libraries -Wl,--export-all -Wl,--no-entry -o "$2.temp" $1

# clang-15 --target=wasm32 --no-standard-libraries -Wl,--export-all -Wl,--no-entry -o $2.temp $1

wasm2wat "$2.temp" > "$2"
rm "$2.temp"
