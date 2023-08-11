#ifndef WASM_STATE_CONTINUE_HPP
#define WASM_STATE_CONTINUE_HPP

#include <immer/flex_vector.hpp>
#include <immer/vector_transient.hpp>

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

Value I32V(int x) {
    Value v;
    v.ty = I32;
    v.i32 = x;
    return v;
};

struct Mem {};
struct Global {};

class State {
    public:
        immer::flex_vector<Mem> memory;
        immer::flex_vector<Global> globals;
        int stack_ptr = 0;
        int frame_ptr = 0;
        Value stack[1000];
        immer::vector_transient<std::function<std::monostate(std::monostate)>> return_stack;

        State(
                immer::flex_vector<Mem> memory,
                immer::flex_vector<Global> globals
            ) : memory(memory), globals(globals) {
            for (int i = 0; i < 1000; i++) {
                stack[i] = I32V(0);
            }
            return_stack = immer::vector_transient<std::function<std::monostate(std::monostate)>>();
        }

        Value stack_at(int i) {
            return stack[i];
        }

        void push_stack(Value v) {
            stack[stack_ptr++] = v;
        }

        Value pop_stack() {
            return stack[--stack_ptr];
        }

        Value peek_stack() {
            return stack[stack_ptr - 1];
        }

        void print_stack() {
            printf("sp: %d, fp: %d, Stack: ", stack_ptr, frame_ptr);
            for (int i = 0; i < stack_ptr; i++) {
                printf("%d ", stack[i].i32);
            }
            printf("\n");
        }

        // [0, 1 | 2, 3, 4]
        //         0  1
        // stackPtr = 5
        // get_local(0) = stack(0) -> stack.size() - stackPtr - 1 + 0
        // get_local(1) = stack(3) -> stack.size() - stackPtr - 1 + 1
        Value get_local(int i) {
            return stack[frame_ptr + i];
        }

        void set_local(int i, Value v) {
            stack[frame_ptr + i] = v;
        }

        void return_from_fun(int num_locals, int ret_num) {
            remove_stack_range(frame_ptr - num_locals, frame_ptr);
            remove_stack_range(frame_ptr + ret_num, stack_ptr);
            stack_ptr = frame_ptr - num_locals + ret_num;
        }

        void set_frame_ptr() {
            frame_ptr = stack_ptr;
        }

        void remove_stack_range(int start, int end) {
            for (int i = start; i < end; i++) {
                int j = end + (i - start);
                if (j < stack_ptr) {
                    stack[i] = stack[j];
                } else {
                    stack[i] = I32V(0);
                }
            }
        }
};

static State global_state = State(immer::flex_vector<Mem>(), immer::flex_vector<Global>());

State& init_state(immer::flex_vector<Mem> memory, immer::flex_vector<Global> globals, int num_locals) {
    global_state = State(memory, globals);
    global_state.stack_ptr = num_locals;
    global_state.frame_ptr = num_locals;
    return global_state;
}

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
