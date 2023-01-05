#ifdef KLEE
#include "klee/klee.h"
#endif
#ifdef GENSYM
#include "gensym_client.h"
#endif

#define SIZE 10

int pi[10];
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
  char P[11] = "helloworld";
	char T[SIZE];
#ifdef KLEE
  klee_make_symbolic(T, SIZE, "T");
#endif
#ifdef LLSC
  make_symbolic(T, SIZE);
#endif
#ifdef GENSYM
  make_symbolic(T, SIZE);
#endif
	lt = SIZE-1;
	lp = 10;

	KMP_matcher(P, T);

	return 0;
}