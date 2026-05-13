CXX = g++
CXXFLAGS = -std=c++17 -Wall -Iinclude -I../third-party/immer -fPIC
BUILD_DIR = build

SRC = lib/genwasym.cpp lib/wasm_state_continue.cpp
OBJ = build/genwasym.o build/wasm_state_continue.o

STATIC_LIB = build/libgenwasym.a
SHARED_LIB = build/libgenwasym.so

.PHONY: all clean

all: build $(STATIC_LIB) $(SHARED_LIB)

build:
	mkdir -p $(BUILD_DIR)

build/genwasym.o: lib/genwasym.cpp | build
	$(CXX) $(CXXFLAGS) -c lib/genwasym.cpp -o build/genwasym.o

build/wasm_state_continue.o: lib/wasm_state_continue.cpp | build
	$(CXX) $(CXXFLAGS) -c lib/wasm_state_continue.cpp -o build/wasm_state_continue.o

$(STATIC_LIB): $(OBJ)
	ar rcs $(STATIC_LIB) $(OBJ)

$(SHARED_LIB): $(OBJ)
	$(CXX) -shared -Wl,-install_name,@rpath/libgenwasym.so -o $(SHARED_LIB) $(OBJ)

clean:
	rm -rf $(BUILD_DIR)