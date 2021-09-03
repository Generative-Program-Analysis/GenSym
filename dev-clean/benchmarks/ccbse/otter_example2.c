void level4(int x) {
    if (x == 9) target();
}
void level3(int x) {
    int n;
    make_symbolic(&n, 1);
    level4(n?x+1:x-1);
}
void level2(int x) {
    int n;
    make_symbolic(&n, 1);
    level3(n?x+1:x-1);
}
void level1(int x) {
    int n;
    make_symbolic(&n, 1);
    level2(n?x+1:x-1);
}
void level0(int x) {
    int n;
    make_symbolic(&n, 1);
    level1(n?x+1:x-1);
}
void main() {
    level0(5);
}
