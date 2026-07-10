#!/usr/bin/env python3
import subprocess
import sys
from pathlib import Path


def main() -> int:
    here = Path(__file__).resolve().parent
    shared_runner = here.parent.parent / "run.py"
    return subprocess.call(
        [sys.executable, str(shared_runner), "btree/wasp_btree", *sys.argv[1:]]
    )


if __name__ == "__main__":
    raise SystemExit(main())
