KLEE_INCLUDE = ../../../../klee_experiment/klee-2.1/include

all: generate_sse

generate_sse:
	clang-11 multipath_1024_sym.c -O0 -emit-llvm -S -fno-discard-value-names -o multipath_1024_sym.ll
	clang-11 multipath_65536_sym.c -O0 -emit-llvm -S -fno-discard-value-names -o multipath_65536_sym.ll
	clang-11 multipath_1048576_sym.c -O0 -emit-llvm -S -fno-discard-value-names -o multipath_1048576_sym.ll
	clang-11 maze_test.c -O0 -emit-llvm -S -fno-discard-value-names -o maze_test.ll

generate_klee:
	clang-11 -I $(KLEE_INCLUDE) -g -Xclang -disable-O0-optnone -c -emit-llvm multipath_1024_klee.c -o multipath_1024_klee.bc
	clang-11 -I $(KLEE_INCLUDE) -g -Xclang -disable-O0-optnone -c -emit-llvm multipath_65536_klee.c -o multipath_65536_klee.bc
	clang-11 -I $(KLEE_INCLUDE) -g -Xclang -disable-O0-optnone -c -emit-llvm multipath_1048576_klee.c -o multipath_1048576_klee.bc
	clang-11 -I $(KLEE_INCLUDE) -g -Xclang -disable-O0-optnone -c -emit-llvm maze_test_klee.c -o maze_test_klee.bc

clean:
	rm *.bc
	rm *.ll

