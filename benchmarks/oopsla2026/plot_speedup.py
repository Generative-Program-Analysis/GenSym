#!/usr/bin/env python3
"""Plot grouped speedup bars for the uniform benchmark suite.

The figure has five benchmark groups:
  1. Extended B-Tree
  2. Collection-C
  3. Arithmetic Evaluator
  4. Quicksort (Setting 1)
  5. Quicksort (Setting 2)

Each group reports the mean of the speedup values available in the CSVs:
  - Baseline: fixed at 1x
  - Compilation: WASP exec time divided by GenWasym noreuse exec time
  - Snapshot: WASP exec time divided by GenWasym snapshot exec time
  - Heuristic: WASP exec time divided by GenWasym heuristic exec time

The output contains the per-benchmark chart and an equally weighted mean
speedup chart, arranged side by side with identical plotting rectangles.
"""

from __future__ import annotations

import argparse
import csv
import math
import re
import shutil
import subprocess
import tempfile
from dataclasses import dataclass
from pathlib import Path
from statistics import mean, median

try:
    import matplotlib.pyplot as plt
except ModuleNotFoundError:  # Keep the script usable on a minimal Python setup.
    plt = None


HERE = Path(__file__).resolve().parent

# Keep both figures on the same canvas and, more importantly, give them the
# same plotting rectangle. This lets the two PDFs align when placed together.
FIGURE_WIDTH = 456
# Trim the canvas by approximately one 7 pt text line while retaining the
# spacing between the x-axis labels and legend.
FIGURE_HEIGHT = 113
CANVAS_TOP_CROP = 10
PLOT_LEFT = 45
PLOT_RIGHT = 135
PLOT_TOP = 10
PLOT_BOTTOM = 46
PANEL_GAP = 50
LEGEND_ITEM_WIDTHS = (74, 42, 95, 144)
LEGEND_GAP = 12
LEGEND_BOX_WIDTH = 145
LEGEND_BOX_Y = 82
LEGEND_BOX_HEIGHT = 30
CANVAS_RIGHT_PADDING = 8

DISPLAY_LABELS = {
    "Quicksort (Setting 1)": r"Quicksort ($n_{\mathrm{comp}} < 10^{2}$)",
    "Quicksort (Setting 2)": r"Quicksort ($n_{\mathrm{comp}} > 10^{4}$)",
}

# The PDF output is produced through SVG, whose text renderer does not parse
# LaTeX. These equivalent labels preserve the subscript in that output path.
SVG_DISPLAY_LABELS = {
    "Quicksort (Setting 1)": (
        'Quicksort (<tspan font-style="italic">n</tspan>'
        '<tspan baseline-shift="sub" font-size="5">comp</tspan> &lt; 10'
        '<tspan baseline-shift="super" font-size="5">2</tspan>)'
    ),
    "Quicksort (Setting 2)": (
        'Quicksort (<tspan font-style="italic">n</tspan>'
        '<tspan baseline-shift="sub" font-size="5">comp</tspan> &gt; 10'
        '<tspan baseline-shift="super" font-size="5">4</tspan>)'
    ),
}


@dataclass(frozen=True)
class SpeedupGroup:
    name: str
    compilation: float
    snapshot: float
    heuristic: float
    n: int


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Create a grouped bar chart of speedups for the benchmark suite."
    )
    parser.add_argument(
        "--left-output",
        type=Path,
        default=HERE / "fig_speedup_left.pdf",
        help="Output path for the benchmark-speedup panel.",
    )
    parser.add_argument(
        "--right-output",
        type=Path,
        default=HERE / "fig_speedup_right.pdf",
        help="Output path for the mean-speedup panel and legend.",
    )
    parser.add_argument(
        "--summary",
        choices=("mean", "median"),
        default="mean",
        help="How to summarize speedups within each benchmark group.",
    )
    parser.add_argument(
        "--linear",
        action="store_true",
        help="Use a linear y-axis instead of the default log y-axis.",
    )
    return parser.parse_args()


def numeric(value: str) -> float | None:
    value = value.strip()
    if not value or value in {"-", "Timeout", r"\TO"}:
        return None
    try:
        x = float(value)
    except ValueError:
        return None
    if not math.isfinite(x):
        return None
    return x


