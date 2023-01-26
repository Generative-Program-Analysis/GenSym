SRC_FILES := $(wildcard ./*.c)

CC := clang-11

KLEE_INCL := /icse23/klee/include

GS_INCL := /icse23/GenSym/headers

FLAGS := -emit-llvm -O0 -Xclang -disable-O0-optnone -c
KLEE_FLAGS := -D KLEE -g -I $(KLEE_INCL)
GS_FLAGS := -D GENSYM -fno-discard-value-names -S -I $(GS_INCL)
LLSC_FLAGS := -D LLSC -fno-discard-value-names -S

KLEE_TARGET := $(SRC_FILES:%.c=%.bc)

KLEE_REPLAY_TARGET := $(SRC_FILES:%.c=%-replay)

KLEE_GEN := $(wildcard ./klee-*)

GS_TARGET := $(SRC_FILES:%.c=%.ll)

LLSC_TARGET := $(SRC_FILES:%.c=%_llsc.ll)

all: gensym klee llsc

gensym: $(GS_TARGET)
klee: $(KLEE_TARGET)
llsc: $(LLSC_TARGET)
klee-exe: $(KLEE_REPLAY_TARGET)

$(KLEE_TARGET): %.bc : %.c
	$(CC) $(KLEE_FLAGS) $(FLAGS) -o $@ $<

$(KLEE_REPLAY_TARGET): %-replay : %.c
	$(CC) $(KLEE_FLAGS) -o $@ $< -lkleeRuntest

$(GS_TARGET): %.ll : %.c
	$(CC) $(GS_FLAGS) $(FLAGS) -o $@ $<

$(LLSC_TARGET): %_llsc.ll : %.c
	$(CC) $(LLSC_FLAGS) $(FLAGS) -o $@ $<

clean:
	$(RM) -rf $(KLEE_TARGET) $(KLEE_REPLAY_TARGET) $(KLEE_GEN) $(GS_TARGET)
