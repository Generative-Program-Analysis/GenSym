import csv
from dataclasses import dataclass
import json
from pathlib import Path


@dataclass
class BenchmarkResult:
    instr_time: float
    solver_time: float
    resuming_time: float
    total_time: float
    total_no_solver_time: float
    cost_model_time: float
    path_count: int


def read_json_output(file_path) -> BenchmarkResult:
    with open(file_path, "r") as f:
        data = json.load(f)
    instr_time = data.get("profile_summary").get("total_time_instruction_execution_s")
    solver_time = data.get("profile_summary").get("total_time_solver_s")
    resuming_time = data.get("profile_summary").get(
        "total_time_resuming_from_snapshot_s"
    )
    cost_model_time = data.get("profile_summary").get(
        "total_time_counting_symbolic_size_s"
    )
    path_count = data.get("finished_count")
    return BenchmarkResult(
        instr_time=instr_time,
        solver_time=solver_time,
        resuming_time=resuming_time,
        total_time=instr_time + solver_time + resuming_time + cost_model_time,
        total_no_solver_time=resuming_time + instr_time,
        cost_model_time=cost_model_time,
        path_count=path_count,
    )


def read_json_output_for_dir(directory_path) -> dict[str, BenchmarkResult]:
    result = dict()
    for json_file in directory_path.glob("**/*.json"):
        parent_dir = json_file.parent
        benchmark_name = parent_dir.name.split(".")[0]
        benchmark_result = read_json_output(json_file)
        result[benchmark_name] = benchmark_result
    return result


def entry():
    current_dir = Path(__file__).parent
    NoConfig_dir = current_dir / "NoConfig"
    Snapshot_dir = current_dir / "Snapshot"
    CostModel_dir = current_dir / "CostModel"
    no_config_result = read_json_output_for_dir(NoConfig_dir)
    snapshot_result = read_json_output_for_dir(Snapshot_dir)
    cost_model_result = read_json_output_for_dir(CostModel_dir)
    all_configs = (
        set(no_config_result.keys())
        .union(set(snapshot_result.keys()))
        .union(set(cost_model_result.keys()))
    )
    output_csv = current_dir / "benchmark_results.csv"
    with open(output_csv, "w", newline="") as csvfile:
        fieldnames = [
            "Benchmark",
            "NoConfig_InstrTime(s)",
            "NoConfig_SolverTime(s)",
            "NoConfig_ResumingTime(s)",
            "NoConfig_CostModelTime(s)",
            "NoConfig_TotalTime(s)",
            "NoConfig_PathCount",
            "Snapshot_InstrTime(s)",
            "Snapshot_NoSolverTime(s)",
            "Snapshot_SolverTime(s)",
            "Snapshot_ResumingTime(s)",
            "Snapshot_CostModelTime(s)",
            "Snapshot_TotalTime(s)",
            "Snapshot_PathCount",
            "CostModel_InstrTime(s)",
            "CostModel_SolverTime(s)",
            "CostModel_ResumingTime(s)",
            "CostModel_CostModelTime(s)",
            "CostModel_TotalTime(s)",
            "CostModel_PathCount",
        ]
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        for benchmark in sorted(all_configs):
            no_config = no_config_result.get(benchmark)
            snapshot = snapshot_result.get(benchmark)
            row = {"Benchmark": benchmark}
            if no_config:
                row.update(
                    {
                        "NoConfig_InstrTime(s)": no_config.instr_time,
                        "NoConfig_SolverTime(s)": no_config.solver_time,
                        "NoConfig_ResumingTime(s)": no_config.resuming_time,
                        "NoConfig_CostModelTime(s)": no_config.cost_model_time,
                        "NoConfig_TotalTime(s)": no_config.total_time,
                        "NoConfig_PathCount": no_config.path_count,
                    }
                )
            else:
                row.update(
                    {
                        "NoConfig_InstrTime(s)": "x",
                        "NoConfig_SolverTime(s)": "x",
                        "NoConfig_ResumingTime(s)": "x",
                        "NoConfig_CostModelTime(s)": "x",
                        "NoConfig_TotalTime(s)": "x",
                        "NoConfig_PathCount": "x",
                    }
                )
            if snapshot:
                row.update(
                    {
                        "Snapshot_InstrTime(s)": snapshot.instr_time,
                        "Snapshot_NoSolverTime(s)": snapshot.total_no_solver_time,
                        "Snapshot_SolverTime(s)": snapshot.solver_time,
                        "Snapshot_ResumingTime(s)": snapshot.resuming_time,
                        "Snapshot_CostModelTime(s)": snapshot.cost_model_time,
                        "Snapshot_TotalTime(s)": snapshot.total_time,
                        "Snapshot_PathCount": snapshot.path_count,
                    }
                )
            else:
                row.update(
                    {
                        "Snapshot_InstrTime(s)": "x",
                        "Snapshot_NoSolverTime(s)": "x",
                        "Snapshot_SolverTime(s)": "x",
                        "Snapshot_ResumingTime(s)": "x",
                        "Snapshot_CostModelTime(s)": "x",
                        "Snapshot_TotalTime(s)": "x",
                        "Snapshot_PathCount": "x",
                    }
                )

            cost_model = cost_model_result.get(benchmark)
            if cost_model:
                row.update(
                    {
                        "CostModel_InstrTime(s)": cost_model.instr_time,
                        "CostModel_SolverTime(s)": cost_model.solver_time,
                        "CostModel_ResumingTime(s)": cost_model.resuming_time,
                        "CostModel_CostModelTime(s)": cost_model.cost_model_time,
                        "CostModel_TotalTime(s)": cost_model.total_time,
                        "CostModel_PathCount": cost_model.path_count,
                    }
                )
            else:
                row.update(
                    {
                        "CostModel_InstrTime(s)": "x",
                        "CostModel_SolverTime(s)": "x",
                        "CostModel_ResumingTime(s)": "x",
                        "CostModel_CostModelTime(s)": "x",
                        "CostModel_TotalTime(s)": "x",
                        "CostModel_PathCount": "x",
                    }
                )
            writer.writerow(row)


if __name__ == "__main__":
    entry()