def summarize(values: list[float], method: str) -> float:
    if not values:
        raise ValueError("cannot summarize an empty speedup list")
    if method == "median":
        return median(values)
    return mean(values)


def geomean(values: list[float]) -> float:
    positive_values = [value for value in values if value > 0]
    if not positive_values:
        raise ValueError("cannot compute geomean of an empty speedup list")
    return math.exp(mean(math.log(value) for value in positive_values))


def first_numeric(row: dict[str, str], *names: str) -> float | None:
    for name in names:
        value = numeric(row.get(name, ""))
        if value is not None:
            return value
    return None


def ratio(numerator: float | None, denominator: float | None) -> float | None:
    if numerator is None or denominator is None or denominator == 0:
        return None
    return numerator / denominator


def read_speedup_values(path: Path, row_filter=None) -> tuple[list[float], list[float], list[float]]:
    compilation_values: list[float] = []
    snapshot_values: list[float] = []
    heuristic_values: list[float] = []

    with path.open(newline="") as f:
        reader = csv.DictReader(f)
        for row in reader:
            if row_filter is not None and not row_filter(row):
                continue

            baseline_exec = first_numeric(
                row,
                "T_WASP_exec(s)",
                "T_WASP_instr_exec(s)",
            )
            noreuse_exec = first_numeric(
                row,
                "T_GenWasym_noreuse_exec(s)",
                "T_GenWasym_NoConfig_instr_exec(s)",
            )
            snapshot_exec = first_numeric(
                row,
                "T_GenWasym_snapshot_exec(s)",
                "T_GenWasym_Snapshot_instr_exec(s)",
            )
            heuristic_exec = first_numeric(
                row,
                "T_GenWasym_heuristic_exec(s)",
                "T_GenWasym_CostModel_instr_exec(s)",
            )
            compilation = ratio(baseline_exec, noreuse_exec)
            snapshot = ratio(baseline_exec, snapshot_exec)
            heuristic = ratio(baseline_exec, heuristic_exec)

            if compilation is not None:
                compilation_values.append(compilation)
            if snapshot is not None:
                snapshot_values.append(snapshot)
            if heuristic is not None:
                heuristic_values.append(heuristic)

    return compilation_values, snapshot_values, heuristic_values


def read_speedups(path: Path, method: str, row_filter=None) -> SpeedupGroup:
    compilation_values, snapshot_values, heuristic_values = read_speedup_values(path, row_filter)
    return SpeedupGroup(
        name="",
        compilation=summarize(compilation_values, method),
        snapshot=summarize(snapshot_values, method),
        heuristic=summarize(heuristic_values, method),
        n=max(len(compilation_values), len(snapshot_values), len(heuristic_values)),
    )


def quicksort_setting(setting: int):
    pattern = re.compile(rf"^quicksort{setting}\.")

    def keep(row: dict[str, str]) -> bool:
        return bool(pattern.match(row["Benchmark"]))

    return keep


def load_groups(method: str) -> list[SpeedupGroup]:
    specs = [
        (
            "B-Tree",
            HERE / "final_results_btree.compilation.csv",
            None,
        ),
        (
            "Collection-C",
            HERE / "final_results_Collection-C.compilation.csv",
            None,
        ),
        (
            "Arithmetic Evaluator",
            HERE / "final_results_evaluator.compilation.csv",
            None,
        ),
        (
            "Quicksort (Setting 1)",
            HERE / "final_results_quicksort.compilation.csv",
            quicksort_setting(1),
        ),
        (
            "Quicksort (Setting 2)",
            HERE / "final_results_quicksort.compilation.csv",
            quicksort_setting(2),
        ),
    ]

    groups: list[SpeedupGroup] = []
    for name, path, row_filter in specs:
        compilation_values, snapshot_values, heuristic_values = read_speedup_values(
            path, row_filter
        )
        groups.append(
            SpeedupGroup(
                name=name,
                compilation=summarize(compilation_values, method),
                snapshot=summarize(snapshot_values, method),
                heuristic=summarize(heuristic_values, method),
                n=max(
                    len(compilation_values),
                    len(snapshot_values),
                    len(heuristic_values),
                ),
            )
        )

    return groups


