#!/usr/bin/env python3
"""Summarize GenWasym and WASP runtimes for Collection-C buggy cases."""

from __future__ import annotations

import argparse
import csv
import json
import re
import sys
from pathlib import Path


SCRIPT_DIR = Path(__file__).resolve().parent
DEFAULT_ARTIFACTS = SCRIPT_DIR / "Collection-C" / "genwasym-test-artifacts" / "buggy"
DEFAULT_WASP_OUTPUT = SCRIPT_DIR / "Collection-C" / "wasp-test-output" / "buggy"
CONFIGS = ("NoConfig", "Snapshot", "CostModel")

NUMBER = r"([-+]?\d+(?:\.\d+)?(?:[eE][-+]?\d+)?)"
ELAPSED_RE = re.compile(rf"elapsed-seconds:\s*{NUMBER}")
MAIN_LOOP_RE = re.compile(rf"Total time in main loop \(s\):\s*{NUMBER}")
INSTR_RE = re.compile(rf"Total time in instruction execution \(s\):\s*{NUMBER}")
PATHS_RE = re.compile(r"Finished paths:\s*(\d+)")
EXIT_RE = re.compile(r"exit-code:\s*(-?\d+)")
BUG_RE = re.compile(r"Address\s+\d+\s+with\s+width\s+\d+\s+is\s+not\s+in\s+any\s+allocated\s+range\.")


def number(value: object) -> float | None:
    try:
        return float(value)
    except (TypeError, ValueError):
        return None


def parse_log(path: Path) -> dict[str, object]:
    text = path.read_text(errors="replace")

    def match(pattern: re.Pattern[str]) -> float | int | None:
        found = pattern.search(text)
        if found is None:
            return None
        return int(found.group(1)) if pattern in (PATHS_RE, EXIT_RE) else float(found.group(1))

    return {
        "elapsed_s": match(ELAPSED_RE),
        "main_loop_s": match(MAIN_LOOP_RE),
        "instr_exec_s": match(INSTR_RE),
        "paths": match(PATHS_RE),
        "exit_code": match(EXIT_RE),
        "finds_bug": bool(BUG_RE.search(text)),
    }


def parse_wasp_report(path: Path) -> dict[str, object]:
    data = json.loads(path.read_text())
    loop_time = number(data.get("loop_time"))
    solver_time = number(data.get("solver_time"))
    instr_time = None
    if loop_time is not None and solver_time is not None:
        instr_time = loop_time - solver_time
    return {
        "total_s": loop_time,
        "solver_s": solver_time,
        "instr_exec_s": instr_time,
        "paths": data.get("paths_explored"),
        "specification": data.get("specification"),
        "finds_bug": data.get("specification") is False,
    }


def fmt(value: object) -> str:
    if value is None:
        return "-"
    if isinstance(value, str):
        return value
    if isinstance(value, bool):
        return str(value).lower()
    if isinstance(value, int):
        return str(value)
    return f"{float(value):.6f}"


def rows(artifacts: Path, wasp_output: Path, run: int) -> list[dict[str, object]]:
    result: list[dict[str, object]] = []
    for case_dir in sorted(path for path in artifacts.iterdir() if path.is_dir()):
        benchmark = case_dir.name
        row: dict[str, object] = {"Benchmark": benchmark}
        wasp_report = wasp_output / f"{benchmark}.out" / f"report_{run}.json"
        if wasp_report.is_file():
            row.update({f"WASP_{key}": value for key, value in parse_wasp_report(wasp_report).items()})
        else:
            row.update({f"WASP_{key}": None for key in ("total_s", "solver_s", "instr_exec_s", "paths", "specification", "finds_bug")})

        for config in CONFIGS:
            suffix = {"NoConfig": "", "Snapshot": ".snapshot", "CostModel": ".costmodel"}[config]
            log = case_dir / "run-logs" / f"{benchmark}.wat{suffix}.run_{run}.log"
            values = parse_log(log) if log.is_file() else {}
            if config == "NoConfig":
                values["total_s"] = values.get("elapsed_s")
            for key in ("elapsed_s", "main_loop_s", "instr_exec_s", "paths", "exit_code"):
                row[f"GenWasym_{config}_{key}"] = values.get(key)
            if config == "NoConfig":
                row["GenWasym_NoConfig_total_s"] = values.get("total_s")
                row["GenWasym_finds_bug"] = values.get("finds_bug")
        row["Finds Bug"] = (
            f"GenWasym={'yes' if row.get('GenWasym_finds_bug') else 'no'}; "
            f"WASP={'yes' if row.get('WASP_finds_bug') else 'no'}"
        )
        row["n_GenWasymPaths"] = row.get("GenWasym_NoConfig_paths")
        row["n_WASP_paths"] = row.get("WASP_paths")
        row["T_GenWasym_instr_exec_s"] = row.get("GenWasym_NoConfig_instr_exec_s")
        row["T_GenWasym_total_s"] = row.get("GenWasym_NoConfig_total_s")
        row["T_WASP_instr_exec_s"] = row.get("WASP_instr_exec_s")
        row["T_WASP_total_s"] = row.get("WASP_total_s")
        result.append(row)
    return result


def columns() -> list[str]:
    return [
        "Benchmark",
        "n_GenWasymPaths",
        "n_WASP_paths",
        "Finds Bug",
        "T_GenWasym_instr_exec_s",
        "T_GenWasym_total_s",
        "T_WASP_instr_exec_s",
        "T_WASP_total_s",
    ]


def write_csv(path: Path, data: list[dict[str, object]]) -> None:
    fields = columns()
    with path.open("w", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=fields)
        writer.writeheader()
        for row in data:
            writer.writerow({field: fmt(row.get(field)) for field in fields})


def print_markdown(data: list[dict[str, object]]) -> None:
    fields = columns()
    print("| " + " | ".join(fields) + " |")
    print("| " + " | ".join("---" for _ in fields) + " |")
    for row in data:
        print("| " + " | ".join(fmt(row.get(field)) for field in fields) + " |")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--artifacts", type=Path, default=DEFAULT_ARTIFACTS)
    parser.add_argument("--wasp-output", type=Path, default=DEFAULT_WASP_OUTPUT)
    parser.add_argument("--run", type=int, default=0, help="Run/report index to summarize (default: 0).")
    parser.add_argument("-o", "--output", type=Path, help="Write CSV output instead of a Markdown table.")
    args = parser.parse_args()
    if args.run < 0:
        parser.error("--run must be non-negative")
    if not args.artifacts.is_dir():
        print(f"GenWasym artifact directory does not exist: {args.artifacts}", file=sys.stderr)
        return 1
    data = rows(args.artifacts, args.wasp_output, args.run)
    if not data:
        print(f"No buggy benchmark directories found under {args.artifacts}", file=sys.stderr)
        return 1
    if args.output:
        write_csv(args.output, data)
        print(f"Wrote {len(data)} rows to {args.output}")
    else:
        print_markdown(data)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
