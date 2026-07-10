#!/usr/bin/env python3

from __future__ import annotations

import argparse
import csv
import json
import statistics
from pathlib import Path


HERE = Path(__file__).resolve().parent


def default_output_path(suite: str) -> Path:
    return HERE / f"final_results_{suite}.csv"


def default_raw_output_path(suite: str) -> Path:
    return HERE / f"raw_results_{suite}.csv"


def benchmark_name_from_genwasym_dir(path: Path) -> str:
    name = path.name
    for suffix in (".wat.output", ".wat.snapshot.output", ".wat.costmodel.output"):
        if name.endswith(suffix):
            return name[: -len(suffix)]
    return name


def prefixed_benchmark_name(prefix: Path, benchmark: str) -> str:
    if prefix == Path("."):
        return benchmark
    return "/".join(prefix.parts + (benchmark,))


def should_ignore_benchmark(prefix: Path) -> bool:
    return "bugs" in prefix.parts


def wasp_result_root(suite: str) -> Path:
    if suite == "btree":
        return HERE / "btree" / "wasp_btree"
    if suite == "Collection-C":
        return HERE / suite / "test-original"
    return HERE / suite / "tests-denormalized"


def report_files(output_dir: Path) -> list[Path]:
    numbered = sorted(output_dir.glob("report_*.json"))
    if numbered:
        return numbered
    default = output_dir / "report.json"
    return [default] if default.is_file() else []


def run_index(report_file: Path) -> int | None:
    stem = report_file.stem
    if stem == "report":
        return None
    if stem.startswith("report_"):
        try:
            return int(stem.removeprefix("report_"))
        except ValueError:
            return None
    return None


def read_genwasym_rows(suite: str) -> list[dict[str, object]]:
    rows: list[dict[str, object]] = []
    suite_root = HERE / suite / "tests-normalized"
    config_names = ("NoConfig", "Snapshot", "CostModel")

    for config in config_names:
        for config_dir in sorted(
            p for p in suite_root.rglob(config) if p.is_dir()
        ):
            benchmark_prefix = config_dir.parent.relative_to(suite_root)
            if should_ignore_benchmark(benchmark_prefix):
                continue
            for output_dir in sorted(p for p in config_dir.iterdir() if p.is_dir()):
                benchmark = prefixed_benchmark_name(
                    benchmark_prefix, benchmark_name_from_genwasym_dir(output_dir)
                )
                for report_file in report_files(output_dir):
                    with report_file.open(encoding="utf-8") as handle:
                        data = json.load(handle)

                    profile = data.get("profile_summary", {})
                    solver_time = profile.get("total_time_solver_s")
                    resuming_time = profile.get("total_time_resuming_from_snapshot_s")
                    cost_model_time = profile.get("total_time_counting_symbolic_size_s")
                    loop_time = profile.get("total_time_main_loop_s")
                    instr_time = None
                    if loop_time is not None and solver_time is not None:
                        instr_time = loop_time - solver_time

                    rows.append(
                        {
                            "Suite": suite,
                            "Engine": "GenWasym",
                            "Config": config,
                            "Benchmark": benchmark,
                            "Run": run_index(report_file),
                            "InstrTime(s)": instr_time,
                            "SolverTime(s)": solver_time,
                            "ResumingTime(s)": resuming_time,
                            "CostModelTime(s)": cost_model_time,
                            "LoopTime(s)": loop_time,
                            "PathsExplored": data.get("finished_count"),
                            "FailedCount": data.get("failed_count"),
                        }
                    )

    return rows


def read_wasp_rows(suite: str) -> list[dict[str, object]]:
    rows: list[dict[str, object]] = []
    wasp_root = wasp_result_root(suite)
    if not wasp_root.exists():
        return rows

    for output_dir in sorted(p for p in wasp_root.rglob("*.out") if p.is_dir()):
        reports = report_files(output_dir)
        if not reports:
            continue
        for report_file in reports:
            benchmark_prefix = output_dir.parent.relative_to(wasp_root)
            if should_ignore_benchmark(benchmark_prefix):
                continue
            with report_file.open(encoding="utf-8") as handle:
                data = json.load(handle)

            def require_float(value: object, field: str) -> float:
                if isinstance(value, (int, float)):
                    return float(value)
                if isinstance(value, str):
                    try:
                        return float(value)
                    except ValueError as exc:
                        raise ValueError(
                            f"Invalid float for {field} in {report_file}: {value!r}"
                        ) from exc
                raise ValueError(
                    f"Invalid type for {field} in {report_file}: {type(value).__name__}"
                )

            solver_time = require_float(data.get("solver_time"), "solver_time")
            loop_time = require_float(data.get("loop_time"), "loop_time")
            instr_time = loop_time - solver_time

            rows.append(
                {
                    "Suite": f"wasp_{suite}",
                    "Engine": "WASP",
                    "Config": "Default",
                    "Benchmark": prefixed_benchmark_name(
                        benchmark_prefix,
                        output_dir.name.removesuffix(".out"),
                    ),
                    "Run": run_index(report_file),
                    "InstrTime(s)": instr_time,
                    "SolverTime(s)": solver_time,
                    "LoopTime(s)": loop_time,
                    "PathsExplored": data.get("paths_explored"),
                    "SolverCounter": data.get("solver_counter"),
                    "InstructionCounter": data.get("instruction_counter"),
                    "Specification": data.get("specification"),
                    "Incomplete": data.get("incomplete"),
                }
            )

    return rows


