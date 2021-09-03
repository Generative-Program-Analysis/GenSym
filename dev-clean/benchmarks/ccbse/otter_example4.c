
void g(int n) {
    if (n == 0) {
        target();
    } else if (n == 1) {
        // OK
    } else if (n == 2) {
        target();
    }
}

void f(int m) {
    if (m == 1) {
        g(1);
    } else if (m != 0) {
        g(m);
    }
}

void main() {
    int x; make_symbolic(&x, 1);
    f(x);
}
