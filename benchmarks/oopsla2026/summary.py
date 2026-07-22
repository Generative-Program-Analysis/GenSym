#!/usr/bin/env python3

from __future__ import annotations

import argparse
import csv
import statistics
from pathlib import Path


HERE = Path(__file__).resolve().parent


def parse_float(row: dict[str, str], key: str) -> float | None:
    value = row.get(key, "")
    if value == "":
        return None
    try:
        return float(value)
    except ValueError:
        return None


def final_result_path(suite: str) -> Path:
    return HERE / f"final_results_{suite}.csv"


def read_rows(suites: list[str]) -> list[dict[str, str]]:
    rows: list[dict[str, str]] = []
    for suite in suites:
        path = final_result_path(suite)
        if not path.is_file():
            raise SystemExit(f"Missing final result file: {path}")
        with path.open(newline="", encoding="utf-8") as handle:
            rows.extend(csv.DictReader(handle))
    return rows


def available_suites() -> list[str]:
    suites = []
    for path in sorted(HERE.glob("final_results_*.csv")):
        suite = path.stem.removeprefix("final_results_")
        if "." not in suite:
            suites.append(suite)
    return suites


def default_output_path(suites: list[str], rq: str) -> Path:
    if len(suites) == 1:
        input_path = final_result_path(suites[0])
        return input_path.with_name(f"{input_path.stem}.{rq}{input_path.suffix}")
    return HERE / f"final_results.{rq}.csv"


def mean(values: list[float]) -> float | None:
    return statistics.fmean(values) if values else None


def geometric_mean(values: list[float]) -> float | None:
    return statistics.geometric_mean(values) if values else None


def fmt(value: float | None, digits: int = 2) -> str:
    if value is None:
        return "-"
    return f"{value:.{digits}f}"


def fmt_time(value: float | None) -> str:
    if value is None:
        return "-"
    if abs(value) < 1.0:
        return f"{value:.4f}"
    return f"{value:.2f}"


def fmt_metric(row: dict[str, str], prefix: str, suffix: str = "average") -> str:
    return fmt_time(parse_float(row, f"{prefix}_{suffix}"))


def print_table(headers: list[str], rows: list[list[str]]) -> None:
    widths = [len(header) for header in headers]
    for row in rows:
        for index, cell in enumerate(row):
            widths[index] = max(widths[index], len(cell))

    print("  ".join(header.ljust(widths[index]) for index, header in enumerate(headers)))
    print("  ".join("-" * width for width in widths))
    for row in rows:
        print("  ".join(cell.ljust(widths[index]) for index, cell in enumerate(row)))


def write_summary_csv(headers: list[str], rows: list[list[str]], output_path: Path) -> None:
    output_path.parent.mkdir(parents=True, exist_ok=True)
    with output_path.open("w", newline="", encoding="utf-8") as handle:
        writer = csv.writer(handle)
        writer.writerow(headers)
        writer.writerows(rows)


