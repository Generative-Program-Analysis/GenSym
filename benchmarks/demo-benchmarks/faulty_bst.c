#include <stdio.h>
#include <assert.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#ifdef KLEE
#include "klee/klee.h"
#endif

#define SIZE 4

typedef struct node_s {
    struct node_s* left;
    struct node_s* right;
    int val;
} Node;

Node* init_node(int val) {
    Node* new_node = malloc(sizeof(Node));
    new_node->val = val;

    // Because these are left uninitialized,
    // there may be an error in insert and find
    /* new_node->left = NULL; */
    /* new_node->right = NULL; */

    return new_node;
}

Node* bst_insert(Node* root, int val) {
    if (root == NULL) {
        return init_node(val);
    } 

    assert(root != NULL);
    if (root->val == -1) {
        return init_node(val);
    }

    if (val > root->val) {
        root->right = bst_insert(root->right, val);
        return root;
    } else {
        root->left = bst_insert(root->left, val);
        return root;
    }
}

Node* bst_from_list(int* arr, int len) {
    Node* root = NULL;
    for (int i = 0; i < len; i += 1) {
        root = bst_insert(root, arr[i]);
    }

    return root;
}

/* void bst_print_aux(Node* root) { */
/*     if (root != NULL) { */
/*         printf("%d ", root->val); */
/*         bst_print_aux(root->left); */
/*         bst_print_aux(root->right); */
/*     } */
/* } */

/* void bst_print(Node* root) { */
/*     bst_print_aux(root); */
/*     printf("\n"); */
/* } */

bool bst_find(Node* root, int key) {
    if (root == NULL || root->val == -1) {
        return false;
    } else if (key == root->val) {
        return true;
    } else if (key > root->val) {
        return bst_find(root->right, key);
    } else {
        return bst_find(root->left, key);
    }
}

int main() {
    int data[SIZE];
    int key;
    /* for (int i = 0; i < SIZE; i += 1) { */
    /*     data[i] = i; */
    /* } */
#ifdef KLEE
    klee_make_symbolic(data, sizeof(data), "data");
    klee_make_symbolic(&key, sizeof(int), "key");
#else
    make_symbolic(data, sizeof(int) * SIZE, "data");
    make_symbolic(&key, sizeof(int), "key");
#endif

    Node* tree = bst_from_list(data, SIZE);
    /* bst_print(tree); */
    bst_find(tree, key);
}
