int g = 5;

int add(int a) {
    g += a;
    if (a == g) {
        return a + g;
    }
    a += g;
    return g + a;
}

int main() {
    int a, b;
    a = 0;
    b = add(a);
    return 0;
}