import sys
import subprocess
from pathlib import Path

#!/usr/bin/env python3

# Compile all .cpp files in this script's directory into executables with the same stem.
# Usage: python compile.py [extra g++ flags...]

HERE = Path(__file__).resolve().parent
CPP_FILES = sorted(HERE.glob("*.cpp"))

if not CPP_FILES:
    print(f"No .cpp files found in {HERE}")
    sys.exit(0)

DEFAULT_FLAGS = [
    "-std=c++17",
    "-g",
    "-O3",
    "-Wall",
    "-Wextra",
    "-DUSE_IMM",
    "-I/home/zdh/WorkSpace/GenSym/headers",
    "-lz3",
    "-DENABLE_PROFILE_TIME",
    "-DNO_REUSE",
]
EXTRA_FLAGS = sys.argv[1:]
FLAGS = DEFAULT_FLAGS + EXTRA_FLAGS


def compile_all(cpp_files=None, flags=None):
    if cpp_files is None:
        cpp_files = CPP_FILES
    if flags is None:
        flags = FLAGS

    compiled_total = 0
    compiled_success = 0
    compiled_failed = 0

    for cpp in cpp_files:
        out = cpp.with_suffix(".exe")  # foo.cpp -> ./foo
        cmd = ["g++", str(cpp), "-o", str(out)] + flags
        print("Compiling:", cpp.name)
        try:
            print("  Executing command:")
            print("   ", " ".join(cmd))
            proc = subprocess.run(cmd, capture_output=True, text=True)
        except FileNotFoundError:
            print("Error: g++ not found. Install a C++ compiler.")
            sys.exit(1)

        compiled_total += 1
        if proc.returncode == 0:
            compiled_success += 1
            print("  -> OK:", out.name)
        else:
            compiled_failed += 1
            print("  -> FAILED:", cpp.name)
            if proc.stdout:
                print(proc.stdout.strip())
            if proc.stderr:
                print(proc.stderr.strip())

    print()
    print("Overall summary:")
    print(f"  Files compiled: {compiled_total}")
    print(f"  Succeeded: {compiled_success}")
    print(f"  Failed: {compiled_failed}")
    return compiled_total, compiled_success, compiled_failed


if __name__ == "__main__":
    compile_all()
