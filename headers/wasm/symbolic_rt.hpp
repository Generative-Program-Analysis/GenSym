#ifndef WASM_SYMBOLIC_RT_HPP
#define WASM_SYMBOLIC_RT_HPP

#include "concrete_rt.hpp"
#include <variant>

class SymVal {
public:
  SymVal operator+(const SymVal &other) const {
    // Define how to add two symbolic values
    // Not implemented yet
    return SymVal();
  }

  SymVal is_zero() const {
    // Check if the symbolic value is zero
    // Not implemented yet
    return SymVal();
  }
};

class SymStack_t {
public:
  void push(SymVal val) {
    // Push a symbolic value to the stack
    // Not implemented yet
  }

  SymVal pop() {
    // Pop a symbolic value from the stack
    // Not implemented yet
    return SymVal();
  }

  SymVal peek() { return SymVal(); }
};

static SymStack_t SymStack;

class SymFrames_t {
public:
  void pushFrame(int size) {
    // Push a new frame with the given size
    // Not implemented yet
  }
  std::monostate popFrame(int size) {
    // Pop the frame of the given size
    // Not implemented yet
    return std::monostate();
  }

  SymVal get(int index) {
    // Get the symbolic value at the given index
    // Not implemented yet
    return SymVal();
  }

  void set(int index, SymVal val) {
    // Set the symbolic value at the given index
    // Not implemented yet
  }
};

static SymFrames_t SymFrames;

static SymVal Concrete(Num num) {
  // Convert a concrete number to a symbolic value
  // Not implemented yet
  return SymVal();
}

#endif // WASM_SYMBOLIC_RT_HPP