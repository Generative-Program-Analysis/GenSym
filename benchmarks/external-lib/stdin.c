int main()
{
  /* --sym-stdin 10 */
  char buf[10];
  int c;
  c = read(0, buf, 15);
  llsc_assert_eager(c == 10, "should read 10 bytes");
  if (buf[0] == 'a') {
    sym_print("path 1");
  } else {
    sym_print("path 2");
  }
}
