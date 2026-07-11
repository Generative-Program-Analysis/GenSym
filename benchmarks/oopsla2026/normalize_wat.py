#!/usr/bin/env python3
"""Normalize WASP WAT by rewriting imports and symbolic helpers."""

from __future__ import annotations

import argparse
import pathlib
import re

PRELUDE = """(module 
\t(import "i32" "symbolic" (func $i32.symbolic (param i32) (result i32)))

\t(import "i32" "sym_assume" (func $i32.sym_assume (param i32)))

\t(import "i32" "sym_assert" (func $i32.sym_assert (param i32)))

\t(import "i32" "is_symbolic" (func $i32.is_symbolic (param i32 i32) (result i32)))

\t(import "sym" "get_sym_int32" (func $get_sym_int32 (param i32) (result i32)))

  (import "mem" "alloc" (func $mem.alloc (param i32 i32) (result i32)))

  (import "mem" "free" (func $mem.free (param i32)))
"""

MODULE_PATTERN = re.compile(r"^\s*\(module\b")
IMPORT_PATTERN = re.compile(r"^\s*\(import\b")
MEM_FREE_PATTERN = re.compile(
    r'^\s*\(import\s+"mem"\s+"free"\s+\(func\s+\$mem\.free\s+\(param\s+i32\)\)\)\s*$'
)
SYMBOLIC_INSTR = "i32.symbolic"
SYMBOLIC_CALL = "call $i32.symbolic"
IS_SYMBOLIC_CALL = "call $i32.is_symbolic"
SYMBOLIC_INSTR_PATTERN = re.compile(r"(?<![\w.$])i32\.symbolic(?![\w.$])")
B32_SYMBOLIC_INSTR_PATTERN = re.compile(r"(?<![\w.$])b32\.symbolic(?![\w.$])")
LOGAND_INSTR_PATTERN = re.compile(r"(?<![\w.$])i32\.__logand(?![\w.$])")
LOGAND_CALL = "call $__logand"
LOGOR_INSTR_PATTERN = re.compile(r"(?<![\w.$])i32\.__logor(?![\w.$])")
LOGOR_CALL = "call $__logor"
IS_SYMBOLIC_INSTR_PATTERN = re.compile(r'(?<![\w.$"])is_symbolic(?![\w.$"])')
SYM_ASSUME_INSTR_PATTERN = re.compile(r'(?<![\w.$"])sym_assume(?![\w.$"])')
SYM_ASSUME_CALL = "call $i32.sym_assume"
SYM_ASSERT_INSTR_PATTERN = re.compile(r'(?<![\w.$"])sym_assert(?![\w.$"])')
SYM_ASSERT_CALL = "call $i32.sym_assert"
ALLOC_INSTR_PATTERN = re.compile(r'(?<![\w.$"])alloc(?![\w.$"])')
ALLOC_CALL = "call $mem.alloc"
ALLOC_FUNC_CALL_PATTERN = re.compile(r'(?<![\w.$"])call\s+\$alloc(?![\w.$"])')
DENORMALIZED_ALLOC_CALL = "call $alloc"
DEALLOC_INSTR_PATTERN = re.compile(r'(?<![\w.$"])dealloc(?![\w.$"])')
DENORMALIZED_DEALLOC_CALL = "call $dealloc"
FREE_INSTR_PATTERN = re.compile(r'(?<![\w.$"])free(?![\w.$"])')
FREE_CALL = "call $mem.free"
FREE_FUNC_CALL_PATTERN = re.compile(r'(?<![\w.$"])call\s+\$(?:free|dealloc)(?![\w.$"])')
DENORMALIZED_FREE_CALL = "call $dealloc"
PRINT_BTREE_LINE_PATTERN = re.compile(r"^[ \t]*\(print_btree\)\s*\n?", re.MULTILINE)
PRINT_STACK_LINE_PATTERN = re.compile(r"^[ \t]*\(print_stack\)\s*\n?", re.MULTILINE)
TRAILING_INVOKE_PATTERN = re.compile(r"\n[ \t]*\(invoke\b.*\)\s*\Z", re.DOTALL)
DATA_SEGMENT_PATTERN = re.compile(
    r'\(data(?:\s+\$\S+)?\s+\(i32\.const\s+(\d+)\)\s+"((?:[^"\\]|\\.)*)"\)',
    re.MULTILINE,
)
GET_SYM_INT32_PATTERN = re.compile(
    r'(^[ \t]*)\(get_sym_int32\s+"((?:[^"\\]|\\.)*)"\)\s*$',
    re.MULTILINE,
)
SYM_INT32_PATTERN = re.compile(
    r'(^[ \t]*)\(sym_int32\s+"((?:[^"\\]|\\.)*)"\)\s*$',
    re.MULTILINE,
)
SYMBOLIC_IMPORT_PATTERN = re.compile(
    r'(^[ \t]*)\(import\s+"i32"\s+"symbolic"\s+\(func\s+(?:\(;(?P<idx>\d+);\)|\$\S+)\s+\(type\s+(?P<type>\d+)\)\)\)\s*$',
    re.MULTILINE,
)


