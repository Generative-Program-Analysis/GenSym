
#define SIZE 8
#include <stdio.h>

void bubble_sort(int*d, int n)
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
	int a[SIZE] = {0, 0, 0, 0, 0, 0, 0, 0};
	mark_symbolic(a, sizeof(a), sizeof(int));

	bubble_sort(a, SIZE);
	
	return 0;
}
