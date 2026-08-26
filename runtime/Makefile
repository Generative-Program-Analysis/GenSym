CXX ?= g++
AR ?= ar
ROOT := $(abspath ..)
BUILD_DIR := build
TARGET := $(BUILD_DIR)/libgensym_runtime.a
OBJECT := $(BUILD_DIR)/runtime.o
DEP := $(OBJECT:.o=.d)

CPPFLAGS := -I$(ROOT)/headers -I$(ROOT)/third-party/immer -I$(ROOT)/third-party/parallel-hashmap
CXXFLAGS ?= -O3
CXXFLAGS += -std=c++17 -Wno-format-security -fno-omit-frame-pointer -MMD -MP

.DEFAULT_GOAL := all

all: $(TARGET)

$(TARGET): $(OBJECT)
	$(AR) rcs $@ $^

$(OBJECT): runtime.cpp
	@mkdir -p $(@D)
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) -c -o $@ $<

clean:
	rm -rf $(BUILD_DIR)

-include $(DEP)

.PHONY: all clean
