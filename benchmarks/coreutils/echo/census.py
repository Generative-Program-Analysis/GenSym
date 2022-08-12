ll_filename = "echo.ll"

with open(ll_filename) as f:
    read_data = f.read()
    declared_fun = [ l.split("@")[1].split("(")[0] for l in read_data.splitlines() if "declare" in l ]
    for fun_name in declared_fun:
        print(fun_name)