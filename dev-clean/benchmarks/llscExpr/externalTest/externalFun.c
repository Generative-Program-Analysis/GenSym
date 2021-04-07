int a = 10;
int b = 20;
int sumList[3] = {1, 2, 3};

int externalFun() {
  int res = 0;
  res += a;
  res += b;
  for (int i = 0; i < 3; i++) {
    res += sumList[i];
  }
  return res; // return 36
}