include ../../llvm-toolchain.mk

all:
	$(LLVM_CC) multi_path_1024_sym.c -g -O0 -emit-llvm -S -fno-discard-value-names -o multi_path_1024_sym.ll
	$(LLVM_CC) multi_path_65536_sym.c -g -O0 -emit-llvm -S -fno-discard-value-names -o multi_path_65536_sym.ll
	$(LLVM_CC) multi_path_1048576_sym.c -g -O0 -emit-llvm -S -fno-discard-value-names -o multi_path_1048576_sym.ll
	$(LLVM_CC) -I $(KLEE_INCL) -g -Xclang -disable-O0-optnone -c -emit-llvm multi_path_1024_klee.c -o multi_path_1024_klee.bc
	$(LLVM_CC) -I $(KLEE_INCL) -g -Xclang -disable-O0-optnone -c -emit-llvm multi_path_65536_klee.c -o multi_path_65536_klee.bc
	$(LLVM_CC) -I $(KLEE_INCL) -g -Xclang -disable-O0-optnone -c -emit-llvm multi_path_65536_klee_single.c -o multi_path_65536_klee_single.bc
	$(LLVM_CC) -I $(KLEE_INCL) -g -Xclang -disable-O0-optnone -c -emit-llvm multi_path_1048576_klee.c -o multi_path_1048576_klee.bc

clean:
	rm *.bc
	rm *.ll
