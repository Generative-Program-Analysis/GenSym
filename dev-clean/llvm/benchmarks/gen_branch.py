import random

def gen():
    print("int f(int a, int b, int c) {")
    print("int x = a;")
    print("int y = b;")
    print("int z = c;")
    for i in range(1, 11):
        r1 = random.randrange(0, 100)
        r2 = random.randrange(0, 100)
        print("if (x > {}) ".format(i) + "{")
        print(f"  y = x + z + {r1};")
        print(f"  z = x + y + {r2};")
        print("}")
    """
    for i in range(20, 30):    
        print("if (y > {}) ".format(i) + "{")
        print("  y = x + z;")
        print("  z = x + y;")
        print("}")
    """ 
    print("return x + y + c;")
    print("}")

gen()
