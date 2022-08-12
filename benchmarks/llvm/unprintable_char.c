#include "../../headers/llsc_client.h"

char b[10] = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0};
char* c = "\\61";
char d[4] = {0x5c, 0x36, 0x31, 0x00};
char* e = "\n\\\\\\\\\n\n1";
char f[9] =  {0x0A, 0x5c, 0x5c, 0x5c, 0x5c, 0x0A, 0x0A, 0x31, 0x00};
int main() {
  for (int i=0; i<10;i++) {
    llsc_assert_eager(b[i] == ((i+1)%10));
  }
  for (int i=0; i<4;i++) {
    llsc_assert_eager(c[i] == d[i]);
  }
  llsc_assert_eager(c[0] == 92);
  llsc_assert_eager(c[1] == 54);
  llsc_assert_eager(c[2] == 49);
  llsc_assert_eager(c[3] == 0);
  for (int i=0; i<9;i++) {
    llsc_assert_eager(e[i] == f[i]);
  }
}