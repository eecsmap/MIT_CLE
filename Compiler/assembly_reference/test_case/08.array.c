long long array[10];
void main ( ) {
  long long j;
  printf ( "below output should be 18 16 14 12 10 8 6 4 2 0\n" );
  for ( j = 0; j < len(array); j += 1 ) {
    array[j] = j*2;
  }
  for ( j = 0; j < 10; j += 1 ) {
    printf ( "%dl ", array[9 - j] );
  }
  printf ( "\n" );
}