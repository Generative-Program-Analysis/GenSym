#!/usr/bin/env python3
import os
import sys
import glob
import comby

patterns = [
        ('#include <gillian-c/gillian-c.h>', '#include "mockups.h"'),
        ("int :[[x]] = __builtin_annot_intval(\"symb_int\", :[[x]]);", \
                "int :[x] = sym_int(\":[x]\");"),
        ("char :[[x]] = __builtin_annot_intval(\"symb_int\", :[[x]]);", \
                "int :[x] = sym_int(\":[x]\");"),
        ("char :[[x]] = (char)__builtin_annot_intval(\"symb_int\", :[[x]]);", \
                "int :[x] = sym_int(\":[x]\");"),
        (':[[type]] X = (:[[type]])__builtin_annot_intval("symb_int", :[[x]])', \
                ":[type] :[x] = sym_int(\":[x]\");"),
        (":[[x]] = __builtin_annot_intval(\"symb_int\", :[[x]]);", \
                ":[x] = sym_int(\":[x]\");"),
        ('*:[[x]] = __builtin_annot_intval("symb_int", *:[[x]]);', \
                ":[x] = sym_int(\":[x]\");"),
        ('ASSERT(:[assertion]);','assert(:[assertion]);'),
        ('ASSUME(:[assertion]);','assume(:[assertion]);'),
        ('ASSUME (:[assertion]);','assume(:[assertion]);')
]

def main(argv=None):
    if argv is None:
        argv = sys.argv[1:]

    src_dir = argv[0]
    if not os.path.exists(src_dir):
        print('Directory \'{src_dir}\' does not exist!')
        return -1

    cby = comby.Comby()

    sources = os.path.join(src_dir, '**/*.c')
    headers = os.path.join(src_dir, '**/*.h')

    srcs = glob.glob(sources, recursive=True) + \
           glob.glob(headers, recursive=True)
    for src in srcs:
        print(f'Transforming {src}...')

        with open(src, 'r') as f:
            data = f.read()

        for p in patterns:
            data = cby.rewrite(data, p[0], p[1])

        with open(src, 'w') as f:
            f.write(data)

    return 0

if __name__ == '__main__':
    sys.exit(main())
