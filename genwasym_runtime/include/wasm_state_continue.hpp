#ifndef WASM_STATE_CONTINUE_HPP
#define WASM_STATE_CONTINUE_HPP

#include <functional>
#include <variant>
#include <cstdio>
#include <cstddef>

#include <immer/flex_vector.hpp>
#include <immer/vector_transient.hpp>
#include <immer/flex_vector_transient.hpp>

typedef std::function<std::monostate(std::monostate)> cont_t;

extern cont_t fun_ret_cont_stack[1000];
extern int fun_ret_cont_stack_ptr;

void push_fun_ret_cont_stack(cont_t cont);
std::monostate pop_fun_ret_cont_stack();

template <typename T>
immer::flex_vector<T> flex_vector_reverse(immer::flex_vector<T> v) {
  immer::flex_vector<T> result = immer::flex_vector<T>();
  for (auto it = v.rbegin(); it != v.rend(); it++) {
    result = result.push_back(*it);
  }
  return result;
}

enum ValueTy {
  I32,
};

struct Value {
  ValueTy ty;
  union {
    int i32;
  };
};

Value I32V(int x);

struct Mem {};
struct Global {};

class State {
  public:
    immer::flex_vector<Mem> memory;
    immer::flex_vector<Global> globals;
    size_t stack_ptr = 0;
    size_t frame_ptr = 0;
    Value stack[1000];
    immer::vector_transient<std::function<std::monostate(std::monostate)>> return_stack;

    size_t tmp_frame_ptr = 0;

    State(immer::flex_vector<Mem> memory, immer::flex_vector<Global> globals);

    Value stack_at(int i);
    void push_stack(Value v);
    Value pop_stack();
    Value peek_stack();
    void print_stack();
    Value get_local(int i);
    void set_local(int i, Value v);
    void return_from_fun(int num_locals, int ret_num);
    void bump_frame_ptr();
    void set_frame_ptr(int fp);
    int get_frame_ptr();
    void save_frame_ptr();
    void restore_frame_ptr();
    void remove_stack_range(int start, int end);
    void reverse_top_n(int n);
};

extern State global_state;

State& init_state(immer::flex_vector<Mem> memory,
                  immer::flex_vector<Global> globals,
                  int num_locals);

enum EvalTag {
  CONTINUE,
  RETURNING,
  BREAKING,
};

struct EvalResult {
  EvalTag tag;
  int n;
  State state;
};

#endif