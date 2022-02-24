SRC_FILES := $(wildcard ./*.c)

CC := clang-9

FLAGS := -emit-llvm -O0 -Xclang -disable-O0-optnone -c
KLEE_FLAGS := -D KLEE -g -I $(KLEE_INCL)
LLSC_FLAGS := -fno-discard-value-names -S

KLEE_TARGET := $(SRC_FILES:%.c=%.bc)

KLEE_REPLAY_TARGET := $(SRC_FILES:%.c=%-replay)

KLEE_GEN := $(wildcard ./klee-*)

LLSC_TARGET := $(SRC_FILES:%.c=%.ll)

all: llsc

llsc: $(LLSC_TARGET)
klee: $(KLEE_TARGET)
klee-exe: $(KLEE_REPLAY_TARGET)

$(KLEE_TARGET): %.bc : %.c
	$(CC) $(KLEE_FLAGS) $(FLAGS) -o $@ $<

$(KLEE_REPLAY_TARGET): %-replay : %.c
	$(CC) $(KLEE_FLAGS) -o $@ $< -lkleeRuntest

$(LLSC_TARGET): %.ll : %.c
	$(CC) $(LLSC_FLAGS) $(FLAGS) -o $@ $<

clean:
	$(RM) -rf $(KLEE_TARGET) $(KLEE_REPLAY_TARGET) $(KLEE_GEN) $(LLSC_TARGET)
