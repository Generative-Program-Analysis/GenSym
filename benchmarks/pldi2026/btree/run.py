import os
import shutil
import sys
import subprocess
from pathlib import Path
import argparse


HERE = Path(__file__).resolve().parent
ME = Path(__file__).name


def is_executable(path: Path) -> bool:
    if not path.is_file():
        return False
    # executable bit or .exe suffix
    return os.access(str(path), os.X_OK) or path.suffix.lower() == ".exe"

def is_snapshot_executable(path: Path) -> bool:
    if not path.is_file():
        return False

    return path.name.endswith(".snapshot.exe")

def is_cost_model_executable(path: Path) -> bool:
    if not path.is_file():
        return False

    return path.name.endswith(".costmodel.exe")

def run_all(targets: list[Path], action):
    for file_path in targets:
        if not is_executable(file_path):
            continue
        file_name = file_path.name.removesuffix(".exe")
        if is_snapshot_executable(file_path):
            output_path = "Snapshot" + f"/{file_name}.output"
        elif is_cost_model_executable(file_path):
            output_path = "CostModel" + f"/{file_name}.output"
        else:
            output_path = "NoConfig" + f"/{file_name}.output"

        if Path(output_path).exists():
            print(f"Output path {output_path} already exists, skipping...")
            continue
        if action == "run":
            env = os.environ.copy()
            env.update({"OUTPUT_DIR": output_path})

            print("Now executing: " + str(file_path))
            proc = subprocess.Popen(
                [str(file_path.resolve())], stderr=subprocess.STDOUT, env=env
            )
            rc = proc.wait()

            if rc != 0:
                print(f"{file_name} exited with return code {rc}", file=sys.stderr)
        elif action == "clean":
            p = Path(output_path)
            if p.exists():
                try:
                    if p.is_dir():
                        shutil.rmtree(p)
                except Exception as e:
                    print(f"Failed to remove {output_path}: {e}", file=sys.stderr)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "-r",
        "--run-all",
        action="store_true",
        help="Run all compiled executables in the current directory",
    )
    parser.add_argument(
        "--clean",
        action="store_true"
    )
    args = parser.parse_args()

    if not args.run_all and not args.clean:
        parser.print_help()
        return 0

    # targets = list(sorted(list(Path(".").iterdir())))
    targets = list(map(Path, [
        "2o1u.wat.exe",
        "2o1u.wat.snapshot.exe",
        "2o1u.wat.costmodel.exe",
        "3o1u.wat.exe",
        "3o1u.wat.snapshot.exe",
        "3o1u.wat.costmodel.exe",
        "4o1u.wat.exe",
        "4o1u.wat.snapshot.exe",
        "4o1u.wat.costmodel.exe",
        "5o1u.wat.exe",
        "5o1u.wat.snapshot.exe",
        "5o1u.wat.costmodel.exe",
        "6o1u.wat.exe",
        "6o1u.wat.snapshot.exe",
        "6o1u.wat.costmodel.exe",
        "7o1u.wat.exe",
        "7o1u.wat.snapshot.exe",
        "7o1u.wat.costmodel.exe",
        "8o1u.wat.exe",
        "8o1u.wat.snapshot.exe",
        "8o1u.wat.costmodel.exe",
        "9o1u.wat.exe",
        "9o1u.wat.snapshot.exe",
        "9o1u.wat.costmodel.exe",
        "2o2u.wat.exe",
        "2o2u.wat.snapshot.exe",
        "2o2u.wat.costmodel.exe",
        "3o2u.wat.exe",
        "3o2u.wat.snapshot.exe",
        "3o2u.wat.costmodel.exe",
        "4o2u.wat.exe",
        "4o2u.wat.snapshot.exe",
        "4o2u.wat.costmodel.exe",
        "5o2u.wat.exe",
        "5o2u.wat.snapshot.exe",
        "5o2u.wat.costmodel.exe",
        "6o2u.wat.exe",
        "6o2u.wat.snapshot.exe",
        "6o2u.wat.costmodel.exe",
        "7o2u.wat.exe",
        "7o2u.wat.snapshot.exe",
        "7o2u.wat.costmodel.exe",
        # "8o2u.wat.exe" is not runnable even in wasp
        # "9o2u.wat.exe",
        "3o3u.wat.exe",
        "3o3u.wat.snapshot.exe",
        "3o3u.wat.costmodel.exe",
        "4o3u.wat.exe",
        "4o3u.wat.snapshot.exe",
        "4o3u.wat.costmodel.exe",
        "5o3u.wat.exe",
        "5o3u.wat.snapshot.exe",
        "5o3u.wat.costmodel.exe",
        "6o3u.wat.exe",
        "6o3u.wat.snapshot.exe",
        "6o3u.wat.costmodel.exe",
        "7o3u.wat.exe",
        "7o3u.wat.snapshot.exe",
        "7o3u.wat.costmodel.exe",
    ]))
    print(targets)
    if args.run_all:
        run_all(targets, action="run")
    elif args.clean:
        run_all(targets, action="clean")



if __name__ == "__main__":
    sys.exit(main())