def overall_speedup_group() -> SpeedupGroup:
    """Compute geometric means across all available benchmark rows."""
    csv_paths = (
        HERE / "final_results_btree.compilation.csv",
        HERE / "final_results_Collection-C.compilation.csv",
        HERE / "final_results_evaluator.compilation.csv",
        HERE / "final_results_quicksort.compilation.csv",
    )
    compilation_values: list[float] = []
    snapshot_values: list[float] = []
    heuristic_values: list[float] = []

    for path in csv_paths:
        compilation, snapshot, heuristic = read_speedup_values(path)
        compilation_values.extend(compilation)
        snapshot_values.extend(snapshot)
        heuristic_values.extend(heuristic)

    return SpeedupGroup(
        name="",
        compilation=geomean(compilation_values),
        snapshot=geomean(snapshot_values),
        heuristic=geomean(heuristic_values),
        n=max(len(compilation_values), len(snapshot_values), len(heuristic_values)),
    )


def plot_matplotlib(groups: list[SpeedupGroup], output: Path, use_log_scale: bool) -> None:
    if plt is None:
        raise RuntimeError("matplotlib is not available")

    labels = [DISPLAY_LABELS.get(group.name, group.name) for group in groups]
    baseline = [1.0 for _ in groups]
    compilation = [group.compilation for group in groups]
    snapshot = [group.snapshot for group in groups]
    heuristic = [group.heuristic for group in groups]

    group_spacing = 0.78
    x = [i * group_spacing for i in range(len(groups))]
    width = 0.07

    plt.rcParams.update(
        {
            "font.size": 10,
            "axes.spines.top": False,
            "axes.spines.right": False,
        }
    )

    fig, ax = plt.subplots(figsize=(7.4, 2.0), constrained_layout=True)
    offsets = [-1.5 * width, -0.5 * width, 0.5 * width, 1.5 * width]
    bars_baseline = ax.bar(
        [i + offsets[0] for i in x],
        baseline,
        width,
        label="WASP (Baseline)",
        color="#9A9A9A",
    )
    bars_compilation = ax.bar(
        [i + offsets[1] for i in x],
        compilation,
        width,
        label="Staging",
        color="#4C78A8",
    )
    bars_snapshot = ax.bar(
        [i + offsets[2] for i in x],
        snapshot,
        width,
        label="Staging + Snapshot",
        color="#F58518",
    )
    bars_heuristic = ax.bar(
        [i + offsets[3] for i in x],
        heuristic,
        width,
        label="Staging + Snapshot + Heuristic",
        color="#54A24B",
    )

    ax.axhline(1.0, color="0.35", linewidth=0.8, linestyle="--")
    ax.set_ylabel("Speedup")
    ax.set_xticks(x)
    ax.set_xticklabels(labels, rotation=30, ha="right", rotation_mode="anchor")
    ax.legend(
        ncols=4,
        loc="upper center",
        bbox_to_anchor=(0.5, 1.14),
        frameon=False,
        fontsize=8,
        handletextpad=0.35,
        columnspacing=0.9,
    )
    if use_log_scale:
        ax.set_yscale("log")
        ax.set_ylim(0.55, max(baseline + compilation + snapshot + heuristic) * 1.2)
    else:
        ax.set_ylim(0, max(baseline + compilation + snapshot + heuristic) * 1.18)

    output.parent.mkdir(parents=True, exist_ok=True)
    fig.savefig(output, bbox_inches="tight")
    plt.close(fig)


def svg_y(value: float, height: float, top: float, bottom: float, max_value: float, log_scale: bool) -> float:
    if log_scale:
        min_value = 0.35
        log_min = math.log10(min_value)
        log_max = math.log10(max_value)
        ratio = (math.log10(max(value, min_value)) - log_min) / (log_max - log_min)
    else:
        ratio = value / max_value
    return height - bottom - ratio * (height - top - bottom)


