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
        immer::vector_transient<Value> stack;

        State(
                immer::flex_vector<Mem> memory,
                immer::flex_vector<Global> globals,
                immer::vector_transient<Value> stack
            ) : memory(memory), globals(globals), stack(stack) {}

        void push_stack(Value v) {
            stack.push_back(v);
        }

        Value pop_stack() {
            Value v = stack[stack.size() - 1];
            stack.take(stack.size() - 1);
            return v;
        }

        Value peek_stack() {
            return stack[stack.size() - 1];
        }

        void print_stack() {
            printf("Stack:\n");
            for (auto it = stack.begin(); it != stack.end(); it++) {
                printf("%d ", it->i32);
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
            stack.set(i, v);
        }

        void remove_stack_range(int start, int end) {
            int size = stack.size();
            for (int i = start; i < end; i++) {
                if (i + (end - start) < size) {
                    stack.set(i, stack[i + (end - start)]);
                } else {
                    stack.set(i, I32V(0));
                }
            }
            stack.take(size - (end - start));
        }
};
static State global_state = State(
        immer::flex_vector<Mem>(),
        immer::flex_vector<Global>(),
        immer::vector_transient<Value>()
    );

State& init_state(immer::flex_vector<Mem> memory, immer::flex_vector<Global> globals, immer::flex_vector<Value> stack) {
    immer::vector_transient<Value> s;
    for (auto it = stack.begin(); it != stack.end(); it++) {
        s.push_back(*it);
    }
    global_state = State(memory, globals, s);
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
