// example run
// cc -O3 query_t1gram2.hand.c; ./a.out ../data/t1gram.csv

#include <stdio.h>

#define MAX_LINE_LENGTH 1000

int main(int argc, char *argv[])
{
  if (argc != 2) {
    printf("usage: query <filename>\n");
    return 0;
  }

  printf("Phrase,Year,MatchCount,VolumeCount\n");

  char *phrase = "Auswanderung";
  char line[MAX_LINE_LENGTH];
  FILE *f = fopen(argv[1], "r");

  while (feof(f)==0) {
    fscanf(f,"%[^\n]\n", line); // no overflow check

    char* cur_phrase = phrase;
    char* cur_line = line;

    while (*cur_line != '\t' && *cur_phrase != '\0' &&
           *cur_line++ == *cur_phrase++) {}

    if (*cur_line == '\t' && *cur_phrase == '\0') {
      while (*cur_line != '\0') {
        if (*cur_line == '\t') {
          *cur_line = ',';
        }
        cur_line++;
      }

      printf("%s\n", line);
    }
  }

  close(f);
}
