#include <stdio.h>
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
  
  int x = 1;

  mark_symbolic(&x, sizeof(x), sizeof(int));
  mark_symbolic(arr, sizeof(arr), sizeof(int));

  int result = binary_search(12, x);
  return 1;
}
