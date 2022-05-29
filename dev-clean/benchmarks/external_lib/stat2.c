#include <sys/stat.h>

int main(){
	struct stat sfile;

	// --add-sym-file A
	/* TODO: parameterize the stat fields upon file creation
	 * generate utility functions to assign/read stat fields <2022-05-08, David Deng> */

	int status = stat("A", &sfile);
	if (status == -1) {
		// no arguments
		sym_exit(1);
	}

	if (sfile.st_dev == 1) {
		if (sfile.st_rdev == 2) {
			print_string("path 1/3\n");
		} else /* sfile.st_dev == 1, sfile.st_rdev != 2 */ {
			print_string("path 2/3\n");
			if (sfile.st_rdev == 2) {
				// should be unreachable, because we know that sfile.st_rdev != 2
				sym_exit(2);
			}
		}
	} else /* sfile.st_dev != 1 */ {
		print_string("path 3/3\n");
	}
	return 0;
}
