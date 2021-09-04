
#define SIZE 7

#include <limits.h>
int arr[SIZE] = {0, 0, 0, 0, 0, 0, 0};

// A Divide and Conquer based program for maximum subarray  sum problem
// https://www.geeksforgeeks.org/maximum-subarray-sum-using-divide-and-conquer-algorithm/
// A utility funtion to find maximum of two integers
int max(int a, int b) { return (a > b) ? a : b; }
 
// A utility funtion to find maximum of three integers
int max3(int a, int b, int c) { return max(max(a, b), c); }
 
// Find the maximum possible sum in arr[] auch that arr[m]
// is part of it
int maxCrossingSum(int l, int m, int h)
{
    // Include elements on left of mid.
    int sum = 0;
    int left_sum = INT_MIN;
    for (int i = m; i >= l; i--) {
        sum = sum + arr[i];
        if (sum > left_sum)
            left_sum = sum;
    }
 
    // Include elements on right of mid
    sum = 0;
    int right_sum = INT_MIN;
    for (int i = m + 1; i <= h; i++) {
        sum = sum + arr[i];
        if (sum > right_sum)
            right_sum = sum;
    }
 
    // Return sum of elements on left and right of mid
    // returning only left_sum + right_sum will fail for
    // [-2, 1]
    return max3(left_sum + right_sum, left_sum, right_sum);
}
 
// Returns sum of maxium sum subarray in aa[l..h]
int maxSubArraySum(int l, int h)
{
    // Base Case: Only one element
    if (l == h)
        return arr[l];
 
    // Find middle point
    int m = (l + h) / 2;
 
    /* Return maximum of following three possible cases
            a) Maximum subarray sum in left half
            b) Maximum subarray sum in right half
            c) Maximum subarray sum such that the subarray
       crosses the midpoint */
    return max3(maxSubArraySum(l, m),
               maxSubArraySum(m + 1, h),
               maxCrossingSum(l, m, h));
}
 
/*Driver program to test maxSubArraySum*/
int main()
{

    mark_symbolic(arr, sizeof(int) * SIZE,  sizeof(int));

    int n = SIZE; //sizeof(arr) / sizeof(arr[0]);
    int max_sum = maxSubArraySum(0, n - 1);
    return 0;
}
