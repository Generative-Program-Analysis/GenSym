SRC_FILES := $(wildcard ./fd_init.c ./fd.c ./fd_64.c  ./llsc.c  ./test.c)

CC := clang-9

FLAGS := -emit-llvm -O0 -disable-O0-optnone -c
#FLAGS := -emit-llvm -Os -c
#KLEE_FLAGS := -D KLEE -g -I $(KLEE_INCL)
LLSC_FLAGS := -D _LARGEFILE64_SOURCE -fno-discard-value-names -S

#KLEE_TARGET := $(SRC_FILES:%.c=%.bc)
#
#KLEE_REPLAY_TARGET := $(SRC_FILES:%.c=%-replay)
#
#KLEE_GEN := $(wildcard ./klee-*)
#
LLSC_TARGET := $(SRC_FILES:%.c=%.ll)

LLSC_LINK_TARGET := klee_lib_64.ll

all: llsctarget

llsctarget: $(LLSC_LINK_TARGET)
#klee: $(KLEE_TARGET)
#klee-exe: $(KLEE_REPLAY_TARGET)

$(LLSC_TARGET): %.ll : %.c
	$(CC) $(LLSC_FLAGS) $(FLAGS) -o $@ $<

$(LLSC_LINK_TARGET): $(LLSC_TARGET)
	llvm-link-9 -S $(LLSC_TARGET) -o klee_lib_64.ll

clean:
	$(RM) -rf $(LLSC_TARGET) ${LLSC_LINK_TARGET}