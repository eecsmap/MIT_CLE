int main() {
    int a, b, c, d, e, f, g;
    a = 0; b = 1; c = 2; d = 3; e = 4; f = 5; g = 6;
    a = b + c;
    b = c - d;
    c++;
    --d;
    e = (f == g);
    f = (e && g);
    g = (e || f);
    e = !f;
    e = -f;
    return 0;
}