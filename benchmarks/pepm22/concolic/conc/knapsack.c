
 
int max(int a, int b) { return (a > b) ? a : b; }
 
int knapSack(int W, int wt[], int val[], int n)
{
    // Base Case
    if (n == 0 || W == 0) return 0;
 
    // If weight of the nth item is more than
    // Knapsack capacity W, then this item cannot
    // be included in the optimal solution
    if (wt[n - 1] > W)
        return knapSack(W, wt, val, n - 1);
 
    // Return the maximum of two cases:
    // (1) nth item included
    // (2) not included
    else
        return max(
            val[n - 1]
                + knapSack(W - wt[n - 1],
                           wt, val, n - 1),
            knapSack(W, wt, val, n - 1));
}

#define N 4
 
// Driver program to test above function
int main()
{
    //int val[N] = { 60, 100, 120, 130 };
    int val[N];
    val[0] = 60; val[1] = 100; val[2] = 120; val[3] = 130;
    int wt[N] = {0, 0, 0, 0};
    mark_symbolic(wt, sizeof(int) * N, sizeof(int));
    int W = 50;
    knapSack(W, wt, val, N);
    return 0;
}
