#include <sys/stat.h>

int main(){
	struct stat sfile;

	// test assignment
	sfile.st_dev = 1;
	if (sfile.st_dev != 1) {
		sym_exit(1);
	}

	// test padding
	sfile.st_rdev = 2;
	if (sfile.st_rdev != 2) {
		sym_exit(2);
	}
	
	// test nested assignment
	sfile.st_atim.tv_sec  = 3;
	if (sfile.st_atim.tv_sec != 3) {
		sym_exit(3);
	}

	// stat memory layout. Should be reflected in the compiled .ll file
	sfile.st_dev          = 1;  // 8 bytes
	sfile.st_ino          = 2;  // 8 bytes
	sfile.st_nlink        = 3;  // 8 bytes
	sfile.st_mode         = 4;  // 4 bytes
	sfile.st_uid          = 5;  // 4 bytes
	sfile.st_gid          = 6;  // 4 bytes
	// padding                  // 4 bytes
	sfile.st_rdev         = 7;  // 8 bytes
	sfile.st_size         = 8;  // 8 bytes
	sfile.st_blksize      = 9;  // 8 bytes
	sfile.st_blocks       = 10; // 8 bytes
	sfile.st_atim.tv_sec  = 11; // 8 bytes
	sfile.st_atim.tv_nsec = 12; // 8 bytes
	sfile.st_mtim.tv_sec  = 13; // 8 bytes
	sfile.st_mtim.tv_nsec = 14; // 8 bytes
	sfile.st_ctim.tv_sec  = 15; // 8 bytes
	sfile.st_ctim.tv_nsec = 16; // 8 bytes
	// total size: 120 + 24 (reserved) = 144 bytes
	unsigned total_size = sizeof(sfile);
	sym_exit(0);
}
