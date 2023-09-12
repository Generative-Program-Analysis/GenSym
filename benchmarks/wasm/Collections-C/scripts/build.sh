#!/bin/sh

rm -v -rf for-wasp 
cp -v -R for-gillian for-wasp

time ./scripts/patch_c.py for-wasp

make -j$(nproc)
