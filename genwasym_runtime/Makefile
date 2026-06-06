CXX = g++
CXXFLAGS = -std=c++17 -Wall -Iinclude -I../third-party/immer -I../third-party/z3/build/z3_install/usr/local/include -fPIC
BUILD_DIR = build

Z3_LIB_DIR = ../third-party/z3/build/z3_install/usr/local/lib
LDFLAGS = -L$(Z3_LIB_DIR) -Wl,-rpath,$(Z3_LIB_DIR)
LDLIBS = -lz3

SRC = $(filter-out lib/wasm_state_continue.cpp,$(wildcard lib/*.cpp))
OBJ = $(patsubst lib/%.cpp,$(BUILD_DIR)/%.o,$(SRC))

STATIC_LIB = $(BUILD_DIR)/libgenwasym.a
SHARED_LIB = $(BUILD_DIR)/libgenwasym.so

.PHONY: all clean

all: $(BUILD_DIR) $(STATIC_LIB) $(SHARED_LIB)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

$(BUILD_DIR)/%.o: lib/%.cpp | $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(STATIC_LIB): $(OBJ)
	ar rcs $(STATIC_LIB) $(OBJ)

$(SHARED_LIB): $(OBJ)
	$(CXX) -shared -Wl,-install_name,@rpath/libgenwasym.so $(LDFLAGS) -o $(SHARED_LIB) $(OBJ) $(LDLIBS)

clean:
	rm -rf $(BUILD_DIR)