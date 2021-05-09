#ifdef KLEE
#include "klee/klee.h"
#endif

// https://www.geeksforgeeks.org/n-queen-problem-backtracking-3/
#define N 5
#include <stdbool.h>
#include <stdio.h>
  
bool isSafe(int board[N][N], int row, int col) {
    int i, j;
    for (i = 0; i < col; i++)
        if (board[row][i])
            return false;
    for (i = row, j = col; i >= 0 && j >= 0; i--, j--)
        if (board[i][j])
            return false;
    for (i = row, j = col; j >= 0 && i < N; i++, j--)
        if (board[i][j])
            return false;
    return true;
}
  
bool solveNQUtil(int board[N][N], int col) {
    if (col >= N) return true;
    for (int i = 0; i < N; i++) {
        if (isSafe(board, i, col)) {
            board[i][col] = 1;
            if (solveNQUtil(board, col + 1))
                return true;
            board[i][col] = 0; // BACKTRACK
        }
    }
    return false;
}

bool solveNQ() {
    int board[N][N];
#ifdef KLEE
    klee_make_symbolic(board, sizeof(int) * N * N, "board");
#else
    make_symbolic(board, sizeof(int) * N * N);
#endif
    if (solveNQUtil(board, 0) == false) {
        return false;
    }
  
    return true;
}
  
int main()
{
    solveNQ();
    return 0;
}
