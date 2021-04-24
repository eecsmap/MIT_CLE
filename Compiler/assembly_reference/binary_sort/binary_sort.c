#include <stdio.h>
  
int binarySearch(int arr[], int l, int r, int x)
{
    if (r >= l) {
        int mid = l + (r - l) / 2;
  
        if (arr[mid] == x)
            return mid;
  
        if (arr[mid] > x)
            return binarySearch(arr, l, mid - 1, x);
  
        return binarySearch(arr, mid + 1, r, x);
    }
  
    return -1;
}
  
int main(void)
{
    int arr[5], n, x, result;
    arr[0] = 2;
    arr[1] = 3;
    arr[2] = 4;
    arr[3] = 10;
    arr[4] = 40;
    n = sizeof(arr) / sizeof(arr[0]);
    x = 10;
    result = binarySearch(arr, 0, n - 1, x);
    (result == -1) ? printf("Element is not present in array")
                   : printf("Element is present at index %d",
                            result);
    return 0;
}