def summarize_compilation(rows: list[dict[str, str]]) -> tuple[list[str], list[list[str]]]:
    complete_rows = []
    configs = ("NoConfig", "Snapshot", "CostModel")
    speedups = []
    path_matches = 0
    headers = [
        "Suite",
        "Benchmark",
        "npaths",
        "T_WASP_instr_exec(s)",
        "SD_WASP_instr_exec(s)",
        "T_WASP_total(s)",
        "T_GenWasym_NoConfig_instr_exec(s)",
        "SD_GenWasym_NoConfig_instr_exec(s)",
        "T_GenWasym_NoConfig_total(s)",
        "Speedup_NoConfig",
        "T_GenWasym_Snapshot_instr_exec(s)",
        "SD_GenWasym_Snapshot_instr_exec(s)",
        "T_GenWasym_Snapshot_total(s)",
        "T_GenWasym_CostModel_instr_exec(s)",
        "SD_GenWasym_CostModel_instr_exec(s)",
        "T_GenWasym_CostModel_total(s)",
        "SpeedupSnapshot",
        "SpeedupHeuristic",
    ]

    for row in rows:
        wasp_instr_time = parse_float(row, "WASP_InstrTime(s)_average")
        wasp_total_time = parse_float(row, "WASP_LoopTime(s)_average")
        genwasym_paths = parse_float(row, "GenWasym_NoConfig_PathsExplored_average")
        wasp_paths = parse_float(row, "WASP_PathsExplored_average")
        genwasym = {
            config: (
                parse_float(row, f"GenWasym_{config}_InstrTime(s)_average"),
                parse_float(row, f"GenWasym_{config}_LoopTime(s)_average"),
            )
            for config in configs
        }
        if (
            wasp_instr_time is None
            or wasp_total_time is None
            or any(
                instr_time is None or total_time is None or instr_time <= 0
                for instr_time, total_time in genwasym.values()
            )
        ):
            continue

        no_config_speedup = wasp_instr_time / genwasym["NoConfig"][0]
        snapshot_speedup = genwasym["NoConfig"][0] / genwasym["Snapshot"][0]
        heuristic_speedup = genwasym["NoConfig"][0] / genwasym["CostModel"][0]
        speedups.append(no_config_speedup)
        if genwasym_paths is not None and wasp_paths is not None and genwasym_paths == wasp_paths:
            path_matches += 1
        npaths = genwasym_paths if genwasym_paths is not None else wasp_paths
        result = [
            row.get("Suite", ""),
            row.get("Benchmark", ""),
            fmt(npaths, 0),
            fmt_metric(row, "WASP_InstrTime(s)"),
            fmt_metric(row, "WASP_InstrTime(s)", "stdev"),
            fmt_time(wasp_total_time),
        ]
        for config in configs:
            result.extend(
                [
                    fmt_metric(row, f"GenWasym_{config}_InstrTime(s)"),
                    fmt_metric(row, f"GenWasym_{config}_InstrTime(s)", "stdev"),
                    fmt_time(genwasym[config][1]),
                ]
            )
            if config == "NoConfig":
                result.append(fmt(no_config_speedup))
        result.extend([fmt(snapshot_speedup), fmt(heuristic_speedup)])
        complete_rows.append(result)

    print("RQ: compilation")
    print(f"Benchmarks with both GenWasym and WASP results: {len(complete_rows)}")
    print(f"Mean WASP/GenWasym NoConfig instruction-time ratio: {fmt(mean(speedups))}x")
    print(
        "Geomean WASP/GenWasym NoConfig instruction-time ratio: "
        f"{fmt(geometric_mean(speedups))}x"
    )
    print(f"Path-count matches: {path_matches}/{len(complete_rows)}")
    print()
    print_table(headers, complete_rows)
    return headers, complete_rows


def summarize_heuristic(rows: list[dict[str, str]]) -> tuple[list[str], list[list[str]]]:
    table_rows = []
    snapshot_speedups = []
    heuristic_speedups = []
    all_speedups = []
    snapshot_wins = 0
    heuristic_wins = 0
    all_wins = 0
    headers = [
        "Suite",
        "Benchmark",
        "NoConfigInstr(s)",
        "SD_NoConfigInstr(s)",
        "SnapshotInstr(s)",
        "SD_SnapshotInstr(s)",
        "CostModelInstr(s)",
        "SD_CostModelInstr(s)",
        "SpeedupSnapshot",
        "SpeedupHeuristic",
        "SpeedupAll",
    ]

    for row in rows:
        no_config = parse_float(row, "GenWasym_NoConfig_InstrTime(s)_average")
        snapshot = parse_float(row, "GenWasym_Snapshot_InstrTime(s)_average")
        cost_model = parse_float(row, "GenWasym_CostModel_InstrTime(s)_average")
        if (
            no_config is None
            or snapshot is None
            or cost_model is None
            or no_config <= 0
            or snapshot <= 0
            or cost_model <= 0
        ):
            continue

        snapshot_speedup = no_config / snapshot
        heuristic_speedup = snapshot / cost_model
        all_speedup = snapshot_speedup * heuristic_speedup
        snapshot_speedups.append(snapshot_speedup)
        heuristic_speedups.append(heuristic_speedup)
        all_speedups.append(all_speedup)
        if snapshot_speedup > 1:
            snapshot_wins += 1
        if heuristic_speedup > 1:
            heuristic_wins += 1
        if all_speedup > 1:
            all_wins += 1

        table_rows.append(
            [
                row.get("Suite", ""),
                row.get("Benchmark", ""),
                fmt_metric(row, "GenWasym_NoConfig_InstrTime(s)"),
                fmt_metric(row, "GenWasym_NoConfig_InstrTime(s)", "stdev"),
                fmt_metric(row, "GenWasym_Snapshot_InstrTime(s)"),
                fmt_metric(row, "GenWasym_Snapshot_InstrTime(s)", "stdev"),
                fmt_metric(row, "GenWasym_CostModel_InstrTime(s)"),
                fmt_metric(row, "GenWasym_CostModel_InstrTime(s)", "stdev"),
                fmt(snapshot_speedup),
                fmt(heuristic_speedup),
                fmt(all_speedup),
            ]
        )

    print("RQ: heuristic")
    print(f"Benchmarks with all GenWasym heuristic results: {len(table_rows)}")
    print(
        f"Snapshot wins: {snapshot_wins}/{len(snapshot_speedups)}; "
        f"mean NoConfig/Snapshot ratio: {fmt(mean(snapshot_speedups))}x"
    )
    print(
        "Geomean NoConfig/Snapshot ratio: "
        f"{fmt(geometric_mean(snapshot_speedups))}x"
    )
    print(
        f"Heuristic wins: {heuristic_wins}/{len(heuristic_speedups)}; "
        f"mean Snapshot/CostModel ratio: {fmt(mean(heuristic_speedups))}x"
    )
    print(
        "Geomean Snapshot/CostModel ratio: "
        f"{fmt(geometric_mean(heuristic_speedups))}x"
    )
    print(
        f"Combined wins: {all_wins}/{len(all_speedups)}; "
        f"mean combined speedup: {fmt(mean(all_speedups))}x"
    )
    print(f"Geomean combined speedup: {fmt(geometric_mean(all_speedups))}x")
    print()
    print_table(headers, table_rows)
    return headers, table_rows


