#!/usr/bin/env python3
import os
import sys
import csv
import time
import glob
import json
import argparse
import subprocess

# Globals
MAPPING = {
    'black'  : 90,
    'red'    : 91,
    'green'  : 92,
    'yellow' : 93,
    'blue'   : 94,
    'purple' : 95,
    'cyan'   : 96,
    'white'  : 97
}

BOLD = '\033[1m'
PREFIX = '\033['
SUFFIX = '\033[0m'

TIME_LIMIT=60
INST_LIMIT=10

def progress(msg, curr, total, prev=0):
    status = round((curr / total) * 100)
    color = MAPPING.get('cyan')
    prog_str = f'{BOLD}{PREFIX}{color}m{status:3}%{SUFFIX}'
    sys.stdout.write('\r')
    sys.stdout.write(' ' * prev)
    sys.stdout.write('\r')
    sys.stdout.write(f'[{prog_str}] {msg}')
    sys.stdout.flush()
    return len(msg) + 7

def warning(msg, prefix=None):
    if prefix:
        sys.stdout.write(prefix)
    color = MAPPING.get('purple')
    warn_str = f'{BOLD}{PREFIX}{color}mWARN{SUFFIX}'
    sys.stdout.write(f'[{warn_str}] {msg}\n')
    sys.stdout.flush()

def info(msg, prefix=None):
    if prefix:
        sys.stdout.write(prefix)
    color = MAPPING.get('green')
    warn_str = f'{BOLD}{PREFIX}{color}mINFO{SUFFIX}'
    sys.stdout.write(f'[{warn_str}] {msg}\n')
    sys.stdout.flush()

def indent(msg, prefix=None):
    if prefix:
        sys.stdout.write(prefix)
    color = MAPPING.get('white')
    ident_str = f'{BOLD}{PREFIX}{color}m....{SUFFIX}'
    sys.stdout.write(f'[{ident_str}] {msg}\n')
    sys.stdout.flush()

def execute_wasp(test: str, output_dir: str):

    def _cmd(test, output_dir, instr_limit):
        return [
            'wasp', test,
            '-u',
            '-e', '(invoke \"__original_main\")',
            '-m', str(instr_limit),
            '--workspace', output_dir,
            '-static'
        ]

    try:
        result = subprocess.run(
            _cmd(test, output_dir, INST_LIMIT * 1000 * 1000),
            timeout=TIME_LIMIT,
            capture_output=True,
            check=True
        )
    except subprocess.CalledProcessError as e:
        warning(f'\'{test}\': crashed', prefix='\n')
        return None
    except subprocess.TimeoutExpired as e:
        warning(f'\'{test}: timeout', prefix='\n')
        return None

    return result.stdout


def run_test(test):
    output_dir = os.path.join('output',
                              os.path.basename(os.path.dirname(test)),
                              os.path.basename(test))

    tstart = time.time()
    execute_wasp(test, output_dir)
    tdelta = time.time() - tstart

    report_file = os.path.join(output_dir, 'report.json')
    if not os.path.exists(report_file):
        warning(f'File not found \'{report_file}\'!', prefix='\n')
        return []

    with open(report_file, 'r') as f:
        report = json.load(f)

    return {
        'twasp' : tdelta,
        'paths' : report['paths_explored'],
        'tloop' : report['loop_time'],
        'tsolv' : report['solver_time'],
        'specification': report['specification'],
    }


def run_tests(dirs, output_file='results.csv'):
    results, errors = [], []
    for i, dir in enumerate(dirs):
        prev = 0
        sum_paths, sum_twasp, sum_tloop, sum_tsolv = 0, 0.0, 0.0, 0.0
        info(f'Running tests in \'{dir}\'...', prefix='\n' if i > 0 else '')
        tests = glob.glob(os.path.join(dir, '*.wat'))
        for i, test in enumerate(tests):
            prev = progress(f'Running \'{test}\'...', i+1, len(tests), prev=prev)

            result = run_test(test)
            if result == []:
                continue

            if not result['specification']:
                errors.append(test)

            sum_twasp += result['twasp']
            sum_paths += result['paths']
            sum_tloop += float(result['tloop'])
            sum_tsolv += float(result['tsolv'])

        results.append([
            os.path.basename(dir),
            len(tests),
            round(sum_paths),
            round(sum_twasp, 3),
            round(sum_tloop, 3),
            round(sum_tsolv, 3)
        ])

    if errors:
        warning('Failed tests:', prefix='\n')
        for i, test in enumerate(errors):
            indent(f'{i+1}. \'{test}\'')

    info(f'Finished. Writing results to \'{output_file}\'...', 
         prefix='' if errors else '\n')
    with open(output_file, 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['category', 'ni', 'sum-paths', 'Twasp', 'Tloop', 'Tsolver'])
        writer.writerows(results)


def get_parser():
    parser = argparse.ArgumentParser(
        prog='./run.py',
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )

    parser.add_argument(
        '--normal',
        '-N',
        dest='normal',
        action='store_true',
        default=False,
        help='run normal test for Collections-C'
    )

    parser.add_argument(
        '--bugs',
        '-B',
        dest='bugs',
        action='store_true',
        default=False,
        help='run test that trigger bugs in Collections-C'
    )

    parser.add_argument(
        '--all',
        dest='all',
        action='store_true',
        default=False,
        help='run all tests'
    )

    parser.add_argument(
        '--single',
        dest='target',
        action='store',
        default=None,
        help='run tests in the pointed directory'
    )

    return parser

def parse(argv):
    parser = get_parser()
    return parser.parse_args(argv)

def main(argv=None):
    if argv is None:
        argv = sys.argv[1:]
    args = parse(argv)

    if not args.target is None:
        base_dir = os.path.basename(args.target)
        output_file = f'results_{base_dir}.csv'
        info(f'Starting \'{base_dir}\' Collections-C benchmarks...')
        run_tests([args.target], output_file=output_file)

    if args.normal or args.all:
        info('Starting \'normal\' Collections-C benchmarks...')
        run_tests(glob.glob('_build/for-wasp/normal/*'), 
                  output_file='results_normal.csv')

    if args.bugs or args.all:
        info('Sarting \'bugs\' Collections-C benchmarks...')
        run_tests(glob.glob('_build/for-wasp/bugs/*'),
                  output_file='results_bugs.csv')

    return 0

if __name__ == '__main__':
    sys.exit(main())
