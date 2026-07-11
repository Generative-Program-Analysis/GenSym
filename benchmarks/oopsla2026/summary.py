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


def fmt(value: float | None, digits: int = 2) -> str:
    if value is None:
        return "-"
    return f"{value:.{digits}f}"


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
    speedups = []
    path_matches = 0
    headers = [
        "Suite",
        "Benchmark",
        "npaths",
        "T_WASP_instr_exec(s)",
        "T_WASP_total(s)",
        "T_GenWasym_instr_exec(s)",
        "T_GenWasym_total(s)",
        "Speedup",
    ]

    for row in rows:
        genwasym_instr_time = parse_float(row, "GenWasym_NoConfig_InstrTime(s)_mean")
        genwasym_total_time = parse_float(row, "GenWasym_NoConfig_LoopTime(s)_mean")
        wasp_instr_time = parse_float(row, "WASP_InstrTime(s)_mean")
        wasp_total_time = parse_float(row, "WASP_LoopTime(s)_mean")
        genwasym_paths = parse_float(row, "GenWasym_NoConfig_PathsExplored_mean")
        wasp_paths = parse_float(row, "WASP_PathsExplored_mean")
        if (
            genwasym_instr_time is None
            or genwasym_total_time is None
            or wasp_instr_time is None
            or wasp_total_time is None
            or genwasym_instr_time <= 0
        ):
            continue

        speedup = wasp_instr_time / genwasym_instr_time
        speedups.append(speedup)
        if genwasym_paths is not None and wasp_paths is not None and genwasym_paths == wasp_paths:
            path_matches += 1
        npaths = genwasym_paths if genwasym_paths is not None else wasp_paths
        complete_rows.append(
            [
                row.get("Suite", ""),
                row.get("Benchmark", ""),
                fmt(npaths, 0),
                fmt(wasp_instr_time),
                fmt(wasp_total_time),
                fmt(genwasym_instr_time),
                fmt(genwasym_total_time),
                fmt(speedup),
            ]
        )

    print("RQ: compilation")
    print(f"Benchmarks with both GenWasym and WASP results: {len(complete_rows)}")
    print(f"Mean WASP/GenWasym instruction-time ratio: {fmt(mean(speedups))}x")
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
        "SnapshotInstr(s)",
        "CostModelInstr(s)",
        "SpeedupSnapshot",
        "SpeedupHeuristic",
        "SpeedupAll",
    ]

    for row in rows:
        no_config = parse_float(row, "GenWasym_NoConfig_InstrTime(s)_mean")
        snapshot = parse_float(row, "GenWasym_Snapshot_InstrTime(s)_mean")
        cost_model = parse_float(row, "GenWasym_CostModel_InstrTime(s)_mean")
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
                fmt(no_config),
                fmt(snapshot),
                fmt(cost_model),
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
        f"Heuristic wins: {heuristic_wins}/{len(heuristic_speedups)}; "
        f"mean Snapshot/CostModel ratio: {fmt(mean(heuristic_speedups))}x"
    )
    print(
        f"Combined wins: {all_wins}/{len(all_speedups)}; "
        f"mean combined speedup: {fmt(mean(all_speedups))}x"
    )
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
    if args.rq == "compilation":
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
