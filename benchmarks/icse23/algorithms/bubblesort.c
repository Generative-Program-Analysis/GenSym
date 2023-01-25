#ifdef KLEE
#include "klee/klee.h"
#endif
#ifdef GENSYM
#include "gensym_client.h"
#endif

#define SIZE 6

void bubble_sort(int *d, int n)
{
	for (int k = 1; k < n; k++)
	{
		for (int i = 1; i < n; i++)
		{
			if (d[i] < d[i - 1])
			{
				int temp = d[i];
				d[i] = d[i - 1];
				d[i - 1] = temp;
			}
		}
	}
}

int main()
{
	int data[SIZE];
#ifdef KLEE
  klee_make_symbolic(data, sizeof data, "data");
#endif
#ifdef GENSYM
  for (int i = 0; i < SIZE; i++)
    make_symbolic_whole(data + i, sizeof(int));
#endif

	bubble_sort(data, SIZE);
	return 0;
}
