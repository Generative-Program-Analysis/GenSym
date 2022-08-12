SRC_FILES := $(wildcard ./*.c)
all: llsc

llsc:
	for SRC in $(SRC_FILES) ; do \
	  clang-11 $$SRC -O0 -emit-llvm -S -disable-O0-optnone -fno-discard-value-names ; \
	done

clean:
	rm -rf *.ll
	rm -rf *.bc
