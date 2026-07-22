#!/usr/bin/env python3

import argparse
import fnmatch
import os
import subprocess
import sys
import time
from pathlib import Path
from typing import Optional


BASE_FLAGS = [
    "-std=c++17",
    "-g",
    "-O3",
    "-DNDEBUG",
    "-DUSE_IMM",
    "-DENABLE_PROFILE_TIME",
    "-DNO_INFO",
    "-DBY_COVERAGE",
]
Z3_LINK_FLAG = "-lz3"
SOFT_ASSERT_FLAG = "-DUSE_SOFT_ASSERT"
USE_SOFT_ASSERT_ENV = "GENSYM_USE_SOFT_ASSERT"
WARNING_FLAGS = [
    "-Wall",
    "-Wextra",
]
TOOL_CONFIG_DEFAULT = [
    "-DNO_REUSE",
]
TOOL_CONFIG_SNAPSHOT_UNIFORMLY = []
TOOL_CONFIG_SNAPSHOT_COST_MODEL = ["-DUSE_COST_MODEL"]


def should_skip_case(path: Path, patterns: list[str]) -> bool:
    candidates = {
        path.name,
        path.stem,
        str(path),
    }
    if path.suffix == ".cpp":
        source_like = path.with_suffix("")
        candidates.add(source_like.name)
        candidates.add(source_like.stem)
        candidates.add(str(source_like))

    for pattern in patterns:
        if any(fnmatch.fnmatch(candidate, pattern) for candidate in candidates):
            return True
    return False


def outputs_are_newer_than_input(input_file: Path, output_files: list[Path]) -> bool:
    if any(not output.exists() for output in output_files):
        return False
    input_mtime = input_file.stat().st_mtime
    return all(output.stat().st_mtime > input_mtime for output in output_files)


def collect_cpp_files(target_path: Path) -> tuple[Path, list[Path]]:
    if target_path.is_file():
        if target_path.suffix != ".cpp":
            print(f"Target file must be a .cpp file: {target_path}", file=sys.stderr)
            sys.exit(2)
        return target_path.parent, [target_path]

    if target_path.is_dir():
        return target_path, sorted(target_path.glob("*.cpp"))

    print(f"Target path does not exist: {target_path}", file=sys.stderr)
    sys.exit(2)


def should_use_soft_assert() -> bool:
    value = os.getenv(USE_SOFT_ASSERT_ENV)
    if value is None:
        return True
    return value.lower() not in {"0", "false", "no", "off"}


def run_compile(cmd: list[str], log_path: Path) -> subprocess.CompletedProcess[str]:
    print("  Executing command:")
    print("   ", " ".join(cmd))
    print(f"  Log: {log_path}")
    start = time.monotonic()
    try:
        proc = subprocess.run(cmd, capture_output=True, text=True)
    except FileNotFoundError:
        print("Error: clang++ not found. Install a C++ compiler.")
        sys.exit(1)
    elapsed = time.monotonic() - start
    log_path.write_text(
        "\n".join(
            [
                f"$ {' '.join(cmd)}",
                f"exit-code: {proc.returncode}",
                "",
                "=== STDOUT ===",
                proc.stdout or "",
                "",
                "=== STDERR ===",
                proc.stderr or "",
            ]
        ),
        encoding="utf-8",
    )
    print(f"  Finished in {elapsed:.3f}s with exit code {proc.returncode}")
    return proc


def format_include_path(include_dir: Path, base_dir: Path) -> str:
    relative = Path(os.path.relpath(include_dir, base_dir))
    relative_str = str(relative)
    if relative_str == ".":
        return "."
    if not relative_str.startswith((".", "..")):
        return f"./{relative_str}"
    return relative_str


def split_env_paths(name: str) -> list[Path]:
    value = os.getenv(name)
    if not value:
        return []
    return [Path(part) for part in value.split(os.pathsep) if part]


def first_dir_containing_file(candidates: list[Path], filename: str) -> Optional[Path]:
    for candidate in candidates:
        if (candidate / filename).is_file():
            return candidate
    return None