def _decode_wat_string_bytes(encoded: str) -> bytes:
    result = bytearray()
    idx = 0
    while idx < len(encoded):
        ch = encoded[idx]
        if ch != "\\":
            result.extend(ch.encode("utf-8"))
            idx += 1
            continue

        if idx + 1 >= len(encoded):
            result.append(ord("\\"))
            idx += 1
            continue

        nxt = encoded[idx + 1]
        hex_escape = encoded[idx + 1 : idx + 3]
        if len(hex_escape) == 2 and all(c in "0123456789abcdefABCDEF" for c in hex_escape):
            result.append(int(hex_escape, 16))
            idx += 3
            continue

        escapes = {
            "n": b"\n",
            "r": b"\r",
            "t": b"\t",
            "\\": b"\\",
            '"': b'"',
            "'": b"'",
        }
        replacement = escapes.get(nxt)
        if replacement is None:
            result.extend(nxt.encode("utf-8"))
        else:
            result.extend(replacement)
        idx += 2
    return bytes(result)


def build_symbol_address_map(text: str) -> dict[str, int]:
    symbol_addrs: dict[str, int] = {}
    for match in DATA_SEGMENT_PATTERN.finditer(text):
        base_addr = int(match.group(1))
        data = _decode_wat_string_bytes(match.group(2))
        start = 0
        for idx, byte in enumerate(data):
            if byte != 0:
                continue
            if idx > start:
                symbol = data[start:idx].decode("utf-8")
                symbol_addrs.setdefault(symbol, base_addr + start)
            start = idx + 1
    return symbol_addrs


def replace_get_sym_int32_calls(text: str) -> str:
    symbol_addrs = build_symbol_address_map(text)

    def repl(match: re.Match[str]) -> str:
        indent = match.group(1)
        name = _decode_wat_string_bytes(match.group(2)).decode("utf-8")
        addr = symbol_addrs.get(name)
        if addr is None:
            return match.group(0)
        return f"{indent}(i32.const {addr})\n{indent}({SYMBOLIC_CALL})"

    text = GET_SYM_INT32_PATTERN.sub(repl, text)
    text = SYM_INT32_PATTERN.sub(repl, text)
    return text


def _find_first_match(lines: list[str], pattern: re.Pattern[str], start: int = 0) -> int | None:
    for idx in range(start, len(lines)):
        if pattern.match(lines[idx]):
            return idx
    return None


def replace_initial_prelude(text: str) -> str:
    """Replace the file's first `(module ... mem.free import)` header block."""
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    module_line = _find_first_match(lines, MODULE_PATTERN)
    if module_line is None:
        return text

    prelude_end_line = _find_first_match(lines, MEM_FREE_PATTERN, start=module_line)

    prelude_lines = PRELUDE.rstrip("\n").split("\n")
    if prelude_end_line is None:
        # No `mem.free` marker: replace `(module` and any contiguous initial import lines.
        next_line = module_line + 1
        while next_line < len(lines):
            stripped = lines[next_line].strip()
            if stripped == "" or IMPORT_PATTERN.match(lines[next_line]):
                next_line += 1
                continue
            break
        replaced = lines[:module_line] + prelude_lines + lines[next_line:]
    else:
        # Existing import block found: replace from `(module` to `mem.free` import.
        replaced = lines[:module_line] + prelude_lines + lines[prelude_end_line + 1 :]
    return "\n".join(replaced).rstrip("\n") + "\n"