def summarize_collection(rows: list[dict[str, str]]) -> tuple[list[str], list[list[str]]]:
    module_order = (
        "array",
        "list",
        "slist",
        "ring_buffer",
        "queue",
        "treeset",
        "treetable",
        "deque",
    )
    configs = {
        "WASP": "WASP",
        "noreuse": "GenWasym_NoConfig",
        "snapshot": "GenWasym_Snapshot",
        "heuristic": "GenWasym_CostModel",
    }
    required = [
        f"{prefix}_InstrTime(s)_average"
        for prefix in configs.values()
    ]
    grouped: dict[str, list[dict[str, str]]] = {module: [] for module in module_order}
    path_rows: list[list[str]] = []

    for row in rows:
        benchmark_parts = row.get("Benchmark", "").split("/")
        if len(benchmark_parts) < 2 or benchmark_parts[0] != "normal":
            continue
        module = benchmark_parts[1]
        if module not in grouped:
            continue

        genwasym_paths = parse_float(
            row, "GenWasym_NoConfig_PathsExplored_average"
        )
        wasp_paths = parse_float(row, "WASP_PathsExplored_average")
        path_match = (
            genwasym_paths is not None
            and wasp_paths is not None
            and genwasym_paths == wasp_paths
        )
        path_rows.append(
            [
                row.get("Benchmark", ""),
                fmt(genwasym_paths, 0),
                fmt(wasp_paths, 0),
                "yes" if path_match else "no" if genwasym_paths is not None and wasp_paths is not None else "-",
            ]
        )

        if any(parse_float(row, key) is None for key in required):
            continue
        grouped[module].append(row)

    headers = [
        "Module",
        "n_i",
        "n_paths_GenWasym",
        "n_paths_WASP",
        "PathCountMatch",
        "T_WASP_exec(s)",
        "SD_WASP_exec(s)",
        "T_WASP_total(s)",
        "T_GenWasym_noreuse_exec(s)",
        "SD_GenWasym_noreuse_exec(s)",
        "T_GenWasym_noreuse_total(s)",
        "T_GenWasym_snapshot_exec(s)",
        "SD_GenWasym_snapshot_exec(s)",
        "T_GenWasym_snapshot_total(s)",
        "T_GenWasym_heuristic_exec(s)",
        "SD_GenWasym_heuristic_exec(s)",
        "T_GenWasym_heuristic_total(s)",
        "RQ1",
        "RQ2",
        "RQ3",
    ]

    table_rows = []
    for module in module_order:
        module_rows = grouped[module]
        if not module_rows:
            continue

        metrics: dict[str, tuple[float, float, float]] = {}
        for name, prefix in configs.items():
            exec_values = [
                parse_float(row, f"{prefix}_InstrTime(s)_average") or 0.0
                for row in module_rows
            ]
            exec_stdevs = [
                parse_float(row, f"{prefix}_InstrTime(s)_stdev") or 0.0
                for row in module_rows
            ]
            total_values = [
                parse_float(row, f"{prefix}_LoopTime(s)_average") or 0.0
                for row in module_rows
            ]
            metrics[name] = (
                sum(exec_values),
                sum(value * value for value in exec_stdevs) ** 0.5,
                sum(total_values),
            )

        noreuse_exec = metrics["noreuse"][0]
        snapshot_exec = metrics["snapshot"][0]
        heuristic_exec = metrics["heuristic"][0]
        baseline_exec = metrics["WASP"][0]
        genwasym_path_values = [
            parse_float(row, "GenWasym_NoConfig_PathsExplored_average")
            for row in module_rows
            if parse_float(row, "GenWasym_NoConfig_PathsExplored_average") is not None
        ]
        wasp_path_values = [
            parse_float(row, "WASP_PathsExplored_average")
            for row in module_rows
            if parse_float(row, "WASP_PathsExplored_average") is not None
        ]
        genwasym_paths = sum(genwasym_path_values) if genwasym_path_values else None
        wasp_paths = sum(wasp_path_values) if wasp_path_values else None
        path_match = (
            genwasym_paths is not None
            and wasp_paths is not None
            and genwasym_paths == wasp_paths
        )
        module_name = "".join(part.capitalize() for part in module.split("_"))
        table_rows.append(
            [
                module_name,
                fmt(len(module_rows), 0),
                fmt(genwasym_paths, 0),
                fmt(wasp_paths, 0),
                "yes" if path_match else "no" if genwasym_paths is not None and wasp_paths is not None else "-",
                fmt_time(metrics["WASP"][0]),
                fmt_time(metrics["WASP"][1]),
                fmt_time(metrics["WASP"][2]),
                fmt_time(metrics["noreuse"][0]),
                fmt_time(metrics["noreuse"][1]),
                fmt_time(metrics["noreuse"][2]),
                fmt_time(metrics["snapshot"][0]),
                fmt_time(metrics["snapshot"][1]),
                fmt_time(metrics["snapshot"][2]),
                fmt_time(metrics["heuristic"][0]),
                fmt_time(metrics["heuristic"][1]),
                fmt_time(metrics["heuristic"][2]),
                fmt(baseline_exec / noreuse_exec),
                fmt(noreuse_exec / snapshot_exec),
                fmt(snapshot_exec / heuristic_exec),
            ]
        )

    print("RQ: Collection-C")
    print("Path counts by benchmark")
    print_table(
        ["Benchmark", "GenWasymPaths", "WASPPaths", "Match"],
        sorted(path_rows),
    )
    comparable = [row for row in path_rows if row[3] in {"yes", "no"}]
    matches = sum(row[3] == "yes" for row in comparable)
    print(f"Path-count matches: {matches}/{len(comparable)} comparable benchmarks")
    mismatches = [row[0] for row in comparable if row[3] == "no"]
    if mismatches:
        print(f"Path-count mismatches: {', '.join(mismatches)}")
    print()
    print(f"Modules reported: {len(table_rows)}")
    print()
    print_table(headers, table_rows)
    return headers, table_rows


