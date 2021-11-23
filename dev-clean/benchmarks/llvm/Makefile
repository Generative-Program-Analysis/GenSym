SRC_FILES := $(wildcard ./*.c)

all:
	for SRC in $(SRC_FILES) ; do \
	  clang $$SRC -O0 -emit-llvm -S -fno-discard-value-names ; \
	done

allO1:
	for SRC in $(SRC_FILES) ; do \
	  clang $$SRC -O1 -emit-llvm -S -fno-discard-value-names -D__NO_STRING_INLINES  -D_FORTIFY_SOURCE=0 -U__OPTIMIZE__ ; \
	done

clean:
	rm -rf *.ll