def plot_svg(
    groups: list[SpeedupGroup],
    output: Path,
    use_log_scale: bool,
    include_legend: bool = True,
    show_y_axis: bool = True,
    show_y_ticks: bool | None = None,
    max_value_override: float | None = None,
    bar_width: float = 3.5,
    bar_gap: float = 0,
    show_transition_arrows: bool = False,
    show_speedup_values: bool = False,
    legend_x: float = 2,
    group_span_scale: float = 1.0,
    align_groups_left: bool = False,
    align_group_bars_to_edges: bool = False,
    group_bar_edge_inset: float = 0,
    align_bars_left: bool = False,
    align_bars_right: bool = False,
    bar_cluster_left_padding: float = 0,
    bar_cluster_right_padding: float = 0,
    plot_width_scale: float = 1.0,
    canvas_width: float | None = None,
    plot_top: float | None = None,
) -> None:
    width = FIGURE_WIDTH
    height = FIGURE_HEIGHT
    left = PLOT_LEFT
    right = PLOT_RIGHT
    right = width - left - (width - left - right) * plot_width_scale
    top = PLOT_TOP if plot_top is None else plot_top
    bottom = PLOT_BOTTOM
    if show_y_ticks is None:
        show_y_ticks = show_y_axis
    plot_width = width - left - right
    plot_height = height - top - bottom
    values = [v for g in groups for v in (1.0, g.compilation, g.snapshot, g.heuristic)]
    max_value = max_value_override or max(values) * (1.2 if use_log_scale else 1.12)
    group_span = plot_width * group_span_scale
    group_offset = 0 if align_groups_left else (plot_width - group_span) / 2
    group_width = group_span / len(groups)
    colors = ["#9A9A9A", "#4C78A8", "#F58518", "#54A24B"]
    series = [
        ("WASP (Baseline)", [1.0 for _ in groups]),
        ("Staging", [g.compilation for g in groups]),
        ("Staging + Snapshot", [g.snapshot for g in groups]),
        ("Staging + Snapshot + Heuristic", [g.heuristic for g in groups]),
    ]

    def text(
        x,
        y,
        content,
        size=12,
        anchor="middle",
        weight="normal",
        rotate: float | None = None,
        fill: str = "black",
        dominant_baseline: str | None = None,
    ):
        transform = f' transform="rotate({rotate} {x} {y})"' if rotate is not None else ""
        baseline_attr = f' dominant-baseline="{dominant_baseline}"' if dominant_baseline else ""
        return (
            f'<text x="{x:.1f}" y="{y:.1f}" font-family="Arial, sans-serif" '
            f'font-size="{size}" font-weight="{weight}" text-anchor="{anchor}" '
            f'fill="{fill}"{baseline_attr}{transform}>'
            f"{content}</text>"
        )

    output_width = canvas_width or width
    output_height = height - CANVAS_TOP_CROP
    parts = [
        f'<svg xmlns="http://www.w3.org/2000/svg" width="{output_width}" height="{output_height}" '
        f'viewBox="0 {CANVAS_TOP_CROP} {output_width} {output_height}">'
    ]
    if show_transition_arrows:
        parts.append(
            '<defs><marker id="transition-arrow" markerWidth="6" markerHeight="6" '
            'refX="5.5" refY="3" orient="auto" markerUnits="strokeWidth">'
            '<path d="M 0 0 L 6 3 L 0 6 Z" fill="#444"/></marker></defs>'
        )
    if show_y_axis:
        parts.append(text(7, top + (height - top - bottom) / 2, "Speedup", 9, rotate=-90))

    y_one = svg_y(1.0, height, top, bottom, max_value, use_log_scale)
    parts.append(
        f'<line x1="{left}" y1="{y_one:.1f}" x2="{width - right}" y2="{y_one:.1f}" '
        'stroke="#666" stroke-dasharray="4 4" stroke-width="1"/>'
    )
    if show_y_ticks and use_log_scale:
        parts.append(text(left - 8, y_one + 4, "1", 9, anchor="end"))

    tick_values = [10, 100, 1000] if use_log_scale else [0, max_value / 4, max_value / 2, max_value * 0.75, max_value]
    for tick in tick_values:
        if tick > max_value:
            continue
        y = svg_y(tick, height, top, bottom, max_value, use_log_scale)
        parts.append(
            f'<line x1="{left}" y1="{y:.1f}" x2="{width - right}" y2="{y:.1f}" '
            'stroke="#e6e6e6" stroke-width="1"/>'
        )
        if show_y_ticks:
            parts.append(text(left - 8, y + 4, f"{tick:g}", 9, anchor="end"))

    baseline_y = height - bottom
    parts.append(
        f'<line x1="{left}" y1="{baseline_y}" x2="{width - right}" y2="{baseline_y}" '
        'stroke="#222" stroke-width="1"/>'
    )
    for group_index, group in enumerate(groups):
        center = left + group_offset + group_width * (group_index + 0.5)
        if align_group_bars_to_edges and len(groups) > 1:
            bar_cluster_width = len(series) * bar_width + (len(series) - 1) * bar_gap
            group_distance = (
                plot_width - 2 * group_bar_edge_inset - bar_cluster_width
            ) / (len(groups) - 1)
            center = (
                left
                + group_bar_edge_inset
                + (len(series) - 1) / 2 * (bar_width + bar_gap)
                + group_index * group_distance
            )
        if align_bars_left:
            center = (
                left
                + bar_cluster_left_padding
                + (len(series) - 1) / 2 * (bar_width + bar_gap)
            )
        elif align_bars_right:
            center = (
                width
                - right
                - bar_cluster_right_padding
                - bar_width
                - (len(series) - 1) / 2 * (bar_width + bar_gap)
            )
        bar_positions = [
            center + (series_index - (len(series) - 1) / 2) * (bar_width + bar_gap)
            for series_index in range(len(series))
        ]
        bar_tops: list[float] = []
        for series_index, (_, series_values) in enumerate(series):
            value = series_values[group_index]
            x = bar_positions[series_index]
            y = svg_y(value, height, top, bottom, max_value, use_log_scale)
            bar_tops.append(y)
            bar_height = baseline_y - y
            parts.append(
                f'<rect x="{x:.1f}" y="{y:.1f}" width="{bar_width}" height="{bar_height:.1f}" '
                f'fill="{colors[series_index]}"/>'
            )
        if show_transition_arrows:
            for series_index, (start_x, start_y, end_x, end_y) in enumerate(
                zip(
                    bar_positions,
                    bar_tops,
                    bar_positions[1:],
                    bar_tops[1:],
                )
            ):
                if series_index == 0:
                    continue
                arrow_start_x = start_x + bar_width + 3
                arrow_end_x = end_x - 3
                parts.append(
                    f'<line x1="{arrow_start_x:.1f}" y1="{start_y:.1f}" '
                    f'x2="{arrow_end_x:.1f}" y2="{end_y:.1f}" stroke="#444" '
                    'stroke-width="1.2" marker-end="url(#transition-arrow)"/>'
                )
                previous = series[series_index][1][group_index]
                current = series[series_index + 1][1][group_index]
                parts.append(
                    text(
                        (arrow_start_x + arrow_end_x) / 2,
                        (start_y + end_y) / 2 + 7,
                        f"{current / previous:.1f}x",
                        8,
                    )
                )
        if show_speedup_values:
            for x, y, (_, series_values) in zip(bar_positions, bar_tops, series):
                value = series_values[group_index]
                parts.append(text(x + bar_width / 2, y - 2, f"{value:.1f}x", 8, anchor="middle"))
        label_kwargs = {"anchor": "end", "dominant_baseline": "hanging", "rotate": -30}
        if len(groups) == 1:
            label_kwargs = {"anchor": "middle", "dominant_baseline": "hanging"}
        label_center = (
            center + bar_width / 2
            if (align_bars_left or align_bars_right or align_group_bars_to_edges)
            else center
        )
        label = SVG_DISPLAY_LABELS.get(group.name, group.name)
        parts.append(text(label_center, baseline_y + 7, label, 7, **label_kwargs))

    if include_legend:
        parts.append(
            f'<rect x="{legend_x:.1f}" y="{LEGEND_BOX_Y}" width="{LEGEND_BOX_WIDTH}" '
            f'height="{LEGEND_BOX_HEIGHT}" fill="white" stroke="#777" stroke-width="0.6"/>'
        )
        for series_index, _ in enumerate(LEGEND_ITEM_WIDTHS):
            label, _ = series[series_index]
            legend_y = LEGEND_BOX_Y + 7 + series_index * 7
            parts.append(
                f'<rect x="{legend_x + 4:.1f}" y="{legend_y - 7}" width="7" height="7" '
                f'fill="{colors[series_index]}"/>'
            )
            parts.append(text(legend_x + 14, legend_y, label, 7, anchor="start"))

    parts.append("</svg>")
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text("\n".join(parts))


