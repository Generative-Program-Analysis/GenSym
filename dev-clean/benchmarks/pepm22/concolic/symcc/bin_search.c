#include <stdint.h>
#include <stdio.h>
#include <unistd.h>

 
int arr[12];
int binary_search(int size, int target) {
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

  int x;
  if (read(STDIN_FILENO, arr, sizeof(arr)) != sizeof(arr)) {
        fprintf(stderr, "Failed to read the input1\n");
        return -1;
  }
  if (read(STDIN_FILENO, &x, sizeof(x)) != sizeof(x)) {
        fprintf(stderr, "Failed to read the input2\n");
        return -1;
  }
  int result = binary_search(12, x);

  return 1;
}
