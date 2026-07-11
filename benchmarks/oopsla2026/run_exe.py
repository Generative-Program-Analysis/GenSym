#!/usr/bin/env python3
import argparse
import fnmatch
import os
import shutil
import subprocess
import sys
import time
from pathlib import Path
from typing import Optional


HERE = Path(__file__).resolve().parent
REPO_ROOT = HERE.parents[1]


def is_executable(path: Path) -> bool:
    if not path.is_file():
        return False
    return os.access(str(path), os.X_OK) or path.suffix.lower() == ".exe"


def is_snapshot_executable(path: Path) -> bool:
    return path.is_file() and path.name.endswith(".snapshot.exe")


def is_cost_model_executable(path: Path) -> bool:
    return path.is_file() and path.name.endswith(".costmodel.exe")


def split_env_paths(name: str) -> list[Path]:
    value = os.getenv(name)
    if not value:
        return []
    return [Path(part) for part in value.split(os.pathsep) if part]


def first_dir_containing_glob(candidates: list[Path], pattern: str) -> Optional[Path]:
    for candidate in candidates:
        if candidate.is_dir() and any(candidate.glob(pattern)):
            return candidate
    return None


def z3_runtime_env() -> dict[str, str]:
    env = os.environ.copy()
    opam_prefix = os.getenv("OPAM_SWITCH_PREFIX")
    candidates = split_env_paths("Z3_LIB_DIR")
    candidates += [
        HERE.parents[1] / "third-party" / "z3" / "build" / "z3_install" / "lib",
        HERE.parents[1] / "third-party" / "z3" / "build" / "z3_install" / "usr" / "local" / "lib",
    ]
    if opam_prefix:
        opam_root = Path(opam_prefix)
        candidates += [opam_root / "lib" / "z3", opam_root / "lib"]
    candidates += [
        Path("/usr/local/lib"),
        Path("/usr/lib/x86_64-linux-gnu"),
        Path("/usr/lib"),
    ]

    z3_lib_dir = first_dir_containing_glob(candidates, "libz3.so*")
    if z3_lib_dir:
        old = env.get("LD_LIBRARY_PATH")
        env["LD_LIBRARY_PATH"] = (
            str(z3_lib_dir) if not old else f"{z3_lib_dir}{os.pathsep}{old}"
        )
    return env


def filter_targets(targets: list[Path], case: str | None) -> list[Path]:
    if case is None:
        return targets

    filtered = []
    for target in targets:
        target_name = target.name
        base_name = target_name.removesuffix(".costmodel.exe")
        base_name = base_name.removesuffix(".snapshot.exe")
        base_name = base_name.removesuffix(".exe")
        if case in {target_name, base_name}:
            filtered.append(target)
    return filtered


def should_skip_target(target: Path, patterns: list[str]) -> bool:
    target_name = target.name
    base_name = target_name.removesuffix(".costmodel.exe")
    base_name = base_name.removesuffix(".snapshot.exe")
    base_name = base_name.removesuffix(".exe")

    candidates = {
        target_name,
        base_name,
        str(target),
    }

    source_like = Path(base_name)
    candidates.add(source_like.stem)

    for pattern in patterns:
        if any(fnmatch.fnmatch(candidate, pattern) for candidate in candidates):
            return True
    return False


def discover_targets(target_dir: Path) -> list[Path]:
    return sorted(
        (
            path
            for path in target_dir.iterdir()
            if path.is_file() and path.name.endswith(".exe")
        ),
        key=lambda path: path.name,
    )


def output_path_for(target_dir: Path, file_path: Path) -> Path:
    output_root = output_root_for(target_dir)
    file_name = file_path.name.removesuffix(".exe")
    if is_snapshot_executable(file_path):
        return output_root / "Snapshot" / f"{file_name}.output"
    if is_cost_model_executable(file_path):
        return output_root / "CostModel" / f"{file_name}.output"
    return output_root / "NoConfig" / f"{file_name}.output"


def output_root_for(target_dir: Path) -> Path:
    parts = list(target_dir.parts)
    if "genwasym-test-artifacts" not in parts:
        return target_dir
    index = parts.index("genwasym-test-artifacts")
    parts[index] = "genwasym-test-output"
    return Path(*parts)


def count_reports(output_path: Path) -> int:
    if not output_path.exists():
        return 0
    return len(list(output_path.glob("*.json")))