def plot_svg_panels(
    panels: list[list[SpeedupGroup]], output: Path, use_log_scale: bool
) -> None:
    """Place charts side by side while preserving each panel's dimensions."""
    all_values = [
        value
        for groups in panels
        for group in groups
        for value in (1.0, group.compilation, group.snapshot, group.heuristic)
    ]
    shared_max_value = max(all_values) * (1.2 if use_log_scale else 1.12)
    base_plot_width = FIGURE_WIDTH - PLOT_LEFT - PLOT_RIGHT
    left_plot_width = base_plot_width * 0.65
    panel_stride = left_plot_width + PANEL_GAP
    width = panel_stride + PLOT_LEFT + left_plot_width + CANVAS_RIGHT_PADDING
    legend_box_x = width - LEGEND_BOX_WIDTH - 2
    with tempfile.TemporaryDirectory() as tmpdir:
        panel_contents: list[str] = []
        for index, groups in enumerate(panels):
            panel_output = Path(tmpdir) / f"panel-{index}.svg"
            plot_svg(
                groups,
                panel_output,
                use_log_scale,
                include_legend=index == 0,
                show_y_axis=True,
                show_y_ticks=True,
                max_value_override=shared_max_value,
                bar_width=13 if index == 1 else 3.5,
                bar_gap=39.8 if index == 1 else 0,
                show_transition_arrows=index == 1,
                show_speedup_values=index == 1,
                legend_x=legend_box_x if index == 0 else 2,
                group_span_scale=0.8 if index == 0 else 1.0,
                align_groups_left=False,
                align_group_bars_to_edges=index == 0,
                group_bar_edge_inset=4 if index == 0 else 0,
                align_bars_left=False,
                align_bars_right=index == 1,
                bar_cluster_left_padding=8 if index == 1 else 0,
                bar_cluster_right_padding=4 if index == 1 else 0,
                plot_width_scale=0.65,
            )
            source = panel_output.read_text()
            _, _, content = source.partition(">")
            content, _, _ = content.rpartition("</svg>")
            panel_contents.append(content)

    output_height = FIGURE_HEIGHT - CANVAS_TOP_CROP
    parts = [
        f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" '
        f'height="{output_height}" '
        f'viewBox="0 {CANVAS_TOP_CROP} {width} {output_height}">'
    ]
    for index, content in enumerate(panel_contents):
        parts.append(f'<g transform="translate({index * panel_stride} 0)">')
        parts.append(content)
        parts.append("</g>")
    parts.append("</svg>")
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text("\n".join(parts))


