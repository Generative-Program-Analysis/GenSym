int * __errno_location(void) {
  static int errorno;
  return &errorno;
}