int a = 10;
int b = 20;
int n = 0;
int sv = 0;
int sumList[3] = {1, 2, 3};

int externalFun(int n) {
  int res = 0;
  res += a;
  res += b;
  res += n;
  for (int i = 0; i < 3; i++) {
    res += sumList[i];
  }
  return res; // return 36
}