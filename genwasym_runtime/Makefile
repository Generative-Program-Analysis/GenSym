CXX = clang++

CXXFLAGS = -std=c++17 -Wall -Iinclude -I../headers -I../third-party/immer -fPIC -DUSE_IMM

BUILD_DIR = build

Z3_PREFIX ?=
Z3_INCLUDE_DIR ?= $(if $(strip $(Z3_PREFIX)),$(Z3_PREFIX)/include)
Z3_LIB_DIR ?= $(if $(strip $(Z3_PREFIX)),$(Z3_PREFIX)/lib)

Z3_CPPFLAGS = $(if $(strip $(Z3_INCLUDE_DIR)),-I"$(Z3_INCLUDE_DIR)")
Z3_LDFLAGS =
ifneq ($(strip $(Z3_LIB_DIR)),)
Z3_LDFLAGS = -L"$(Z3_LIB_DIR)" -Wl,-rpath,"$(Z3_LIB_DIR)"
endif

LDLIBS = -lz3

SRC = $(filter-out lib/wasm_state_continue.cpp,$(wildcard lib/*.cpp))

OBJ = $(patsubst lib/%.cpp,$(BUILD_DIR)/%.o,$(SRC))

STATIC_LIB = $(BUILD_DIR)/libgenwasym.a

SHARED_LIB = $(BUILD_DIR)/libgenwasym.so

UNAME_S := $(shell uname -s)

ifeq ($(UNAME_S),Darwin)
SHARED_PLATFORM_FLAGS = -Wl,-install_name,@rpath/libgenwasym.so
else
SHARED_PLATFORM_FLAGS =
endif

.PHONY: all clean

all: $(BUILD_DIR) $(STATIC_LIB) $(SHARED_LIB)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

$(BUILD_DIR)/%.o: lib/%.cpp | $(BUILD_DIR)
	$(CXX) $(CPPFLAGS) $(Z3_CPPFLAGS) $(CXXFLAGS) -MMD -MP -c $< -o $@

$(STATIC_LIB): $(OBJ)
	ar rcs $(STATIC_LIB) $(OBJ)

$(SHARED_LIB): $(OBJ)
	$(CXX) -shared $(SHARED_PLATFORM_FLAGS) $(LDFLAGS) $(Z3_LDFLAGS) -o $(SHARED_LIB) $(OBJ) $(LDLIBS)

-include $(OBJ:.o=.d)

clean:
	rm -rf $(BUILD_DIR)
