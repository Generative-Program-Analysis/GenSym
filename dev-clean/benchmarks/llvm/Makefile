SRC_FILES := $(wildcard ./*.c)

all:
	for SRC in $(SRC_FILES) ; do \
	  clang $$SRC -O0 -emit-llvm -S -fno-discard-value-names ; \
	done

clean:
	rm -rf *.ll
