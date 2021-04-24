int main() {
    int a = 0, b = 0;
    if (a) {
        b += 1;
    }
    if (a > b) {
        b += 1;
    } else {
        b -= 1;
    }
    while (a) {
        b += 1;
    }
    for (int c = 0; c < 5; c++) {
        b += 1;
    }
    for (int c = 0; c < 5; c++) {
        while (a) {
            if (a) {
                b += 1;
            } else {
                b -= 1;
            }
        }
    }
    return 0;
}