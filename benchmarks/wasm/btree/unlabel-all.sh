#!/bin/bash

# get all files with ending u.wat
for f in *u.wat; do
    # use wasm2wat to get rid of wat labels and the nested form
    x=${f%.*}
    wat2wasm "$f" -o "$x.wasm"
    wasm2wat "$x.wasm" -o "$x-unlabeled.wat"
    rm "$x.wasm"
done
