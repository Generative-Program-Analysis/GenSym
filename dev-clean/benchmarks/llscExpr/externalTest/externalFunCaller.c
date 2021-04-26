extern int n;
static int sv = 0;
int f() {
  
  return sv + 1;
}

int main () {
  f();
  int extRes = externalFun(n);
  return 0;
}