def run_all(
    target_dir: Path,
    targets: list[Path],
    action: str,
    skip_existing: bool = False,
    runs: int = 1,
) -> dict[str, int]:
    stats = {
        "selected": 0,
        "executed": 0,
        "already_complete": 0,
        "failed": 0,
        "skipped_existing": 0,
        "complete_after": 0,
    }

    for file_path in targets:
        if not is_executable(file_path):
            continue

        stats["selected"] += 1
        file_name = file_path.name.removesuffix(".exe")
        output_path = output_path_for(target_dir, file_path)

        if action == "run":
            if skip_existing and output_path.exists():
                stats["skipped_existing"] += 1
                print(
                    f"Skip {file_name}: output already exists and --skip-existing is set."
                )
                continue

            target_executions = 0
            while True:
                num = count_reports(output_path)
                if num >= runs:
                    stats["complete_after"] += 1
                    if target_executions == 0:
                        stats["already_complete"] += 1
                        print(
                            f"Complete {file_name}: already has {num}/{runs} reports; "
                            "no execution needed."
                        )
                    else:
                        print(
                            f"Complete {file_name}: now has {num}/{runs} reports "
                            f"after {target_executions} execution(s) in this command."
                        )
                    break

                env = z3_runtime_env()
                env.update({"OUTPUT_FILE": str(output_path / f"report_{num}.json")})
                log_dir = target_dir / "run-logs"
                log_dir.mkdir(exist_ok=True)
                log_path = log_dir / f"{file_name}.run_{num}.log"
                cmd = [str(file_path.resolve())]

                print(f"Now executing: {file_path}")
                print(f"  Run: {num + 1}/{runs}")
                print(f"  Command: {' '.join(cmd)}")
                print(f"  Output JSON: {env['OUTPUT_FILE']}")
                print(f"  Log: {log_path}")
                start = time.monotonic()
                stats["executed"] += 1
                target_executions += 1
                with log_path.open("w", encoding="utf-8") as log_file:
                    log_file.write(f"$ {' '.join(cmd)}\n")
                    log_file.write(f"cwd: {target_dir}\n")
                    log_file.write(f"OUTPUT_FILE: {env['OUTPUT_FILE']}\n\n")
                    log_file.write("=== OUTPUT ===\n")
                    log_file.flush()
                    proc = subprocess.Popen(
                        cmd,
                        cwd=str(target_dir),
                        stdout=log_file,
                        stderr=subprocess.STDOUT,
                        env=env,
                    )
                    rc = proc.wait()
                    elapsed = time.monotonic() - start
                    log_file.write(
                        f"\n=== EXIT ===\nexit-code: {rc}\nelapsed-seconds: {elapsed:.3f}\n"
                    )
                print(f"  Finished in {elapsed:.3f}s with exit code {rc}")
                if rc != 0:
                    stats["failed"] += 1
                    print(f"{file_name} exited with return code {rc}", file=sys.stderr)
                    break
        elif action == "clean":
            if output_path.exists():
                try:
                    if output_path.is_dir():
                        shutil.rmtree(output_path)
                except Exception as e:
                    print(f"Failed to remove {output_path}: {e}", file=sys.stderr)

    return stats


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "target_dir",
        help="Directory containing the compiled .exe targets to run",
    )
    parser.add_argument(
        "-r",
        "--run-all",
        action="store_true",
        help="Run all compiled executables in target_dir",
    )
    parser.add_argument("--clean", action="store_true")
    parser.add_argument(
        "--case",
        help="Run or clean only a specific case, e.g. 2o1u.wat or 2o1u.wat.exe",
    )
    parser.add_argument(
        "--skip-existing",
        action="store_true",
        help="When running, skip a target if its output directory already exists.",
    )
    parser.add_argument(
        "--runs",
        type=int,
        default=1,
        help="Target number of reports per executable when running.",
    )
    parser.add_argument(
        "--skip",
        action="append",
        default=[],
        metavar="PATTERN",
        help="Skip targets matching a filename, stem, or glob pattern; repeatable.",
    )
    args = parser.parse_args()

    if not args.run_all and not args.clean:
        parser.print_help()
        return 0
    if args.runs < 1:
        print(f"Invalid runs value: {args.runs}", file=sys.stderr)
        return 2

    target_dir = (HERE / args.target_dir).resolve()
    if not target_dir.is_dir():
        print(f"Target directory does not exist: {target_dir}", file=sys.stderr)
        return 2

    targets = discover_targets(target_dir)
    if not targets:
        print(f"No .exe targets found in {target_dir}.", file=sys.stderr)
        return 1

    targets = filter_targets(targets, args.case)
    if args.case and not targets:
        print(f"No targets matched case {args.case!r}.", file=sys.stderr)
        return 1

    targets = [target for target in targets if not should_skip_target(target, args.skip)]
    if args.skip and not targets:
        print("No targets left after applying skip filters.", file=sys.stderr)
        return 1

    if args.clean:
        run_all(target_dir, targets, action="clean")
    elif args.run_all:
        stats = run_all(
            target_dir,
            targets,
            action="run",
            skip_existing=args.skip_existing,
            runs=args.runs,
        )
        print()
        print("Run summary:")
        print(f"  Selected executable tests: {stats['selected']}")
        print(f"  Target reports per test: {args.runs}")
        print(f"  Executions launched this command: {stats['executed']}")
        print(f"  Already complete before this command: {stats['already_complete']}")
        print(f"  Complete after this command: {stats['complete_after']}/{stats['selected']}")
        if stats["skipped_existing"]:
            print(f"  Skipped by --skip-existing: {stats['skipped_existing']}")
        if stats["failed"]:
            print(f"  Failed executions: {stats['failed']}")

    return 0


if __name__ == "__main__":
    sys.exit(main())
