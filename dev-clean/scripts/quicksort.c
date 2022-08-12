#include "../headers/llsc_client.h"
#define SIZE 5

int d[SIZE];

void qsort(int l, int r)
{
	if (l < r)
	{
		int x = d[r];
		int j = l - 1;

		for (int i = l; i <= r; i++)
		{
			if (d[i] <= x)
			{
				j++;
				int temp = d[i];
				d[i] = d[j];
				d[j] = temp;
			}
		}

		qsort(l, j - 1);
		qsort(j + 1, r);
	}
}

int main()
{
	llsc_make_symbolic(d, sizeof(int) * SIZE, "x");
  for (int i = 0; i < SIZE; i++) printf("%d,", d[i]);
  printf("\n");
	qsort(0, SIZE-1);
  for (int i = 0; i < SIZE; i++) printf("%d,", d[i]);
  printf("\n");
	return 0;
}
