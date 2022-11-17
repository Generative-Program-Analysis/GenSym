#include <stdio.h>
#include <assert.h>
#include <stdbool.h>
#include <stdlib.h>
#ifdef KLEE
#include "klee/klee.h"
#endif

#define SIZE 4

void bst_insert(int* arr, int key) {
    int i = 0;
    while (arr[i] != -1) {
        if (key > arr[i]) {
            i = i * 2 + 2;
        } else /* key <= arr[i] */ {
            i = i * 2 + 1;
        }
    }

    arr[i] = key;
}

bool bst_find(int* arr, int key) {
    int i = 0;
    while (arr[i] != -1) {
        if (key > arr[i]) {
            i = i * 2 + 2;
        } else if (key < arr[i]) {
            i = i * 2 + 1;
        } else /* key == arr[i] */ {
            return true;
        }
    }

    return false;
}

int* bst_from_list(int* list, int size) {
    int* bst = malloc(sizeof(int) * 500 * SIZE);
    for (int i = 0; i < 500 * SIZE; i += 1) {
        bst[i] = -1;
    }

    for (int i = 0; i < size; i += 1) {
        bst_insert(bst, list[i]);
    }

    return bst;
}

int main() {
    int data[SIZE];
    int key;
#ifdef KLEE
    klee_make_symbolic(data, sizeof(data), "data");
    klee_make_symbolic(&key, sizeof(int), "key");
#else
    make_symbolic(data, sizeof(int) * SIZE, "data");
    make_symbolic(&key, sizeof(int), "key");
#endif

    int* tree = bst_from_list(data, SIZE);
    /* bst_print(tree); */
    bst_find(tree, key);
}
