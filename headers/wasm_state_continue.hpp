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
        immer::flex_vector<Value> locals;
        immer::flex_vector<Value> stack;

        State(
                immer::flex_vector<Mem> memory,
                immer::flex_vector<Global> globals,
                immer::flex_vector<Value> locals,
                immer::flex_vector<Value> stack
            ) : memory(memory), globals(globals), locals(locals), stack(stack) {}

        void push_stack(Value v) {
            stack = stack.push_back(v);
        }

        Value pop_stack() {
            Value v = stack.back();
            // TODO: better stack impl
            stack = stack.take(stack.size() - 1);
            return v;
        }

        void print_stack() {
            for (auto it = stack.begin(); it != stack.end(); it++) {
                printf("%d ", it->i32);
            }
            printf("\n");
        }

        State withMemory(immer::flex_vector<Mem> newMemory) {
            return State(newMemory, globals, stack, locals);
        }

        State withGlobals(immer::flex_vector<Global> newGlobals) {
            return State(memory, newGlobals, stack, locals);
        }

        State withLocals(immer::flex_vector<Value> newLocals) {
            return State(memory, globals, newLocals, stack);
        }

        State withStack(immer::flex_vector<Value> newStack) {
            return State(memory, globals, locals, newStack);
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
