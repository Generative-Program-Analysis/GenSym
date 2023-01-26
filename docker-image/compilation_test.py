#!/usr/bin/env python3
from pathlib import Path
from contextlib import contextmanager
from subprocess import DEVNULL, PIPE, STDOUT
from collections import Counter
import subprocess as subp
import argparse
import json
import time
import pandas as pd

parser = argparse.ArgumentParser()
parser.add_argument('--ir-files', action='store', type=Path, default='../coreutils-linked/separate')
parser.add_argument('--make-cores', action='store', type=int, default=192)
subparsers = parser.add_subparsers()
# prepare
p = subparsers.add_parser('prepare')
p.set_defaults(task_prepare=True, require_gs=True)
p.add_argument('--no-build', dest='build_cpp', action='store_false', default=True)
# run
p = subparsers.add_parser('run')
p.set_defaults(task_run=True, require_gs=True, task_format=True)
p.add_argument('--restart-json', action='store', type=Path)
p.add_argument('--repeat-num', action='store', type=int, default=5)
# format
p = subparsers.add_parser('format')
p.set_defaults(task_format=True)
p.add_argument('restart_json', action='store', type=Path)
argv = vars(parser.parse_args())


class StopWatch:
    def __enter__(self):
        self.start = time.time()
        return self

    def __exit__(self, *args):
        self.stop = time.time()
        return False

    def __call__(self):
        return self.stop - self.start


cur = Path().resolve()
ll_dir = argv['ir_files'].resolve()
make_parallel = ['make', '-j', str(argv['make_cores'])]

gs_launcher = "java -Xms4G -Xmx32G -Xss1024M -XX:MaxMetaspaceSize=8G -XX:ReservedCodeCacheSize=2048M -jar".split()
if argv.get('require_gs'):
    subp.run("sbt assembly".split(), check=True)
    jarfile, = list(cur.glob('target/scala-*/GenSym-*.jar'))
    gs_launcher.append(str(jarfile))

if argv.get('task_prepare'):
    irfile = ll_dir / 'libc_kl_posix.ll'
    for noopt in [[], ['--noOpt']]:
        outname = 'libc_klp_' + ('npt' if noopt else 'opt')
        args = [*gs_launcher, '--engine=lib', '--use-argv', *noopt, f'--output={outname}', irfile]
        subp.run(args, check=True)

if argv.get('require_gs'):
    libopt, = list(cur.glob('gs_gen/libc_klp_opt'))
    libnpt, = list(cur.glob('gs_gen/libc_klp_npt'))

if argv.get('task_prepare') and argv['build_cpp']:
    subp.run(make_parallel, check=True, cwd=libopt)
    subp.run(make_parallel, check=True, cwd=libnpt)


general_args = "--output-tests-cov-new --thread=1 --search=random-path --solver=z3 --output-ktest --timeout=3600 --max-sym-array-size=4096".split()
program_arg = {
    "base32":  "--sym-stdout --sym-stdin 2 --sym-arg 2 -sym-files 2 2",
    "base64":  "--sym-stdout --sym-stdin 2 --sym-arg 2 -sym-files 2 2",
    "cat":     "--sym-stdout --sym-stdin 2 --sym-arg 2",
    "comm":    "--sym-stdout --sym-stdin 2 --sym-arg 2 --sym-arg 1 -sym-files 2 2",
    "cut":     "--sym-stdout --sym-stdin 2 --sym-arg 2 --sym-arg 2 -sym-files 2 2",
    "dirname": "--sym-stdout --sym-stdin 2 --sym-arg 6 --sym-arg 10",
    "echo":    "--sym-stdout --sym-arg 2 --sym-arg 7",
    "expand":  "--sym-stdout --sym-stdin 2 --sym-arg 2 -sym-files 2 2",
    "true":    "--sym-stdout --sym-arg 10",
    "false":   "--sym-stdout --sym-arg 10",
    "fold":    "--sym-stdout --sym-stdin 2 --sym-arg 2 -sym-files 2 2",
    "join":    "--sym-stdout --sym-stdin 2 --sym-arg 2 --sym-arg 1 -sym-files 2 2",
    "link":    "--sym-stdout --sym-stdin 2 --sym-arg 2 --sym-arg 1 --sym-arg 1 -sym-files 2 2",
    "paste":   "--sym-stdout --sym-stdin 2 --sym-arg 2 --sym-arg 1 -sym-files 2 2",
    "pathchk": "--sym-stdout --sym-stdin 2 --sym-arg 2 --sym-arg 2 -sym-files 2 2",
}


if argv.get('task_format'):
    restartfile = argv.get('restart_json')
    if restartfile and restartfile.is_file():
        with restartfile.open() as fp:
            timings = json.load(fp)
        cachemap = Counter((a, p) for a, p, t in timings)
    else:
        timings, cachemap = [], {}


