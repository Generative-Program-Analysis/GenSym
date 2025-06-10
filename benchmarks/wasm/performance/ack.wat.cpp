
#include <cassert>
#include <cstdint>
#include <cstdio>
#include <iostream>
#include <memory>
#include <ostream>
#include <variant>
#include <vector>

void info() {
#ifdef DEBUG
  std::cout << std::endl;
#endif
}

template <typename T, typename... Args>
void info(const T &first, const Args &...args) {
#ifdef DEBUG
  std::cout << first << " ";
  info(args...);
#endif
}

struct Num {
  Num(int64_t value) : value(value) {}
  Num() : value(0) {}
  int64_t value;
  int32_t toInt() { return static_cast<int32_t>(value); }

  bool operator==(const Num &other) const { return value == other.value; }
  bool operator!=(const Num &other) const { return !(*this == other); }
  Num operator+(const Num &other) const { return Num(value + other.value); }
  Num operator-(const Num &other) const { return Num(value - other.value); }
  Num operator*(const Num &other) const { return Num(value * other.value); }
  Num operator/(const Num &other) const {
    if (other.value == 0) {
      throw std::runtime_error("Division by zero");
    }
    return Num(value / other.value);
  }
  Num operator<(const Num &other) const { return Num(value < other.value); }
  Num operator<=(const Num &other) const { return Num(value <= other.value); }
  Num operator>(const Num &other) const { return Num(value > other.value); }
  Num operator>=(const Num &other) const { return Num(value >= other.value); }
  Num operator&(const Num &other) const { return Num(value & other.value); }
};

static Num I32V(int v) { return v; }

static Num I64V(int64_t v) { return v; }

using Slice = std::vector<Num>;

class Stack_t {
public:
  std::monostate push(Num &&num) {
    stack_.push_back(std::move(num));
    return std::monostate{};
  }

  std::monostate push(Num &num) {
    stack_.push_back(num);
    return std::monostate{};
  }

  Num pop() {
    if (stack_.empty()) {
      throw std::runtime_error("Stack underflow");
    }
    Num num = std::move(stack_.back());
    stack_.pop_back();
    return num;
  }

  Num peek() {
    if (stack_.empty()) {
      throw std::runtime_error("Stack underflow");
    }
    return stack_.back();
  }

  Num get(int32_t index) {
    assert(index >= 0);
    assert(index < stack_.size());
    return stack_[index];
  }

  int32_t size() { return stack_.size(); }

  void shift(int32_t offset, int32_t size) {
    if (offset < 0) {
      throw std::out_of_range("Invalid offset: " + std::to_string(offset));
    }
    if (size < 0) {
      throw std::out_of_range("Invalid size: " + std::to_string(size));
    }
    // shift last `size` of numbers forward of `offset`
    for (int32_t i = stack_.size() - size; i < stack_.size(); ++i) {
      stack_[i - offset] = stack_[i];
    }
    stack_.resize(stack_.size() - offset);
  }

  Slice take(int32_t size) {
    if (size > stack_.size()) {
      throw std::out_of_range("Invalid size: requested " + std::to_string(size) + ", stack size is " + std::to_string(stack_.size()));
    }
    // todo: avoid re-allocation
    Slice slice(stack_.end() - size, stack_.end());
    stack_.resize(stack_.size() - size);
    return slice;
  }

  void print() {
    std::cout << "Stack contents: " << std::endl;
    for (auto it = stack_.rbegin(); it != stack_.rend(); ++it) {
      std::cout << it->value << std::endl;
    }
  }

  void initialize() { stack_.clear(); }

private:
  std::vector<Num> stack_;
};
static Stack_t Stack;

struct Frame_t {
  std::vector<Num> locals;

  Frame_t(std::int32_t size) : locals() { locals.resize(size); }
  Num &operator[](std::int32_t index) {
    assert(index >= 0);
    if (index >= locals.size()) {
      throw std::out_of_range("Index out of range");
    }
    return locals[index];
  }
  void putAll(Slice slice) {
    for (std::int32_t i = 0; i < slice.size(); ++i) {
      locals[i] = slice[i];
    }
  }
};

class Frames_t {
public:
  std::monostate popFrame() {
    if (!frames.empty()) {
      frames.pop_back();
      return std::monostate{};
    } else {
      std::cout << "No frames to pop." << std::endl;
      throw std::runtime_error("No frames to pop.");
    }
  }

  Num get(std::int32_t index) {
    auto ret = top()[index];
    return ret;
  }

  void set(std::int32_t index, Num num) { frames.back()[index] = num; }

  Frame_t &top() {
    if (frames.empty()) {
      throw std::runtime_error("No frames available");
    }
    return frames.back();
  }

