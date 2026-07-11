#!/usr/bin/env python3
import argparse
import fnmatch
import os
import shutil
import subprocess
import sys
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path
from typing import Optional


HERE = Path(__file__).resolve().parent
SKIP_RC = 10


def result(
    name: str,
    rc: int,
    executed: int = 0,
    already_complete: bool = False,
    complete: bool = False,
    skipped_existing: bool = False,
) -> dict[str, object]:
    return {
        "name": name,
        "rc": rc,
        "executed": executed,
        "already_complete": already_complete,
        "complete": complete,
        "skipped_existing": skipped_existing,
    }


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


def wasp_env() -> dict[str, str]:
    env = os.environ.copy()
    opam_prefix = os.getenv("OPAM_SWITCH_PREFIX")
    candidates = []
    if opam_prefix:
        opam_root = Path(opam_prefix)
        candidates += [opam_root / "lib" / "z3", opam_root / "lib"]
    z3_lib_dir = first_dir_containing_glob(candidates, "libz3.*")
    if z3_lib_dir:
        old = env.get("LD_LIBRARY_PATH")
        env["LD_LIBRARY_PATH"] = (
            str(z3_lib_dir) if not old else f"{z3_lib_dir}{os.pathsep}{old}"
        )
    return env


def should_skip_case(path: Path, patterns: list[str]) -> bool:
    for pattern in patterns:
        if (
            fnmatch.fnmatch(path.name, pattern)
            or fnmatch.fnmatch(path.stem, pattern)
            or fnmatch.fnmatch(str(path), pattern)
        ):
            return True
    return False


def matches_case(path: Path, case: str) -> bool:
    return case in {path.name, path.stem}


def count_wasp_reports(workspace: Path) -> int:
    if not workspace.exists():
        return 0
    reports = list(workspace.glob("report_*.json"))
    if reports:
        return len(reports)
    return 1 if (workspace / "report.json").is_file() else 0


def workspace_for(path: Path, workspace_dir: Path | None) -> Path:
    if workspace_dir is None:
        return path.with_suffix("").with_suffix(".out")
    return workspace_dir / f"{path.with_suffix('').name}.out"


def log_path_for(path: Path, workspace_dir: Path | None, run_index: int) -> Path:
    if workspace_dir is None:
        return path.with_suffix(path.suffix + f".run_{run_index}.log")
    return workspace_dir / f"{path.name}.run_{run_index}.log"


def run_one(
    path: Path,
    timeout: int,
    skip_existing: bool,
    runs: int,
    workspace_dir: Path | None,
) -> dict[str, object]:
    base = path.name
    workspace = workspace_for(path, workspace_dir)
    report_path = workspace / "report.json"

    if skip_existing and count_wasp_reports(workspace) > 0:
        print(f"[SKIP] {base}: output already exists and --skip-existing is set.")
        return result(base, SKIP_RC, skipped_existing=True)

    last_rc = 0
    executions = 0
    while True:
        run_index = count_wasp_reports(workspace)
        if run_index >= runs:
            if executions == 0:
                print(
                    f"[COMPLETE] {base}: already has {run_index}/{runs} reports; "
                    "no execution needed."
                )
            else:
                print(
                    f"[COMPLETE] {base}: now has {run_index}/{runs} reports "
                    f"after {executions} execution(s) in this command."
                )
            return result(
                base,
                last_rc,
                executed=executions,
                already_complete=executions == 0,
                complete=True,
            )

        log_path = log_path_for(path, workspace_dir, run_index)
        cmd = ["wasp", str(path)]
        if path.suffix != ".wast":
            cmd.extend(["-e", '(invoke "__original_main")'])
        cmd.extend(
            [
                "--workspace",
                str(workspace),
                "--smt-assume",
                "--timeout",
                str(timeout),
            ]
        )

        print(f"[RUN] {base} run {run_index + 1}/{runs} -> {workspace}")
        print("      " + " ".join(cmd))
        executions += 1

        try:
            proc = subprocess.run(
                cmd,
                stdout=subprocess.PIPE,
                stderr=subprocess.STDOUT,
                text=True,
                timeout=timeout + 10,
                env=wasp_env(),
            )
            output = proc.stdout or ""
            rc = proc.returncode
        except subprocess.TimeoutExpired as exc:
            output = (exc.stdout or "") + f"\n[ERROR] timeout after {timeout}s\n"
            rc = 124
        except Exception as exc:
            output = f"[ERROR] failed to run wasp: {exc}\n"
            rc = 1

        log_path.write_text(output, encoding="utf-8")
        if report_path.is_file():
            shutil.copyfile(report_path, workspace / f"report_{run_index}.json")
        last_rc = rc
        if rc != 0:
            return result(base, rc, executed=executions)


