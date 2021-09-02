int f(int x) {
    if (x == 12)
        target();
    else return 1;
    return 0;
}

int g(int x) {
    f(x);
}

int main(int x) {
    f(x);
    g(x);
    return 0;
}