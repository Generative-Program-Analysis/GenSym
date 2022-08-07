typedef struct {
  char dummy_packed;
  unsigned size;  /* in bytes */
  char* contents;
} __attribute__((packed)) exe_disk_file_t;

typedef struct {
  unsigned n_sym_files; /* number of symbolic input files, excluding stdin */
  exe_disk_file_t *sym_stdin, *sym_stdout;
  unsigned stdout_writes; /* how many chars were written to stdout */
  char dummy_packed;
  exe_disk_file_t *sym_files;
  /* --- */
  /* the maximum number of failures on one path; gets decremented after each failure */
  unsigned max_failures;

  /* Which read, write etc. call should fail */
  int *read_fail, *write_fail, *close_fail, *ftruncate_fail, *getcwd_fail;
  int *chmod_fail, *fchmod_fail;
} __attribute__((packed)) exe_file_system_t;

exe_file_system_t __exe_fs;

static void __create_new_dfile(exe_disk_file_t *dfile, unsigned size) {

  llsc_assert(size);

  dfile->size = size;
  dfile->contents = malloc(dfile->size);
  if (!dfile->contents) sym_exit(-1);
  make_symbolic(dfile->contents, dfile->size);
}

/* n_files: number of symbolic input files, excluding stdin
   file_length: size in bytes of each symbolic file, including stdin */
void klee_init_fds(unsigned n_files, unsigned file_length) {
  unsigned k;

  __exe_fs.n_sym_files = n_files;
  __exe_fs.sym_files = malloc(sizeof(*__exe_fs.sym_files) * n_files);
  if (n_files && !__exe_fs.sym_files) sym_exit(-1);

  for (k=0; k < n_files; k++) {
    __create_new_dfile(&__exe_fs.sym_files[k], file_length);
  }
}

int main()
{
  // expected paths: 2
  klee_init_fds(2, 10);
  if (__exe_fs.sym_files[1].contents[2]) {
    sym_print(__exe_fs.sym_files[1].contents[2]);
  } else {
    sym_print(__exe_fs.sym_files[1].contents[2]);
  }
  return 0;
}
