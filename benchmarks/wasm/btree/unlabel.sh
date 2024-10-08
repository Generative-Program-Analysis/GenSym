#!/bin/sh

# use wasm2wat to get rid of wat labels and the nested form
a="$1"
x=${a%.*}
wat2wasm "$1" -o "$x.wasm"
wasm2wat "$x.wasm" -o "$x-unlabeled.wat"
rm "$x.wasm"