def gen():
    print("int x0 = argc;")
    for i in range(1, 1000):
        print("int x{} = x{} + {};".format(i, i-1, i))
    """
    for i in range(1500, 2500):
        print("int x{} = x{} - x{};".format(i, i-1, i-100))
    for i in range(200, 300):
        print("int x{} = x{} - x{};".format(i, i-1, i-100))
    for i in range(300, 400):
        print("int x{} = x{} * x{};".format(i, i-1, i-2))
    """

gen()
