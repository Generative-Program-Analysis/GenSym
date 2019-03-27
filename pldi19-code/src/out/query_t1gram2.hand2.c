// example run
// cc -O3 query_t1gram2.hand.c; ./a.out ../data/t1gram.csv

#include <stdio.h>

#include <string.h>
#include <fcntl.h>
#include <errno.h>
#include <err.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <stdint.h>
#ifndef MAP_FILE
#define MAP_FILE MAP_SHARED
#endif

#define MAX_LINE_LENGTH 1024*257

int main(int argc, char *argv[])
{
  if (argc != 2) {
    printf("usage: query <filename>\n");
    return 0;
  }

  printf("Phrase,Year,MatchCount,VolumeCount\n");

  char *phrase_cmp = "Auswanderung";
  size_t phrase_cmp_len = strlen(phrase_cmp);
  char line[MAX_LINE_LENGTH];

  int fd = open(argv[1], 0);
  if (fd < 0) printf("errno = %s\n", strerror(errno));

  struct stat stat;
  int fsv = fstat(fd, &stat);
  if (fsv < 0) printf("errno = %s\n", strerror(errno));

  size_t len = stat.st_size;
  void* addr = mmap(0, len, PROT_READ, MAP_FILE | MAP_SHARED, fd, 0);
  if (addr <= 0) printf("errno = %s\n", strerror(errno));

  char* end = addr + len;
  char* cur_line = addr;
  while (cur_line < end) {

    char* phrase_start = cur_line;
    char* phrase_end = phrase_start;
    while (*phrase_end++ != '\t');

    int phrase_len = phrase_end - phrase_start - 1;

    char* year_start = phrase_end;
    char* year_end = year_start;
    while (*year_end++ != '\t');

    int year_len = year_end - year_start - 1;

    char* match_start = year_end;
    char* match_end = match_start;
    while (*match_end++ != '\t');

    int match_len = match_end - match_start - 1;

    char* vol_start = match_end;
    char* vol_end = vol_start;
    while (*vol_end++ != '\n');

    int vol_len = vol_end - vol_start - 1;

    char* line_end = vol_end;

    if ((phrase_cmp_len == phrase_len) && memcmp(phrase_cmp, phrase_start, phrase_len) == 0) {
      char *cur = line;
      strncpy(cur, phrase_start, phrase_len); cur += phrase_len;
      *cur++ = ',';
      strncpy(cur, year_start, year_len); cur += year_len;
      *cur++ = ',';
      strncpy(cur, match_start, match_len); cur += match_len;
      *cur++ = ',';
      strncpy(cur, vol_start, vol_len); cur += vol_len;
      *cur++ = '\0';

      puts(line);
    }
    cur_line = line_end;
  }

  //close(fd);
}
