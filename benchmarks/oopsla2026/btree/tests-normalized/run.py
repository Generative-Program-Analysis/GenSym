#!/usr/bin/env python3
import subprocess
import sys
from pathlib import Path


HERE = Path(__file__).resolve().parent


if __name__ == "__main__":
    target = HERE.parent / "run_exe.py"
    raise SystemExit(subprocess.call([sys.executable, str(target), *sys.argv[1:]]))
