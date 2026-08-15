# GenSym models the x86-64 Linux ABI. Keep LLVM input on that ABI while
# compiling the generated C++ runtime natively for the host architecture.
GENSYM_HOST_ARCH ?= $(shell uname -m)
GENSYM_LLVM_TARGET ?= x86_64-unknown-linux-gnu
GENSYM_LLVM_SYSROOT ?= /usr/x86_64-linux-gnu

CC := clang-11

ifneq ($(filter x86_64 amd64,$(GENSYM_HOST_ARCH)),)
LLVM_TARGET_FLAGS :=
else
ifeq ($(wildcard $(GENSYM_LLVM_SYSROOT)/include),)
$(error Missing x86-64 cross headers at $(GENSYM_LLVM_SYSROOT)/include; rebuild the devcontainer)
endif
LLVM_TARGET_FLAGS := --target=$(GENSYM_LLVM_TARGET) --sysroot=$(GENSYM_LLVM_SYSROOT)
endif

LLVM_CC = $(CC) $(LLVM_TARGET_FLAGS)
