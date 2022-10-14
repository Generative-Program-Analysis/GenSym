#include <sys/stat.h>
#include <sys/types.h>
#include <stdbool.h>
void make_symbolic(void *addr, size_t nbytes);
void gs_assert(bool);
//#pragma pack (0)
typedef struct {
  unsigned size;  /* in bytes */
  char* contents;
  struct stat* stat;
} exe_disk_file_t;

typedef struct {
  int fd;                   /* actual fd if not symbolic */
  unsigned flags;           /* set of exe_file_flag_t values. fields
                               are only defined when flags at least
                               has eOpen. */
  off_t off;              /* offset */
  exe_disk_file_t* dfile;   /* ptr to file on disk, if symbolic */
} exe_file_t;

#define MAX_FDS 32

/* Note, if you change this structure be sure to update the
   initialization code if necessary. New fields should almost
   certainly be at the end. */
typedef struct {
  exe_file_t fds[MAX_FDS];
  mode_t umask; /* process umask */
  unsigned version;
  /* If set, writes execute as expected.  Otherwise, writes extending
     the file size only change the contents up to the initial
     size. The file offset is always incremented correctly. */
  int save_all_writes;
  int padding;
} exe_sym_env_t;

exe_sym_env_t __exe_env;

int main()
{
  exe_file_t *ftemp;
  sym_print(__exe_env.fds[1].flags);
  for (int i=0; i< MAX_FDS; i++) {
    ftemp = &__exe_env.fds[i];
    make_symbolic(&ftemp->flags, sizeof(ftemp->flags));
  }
  sym_print(__exe_env.fds[1].flags);
  __exe_env.fds[1].flags = 7;
  ftemp = __exe_env.fds;
  sym_print(__exe_env.fds[1].flags);
  if ((ftemp + 1)->flags == 7) {
    if ((ftemp + 31)->flags == 31) {
      sym_print((ftemp + 31)->flags);
    } else {
      if (__exe_env.fds[31].flags == 31) {
        sym_exit(2);
      } else {
        gs_assert(__exe_env.fds[31].flags != 31);
      }
    }
  } else {
    sym_exit(3);
  }
}