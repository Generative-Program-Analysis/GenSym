from pathlib import Path
import re

pat = re.compile(
    r'\(data\b\s+\$\S+\s+\(i32\.const\s+([0-9]+)\)\s*"((?:[^"\\]|\\.)*)"\)'
)

def get_mapping(f: Path) -> list[tuple[int, str]]:
    for line in f.read_text().splitlines():
        for m in pat.finditer(line):
            base_str = m.group(1)
            mapping_str = m.group(2)
            keys = mapping_str.split("\\00")
            assert(keys[-1] == "")
            keys = keys[:-1]
            return [(int(base_str) + i * 2, k) for i, k in enumerate(keys)]


def main(): 
    for file in Path(__file__).parent.glob("*.wat"):
        mapping = get_mapping(file)
        print(f"Mapping for {file.name}: {mapping}")
        for addr, key in mapping:
            # replace (get_sym_int32 "{key}") to (i32.const {addr})\n\t(call $i32.symbolic) in the file
            file_contents = file.read_text()
            file_contents = file_contents.replace(f'(get_sym_int32 "{key}")', f'(i32.const {addr})\n\t\t(call $i32.symbolic)')
            file.write_text(file_contents)


if __name__ == "__main__":
    main()
