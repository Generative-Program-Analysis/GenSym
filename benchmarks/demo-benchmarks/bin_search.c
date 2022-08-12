#include <stdio.h>
#include <assert.h>
#ifdef KLEE
#include "klee/klee.h"
#endif

#define SIZE 10

void print_data(int arr[], int size, int target) {
    printf("searching for %d in:\n[", target);
    for (int i=0; i < size-1; i++) {
        printf("%d, ", arr[i]);
    }
    printf("%d]\n", arr[size-1]);
}

int binary_search(int arr[], int size, int target) {
    print_data(arr, size, target);
    int low = 0;
    int high = size - 1;
    int mid;
    while (low <= high) {
        mid = (low + high)/2;
        if (arr[mid] == target) {
            return mid;
        }
        if (arr[mid] < target) {
            low = mid + 1;
        }
        if (arr[mid] > target) {
            high = mid - 1;
        }
    }
    return -1;
}

int main() {
  int a[SIZE];
  int x;
#ifdef KLEE
  klee_make_symbolic(&a, SIZE * sizeof(int), "a");
  klee_make_symbolic(&x, sizeof(x), "x");
#else
  make_symbolic(&a, SIZE * sizeof(int));
  make_symbolic(&x, sizeof(x));
#endif
  int result = binary_search(a, 10, x);
  printf("result = %d\n", result);
  // check correctness
  if (result != -1) {
    assert(a[result] == x);
  } else {
    // if result == -1, then we didn't find it. Therefore, it shouldn't be in the array
    for (int i = 0; i < 10; i++) {
      assert(a[i] != x);
    }
  }
  return 1;
}
