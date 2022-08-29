#!/bin/bash

# Run this script in a folder with *.bc file for KLEE

# The srcipt
# - runs klee with .bc for N time, keeps raw log of each run,
# - then extracts statistics from `klee-stats` into a single file for each run,
# - finally takes the final result of each statistics and combines all
#   N final results into a single CSV file.

declare -A program_arg
declare -a orders

klee_bin=klee-11

iter_num=1

llsc_gen_dir=/scratch1/gao606/GenSym/llsc_gen
llsc_engine=ImpCPSLLSC
suffix=linked_posix

klee_option="--max-memory=3096120 --only-output-states-covering-new  --search=random-path  --solver-backend=z3 --external-calls=all  --use-cex-cache --use-independent-solver --switch-type=simple  --max-sym-array-size=4096  --stats-write-interval=1s  --max-solver-time=30s --max-time=30min --use-batching-search --batch-instructions=10000 --watchdog"
llsc_option="--output-tests-cov-new  --thread=1  --search=random-path  --solver=z3   --output-ktest --timeout=1800   --cons-indep --max-sym-array-size=4096"

program_arg[echo]="--sym-stdout --sym-arg 3 --sym-arg 8"
program_arg[cat]="--sym-stdout --sym-stdin 3 --sym-arg 3"
program_arg[base32]="--sym-stdout  --sym-stdin 3 --sym-arg 3 -sym-files 2 2"
program_arg[base64]="--sym-stdout  --sym-stdin 3 --sym-arg 3 -sym-files 2 2"
program_arg[comm]="--sym-stdout  --sym-stdin 3 --sym-arg 2  --sym-arg 2 -sym-files 2 2"
program_arg[cut]="--sym-stdout  --sym-stdin 3 --sym-arg 2 --sym-arg  3 -sym-files 2 2"
program_arg[dirname]="--sym-stdout  --sym-stdin 3 --sym-arg 7 --sym-arg 10"
program_arg[expand]="--sym-stdout  --sym-stdin 3 --sym-arg 3 -sym-files 2 2"
program_arg[false]="--sym-stdout  --sym-arg 10"
program_arg[true]="--sym-stdout  --sym-arg 10"
program_arg[fold]="--sym-stdout  --sym-stdin 3 --sym-arg 3    -sym-files 2 2"
program_arg[join]="--sym-stdout  --sym-stdin 3 --sym-arg 2 --sym-arg 2  -sym-files 2 2"
program_arg[link]="--sym-stdout  --sym-stdin 3 --sym-arg 2   --sym-arg 2  --sym-arg 1  -sym-files 2 2"
program_arg[paste]="--sym-stdout  --sym-stdin 3 --sym-arg 2 --sym-arg 2  -sym-files 2 2"
program_arg[pathchk]="--sym-stdout  --sym-stdin 3 --sym-arg 3"


orders+=(echo)
orders+=(cat)
orders+=(base32)
orders+=(base64)
orders+=(comm)
orders+=(cut)
orders+=(dirname)
orders+=(expand)
orders+=(false)
orders+=(true)
orders+=(fold)
orders+=(join)
orders+=(link)
orders+=(paste)
orders+=(pathchk)

rm -rf klee

rm -rf llsc

mkdir klee

mkdir llsc

for id in "${!orders[@]}"; do
  program=${orders[$id]}
  cp ../${program}_klee.ll ./klee/
  bin_name=${llsc_engine}_${program}_${suffix}
  cp ${llsc_gen_dir}/${bin_name}/${bin_name} ./llsc/${program}_llsc
done

#: '

cd klee

printf "\n\n#######running klee\n\n"

# Convoluted option 1
for id in "${!orders[@]}"; do
  program=${orders[$id]}
  arg=${program_arg[${program}]}
  ll_name=${program}_klee.ll
  filename=klee-${program}
  echo "$program: ${arg}"
  for ((i=0; i<${iter_num}; i++)); do
    command="numactl -N1 -m1 ${klee_bin} --output-dir=$filename-$i ${klee_option} ${ll_name} ${arg}"
    echo "Running ${command}"
    ${command} > "$filename-${i}_raw.log" 2>&1
    klee-stats --to-csv --print-all "$filename-$i" > "$filename-$i.log"
  done
done

for id in "${!orders[@]}"; do
  program=${orders[$id]}
  filename=klee-${program}
  head -n1 "$filename-0.log" > "$filename.csv"
  for ((i=0; i<${iter_num}; i++)); do
    tail -q -n 1 "$filename-$i.log" >> "$filename.csv"
  done
done

cd ..

cd llsc

printf "\n\n#######running llsc\n\n"

for id in "${!orders[@]}"; do
  program=${orders[$id]}
  arg=${program_arg[${program}]}
  llsc_bin=${program}_llsc
  filename=llsc-${program}
  echo "$program: ${arg}"
  for ((i=0; i<${iter_num}; i++)); do
    command="numactl -N1 -m1 ./${llsc_bin} ${llsc_option}"" ""--argv=./${program}.bc   ${arg}"
    echo "Running ${command}"
    numactl -N1 -m1 ./${llsc_bin} ${llsc_option} "--argv=./${program}.bc   ${arg}" > "$filename-${i}_raw.log" 2>&1
    mv ./tests ./${program}-tests
  done
done

cd ..

#'
