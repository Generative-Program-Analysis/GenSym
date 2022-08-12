#include <stdio.h>
#include <stdint.h>
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <stdbool.h>

#define LLSC_TEST_FILE "LLSC_TEST_FILE"

int32_t is_key(char* key, char* name_prefix, size_t size) {
  char* p = strstr(key, name_prefix);
  if (p == NULL) return -1;
  assert(strlen(name_prefix) < size);
  char* n = p + strlen(name_prefix);
  int32_t v = strtol(n, (char**)NULL, 10);
  assert(v >= 0);
  return v;
}

// name can only be alphabet letters, containing no numbers
void make_symbolic(void* addr, size_t byte_size, char* name_prefix) {
  const char* path = getenv(LLSC_TEST_FILE);
  char* line = NULL;
  size_t len = 0;
  ssize_t read;
  FILE* fp = fopen(path, "r");
  if (fp == NULL) {
    fprintf(stderr, "Test file %s not exists\n", path);
    exit(-1);
  }
  fprintf(stdout, "Reading test case from %s\n", path);

  uint8_t* reified_data = (uint8_t*)malloc(byte_size);
  while ((read = getline(&line, &len, fp)) != -1) {
    char* value_ptr = strstr(line, "=");
    if (value_ptr == NULL) continue;
    size_t key_size = value_ptr - line;
    line[key_size] = '\0';
    //printf("%s, %s", line, value_ptr+1);
    int idx = is_key(line, name_prefix, key_size);
    if (idx == -1) continue;
    assert(idx >= 0 && idx < byte_size);
    uint8_t v = (uint8_t) strtol(value_ptr+1, (char **)NULL, 10);
    reified_data[idx] = v;
    //printf("%s%d -> %d\n", name_prefix, idx, v);
  }
  memcpy(addr, (void*)reified_data, byte_size);
  free(reified_data);
  free(line);
}

void make_symbolic_whole(void* addr, size_t byte_size, char* name) {

}

/*
int main() {
  int x;
  make_symbolic(&x, 4, "x");
  if (x <= 10) {
    printf("%d\n", x);
  } else {
    if (x <= 15) {
      printf("%d\n", x);
    } else {
      printf("%d\n", x);
    }
  }
  return 0;
}
*/
