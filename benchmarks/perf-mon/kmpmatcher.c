#ifdef KLEE
#include "klee/klee.h"
#endif

#define SIZE 10

int pi[4];
int lt, lp;

void compute_prefix_function(char * P)
{
	int q = 0;
	pi[0] = 0;
	
	for (int i = 1; i < lp; i++)
	{
		while (q > 0 && P[i] != P[q])
		{
			q = pi[q - 1];
		}
		
		if (P[i] == P[q])
		{
			q++;
		}
		
		pi[i] = q;
	}
}

void KMP_matcher(char * P, char * T)
{
	compute_prefix_function(P);
	
	int q = 0;
	
	for (int i = 0; i < lt; i++)
	{
		while (q > 0 && T[i] != P[q])
		{
			q = pi[q - 1];
		}
		
		if (T[i] == P[q])
		{
			q++;
		}
		
		if (q == lp)
		{
			printf("matched with shift %d\n", i - lp + 1);
			q = pi[q - 1];
		}
	}
}

int main()
{
  char P[5] = "llsc";
	char T[SIZE];
#ifdef KLEE
  klee_make_symbolic(T, SIZE, "T");
#else
  make_symbolic(T, SIZE);
#endif
	lt = SIZE-1;
	lp = 4;

	KMP_matcher(P, T);
	
	return 0;
}