def clean_one(path: Path, workspace_dir: Path | None) -> dict[str, object]:
    base = path.name
    workspace = workspace_for(path, workspace_dir)
    log_parent = workspace_dir if workspace_dir is not None else path.parent
    log_paths = list(log_parent.glob(f"{path.name}.run_*.log"))
    if workspace.exists():
        shutil.rmtree(workspace)
        print(f"[CLEAN] {base} -> {workspace}")
    for log_path in log_paths:
        log_path.unlink()
    return result(base, 0)


def main() -> int:
    parser = argparse.ArgumentParser(
        description="Run wasp on all .wast/.wat files in a target directory."
    )
    parser.add_argument("target_dir", help="Directory containing .wast/.wat files to run")
    parser.add_argument(
        "-j", "--jobs", type=int, default=1, help="number of parallel jobs"
    )
    parser.add_argument(
        "--timeout", type=int, default=900, help="per-file timeout in seconds"
    )
    parser.add_argument("--case", help="run one testcase by filename or stem")
    parser.add_argument("--quick", action="store_true", help="run only the first testcase")
    parser.add_argument("--clean", action="store_true", help="remove selected WASP workspaces and logs")
    parser.add_argument(
        "--skip-existing",
        action="store_true",
        help="skip a test if <stem>.out/report.json already exists",
    )
    parser.add_argument(
        "--workspace-dir",
        help="Directory for WASP workspaces/logs. Defaults to the input directory.",
    )
    parser.add_argument(
        "--runs",
        type=int,
        default=1,
        help="Target number of reports per testcase.",
    )
    parser.add_argument(
        "--skip",
        action="append",
        default=[],
        metavar="PATTERN",
        help="skip testcases matching a filename, stem, or glob pattern; repeatable",
    )
    args = parser.parse_args()

    if args.jobs < 1:
        print(f"Invalid jobs value: {args.jobs}", file=sys.stderr)
        return 2
    if args.timeout < 0:
        print(f"Invalid timeout value: {args.timeout}", file=sys.stderr)
        return 2
    if args.runs < 1:
        print(f"Invalid runs value: {args.runs}", file=sys.stderr)
        return 2

    target_dir = (HERE / args.target_dir).resolve()
    if not target_dir.is_dir():
        print(f"Target directory does not exist: {target_dir}", file=sys.stderr)
        return 2
    workspace_dir = (HERE / args.workspace_dir).resolve() if args.workspace_dir else None
    if workspace_dir is not None:
        workspace_dir.mkdir(parents=True, exist_ok=True)

    input_files = sorted(
        p for p in target_dir.iterdir() if p.is_file() and p.suffix in {".wast", ".wat"}
    )
    if args.case:
        input_files = [p for p in input_files if matches_case(p, args.case)]
    input_files = [p for p in input_files if not should_skip_case(p, args.skip)]
    if args.quick:
        input_files = input_files[:1]
    if not input_files:
        print("No .wast or .wat files found in this directory after filtering.")
        return 1 if args.case else 0

    results: list[dict[str, object]] = []
    if args.clean:
        for path in input_files:
            results.append(clean_one(path, workspace_dir))
    elif args.jobs == 1:
        for path in input_files:
            results.append(
                run_one(path, args.timeout, args.skip_existing, args.runs, workspace_dir)
            )
    else:
        with ThreadPoolExecutor(max_workers=args.jobs) as executor:
            futures = {
                executor.submit(
                    run_one,
                    path,
                    args.timeout,
                    args.skip_existing,
                    args.runs,
                    workspace_dir,
                ): path
                for path in input_files
            }
            for future in as_completed(futures):
                results.append(future.result())

    failed = False
    for item in sorted(results, key=lambda row: str(row["name"])):
        name = item["name"]
        rc = item["rc"]
        if rc == 0:
            print(f"{name}: OK")
        elif rc == SKIP_RC:
            print(f"{name}: SKIP")
        else:
            print(f"{name}: FAIL(rc={rc})")
            failed = True

    if not args.clean:
        selected = len(input_files)
        executed = sum(int(item["executed"]) for item in results)
        already_complete = sum(1 for item in results if item["already_complete"])
        complete = sum(1 for item in results if item["complete"])
        skipped_existing = sum(1 for item in results if item["skipped_existing"])
        print()
        print("Run summary:")
        print(f"  Selected WASP tests: {selected}")
        print(f"  Target reports per test: {args.runs}")
        print(f"  Executions launched this command: {executed}")
        print(f"  Already complete before this command: {already_complete}")
        print(f"  Complete after this command: {complete}/{selected}")
        if skipped_existing:
            print(f"  Skipped by --skip-existing: {skipped_existing}")

    if failed:
        print("\nOne or more runs failed. See corresponding .log files for details.")
        return 1

    print("\nAll runs succeeded.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
