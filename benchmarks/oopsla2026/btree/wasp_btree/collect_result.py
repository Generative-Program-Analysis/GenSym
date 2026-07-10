from pathlib import Path
import json, csv


def entry():
    out = Path("results.csv")
    with out.open("w", newline="\n") as f:
        writer = csv.writer(f)
        writer.writerow(["dir", "loop_time", "solver_time", "paths_explored"])

        for case_dir in Path(".").parent.iterdir():
            if case_dir.is_dir():
                print(f"Processing {case_dir.name}...")
                result_file = case_dir / "report.json"
                if result_file.exists():
                    with result_file.open() as rf:
                        data = json.load(rf)

                    row = [case_dir.name, data.get("loop_time"), data.get("solver_time"), data.get("paths_explored")]
                    print(f"  Data: {row[1:]}")
                    writer.writerow(row)

if __name__ == "__main__":
    entry()