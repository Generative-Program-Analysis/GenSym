SRC_FILES := $(wildcard ./*.c)

CC := clang-11

FLAGS := -emit-llvm -O2 -Xclang -disable-O0-optnone -c -fno-vectorize
GS_FLAGS := -fno-discard-value-names -S

GS_TARGET := $(SRC_FILES:%.c=%.ll)

all: gensym

gensym: $(GS_TARGET)

$(GS_TARGET): %.ll : %.c
	$(CC) $(GS_FLAGS) $(FLAGS) -o $@ $<

clean:
	$(RM) -rf $(GS_TARGET)