def main() -> int:
    parser = argparse.ArgumentParser(
        description="Summarize OOPSLA 2026 benchmark final result CSV files."
    )
    parser.add_argument(
        "--rq",
        choices=("compilation", "heuristic"),
        required=True,
        help="Research question summary to print.",
    )
    parser.add_argument(
        "--suite",
        action="append",
        default=[],
        help="Suite to summarize. Repeatable. Defaults to all final_results_*.csv files.",
    )
    parser.add_argument(
        "-o",
        "--output",
        type=Path,
        default=None,
        help=(
            "Output summary CSV path. Defaults to inserting the RQ name before "
            "the input CSV suffix, e.g. final_results_quicksort.heuristic.csv."
        ),
    )
    args = parser.parse_args()

    suites = args.suite or available_suites()
    if not suites:
        raise SystemExit(f"No final_results_*.csv files found in {HERE}")

    rows = read_rows(suites)
    if suites == ["Collection-C"]:
        headers, summary_rows = summarize_collection(rows)
    elif args.rq == "compilation":
        headers, summary_rows = summarize_compilation(rows)
    elif args.rq == "heuristic":
        headers, summary_rows = summarize_heuristic(rows)
    output_path = (args.output or default_output_path(suites, args.rq)).resolve()
    write_summary_csv(headers, summary_rows, output_path)
    print()
    print(f"Wrote {len(summary_rows)} summary rows to {output_path}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
