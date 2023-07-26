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
        Value stack[1000];

        State(
                immer::flex_vector<Mem> memory,
                immer::flex_vector<Global> globals
            ) : memory(memory), globals(globals) {
            for (int i = 0; i < 1000; i++) {
                stack[i] = I32V(0);
            }
        }

        Value stack_at(int i) {
            return stack[i];
        }

        void push_stack(Value v, int sp) {
            stack[sp] = v;
        }

        Value peek_stack(int sp) {
            return stack[sp - 1];
        }

        void print_stack(int sp) {
            printf("Stack:\n");
            for (int i = 0; i < sp; i++) {
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
            return stack[i];
        }

        void set_local(int i, Value v) {
            stack[i] = v;
        }

        void remove_stack_range(int start, int end, int sp) {
            for (int i = start; i < end; i++) {
                int j = end + (i - start);
                if (j < sp) {
                    stack[i] = stack[j];
                } else {
                    stack[i] = I32V(0);
                }
            }
        }
};

static State global_state = State(immer::flex_vector<Mem>(), immer::flex_vector<Global>());

State& init_state(immer::flex_vector<Mem> memory, immer::flex_vector<Global> globals) {
    global_state = State(memory, globals);
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
