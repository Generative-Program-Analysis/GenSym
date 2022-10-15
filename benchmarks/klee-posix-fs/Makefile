SRC_FILES := $(wildcard ./fd_init.c ./fd.c ./fd_64.c  ./gensym.c  ./test.c)

CC := clang-11

FLAGS := -emit-llvm -O0 -disable-O0-optnone -c
#FLAGS := -emit-llvm -Os -c
#KLEE_FLAGS := -D KLEE -g -I $(KLEE_INCL)
GS_FLAGS := -D _LARGEFILE64_SOURCE -fno-discard-value-names -S

#KLEE_TARGET := $(SRC_FILES:%.c=%.bc)
#
#KLEE_REPLAY_TARGET := $(SRC_FILES:%.c=%-replay)
#
#KLEE_GEN := $(wildcard ./klee-*)
#
GS_TARGET := $(SRC_FILES:%.c=%.ll)

GS_LINK_TARGET := klee_lib_64.ll

all: gensym 

gensym: $(GS_LINK_TARGET)
#klee: $(KLEE_TARGET)
#klee-exe: $(KLEE_REPLAY_TARGET)

$(GS_TARGET): %.ll : %.c
	$(CC) $(GS_FLAGS) $(FLAGS) -o $@ $<

$(GS_LINK_TARGET): $(GS_TARGET)
	llvm-link-11 -S $(GS_TARGET) -o klee_lib_64.ll

clean:
	$(RM) -rf $(GS_TARGET) ${GS_LINK_TARGET}
