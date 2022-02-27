#!/bin/bash
cd "$(dirname $0)"
git pull --quiet --prune --recurse-submodules
make

cd ../..
sbt "Bench / test"
cat bench.csv >>~/.www/benchllsc/bench.csv
rm bench.csv

cd ~/.www/benchllsc
conda run -n base \
    jupyter nbconvert --to html --execute dataprocess.ipynb \
                                --output  dataprocess.html