  void pushFrame(std::int32_t size) {
    Frame_t frame(size);
    frames.push_back(frame);
  }

  void putAll(Slice slice) { top().putAll(slice); }

private:
  std::vector<Frame_t> frames;
};

static Frames_t Frames;

static void initRand() {
  // for now, just do nothing
}

static std::monostate unreachable() {
  std::cout << "Unreachable code reached!" << std::endl;
  throw std::runtime_error("Unreachable code reached");
}

static int32_t pagesize = 65536;
static int32_t page_count = 0;

struct Memory_t {
  std::vector<uint8_t> memory;
  Memory_t(int32_t init_page_count) : memory(init_page_count * pagesize) {}

  int32_t loadInt(int32_t base, int32_t offset) {
    return *reinterpret_cast<int32_t *>(static_cast<uint8_t *>(memory.data()) +
                                        base + offset);
  }

  std::monostate storeInt(int32_t base, int32_t offset, int32_t value) {
    *reinterpret_cast<int32_t *>(static_cast<uint8_t *>(memory.data()) + base +
                                 offset) = value;
    return std::monostate{};
  }

  // grow memory by delta bytes when bytes > 0. return -1 if failed, return old
  // size when success
  int32_t grow(int32_t delta) {
    if (delta <= 0) {
      return memory.size();
    }

    try {
      memory.resize(memory.size() + delta * pagesize);
      auto old_page_count = page_count;
      page_count += delta;
      return memory.size();
    } catch (const std::bad_alloc &e) {
      return -1;
    }
  }
};


static Memory_t Memory(1); // 1 page memory


/*****************************************
Emitting Generated Code
*******************************************/
    

#include <functional>
#include <stdbool.h>
#include <stdint.h>
#include <string>
#include <variant>

/************* Function Declarations **************/
std::monostate x7(std::function<std::monostate(std::monostate)>);