def plot(groups: list[SpeedupGroup], output: Path, use_log_scale: bool) -> Path:
    if plt is not None:
        plot_matplotlib(groups, output, use_log_scale)
        return output

    if output.suffix.lower() == ".svg":
        plot_svg(groups, output, use_log_scale)
        return output

    converter = shutil.which("rsvg-convert")
    if converter is None:
        svg_output = output.with_suffix(".svg")
        plot_svg(groups, svg_output, use_log_scale)
        print("rsvg-convert is not available; wrote SVG instead of PDF.")
        return svg_output

    output.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory() as tmpdir:
        svg_output = Path(tmpdir) / "fig_speedup.svg"
        plot_svg(groups, svg_output, use_log_scale)
        subprocess.run(
            [converter, "-f", "pdf", "-o", str(output), str(svg_output)],
            check=True,
        )
    return output


def plot_panels(
    panels: list[list[SpeedupGroup]], output: Path, use_log_scale: bool
) -> Path:
    if output.suffix.lower() == ".svg":
        plot_svg_panels(panels, output, use_log_scale)
        return output

    converter = shutil.which("rsvg-convert")
    if converter is None:
        svg_output = output.with_suffix(".svg")
        plot_svg_panels(panels, svg_output, use_log_scale)
        print("rsvg-convert is not available; wrote SVG instead of PDF.")
        return svg_output

    output.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory() as tmpdir:
        svg_output = Path(tmpdir) / "fig_speedup.svg"
        plot_svg_panels(panels, svg_output, use_log_scale)
        subprocess.run(
            [converter, "-f", "pdf", "-o", str(output), str(svg_output)],
            check=True,
        )
    return output


