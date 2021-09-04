// https://www.geeksforgeeks.org/k-th-element-two-sorted-arrays/
// Program to find kth element from two sorted arrays
 
int kth(int arr1[], int arr2[], int m, int n, int k)
{
    int* sorted1 = malloc((m + n) * sizeof(int));
    int i = 0, j = 0, d = 0;
    while (i < m && j < n)
    {
        if (arr1[i] < arr2[j])
            sorted1[d++] = arr1[i++];
        else
            sorted1[d++] = arr2[j++];
    }
    while (i < m)
        sorted1[d++] = arr1[i++];
    while (j < n)
        sorted1[d++] = arr2[j++];
    return sorted1[k - 1];
}

#define N 5

// Driver Code
int main()
{
    int arr1[N] = {0, 0, 0, 0, 0};
    int arr2[N] = {0, 0, 0, 0, 0};
    int k = 5;

    mark_symbolic(arr1, sizeof(int) * N, sizeof(int));
    mark_symbolic(arr2, sizeof(int) * N, sizeof(int));
    
    kth(arr1, arr2, N, N, k);

    return 0;
}
