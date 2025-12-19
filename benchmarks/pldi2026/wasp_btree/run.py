from pathlib import Path
import subprocess
import argparse
import sys

#!/usr/bin/env python3
import concurrent.futures

def run_wast(path: Path, timeout: float | None):
        log_path = path.with_suffix(path.suffix + ".log")
        cmd = ["wasp", str(path), "--workspace", str(path.parent / f"{path.stem}.out")]
        try:
                proc = subprocess.run(cmd, capture_output=True, text=True, timeout=timeout)
                out = proc.stdout
                err = proc.stderr
                rc = proc.returncode
        except subprocess.TimeoutExpired as e:
                out = e.stdout or ""
                err = (e.stderr or "") + f"\n[ERROR] timeout after {timeout}s"
                rc = 124
        except Exception as e:
                out = ""
                err = f"[ERROR] failed to run wasp: {e}"
                rc = 1

        with open(log_path, "w", encoding="utf-8") as f:
                f.write(f"$ {' '.join(cmd)}\n\n")
                f.write("=== STDOUT ===\n")
                f.write(out or "")
                f.write("\n\n=== STDERR ===\n")
                f.write(err or "")

        return path.name, rc

def main():
        parser = argparse.ArgumentParser(description="Run wasp on all .wast files in the current directory.")
        parser.add_argument("-j", "--jobs", type=int, default=1, help="number of parallel jobs (default 1)")
        parser.add_argument("--timeout", type=float, default=None, help="per-file timeout in seconds")
        args = parser.parse_args()

        cwd = Path.cwd()
        wast_files = sorted([p for p in cwd.iterdir() if p.is_file() and p.suffix == ".wast"])
        if not wast_files:
                print("No .wast files found in the current directory.")
                return

        results = []
        if args.jobs == 1:
                for p in wast_files:
                        results.append(run_wast(p, args.timeout))
        else:
                with concurrent.futures.ThreadPoolExecutor(max_workers=args.jobs) as exe:
                        futures = {exe.submit(run_wast, p, args.timeout): p for p in wast_files}
                        for fut in concurrent.futures.as_completed(futures):
                                results.append(fut.result())

        failed = [(name, rc) for (name, rc) in results if rc != 0]
        for name, rc in results:
                status = "OK" if rc == 0 else f"FAIL(rc={rc})"
                print(f"{name}: {status}")

        if failed:
                print(f"\n{len(failed)} failed. See corresponding .log files for details.")
                sys.exit(1)
        else:
                print("\nAll runs succeeded.")
                sys.exit(0)

if __name__ == "__main__":
        main()