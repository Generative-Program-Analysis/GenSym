CXX := g++
CXXFLAGS := -std=c++17 -Wall -Wextra -O0 -g -pthread -w
CPPFLAGS := -I../headers \
            -I../third-party/immer \
            -I../third-party/parallel-hashmap

FLOAT_TARGET := float_test
INT_TARGET := int_test
TARGETS := $(FLOAT_TARGET) $(INT_TARGET)

.PHONY: all run run-float run-int clean

all: $(TARGETS)

$(FLOAT_TARGET): float_test.cpp
	$(CXX) $(CXXFLAGS) $(CPPFLAGS) $< -o $@

$(INT_TARGET): int_test.cpp
	$(CXX) $(CXXFLAGS) $(CPPFLAGS) $< -o $@

run: run-float run-int

run-float: $(FLOAT_TARGET)
	./$(FLOAT_TARGET)

run-int: $(INT_TARGET)
	./$(INT_TARGET)

clean:
	$(RM) $(TARGETS)