def first_dir_containing_glob(candidates: list[Path], pattern: str) -> Optional[Path]:
    for candidate in candidates:
        if candidate.is_dir() and any(candidate.glob(pattern)):
            return candidate
    return None


def z3_flags(repo_root: Path, base_dir: Path) -> list[str]:
    opam_prefix = os.getenv("OPAM_SWITCH_PREFIX")
    opam_candidates = []
    if opam_prefix:
        opam_root = Path(opam_prefix)
        opam_candidates = [
            opam_root / "include",
            opam_root / "lib" / "z3",
            opam_root / "lib",
        ]

    include_candidates = (
        split_env_paths("Z3_INCLUDE_DIR")
        + [
            repo_root / "third-party" / "z3" / "build" / "z3_install" / "include",
            repo_root / "third-party" / "z3" / "build" / "z3_install" / "usr" / "local" / "include",
            repo_root / "third-party" / "z3" / "src" / "api" / "c++",
        ]
        + opam_candidates
        + [
            Path("/usr/local/include"),
            Path("/usr/include"),
        ]
    )
    lib_candidates = (
        split_env_paths("Z3_LIB_DIR")
        + [
            repo_root / "third-party" / "z3" / "build" / "z3_install" / "lib",
            repo_root / "third-party" / "z3" / "build" / "z3_install" / "usr" / "local" / "lib",
        ]
        + opam_candidates
        + [
            Path("/usr/local/lib"),
            Path("/usr/lib/x86_64-linux-gnu"),
            Path("/usr/lib"),
        ]
    )

    include_dir = first_dir_containing_file(include_candidates, "z3++.h")
    lib_dir = first_dir_containing_glob(lib_candidates, "libz3.*")
    flags = []

    if include_dir:
        flags.append(f"-I{format_include_path(include_dir.resolve(), base_dir)}")
    else:
        print(
            "Warning: cannot locate z3++.h. Set Z3_INCLUDE_DIR if Z3 is installed elsewhere.",
            file=sys.stderr,
        )

    if lib_dir:
        lib_flag = str(lib_dir.resolve())
        flags += [f"-L{lib_flag}", f"-Wl,-rpath,{lib_flag}"]
    else:
        print(
            "Warning: cannot locate libz3. Set Z3_LIB_DIR if Z3 is installed elsewhere.",
            file=sys.stderr,
        )

    flags.append(Z3_LINK_FLAG)
    return flags


def immer_flags(repo_root: Path, base_dir: Path) -> list[str]:
    candidates = split_env_paths("IMMER_INCLUDE_DIR") + [
        repo_root / "third-party" / "immer",
        repo_root / "GenSym" / "third-party" / "immer",
    ]
    include_dir = first_dir_containing_file(candidates, "immer/vector_transient.hpp")
    if not include_dir:
        print(
            "Warning: cannot locate immer headers. Set IMMER_INCLUDE_DIR if they are installed elsewhere.",
            file=sys.stderr,
        )
        return []
    return [f"-I{format_include_path(include_dir.resolve(), base_dir)}"]