def replace_symbolic_instruction(text: str) -> str:
    text = SYMBOLIC_INSTR_PATTERN.sub(SYMBOLIC_CALL, text)
    text = B32_SYMBOLIC_INSTR_PATTERN.sub(SYMBOLIC_CALL, text)
    text = LOGAND_INSTR_PATTERN.sub(LOGAND_CALL, text)
    text = LOGOR_INSTR_PATTERN.sub(LOGOR_CALL, text)
    text = IS_SYMBOLIC_INSTR_PATTERN.sub(IS_SYMBOLIC_CALL, text)
    text = SYM_ASSUME_INSTR_PATTERN.sub(SYM_ASSUME_CALL, text)
    text = SYM_ASSERT_INSTR_PATTERN.sub(SYM_ASSERT_CALL, text)
    text = ALLOC_FUNC_CALL_PATTERN.sub(ALLOC_CALL, text)
    text = FREE_FUNC_CALL_PATTERN.sub(FREE_CALL, text)
    return text


def denormalize_allocator_calls(text: str) -> str:
    text = ALLOC_INSTR_PATTERN.sub(DENORMALIZED_ALLOC_CALL, text)
    text = DEALLOC_INSTR_PATTERN.sub(DENORMALIZED_DEALLOC_CALL, text)
    text = FREE_INSTR_PATTERN.sub(DENORMALIZED_FREE_CALL, text)
    text = PRINT_BTREE_LINE_PATTERN.sub("", text)
    text = PRINT_STACK_LINE_PATTERN.sub("", text)
    return text


def remove_trailing_invoke(text: str) -> str:
    return TRAILING_INVOKE_PATTERN.sub("", text).rstrip("\n") + "\n"


def wat_to_wasp_wast(text: str) -> str:
    symbolic_func_idx: str | None = None

    def replace_symbolic_import(match: re.Match[str]) -> str:
        nonlocal symbolic_func_idx
        symbolic_func_idx = match.group("idx")
        indent = match.group(1)
        type_idx = match.group("type")
        if symbolic_func_idx is None:
            return f"{indent}(func (type {type_idx}) unreachable)"
        return f"{indent}(func (;{symbolic_func_idx};) (type {type_idx}) unreachable)"

    text = SYMBOLIC_IMPORT_PATTERN.sub(replace_symbolic_import, text, count=1)
    if symbolic_func_idx is not None:
        call_pattern = re.compile(rf"^([ \t]*)call\s+{re.escape(symbolic_func_idx)}[ \t]*$", re.MULTILINE)
        text = call_pattern.sub(rf"\1{SYMBOLIC_INSTR}", text)
    return text


def default_output_path(input_path: pathlib.Path) -> pathlib.Path:
    if input_path.suffix:
        return input_path.with_name(f"{input_path.stem}.norm{input_path.suffix}")
    return input_path.with_name(f"{input_path.name}.norm")


def main() -> int:
    parser = argparse.ArgumentParser(description="Replace initial WAT prelude.")
    parser.add_argument("input", type=pathlib.Path, help="Input .wat file")
    parser.add_argument("output", nargs="?", type=pathlib.Path, help="Optional output file")
    parser.add_argument("-i", "--in-place", action="store_true", help="Write changes to input file")
    parser.add_argument(
        "--denormalize",
        action="store_true",
        help="Convert alloc/dealloc/free instructions to call $alloc/$dealloc/$free",
    )
    parser.add_argument(
        "--wat-to-wast",
        action="store_true",
        help="Convert normalized generated .wat into WASP-runnable .wast.",
    )
    args = parser.parse_args()

    if args.in_place and args.output is not None:
        parser.error("Cannot use --in-place and output path together.")

    original = args.input.read_text(encoding="utf-8", errors="replace")
    if args.denormalize and args.wat_to_wast:
        parser.error("Cannot use --denormalize and --wat-to-wast together.")

    if args.wat_to_wast:
        replaced = wat_to_wasp_wast(original)
    elif args.denormalize:
        replaced = denormalize_allocator_calls(original)
    else:
        replaced = replace_initial_prelude(original)
        replaced = replace_symbolic_instruction(replaced)
        replaced = replace_get_sym_int32_calls(replaced)
        if args.input.suffix == ".wast":
            replaced = remove_trailing_invoke(replaced)

    if args.in_place:
        args.input.write_text(replaced, encoding="utf-8")
    else:
        output_path = args.output if args.output is not None else default_output_path(args.input)
        output_path.write_text(replaced, encoding="utf-8")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
