extern int bar;

int foo();

int main(int argc, char** argv) {
    gs_assert_eager(bar == 10);
    gs_assert_eager(foo() == 1);
    return 0;
}