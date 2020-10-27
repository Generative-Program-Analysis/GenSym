int arrayAccessLocal(int i, int j) {
  int arr[4][3];
  arr[i][j] = 4;
  return arr[i][j];
}

int main() {
  return arrayAccessLocal(3, 0);
}