def plot_configured_panel(
    groups: list[SpeedupGroup], output: Path, use_log_scale: bool, **options
) -> Path:
    """Render one panel with its own SVG layout options."""
    if output.suffix.lower() == ".svg":
        plot_svg(groups, output, use_log_scale, **options)
        return output

    converter = shutil.which("rsvg-convert")
    if converter is None:
        svg_output = output.with_suffix(".svg")
        plot_svg(groups, svg_output, use_log_scale, **options)
        print("rsvg-convert is not available; wrote SVG instead of PDF.")
        return svg_output

    output.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory() as tmpdir:
        svg_output = Path(tmpdir) / "panel.svg"
        plot_svg(groups, svg_output, use_log_scale, **options)
        subprocess.run(
            [converter, "-f", "pdf", "-o", str(output), str(svg_output)],
            check=True,
        )
    return output


def main() -> None:
    args = parse_args()
    groups = load_groups(args.summary)
    left_panel_groups = groups
    right_panel_group = overall_speedup_group()
    use_log_scale = not args.linear
    shared_max_value = max(
        [
            value
            for group in left_panel_groups
            for value in (1.0, group.compilation, group.snapshot, group.heuristic)
        ]
        + [
            1.0,
            right_panel_group.compilation,
            right_panel_group.snapshot,
            right_panel_group.heuristic,
        ]
    ) * (1.2 if use_log_scale else 1.12)
    cropped_panel_width = (
        PLOT_LEFT
        + (FIGURE_WIDTH - PLOT_LEFT - PLOT_RIGHT) * 0.65
        + CANVAS_RIGHT_PADDING
    )
    left_output = plot_configured_panel(
        left_panel_groups,
        args.left_output,
        use_log_scale,
        include_legend=False,
        max_value_override=shared_max_value,
        bar_width=3.5,
        bar_gap=0,
        group_span_scale=0.8,
        align_group_bars_to_edges=True,
        group_bar_edge_inset=4,
        plot_width_scale=0.65,
        canvas_width=cropped_panel_width,
    )
    right_output = plot_configured_panel(
        [right_panel_group],
        args.right_output,
        use_log_scale=True,
        include_legend=True,
        legend_x=cropped_panel_width - LEGEND_BOX_WIDTH - 2,
        max_value_override=100,
        bar_width=13,
        bar_gap=39.8,
        show_transition_arrows=True,
        show_speedup_values=True,
        align_bars_right=True,
        bar_cluster_right_padding=4,
        plot_width_scale=0.65,
        canvas_width=cropped_panel_width,
        # Align the top of the 0--100 right panel with the left panel's
        # highest labeled gridline (1000 on the logarithmic scale).
        plot_top=svg_y(
            1000,
            FIGURE_HEIGHT,
            PLOT_TOP,
            PLOT_BOTTOM,
            shared_max_value,
            True,
        ),
    )

    print(f"Wrote {left_output}")
    print(f"Wrote {right_output}")
    for group in groups:
        print(
            f"{group.name.replace(chr(10), ' ')}: "
            f"baseline=1.00x, "
            f"compilation={group.compilation:.2f}x, "
            f"snapshot={group.snapshot:.2f}x, "
            f"heuristic={group.heuristic:.2f}x "
            f"(n={group.n})"
        )


if __name__ == "__main__":
    main()
