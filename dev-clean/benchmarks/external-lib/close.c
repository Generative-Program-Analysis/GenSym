int main() {
  if (close(999) == -1) {
    // trying to close a non-existing file should return non zero
    return 0;
  } else {
    // should be unreachable
    return 1;
  }
}
