#!/usr/bin/env python3

import argparse
import csv
import json
import sys
from pathlib import Path
from typing import Any


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Analyze a GenWasm profile JSON file."
    )
    parser.add_argument("profile", type=Path, help="profile JSON file to read")
    parser.add_argument(
        "-o",
        "--output",
        type=Path,
        help="CSV file to write; defaults to stdout",
    )
    parser.add_argument(
        "--plot",
        type=Path,
        help="write a snapshot cost scatter plot to this image file",
    )
    return parser.parse_args()


def snapshot_costs(profile: dict[str, Any]) -> tuple[list[float], list[float]]:
    history = profile.get("profile_summary", {}).get("snapshot_history", [])
    restart_costs = []
    resume_costs = []
    for entry in history:
        restart_costs.append(float(entry["restart_cost"]))
        resume_costs.append(float(entry["resume_cost"]))
    return restart_costs, resume_costs


def write_snapshot_csv(profile: dict[str, Any], output_file: Any) -> None:
    restart_costs, resume_costs = snapshot_costs(profile)
    writer = csv.writer(output_file)
    writer.writerow(["restart_cost", "resume_cost"])
    writer.writerows(zip(restart_costs, resume_costs))


def write_snapshot_plot(profile: dict[str, Any], output: Path) -> None:
    try:
        import matplotlib

        matplotlib.use("Agg")
        import matplotlib.pyplot as plt
    except ImportError as exc:
        raise RuntimeError(
            "plotting requires matplotlib; install it with "
            "`python3 -m pip install matplotlib`"
        ) from exc

    restart_costs, resume_costs = snapshot_costs(profile)
    if not restart_costs:
        raise ValueError("profile does not contain snapshot history records")

    min_restart_cost = min(restart_costs)
    max_restart_cost = max(restart_costs)
    x_margin = (max_restart_cost - min_restart_cost) * 0.05
    if x_margin == 0:
        x_margin = max_restart_cost * 0.05
    x_min = min_restart_cost - x_margin
    x_max = max_restart_cost + x_margin

    y_min = 0
    y_max = max(resume_costs) * 1.05
    line_max = min(x_max, y_max)
    line_min = max(x_min, y_min)

    fig, ax = plt.subplots(figsize=(8, 6))
    ax.scatter(restart_costs, resume_costs, s=14, alpha=0.75)
    ax.plot([line_min, line_max], [line_min, line_max], color="black", linewidth=1.2)
    ax.set_xlabel("restart_cost")
    ax.set_ylabel("resume_cost")
    ax.set_title("Snapshot cost profile")
    ax.set_xlim(x_min, x_max)
    ax.set_ylim(y_min, y_max)
    ax.grid(True, linewidth=0.4, alpha=0.4)
    fig.tight_layout()
    fig.savefig(output)
    plt.close(fig)


def main() -> None:
    args = parse_args()
    with args.profile.open() as profile_file:
        profile = json.load(profile_file)

    output_file = args.output.open("w", newline="") if args.output else sys.stdout
    try:
        write_snapshot_csv(profile, output_file)
    finally:
        if args.output:
            output_file.close()

    if args.plot:
        try:
            write_snapshot_plot(profile, args.plot)
        except RuntimeError as exc:
            print(f"error: {exc}", file=sys.stderr)
            raise SystemExit(1) from exc


if __name__ == "__main__":
    main()
