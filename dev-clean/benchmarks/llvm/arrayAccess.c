int arr[4][3]={{1,2,3},{2,3,4},{3,4,5},{4,5,6}};  

int arrayAccess(int i, int j) {
  return arr[i][j];
}

int main() {
  return arrayAccess(3, 0);
}