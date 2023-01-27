#!/usr/bin/env bash
# Uses the test_benchmark script to run experiment. Specifies programs, options, and collect output

###################
#  Static Config  #
###################

PROJ_DIR=`realpath ..`                                # project root directory
LOG_DIR=`realpath ./logs`                             # directory to store the log files
BM_SCRIPT=`realpath ./test_benchmark`                 # the test_benchmark script
export BM_EXPAND_SCRIPT=`realpath ./braces_expand.py` # the script to perform braces expansion, will be detected by BM_SCRIPT
declare -A inputs=(
[posix]=`realpath ../benchmarks/icse23/gensym_posix/`
[uclibc]=`realpath ../benchmarks/icse23/gensym_uclibc/`
)
GCOV_DIR=`realpath ../gcov`                                           # TODO: point to gcov executables
ENV_SCRIPT="~/sai_install/env.sh"                                         # TODO (OPTIONAL): set up PATH, CPLUS_INCLUDE_PATH, LD_LIBRARY_PATH, LIBRARY_PATH, etc.

####################
#  Dynamic Config  #
####################

# whether to generate external lib at the beginning of the experiment
GEN_EXTERNAL=true
# --no-scala for BM_SCRIPT
BM_SCALA=true
# --no-cpp for BM_SCRIPT
BM_CPP=true
# --no-runtime for BM_SCRIPT
BM_RUNTIME=true
# whether to use gcov to measure concrete coverage, require --output-ktest in RUNTIME_OPTION
GCOV=false
# extra arguments passed to BM_SCRIPT
BM_OPTIONS="--njobs 192 --timeout 500"
# global options for the final executable, appended to all benchmarks
RUNTIME_OPTION="--solver=z3 --thread=1 --search=random-path --output-ktest --output-tests-cov-new"
# use the hash of the specified branch. Only affect the result of runtime, but not scala/cpp compilation.
USE_BRANCH=""

# candidate source files in ${inputs}, .ll is appended
candidates=(
base32
base64
comm
cut
dirname
echo
fold
pathchk
expand
paste
)

# runtime options for executable
# for path comparison

