#include <stdint.h>
#include <stdio.h>
#include <unistd.h>

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
	int input[8];
  if (read(STDIN_FILENO, input, sizeof(input)) != sizeof(input)) {
    fprintf(stderr, "Failed to read the input\n");
    return -1;
  }
	
	bubble_sort(input, SIZE);
	
	return 0;
}