def compile_all(
    target_path: Path,
    flags: list[str],
    snapshot_flags: list[str],
    snapshot_cost_model_flags: list[str],
    skip_newer: bool = False,
    skip_patterns: list[str] | None = None,
) -> tuple[int, int, int]:
    target_dir, cpp_files = collect_cpp_files(target_path)
    skip_patterns = skip_patterns or []

    if not cpp_files:
        print(f"No .cpp files found in {target_dir}")
        return 0, 0, 0

    compiled_total = 0
    compiled_success = 0
    compiled_failed = 0
    variant_failed = 0

    for cpp in cpp_files:
        if should_skip_case(cpp, skip_patterns):
            print(f"Skipping {cpp.name}: matched skip pattern.")
            continue

        out = cpp.with_suffix(".exe")
        snapshot_out = cpp.with_suffix(".snapshot.exe")
        snapshot_cost_model_out = cpp.with_suffix(".costmodel.exe")
        log_dir = cpp.parent / "compile-logs"
        log_dir.mkdir(exist_ok=True)

        if skip_newer and outputs_are_newer_than_input(
            cpp, [out, snapshot_out, snapshot_cost_model_out]
        ):
            print(f"Skipping {cpp.name}: outputs are newer than input.")
            continue

        print("Compiling:", cpp.name)
        proc = run_compile(
            ["clang++", str(cpp), "-o", str(out)] + flags,
            log_dir / f"{cpp.name}.compile.log",
        )

        print("Compiling snapshot version:", cpp.name)
        snapshot_proc = run_compile(
            ["clang++", str(cpp), "-o", str(snapshot_out)] + snapshot_flags,
            log_dir / f"{cpp.name}.snapshot.compile.log",
        )

        print("Compiling snapshot cost model version:", cpp.name)
        snapshot_cost_model_proc = run_compile(
            ["clang++", str(cpp), "-o", str(snapshot_cost_model_out)]
            + snapshot_cost_model_flags,
            log_dir / f"{cpp.name}.costmodel.compile.log",
        )

        compiled_total += 1

        if snapshot_proc.returncode == 0:
            print(f"  Successfully compiled snapshot {cpp.name} to {snapshot_out.name}")
        else:
            variant_failed += 1
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
            variant_failed += 1

        if snapshot_cost_model_proc.returncode == 0:
            print(
                f"  Successfully compiled snapshot cost model {cpp.name} to "
                f"{snapshot_cost_model_out.name}"
            )
        else:
            variant_failed += 1
            print(f"  Failed to compile snapshot cost model {cpp.name}")
            print("  Compiler output:")
            print(snapshot_cost_model_proc.stdout)
            print(snapshot_cost_model_proc.stderr)

    print()
    print("Overall summary:")
    print(f"  Files compiled: {compiled_total}")
    print(f"  Succeeded: {compiled_success}")
    print(f"  Failed: {compiled_failed}")
    failed_variants = variant_failed
    return compiled_total, compiled_success, failed_variants


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "target_dir",
        help="Directory containing .cpp files to compile, or a single .cpp file.",
    )
    parser.add_argument(
        "--skip-newer",
        action="store_true",
        help="Skip compilation when all output executables are newer than the input .cpp file.",
    )
    parser.add_argument(
        "--warnings",
        action="store_true",
        help="Enable -Wall and -Wextra.",
    )
    parser.add_argument(
        "--skip",
        action="append",
        default=[],
        metavar="PATTERN",
        help="Skip inputs matching a filename, stem, or glob pattern; repeatable.",
    )
    args, extra_flags = parser.parse_known_args()

    target_path = Path(args.target_dir).resolve()
    repo_root = Path(__file__).resolve().parents[2]
    include_dir = repo_root / "headers"
    include_flag = format_include_path(include_dir, Path.cwd())
    base_flags = BASE_FLAGS.copy()
    if should_use_soft_assert():
        base_flags.append(SOFT_ASSERT_FLAG)
    if args.warnings:
        base_flags += WARNING_FLAGS
    base_flags.append(f"-I{include_flag}")
    base_flags += immer_flags(repo_root, Path.cwd())
    base_flags += z3_flags(repo_root, Path.cwd())

    compiled_total, compiled_success, compiled_failed = compile_all(
        target_path=target_path,
        flags=base_flags + TOOL_CONFIG_DEFAULT + extra_flags,
        snapshot_flags=base_flags + TOOL_CONFIG_SNAPSHOT_UNIFORMLY + extra_flags,
        snapshot_cost_model_flags=(
            base_flags + TOOL_CONFIG_SNAPSHOT_COST_MODEL + extra_flags
        ),
        skip_newer=args.skip_newer,
        skip_patterns=args.skip,
    )
    if compiled_total == 0:
        return 0
    return 0 if compiled_success == compiled_total and compiled_failed == 0 else 1


if __name__ == "__main__":
    raise SystemExit(main())
