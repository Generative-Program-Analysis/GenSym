#include "../../../headers/llsc_client.h"

int main( ) {
  int a[ 100000 ];
  int i = 0;
  int x;
  int y;

  for(int i = 0; i < 100000; i++)
  {
    a[i] = __VERIFIER_nondet_int();
  }

  i = 0;
  while ( i < 100000 ) {
    int k = i + 1;
    int s = i;
    while ( k < 100000 ) {
      if ( a[k] < a[s] ) {
        s = k;
      }
      k = k+1;
    }
    if ( s != i ) {
      int tmp = a[s];
      a[s] = a[i];
      a[i] = tmp;
    }
    for ( x = 0 ; x < i ; x++ ) {
      for ( y = x + 1 ; y < i ; y++ ) {
        __VERIFIER_assert( a[x] <= a[y] );
      }
    }
    for ( x = 0 ; x < 100000 ; x++ ) {
      __VERIFIER_assert( a[x] >= a[i] );
    }
    i = i+1;
  }
  for ( x = 0 ; x < 100000 ; x++ ) {
    for ( y = x + 1 ; y < 100000 ; y++ ) {
      __VERIFIER_assert( a[x] <= a[y] );
    }
  }
  return 0;
}
