#include <fcntl.h>
#include <unistd.h>

#define SIZE 10

int main()
{
  // --sym-file-size 10 --add-sym-file A
  off_t pos;
  char filename[] = "A";
  char buf[4];
  int fd = open(filename, O_RDONLY);
  llsc_assert_eager(fd != -1);

  pos = lseek(fd, 5, SEEK_SET);
  sym_print(pos);
  llsc_assert_eager(pos == 5);
  pos = lseek(fd, 2, SEEK_CUR);
  sym_print(pos);
  llsc_assert_eager(pos == 7);

  // seek beyond end
  pos = lseek(fd, 3, SEEK_END);
  sym_print(pos);
  llsc_assert_eager(pos == SIZE + 3);

  // invalid seeks
  pos = lseek(fd, -5, SEEK_SET);
  sym_print(pos);
  llsc_assert_eager(pos == -1);

  pos = lseek(fd, 1, SEEK_SET);
  sym_print(pos);
  llsc_assert_eager(pos == 1);
  pos = lseek(fd, -3, SEEK_CUR);
  sym_print(pos);
  llsc_assert_eager(pos == -1);

  pos = lseek(fd, -12, SEEK_END);
  sym_print(pos);
  llsc_assert_eager(pos == -1);
  return 0;
}
