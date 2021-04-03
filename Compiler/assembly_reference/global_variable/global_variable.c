int g = 5;

int add(int a) {
    g += a;
    return a + g;
}

int main() {
    int a, b;
    a = 0;
    b = add(a);
    return 0;
}