all:
	clang multi_path_1024_sym.c -O0 -emit-llvm -S -fno-discard-value-names -o multi_path_1024_sym.ll
	clang multi_path_65536_sym.c -O0 -emit-llvm -S -fno-discard-value-names -o multi_path_65536_sym.ll
	clang multi_path_1048576_sym.c -O0 -emit-llvm -S -fno-discard-value-names -o multi_path_1048576_sym.ll
	clang -I /home/kraks/research/klee_experiment/klee-2.1/include -g -Xclang -disable-O0-optnone -c -emit-llvm multi_path_1024_klee.c -o multi_path_1024_klee.bc
	clang -I /home/kraks/research/klee_experiment/klee-2.1/include -g -Xclang -disable-O0-optnone -c -emit-llvm multi_path_65536_klee.c -o multi_path_65536_klee.bc
	clang -I /home/kraks/research/klee_experiment/klee-2.1/include -g -Xclang -disable-O0-optnone -c -emit-llvm multi_path_1048576_klee.c -o multi_path_1048576_klee.bc

clean:
	rm *.bc
	rm *.ll

