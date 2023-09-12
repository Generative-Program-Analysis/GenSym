#!/usr/bin/env python3
import re
import os
import sys

patterns = [
    ('call \$assume' , 'sym_assume'),
    ('call \$assert' , 'sym_assert'),
    ('call \$sym_int', 'i32.symbolic'),
    ('call \$alloc'  , 'alloc'),
    ('call \$free'   , 'free'),
    ('call \$dealloc', 'free'),
    ('anyfunc'       , 'funcref'),
    ('\(elem \(;0;\) \(i32.const 1\) func', '(elem (;0;) (i32.const 1)')
]

def sub_patterns(line):
    for (pattern, repl) in patterns:
        line = re.sub(pattern, repl, line)
    return line

def main(argv=None):
    if argv is None:
        argv = sys.argv[1:]

    test = argv[0]
    if not os.path.exists(test):
        return -1

    print(f'Transforming {test}...')
    with open(test, 'r') as f:
        lines = f.read().splitlines()

    text = '\n'.join(map(sub_patterns, lines))
    with open(test, 'w') as f:
        f.write(text)

    return 0

if __name__ == '__main__':
    sys.exit(main())
