import random

def gen(n, klee):
    if klee:
        print("#include <klee/klee.h>")
    print("int f() {")
    for i in range(n):
        x = "x" + str(i)
        if klee:
            print(f"int {x}; klee_make_symbolic(&{x}, sizeof({x}), \"{x}\"); ")
        else:
            print(f"int {x} = {random.randrange(0, n)};")
    for i in range(n):
        r1 = random.randrange(0, 100)
        r2 = random.randrange(0, 100)
        print("if ({} > {}) ".format("x"+str(i), i) + "{")
        print(f"  x{i} = x{random.randrange(0, n)} + x{random.randrange(0, n)} + {r1};")
        # print(f"  z = x + y + {r2};")
        print("}")
    if klee:
        print("klee_assert(0);")
    print(f"return x{random.randrange(0, n)} + x{random.randrange(0, n)};")
    print("}")
    print()
    print("int main() {\n  f();\n  return 0;\n}")

gen(16, False)
