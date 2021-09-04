#include <stdint.h>
#include <stdio.h>
#include <unistd.h>
#define SIZE 8

int d[SIZE];
void qsort(int l, int r)
{
	if (l < r)
	{
		int x = d[r];
		int j = l - 1;
		
		for (int i = l; i <= r; i++)
		{
			if (d[i] == x) j++;
			if (d[i] < x)
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
	if (read(STDIN_FILENO, d, sizeof(d)) != sizeof(d)) {
    fprintf(stderr, "Failed to read the input\n");
    return -1;
  }

	qsort(0, SIZE-1);
	return 0;
}
