bool a = 1;
bool aa[] = {true, false, false, true};
int ga[] = {1, 2, 3};
int g = 3;

int aaa[200];
bool bb[5];
bool bbb[200];

int cal(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j) {
    int k[5];
    k[1] = k[a] + k[b];
    return a + b + c + d + e + f + g + h + i + j;
}

bool cal(bool a, bool b, bool c, bool d, bool e, bool f, bool g, bool h, bool i, bool j) {
    bool k[5] = {true, true, false, false, true};
    bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
    const int l = sizeof(k);
    return res;
}

int main() {
    cal(1,2,3,4,5,6,7,8,9,10);
    cal(false,false,false,false,false,false,false,false,false,false);
    return 0;
}