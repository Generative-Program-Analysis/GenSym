#!/bin/bash
cd "$(dirname $0)"
git pull --prune --recursive-submodules
make
cd ../..
sbt "Bench / test"
cat bench.csv >>~/.www/benchllsc/bench.csv
rm bench.csv
