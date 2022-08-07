#define _LARGEFILE64_SOURCE
#include "../../headers/llsc_client.h"
#include "fd.h"
#include <fcntl.h>
#include <assert.h>
#include <sys/types.h>
#include <unistd.h>
#include <string.h>

int main()
{
  klee_init_fds(2, 10,
                   0, 0,
                   0, 0);
  char filename[] = "A";
  char buf1[5] = "1234";
  char buf2[5] = "abcd";
  int fd1, fd2;
  int res;
  int off;

  fd1 = klee_open(filename, O_CREAT | O_RDWR, S_IRWXU | S_IRWXG | S_IRWXO);
  if (fd1 < 0) return -1;
  fd2 = klee_open("B", O_CREAT | O_RDWR, S_IRWXU | S_IRWXG | S_IRWXO);
  if (fd2 < 0) return -1;
  print_string("file descriptor:\n");
  sym_print(fd1, fd2);
  print_string("original buf1:\n");
  sym_print(buf1[0], buf1[1], buf1[2], buf1[3]);
  print_string("original buf2:\n");
  sym_print(buf2[0], buf2[1], buf2[2], buf2[3]);
  res = klee_read(fd1, buf1, 4);
  assert(4 == res || 0 == res);
  print_string("new buf1 after read from fd1:\n");
  sym_print(buf1[0], buf1[1], buf1[2], buf1[3]);
  print_string("write buf2 to fd2\n");
  res = klee_write(fd2, buf2, 4);
  assert(4 == res);
  off = klee_lseek(fd2, 0 ,SEEK_CUR);
  assert(4 == off);
  off = klee_lseek(fd2, 0 ,SEEK_SET);
  assert(0 == off);
  print_string("write and lseek successful\n");
  memset(buf1, 0, 5);
  res = klee_read(fd2, buf1, 4);
  assert(4 == res);
  print_string("new buf1 after read from fd2:\n");
  sym_print(buf1[0], buf1[1], buf1[2], buf1[3]);
  assert('a' == buf1[0]);
  assert('b' == buf1[1]);
  assert('c' == buf1[2]);
  assert('d' == buf1[3]);
  klee_close(fd1);
  klee_close(fd2);
  struct stat64 s;
  klee_stat(".", &s);
  sym_print(s.st_mode);
  sym_print(s.st_size);
  sym_print(s.st_nlink);
  print_string("close successfully\n\n\n");
  return 0;
}