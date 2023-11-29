#!/bin/sh

make clean
docker run --rm \
    --volume "$(pwd):/home/wasp/Collections-C" \
    -w "/home/wasp/Collections-C" \
    ghcr.io/wasp-platform/wasp:latest \
    make -j
