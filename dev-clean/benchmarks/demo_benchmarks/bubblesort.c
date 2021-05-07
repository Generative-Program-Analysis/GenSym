//#include "klee/klee.h"

#define SIZE 8

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

  make_symbolic(data, sizeof(int) * SIZE);
  //klee_make_symbolic(data, sizeof data, "data");
	
	bubble_sort(data, SIZE);
	
	return 0;
}
