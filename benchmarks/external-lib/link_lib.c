int app_main(int argc, char** argv);

int bar;

int foo() {
    return 1;
}

int main(int argc, char** argv) {
    bar = 10;
    return app_main(argc, argv);
}