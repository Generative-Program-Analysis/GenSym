#!/bin/bash
cd "$(dirname $0)"
git pull --quiet --prune --recurse-submodules
make
cd ../..
sbt "Bench / test"
cat bench.csv >>~/.www/benchllsc/bench.csv
rm bench.csv
