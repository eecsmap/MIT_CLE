#include <stdio.h>
void main ( ) {
    long long a, b, c;
    long long d, e;
    long long g, h;
    a = 10;
    b = 20;
    c = 30;
    d = ( a + b );
    e = ( c * 999 );
    e = d * e - 100;
    printf ( "%d %d\n", d, e );
    g = d % 16;
    h = e / 100;
    printf ( "%d %d\n", g, h );
}