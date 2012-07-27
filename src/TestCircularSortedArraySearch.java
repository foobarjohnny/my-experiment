public class TestCircularSortedArraySearch
{
	public static int circularArraySearch(int[] a, int size, int V)
	{
		return circularSortedArraySearch(a, 0, size - 1, V);
	}

	public static int circularSortedArraySearch(int[] a, int low, int high, int key)
	{
		// instead of using the division op. (which surprisingly fails on big
		// numbers)
		// we will use the unsigned right shift to get the average
		int mid = (low + high) / 2;
		if (a[mid] == key) {
			return mid;
		}
		// a variable to indicate which half is sorted
		// 1 for left, 2 for right
		int sortedHalf = 0;
		if (a[low] <= a[mid]) {
			// the left half is sorted
			sortedHalf = 1;
			if (key <= a[mid] && key >= a[low]) {
				// the element is in this half
				return binarySearch(a, low, mid, key);
			}
		}
		if (a[mid] <= a[high]) {
			// the right half is sorted
			sortedHalf = 2;
			if (key >= a[mid] && key <= a[high]) {
				return binarySearch(a, mid, high, key);
			}
		}
		// repeat the process on the unsorted half
		if (sortedHalf == 1) {
			// left is sorted, repeat the process on the right one
			return circularSortedArraySearch(a, mid, high, key);
		}
		else {
			// right is sorted, repeat the process on the left
			return circularSortedArraySearch(a, low, mid, key);
		}
	}

	public static int binarySearch(int[] a, int low, int high, int key)
	{
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			if (a[mid] == key) {
				return mid;
			}
			else {
				if (key > a[mid]) {
					low = mid + 1;
				}
				else {
					high = mid - 1;
				}

			}
		}
		return -1;
	}

	public static void main(String[] args)
	{
		int[] a = { 7, 10, 18, 28, 29, 30, 34, -2, 1, 4, 5 };
		System.out.println(circularArraySearch(a, a.length, 29));
		System.out.println(circularArraySearch(a, a.length, 1));
		System.out.println(circularArraySearch(a, a.length, 3));
		System.out.println(circularArraySearch(a, a.length, 5));
		System.out.println(Search(a, a.length, 3));
	}
	
	public static int Search(int A[],int n, int num)  
	{  
	    int left = 0 ;  
	    int right = n - 1 ;  
	    int mid = 0 ;  
	    int pos = -1 ;    //返回-1，表示查找失败  
	  
	    while(left <= right)  
	    {  
	        mid = (left + right)/2 ;  
	  
	        if (A[mid] == num)  
	        {     
	            pos = mid ;   
	            break;       
	        }  
	        if (A[left] <= A[mid])    //前半部分是严格递增的，后半部分是一个更小的循环递增数组  
	        {  
	            if(A[left] <= num && num  < A[mid] )  
	            {  
	                right = mid - 1 ;  
	            }  
	            else  
	            {  
	                left = mid + 1 ;  
	            }   
	        }  
	        else    //后半部分是严格递增的，前半部分是一个更小的循环递增数组  
	        {  
	            if ( A[mid] < num && num <= A[right])  
	            {  
	                left = mid + 1 ;  
	            }  
	            else  
	            {  
	                right = mid - 1 ;  
	            }  
	        }  
	    }  
	    return pos;  
	}  

}