def aggregate_rows(rows: list[dict[str, object]], suite: str) -> list[dict[str, object]]:
    grouped: dict[tuple[str, str, str], list[dict[str, object]]] = {}
    for row in rows:
        grouped.setdefault(
            (str(row["Benchmark"]), str(row["Engine"]), str(row["Config"])),
            [],
        ).append(row)

    averaged_rows: list[dict[str, object]] = []
    numeric_keys = [
        "InstrTime(s)",
        "SolverTime(s)",
        "ResumingTime(s)",
        "CostModelTime(s)",
        "LoopTime(s)",
        "PathsExplored",
        "FailedCount",
        "SolverCounter",
        "InstructionCounter",
    ]

    for (benchmark, engine, config), group in sorted(grouped.items()):
        out: dict[str, object] = {
            "Suite": suite,
            "Engine": engine,
            "Config": config,
            "Benchmark": benchmark,
            "Runs": len(group),
        }
        for key in numeric_keys:
            values = [
                float(row[key])
                for row in group
                if row.get(key) is not None and row.get(key) != ""
            ]
            if not values:
                continue
            out[f"{key}_mean"] = statistics.fmean(values)
            out[f"{key}_stdev"] = statistics.stdev(values) if len(values) > 1 else 0.0
        for key in ("Specification", "Incomplete"):
            values = [row.get(key) for row in group if row.get(key) is not None]
            if values:
                out[key] = values[-1]
        averaged_rows.append(out)

    aggregated: dict[str, dict[str, object]] = {}

    def add_prefixed(row_out: dict[str, object], prefix: str, src: dict[str, object], keys: list[str]) -> None:
        for key in keys:
            row_out[f"{prefix}_{key}"] = src.get(key)

    for row in averaged_rows:
        benchmark = str(row["Benchmark"])
        out = aggregated.setdefault(
            benchmark,
            {
                "Benchmark": benchmark,
                "Suite": suite,
            },
        )

        if row["Engine"] == "GenWasym":
            prefix = f"GenWasym_{row['Config']}"
            add_prefixed(
                out,
                prefix,
                row,
                [
                    "Runs",
                    "InstrTime(s)_mean",
                    "InstrTime(s)_stdev",
                    "SolverTime(s)_mean",
                    "SolverTime(s)_stdev",
                    "ResumingTime(s)_mean",
                    "ResumingTime(s)_stdev",
                    "CostModelTime(s)_mean",
                    "CostModelTime(s)_stdev",
                    "LoopTime(s)_mean",
                    "LoopTime(s)_stdev",
                    "PathsExplored_mean",
                    "FailedCount_mean",
                ],
            )
        elif row["Engine"] == "WASP":
            add_prefixed(
                out,
                "WASP",
                row,
                [
                    "Runs",
                    "InstrTime(s)_mean",
                    "InstrTime(s)_stdev",
                    "SolverTime(s)_mean",
                    "SolverTime(s)_stdev",
                    "LoopTime(s)_mean",
                    "LoopTime(s)_stdev",
                    "PathsExplored_mean",
                    "SolverCounter_mean",
                    "InstructionCounter_mean",
                    "Specification",
                    "Incomplete",
                ],
            )

    return [aggregated[name] for name in sorted(aggregated)]


def write_rows(rows: list[dict[str, object]], output_path: Path) -> None:
    fieldnames: list[str] = []
    for row in rows:
        for key in row:
            if key not in fieldnames:
                fieldnames.append(key)

    with output_path.open("w", newline="", encoding="utf-8") as handle:
        writer = csv.DictWriter(handle, fieldnames=fieldnames)
        writer.writeheader()
        for row in rows:
            writer.writerow(row)


def main() -> int:
    parser = argparse.ArgumentParser(
        description="Collect final benchmark results from raw JSON outputs."
    )
    parser.add_argument(
        "-o",
        "--output",
        type=Path,
        default=None,
        help="Final summary CSV path. Defaults to benchmarks/oopsla2026/final_results_<suite>.csv.",
    )
    parser.add_argument(
        "--raw-output",
        type=Path,
        default=None,
        help="Optional raw per-run CSV path. Defaults to benchmarks/oopsla2026/raw_results_<suite>.csv when --write-raw is set.",
    )
    parser.add_argument(
        "--write-raw",
        action="store_true",
        help="Also write one raw CSV row per report file.",
    )
    parser.add_argument(
        "--suite",
        type=str,
        default="btree",
        help="Benchmark suite directory under the root to collect from. Defaults to btree.",
    )
    args = parser.parse_args()

    suite = args.suite
    output = (args.output if args.output is not None else default_output_path(suite)).resolve()

    rows = read_genwasym_rows(suite)
    rows.extend(read_wasp_rows(suite))
    aggregated_rows = aggregate_rows(rows, suite)

    if args.write_raw:
        raw_output = (
            args.raw_output if args.raw_output is not None else default_raw_output_path(suite)
        ).resolve()
        write_rows(rows, raw_output)
        print(f"Wrote {len(rows)} raw rows to {raw_output}")

    write_rows(aggregated_rows, output)
    print(f"Wrote {len(aggregated_rows)} final rows to {output}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
