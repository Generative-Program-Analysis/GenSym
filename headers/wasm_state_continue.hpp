#ifndef WASM_STATE_CONTINUE_HPP
#define WASM_STATE_CONTINUE_HPP

#include <immer/flex_vector.hpp>

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
        immer::flex_vector<Value> stack;

        State(
                immer::flex_vector<Mem> memory,
                immer::flex_vector<Global> globals,
                immer::flex_vector<Value> stack
            ) : memory(memory), globals(globals), stack(stack) {}

        void push_stack(Value v) {
            stack = stack.push_back(v);
        }

        Value pop_stack() {
            Value v = stack.back();
            // TODO: better stack impl
            stack = stack.take(stack.size() - 1);
            return v;
        }

        Value peek_stack() {
            return stack.back();
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
            return stack[stack.size() - i];
        }

        void set_local(int i, Value v) {
            stack = stack.set(stack.size() - i, v);
        }

        void remove_stack_range(int start, int end) {
            // 1 2 3 4 5
            stack = stack.take(start) + stack.drop(end);
        }
};

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
