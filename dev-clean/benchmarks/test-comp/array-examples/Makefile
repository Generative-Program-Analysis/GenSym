SRC_FILES := $(wildcard ./*.c)

CC := clang-9

FLAGS := -emit-llvm -O2 -disable-O0-optnone -c
LLSC_FLAGS := -fno-discard-value-names -S

LLSC_TARGET := $(SRC_FILES:%.c=%.ll)

all: llsc

llsc: $(LLSC_TARGET)

$(LLSC_TARGET): %.ll : %.c
	$(CC) $(LLSC_FLAGS) $(FLAGS) -o $@ $<

clean:
	$(RM) -rf $(LLSC_TARGET)
