
int a[10];
void main ( ) {
  printf ( "expecting an error next:\n" );
  a[100] = 0;
  exit(255);
}