/************* Functions **************/
std::monostate x7(std::function<std::monostate(std::monostate)> x8) {
info("Entered the function at 0, stackSize =", Stack.size());
Stack.push(Frames.get(0));
Num x9 = Stack.pop();
Frames.set(0, x9);
Stack.push(Frames.get(1));
Num x10 = Stack.pop();
Frames.set(1, x10);
int x11 = Stack.size();
std::function<std::monostate(std::monostate)> x12;
x12 = [&](std::monostate x13)->std::monostate {
info("Exiting the block, stackSize =", Stack.size());
int x14 = Stack.size();
int x15 = x14 - x11;
if (x15 > 0) Stack.shift(x15, 0);
Stack.push(Frames.get(1));
Stack.push(I32V(1));
Num x16 = Stack.pop();
Num x17 = Stack.pop();
Stack.push(x17 + x16);
return x8(std::monostate{});
};
Stack.size();
int x18 = Stack.size();
std::monostate x19 = std::monostate();
std::function<std::monostate(std::monostate)> x20;
x20 = [&](std::monostate x21)->std::monostate {
std::monostate();
info("Entered the loop, stackSize =", Stack.size());
int x22 = Stack.size();
int x23 = x22 - x18;
if (x23 > 0) Stack.shift(x23, 0);
std::monostate();
Stack.push(Frames.get(1));
Num x24 = Stack.pop();
Frames.set(1, x24);
Stack.push(Frames.get(0));
Frames.set(0, Stack.peek());
Num x25 = Stack.pop();
int x26 = x25.toInt();
Num x27 = x26 == 0 ? I32V(1) : I32V(0);
Stack.push(x27);
Num x28 = Stack.pop();
info("The br_if(1)'s condition is ", x28.toInt());
Num x29 = I32V(0);
std::monostate x30 = x28 != x29 ? ({
info("Jump to 1");
x12(std::monostate{});
}) : ({
info("Continue");
int x31 = Stack.size();
int x32 = Stack.size();
Stack.push(Frames.get(1));
Num x33 = Stack.pop();
info("The br_if(0)'s condition is ", x33.toInt());
Num x34 = I32V(0);
std::monostate x35 = x33 != x34 ? ({
std::function<std::monostate(std::monostate)> x36;
x36 = [&](std::monostate x37)->std::monostate {
info("Exiting the block, stackSize =", Stack.size());
int x38 = Stack.size();
int x39 = x38 - x31;
if (x39 > 0) Stack.shift(x39, 0);
Stack.push(Frames.get(0));
Stack.push(I32V(-1));
Num x40 = Stack.pop();
Num x41 = Stack.pop();
Stack.push(x41 + x40);
Num x42 = Stack.pop();
Frames.set(0, x42);
Stack.push(Frames.get(1));
Num x43 = Stack.pop();
Frames.set(1, x43);
info("Jump to 0");
return x20(std::monostate{});
};
std::function<std::monostate(std::monostate)> x44;
x44 = [&](std::monostate x45)->std::monostate {
info("Exiting the block, stackSize =", Stack.size());
int x46 = Stack.size();
int x47 = x46 - x32;
if (x47 > 0) Stack.shift(x47, 0);
Stack.push(Frames.get(0));
Stack.push(Frames.get(1));
Stack.push(I32V(-1));
Num x48 = Stack.pop();
Num x49 = Stack.pop();
Stack.push(x49 + x48);
Stack.size();
Slice x50 = Stack.take(2);
std::monostate x51 = std::monostate();
std::function<std::monostate(std::monostate)> x52;
x52 = [&](std::monostate x53)->std::monostate {
std::monostate();
info("Exiting the function at 0, stackSize =", Stack.size());
Frames.popFrame();
Num x54 = Stack.pop();
Frames.set(1, x54);
return x36(std::monostate{});
};
Frames.pushFrame(2);
Frames.putAll(x50);
return x7(x52);
};
info("Jump to 0");
x44(std::monostate{});
}) : ({
std::function<std::monostate(std::monostate)> x36;
x36 = [&](std::monostate x37)->std::monostate {
info("Exiting the block, stackSize =", Stack.size());
int x38 = Stack.size();
int x39 = x38 - x31;
if (x39 > 0) Stack.shift(x39, 0);
Stack.push(Frames.get(0));
Stack.push(I32V(-1));
Num x40 = Stack.pop();
Num x41 = Stack.pop();
Stack.push(x41 + x40);
Num x42 = Stack.pop();
Frames.set(0, x42);
Stack.push(Frames.get(1));
Num x43 = Stack.pop();
Frames.set(1, x43);
info("Jump to 0");
return x20(std::monostate{});
};
info("Continue");
Stack.push(I32V(1));
Num x55 = Stack.pop();
Frames.set(1, x55);
info("Jump to 1");
x36(std::monostate{});
});
x35;
});
return x30;
};
return x20(std::monostate{});
}
std::monostate Snippet(std::monostate x0) {
std::function<std::monostate(std::monostate)> x1;
x1 = [&](std::monostate x2)->std::monostate {
info("Exiting the program...");
return std::monostate();
};
Stack.initialize();
Frames.pushFrame(2);
Stack.push(I32V(10000));
Num x3 = Stack.pop();
Frames.set(0, x3);
int x4 = Stack.size();
int x5 = Stack.size();
std::monostate x6 = std::monostate();
std::function<std::monostate(std::monostate)> x56;
x56 = [&](std::monostate x57)->std::monostate {
std::monostate();
info("Entered the loop, stackSize =", Stack.size());
int x58 = Stack.size();
int x59 = x58 - x5;
if (x59 > 0) Stack.shift(x59, 0);
std::monostate x60 = std::monostate();
std::function<std::monostate(std::monostate)> x61;
x61 = [&](std::monostate x62)->std::monostate {
std::monostate();
info("Exiting the loop, stackSize =", Stack.size());
int x63 = Stack.size();
int x64 = x63 - x4;
if (x64 > 0) Stack.shift(x64, 0);
return x1(std::monostate{});
};
Stack.push(I32V(2));
Stack.push(I32V(1));
Stack.size();
Slice x65 = Stack.take(2);
std::monostate x66 = std::monostate();
std::function<std::monostate(std::monostate)> x67;
x67 = [&](std::monostate x68)->std::monostate {
std::monostate();
info("Exiting the function at 0, stackSize =", Stack.size());
Frames.popFrame();
Num x69 = Stack.pop();
Frames.set(1, x69);
Stack.push(Frames.get(0));
Stack.push(I32V(1));
Num x70 = Stack.pop();
Num x71 = Stack.pop();
Stack.push(x71 - x70);
Frames.set(0, Stack.peek());
Num x72 = Stack.pop();
info("The br_if(0)'s condition is ", x72.toInt());
Num x73 = I32V(0);
std::monostate x74 = x72 != x73 ? ({
info("Jump to 0");
x56(std::monostate{});
}) : ({
info("Continue");
x61(std::monostate{});
});
return x74;
};
Frames.pushFrame(2);
Frames.putAll(x65);
return x7(x67);
};
x56(std::monostate{});
return Frames.popFrame();
}

/*****************************************
End of Generated Code
*******************************************/
int main(int argc, char *argv[]) {
  Snippet(std::monostate{});
  return 0;
}
