#include "wasm_state_continue.hpp"

cont_t fun_ret_cont_stack[1000];
int fun_ret_cont_stack_ptr = 0;

void push_fun_ret_cont_stack(cont_t cont) {
  fun_ret_cont_stack[fun_ret_cont_stack_ptr++] = cont;
}

std::monostate pop_fun_ret_cont_stack() {
  return fun_ret_cont_stack[--fun_ret_cont_stack_ptr](std::monostate());
}

Value I32V(int x) {
  Value v;
  v.ty = I32;
  v.i32 = x;
  return v;
}

State::State(immer::flex_vector<Mem> memory, immer::flex_vector<Global> globals)
    : memory(memory), globals(globals) {
  for (int i = 0; i < 1000; i++) {
    stack[i] = I32V(0);
  }
  return_stack = immer::vector_transient<std::function<std::monostate(std::monostate)>>();
}

Value State::stack_at(int i) {
  return stack[i];
}

void State::push_stack(Value v) {
  stack[stack_ptr++] = v;
}

Value State::pop_stack() {
  return stack[--stack_ptr];
}

Value State::peek_stack() {
  return stack[stack_ptr - 1];
}

void State::print_stack() {
  printf("sp: %ld, fp: %ld, Stack: ", stack_ptr, frame_ptr);
  for (int i = 0; i < stack_ptr; i++) {
    printf("%d ", stack[i].i32);
  }
  printf("\n");
}

Value State::get_local(int i) {
  return stack[frame_ptr + i];
}

void State::set_local(int i, Value v) {
  stack[frame_ptr + i] = v;
}

void State::return_from_fun(int num_locals, int ret_num) {
  remove_stack_range(frame_ptr - num_locals, frame_ptr);
  remove_stack_range(frame_ptr + ret_num, stack_ptr);
  stack_ptr = frame_ptr - num_locals + ret_num;
}

void State::bump_frame_ptr() {
  frame_ptr = stack_ptr;
}

void State::set_frame_ptr(int fp) {
  frame_ptr = fp;
}

int State::get_frame_ptr() {
  return frame_ptr;
}

void State::save_frame_ptr() {
  tmp_frame_ptr = frame_ptr;
}

void State::restore_frame_ptr() {
  frame_ptr = tmp_frame_ptr;
}

void State::remove_stack_range(int start, int end) {
  for (int i = start; i < end; i++) {
    int j = end + (i - start);
    if (j < stack_ptr) {
      stack[i] = stack[j];
    } else {
      stack[i] = I32V(0);
    }
  }
}

void State::reverse_top_n(int n) {
  for (int i = stack_ptr - n; i < stack_ptr - n / 2; i++) {
    int j = stack_ptr - (i - (stack_ptr - n)) - 1;
    Value tmp = stack[i];
    stack[i] = stack[j];
    stack[j] = tmp;
  }
}

State global_state = State(immer::flex_vector<Mem>(), immer::flex_vector<Global>());

State& init_state(immer::flex_vector<Mem> memory,
                  immer::flex_vector<Global> globals,
                  int num_locals) {
  global_state = State(memory, globals);
  global_state.stack_ptr = num_locals;
  global_state.frame_ptr = num_locals;
  return global_state;
}