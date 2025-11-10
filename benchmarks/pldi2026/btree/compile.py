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

BASE_FLAGS = [
    "-std=c++17",
    "-g",
    "-O3",
    "-Wall",
    "-Wextra",
    "-DUSE_IMM",
    "-I/home/zdh/WorkSpace/GenSym/headers",
    "-lz3",
    "-DENABLE_PROFILE_TIME",
    "-DNO_INFO"
]

TOOL_CONFIG_DEFAULT = [
    "-DNO_REUSE",
]
TOOL_CONFIG_SNAPSHOT_UNIFORMLY = []
TOOL_CONFIG_SNAPSHOT_COST_MODEL = ["-DUSE_COST_MODEL"]
EXTRA_FLAGS = sys.argv[1:]
FLAGS = BASE_FLAGS + TOOL_CONFIG_DEFAULT + EXTRA_FLAGS
SNAPSHOT_FLAGS = BASE_FLAGS + TOOL_CONFIG_SNAPSHOT_UNIFORMLY + EXTRA_FLAGS
SNAPSHOT_COST_MODEL_FLAGS = BASE_FLAGS + TOOL_CONFIG_SNAPSHOT_COST_MODEL + EXTRA_FLAGS

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

        snapshot_out = cpp.with_suffix(".snapshot.exe")
        snapshot_cmd = ["g++", str(cpp), "-o", str(snapshot_out)] + SNAPSHOT_FLAGS
        print("Compiling snapshot version:", cpp.name)
        try:
            print("  Executing command:")
            print("   ", " ".join(snapshot_cmd))
            snapshot_proc = subprocess.run(snapshot_cmd, capture_output=True, text=True)
        except FileNotFoundError:
            print("Error: g++ not found. Install a C++ compiler.")
            sys.exit(1)

        snapshot_cost_model_out = cpp.with_suffix(".costmodel.exe")
        snapshot_cost_model_cmd = ["g++", str(cpp), "-o", str(snapshot_cost_model_out)] + SNAPSHOT_COST_MODEL_FLAGS
        print("Compiling snapshot cost model version:", cpp.name)
        try:
            print("  Executing command:")
            print("   ", " ".join(snapshot_cost_model_cmd))
            snapshot_cost_model_proc = subprocess.run(snapshot_cost_model_cmd, capture_output=True, text=True)
        except FileNotFoundError:
            print("Error: g++ not found. Install a C++ compiler.")
            sys.exit(1)

        compiled_total += 1

        if snapshot_proc.returncode == 0:
            print(f"  Successfully compiled snapshot {cpp.name} to {snapshot_out.name}")
        else:
            print(f"  Failed to compile snapshot {cpp.name}")
            print("  Compiler output:")
            print(snapshot_proc.stdout)
            print(snapshot_proc.stderr)

        if proc.returncode == 0:
            print(f"  Successfully compiled {cpp.name} to {out.name}")
            compiled_success += 1
        else:
            print(f"  Failed to compile {cpp.name}")
            print("  Compiler output:")
            print(proc.stdout)
            print(proc.stderr)
            compiled_failed += 1

        if snapshot_cost_model_proc.returncode == 0:
            print(f"  Successfully compiled snapshot cost model {cpp.name} to {snapshot_cost_model_out.name}")
        else:
            print(f"  Failed to compile snapshot cost model {cpp.name}")
            print("  Compiler output:")
            print(snapshot_cost_model_proc.stdout)
            print(snapshot_cost_model_proc.stderr)

    print()
    print("Overall summary:")
    print(f"  Files compiled: {compiled_total}")
    print(f"  Succeeded: {compiled_success}")
    print(f"  Failed: {compiled_failed}")
    return compiled_total, compiled_success, compiled_failed


if __name__ == "__main__":
    compile_all()
