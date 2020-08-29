int arrayGetSet() {
  int arr[4][3][5];
  arr[3][2][3] = 323;
  arr[1][0][1] = 101;
  arr[2][1][2] = 211;
  arr[2][1][2] = 212;
  // expect 323+101+212=636
  return arr[3][2][3] + arr[1][0][1] + arr[2][1][2];
}

int main() {
  return arrayGetSet();
}