declare -A options=(
[base32_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-files 1 12 -d A"'
[base32_uclibc]='--argv="prg -d A" --sym-stdin 2 --sym-stdout --sym-file-size=12 --add-sym-file A'
[base64_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-files 1 11 -d A"'
[base64_uclibc]='--argv="prg -d A" --sym-stdin 2 --sym-stdout --sym-file-size=11 --add-sym-file A'
[comm_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-files 2 4 A B"'
[comm_uclibc]='--argv="prg A B" --sym-stdin 2 --sym-stdout --sym-file-size=4 --add-sym-file=A --add-sym-file=B'
[cut_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-files 1 15 -b 1-2 A"'
[cut_uclibc]='--argv="prg -b 1-2 A" --sym-stdin 2 --sym-stdout --sym-file-size=15 --add-sym-file A'
[dirname_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-arg 15"'
[dirname_uclibc]='--argv="prg #15" --sym-stdin 2 --sym-stdout'
[echo_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-arg 11"'
[echo_uclibc]='--argv="prg #11" --sym-stdin 2 --sym-stdout'
[fold_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-files 1 7 A"'
[fold_uclibc]='--argv="prg A" --sym-stdout --sym-stdin 2 --sym-file-size=7 --add-sym-file A'
[pathchk_posix]='--argv="prg --sym-stdin 2 --sym-stdout -sym-arg 2"'
[pathchk_uclibc]='--argv="prg #2" --sym-stdin 2 --sym-stdout'
[expand_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-files 1 8 A"'
[expand_uclibc]='--argv="prg A" --sym-stdout --sym-stdin 2 --sym-file-size=8 --add-sym-file A'
[paste_posix]='--argv="prg -sym-stdout -sym-stdin 2 -sym-files 3 5 -s A B C"'
[paste_uclibc]='--argv="prg -s A B C" --sym-stdout --sym-stdin 2 --sym-file-size=5 --add-sym-file=A --add-sym-file=B --add-sym-file=C'
)

###################
#  Script starts  #
###################

mkdir -p $LOG_DIR
if [ -f "$ENV_SCRIPT" ]; then
	source "$ENV_SCRIPT" # set up PATH, CPLUS_INCLUDE_PATH, LD_LIBRARY_PATH, LIBRARY_PATH
fi
TS=`date +%y%m%d%H%M%S` # get a time stamp
cd $PROJ_DIR

if [ "${BM_SCALA}" = "false" ]; then
	BM_OPTIONS="${BM_OPTIONS} --no-scala"
fi
if [ "${BM_CPP}" = "false" ]; then
	BM_OPTIONS="${BM_OPTIONS} --no-cpp"
fi
if [ "${BM_RUNTIME}" = "false" ]; then
	BM_OPTIONS="${BM_OPTIONS} --no-runtime"
fi

if [ -z "$USE_BRANCH" ]; then
	USE_BRANCH=HEAD
else
	echo [run_experiment] Using branch hash $USE_BRANCH.
	echo [run_experiment] WARNING: overriding the branch hash may result in inconsistent compilation result, should make sure the previously compiled programs are correct.
fi

COMMIT_HASH=h_$(git rev-parse --short "$USE_BRANCH")
echo [run_experiment] hash: $COMMIT_HASH

# generate external wrappers
if [ "${GEN_EXTERNAL}" = "true" ]; then
	echo [run_experiment] generating external functions...
	sbt "runMain gensym.GenerateExternal"
	# bloop run sai -m gensym.GenerateExternal
fi

# iterate through each set of input LLVM files (e.g. posix and uclibc)
for i in "${!inputs[@]}"; do
	idir="${inputs[$i]}" # the input LLVM directory

	# set up output destination
	OUTFILE="${LOG_DIR}/full_${TS}_${COMMIT_HASH}_${i}.log"
	OUTFILERUNTIME="${LOG_DIR}/runtime_${TS}_${COMMIT_HASH}_${i}.log"

	# duplicate all output to OUTFILE
	exec 1> >(tee -ia ${OUTFILE})
	exec 2> >(tee -ia ${OUTFILE} >&2)

	echo [run_experiment] OUTFILE:        $OUTFILE
	echo [run_experiment] OUTFILERUNTIME: $OUTFILERUNTIME

    echo "name,opt,time,blocks,br,paths,threads,task-in-q,queries" >> ${OUTFILERUNTIME}
	for f in ${candidates[@]}; do
		if [ -v "options[${f}_${i}]" ]; then
			echo [run_experiment] using input specific option with key: ${f}_${i}
			option="${options[${f}_${i}]} $RUNTIME_OPTION"
		elif [ -v "options[${f}]" ]; then
			echo [run_experiment] using generic input option with key: ${f}
			option="${options[${f}]} $RUNTIME_OPTION"
		else
			echo [run_experiment] no option found for input file ${f} of type ${i}
			echo [run_experiment] HINT: add an item to the "options=(...)" array with key [${f}] or [${f}_${i}]
			option="$RUNTIME_OPTION"
		fi
		# write only succinct output to OUTFILERUNTIME
		BM_OPTIONS="${BM_OPTIONS} --succinct-log ${OUTFILERUNTIME}"
		HEAD=${COMMIT_HASH}_${f}_${i}
		${BM_SCRIPT} ${BM_OPTIONS} -- $idir/$f.ll $HEAD $option
        if [ $? -eq 0 ] && [ "$GCOV" = "true" ]; then
          ( cd gs_gen/$HEAD/tests
          gcov_file=${gcov_gcda_map[$f]:-$f}
          pwd
          echo performing gcov measurement...
          rm -f $GCOV_DIR/src/*.gcda
          klee-replay $GCOV_DIR/src/$f *.ktest
          cd $GCOV_DIR
          pwd
          echo f: $f
          echo gcov_file: $gcov_file
          gcov src/${gcov_file} | tee >(grep -A1 '^File' | grep -vx '\-\-' - >> $OUTFILERUNTIME)
        )
        fi
	done
	# redirect output back to stdout
	exec 1> /dev/tty
	exec 2> /dev/tty
	echo [run_experiment] output written to ${OUTFILE}
	echo [run_experiment] runtime output written to ${OUTFILERUNTIME}
	echo [run_experiment] executable at `realpath gs_gen/$HEAD`
done