if argv.get('task_run'):
    repeat_num, applist = argv['repeat_num'], []
    gs_args = "--engine=app --use-argv --main-opt=O3".split()

    arglist = []  # collect apps and scala tasks
    for item in ll_dir.glob('*.ll'):
        if not item.stem.startswith('libc'):
            for noopt in [[], ['--noOpt']]:
                binname = item.stem + '_' + ('npt' if noopt else 'opt')
                applist.append(binname)
                if cachemap.get((binname, 'scala'), 0) < repeat_num * 2:
                    libpath = libnpt if noopt else libopt
                    arglist.append([*gs_args, *noopt, f'--lib={libpath}', f'--output={binname}', item])

    try:  # finally: dump to restart
        if arglist:
            repeat_arg = ','.join(str(len(i)) for i in arglist)
            repeat_arg = f'--repeat={repeat_num * 2},{repeat_arg}'
            args = sum(arglist, start=[*gs_launcher, repeat_arg])

            p = subp.Popen(args, stdout=DEVNULL, stderr=PIPE, text=True)
            while ln := p.stderr.readline().strip():
                assert ln.startswith('run repeat mode:'), ln
                txt, name, t_scala = ln.rsplit(' ', 2)
                timings.append([name, 'scala', float(t_scala)/1000])
                print(timings[-1])
            assert p.poll() == 0, p.returncode

        for item in applist:
            cwd = cur / 'gs_gen' / item

            if cachemap.get((item, 'cloc'), 0) < 1:
                p = subp.run('cloc .'.split(), check=True, capture_output=True, text=True, cwd=cwd)
                cloc, = [l.split()[-1] for l in p.stdout.splitlines() if l.startswith('C++')]
                timings.append([item, 'cloc', int(cloc)])
                print(timings[-1])

            if cachemap.get((item, 'cpp'), 0) < repeat_num:
                t_cpp = StopWatch()
                for n in range(repeat_num):
                    subp.run('make clean'.split(), check=True, stdout=DEVNULL, cwd=cwd)
                    with t_cpp:
                        subp.run(make_parallel, check=True, stdout=DEVNULL, cwd=cwd)
                    timings.append([item, 'cpp', t_cpp()])
                    print(timings[-1])

            if cachemap.get((item, 'exec'), 0) < repeat_num:
                binopts = program_arg[item.split('_')[0]]
                args = [f'./{item}', *general_args, f'--argv=./{item} {binopts}']
                t_exec = StopWatch()
                for n in range(repeat_num):
                    with t_exec:
                        subp.run(args, check=True, stdout=DEVNULL, stderr=DEVNULL, cwd=cwd)
                    timings.append([item, 'exec', t_exec()])
                    print(timings[-1])

    finally:
        if restartfile:
            with restartfile.open('w') as f:
                json.dump(timings, f, indent=2)


colnames = r'\textbf{$T^\woopt_\scala$} & \textbf{$T^\woopt_\cpp$} & \textbf{$T^\wopt_\scala$} & \textbf{$T^\wopt_\cpp$} & Code Size & \textbf{$T_\text{exec}$}'.split(' & ')
if argv.get('task_format'):
    df = pd.DataFrame(timings, columns=['app', 'phase', 'count']).convert_dtypes()
    tmp = df['app'].str.rsplit('_', n=1, expand=True)
    df['app'] = tmp[0]
    df['conf'] = tmp[1]
    df['rank'] = df.reset_index().groupby(['app', 'phase', 'conf'])['index'] \
                   .rank('first').astype('int32')
    pv = pd.pivot_table(df, index='app', columns=['phase', 'conf'], values='count', aggfunc='median')
    pv.columns = pv.columns.to_flat_index()
    pv[('scala', 'o/n')] = pv[[('scala', 'opt'), ('scala', 'npt')]].apply(tuple, axis=1)
    pv[('cpp', 'o/n')] = pv[[('cpp', 'opt'), ('cpp', 'npt')]].apply(tuple, axis=1)
    pv[('cloc', 'o/n')] = pv[[('cloc', 'opt'), ('cloc', 'npt')]].apply(tuple, axis=1)
    pv[('exec', 'o/n')] = pv[[('exec', 'opt'), ('exec', 'npt')]].apply(tuple, axis=1)
    pv2 = pv[[('scala', 'npt'), ('cpp', 'npt'),
              ('scala', 'o/n'), ('cpp', 'o/n'),
              ('cloc', 'o/n'), ('exec', 'o/n')]].drop('false')
    formatters = {
        ('scala', 'o/n'): lambda x: f'{x[0]:.2f} ({(x[0] - x[1]):+.2f})',
        ('cpp', 'o/n'): lambda x: f'{x[0]:.2f} ({(x[0] - x[1]):+.2f})',
        ('cloc', 'o/n'): lambda x: f'{(x[0] / x[1] - 1):.2%}'.replace('%', '\%'),
        ('exec', 'o/n'): lambda x: f'{(x[0] / x[1] - 1):.2%}'.replace('%', '\%'),
    }
    print(pv2.to_latex(header=colnames, escape=False, index_names=False,
        formatters=formatters, float_format='{:.2f}'.format, column_format='ccccccc'))
