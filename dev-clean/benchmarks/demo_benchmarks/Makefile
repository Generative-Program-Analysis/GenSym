SRC_FILES := $(wildcard ./*.c)
KLEE_INCLUDE := ../../../../klee_experiment/klee-2.1/include
all: llsc klee

llsc:
	for SRC in $(SRC_FILES) ; do \
	  clang-9 $$SRC -O0 -emit-llvm -S -disable-O0-optnone -fno-discard-value-names ; \
	done

klee:
	for SRC in $(SRC_FILES) ; do \
		clang-9 -D KLEE -I $(KLEE_INCLUDE) -emit-llvm -c -g -O0 -Xclang -disable-O0-optnone $$SRC ; \
	done

clean:
	rm -rf *.ll
	rm -rf *.bc
