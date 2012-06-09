import java.util.Arrays;
import java.util.Random;

public class Sort
{
	public String message()
	{
		System.out.println("returned start");
		return "return";
	}

	public int testFinal()
	{
		int a = 1;
		try {
			System.out.println("a=" + a);
			return a;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 3;
		}
		finally {
			a = 2;
			System.out.println("do finally");
			// return "finally";
		}
	}

	public static int recurBinarySearch(int[] k, int low, int high, int key)
	{
		int mid;
		if (low > high) {
			return 0;
		}
		else {
			mid = (low + high) / 2;
			if (k[mid] == key) {
				return mid;
			}
			else {
				if (key > k[mid]) {
					return recurBinarySearch(k, mid + 1, high, key);
				}
				else {
					return recurBinarySearch(k, low, mid - 1, key);
				}
			}

		}
	}

	public static int binarySearch(int[] k, int low, int high, int key)
	{
		int mid;
		int j = 0;
		try {
			while (low <= high) {
				j++;
				mid = (low + high) / 2;
				if (k[mid] == key) {
					return mid;
				}
				else {
					if (key > k[mid]) {
						low = mid + 1;
					}
					else {
						high = mid - 1;
					}

				}

			}

			return 0;
		}
		finally {
			System.out.println("j=" + j);
			// compare times: <=low(log2(n))+1
			// ASL: (n+1)/2*log2(n+1)-1 ~= log2(n+1)-1
		}
	}

	public static void swapSort(int[] k, int n)
	{
		int i, j, temp;
		for (i = 1; i <= n; i++) {
			for (j = i + 1; j <= n; j++) {
				if (k[j] < k[i]) {
					temp = k[i];
					k[i] = k[j];
					k[j] = temp;
				}
			}
		}
		// n(n-1)/2 = O(n^2); at least n-1 passes;
		// stable sort; one temp
	}

	public static void insertSort(int k[], int n)
	{
		int i, j;
		int temp;
		for (i = 2; i <= n; i++) {
			temp = k[i];
			j = i - 1;
			while (j > 0 && temp < k[j]) {
				k[j + 1] = k[j--];
			}
			k[j + 1] = temp;
			// best: n-1;worst: n^2/4; O(n^2); at least n-1 passes;
			// stable sort; one temp
		}
	}

	public static void binInsertSort(int k[], int length)
	{
		int i, j, low, high, mid;
		int temp;
		int n = k.length;
		n = length;
		for (i = 2; i < n; i++) {
			temp = k[i];
			low = 1;
			high = i - 1;
			while (low <= high) {
				mid = (low + high) / 2;
				if (temp < k[mid]) {
					high = mid - 1;
				}
				else {
					low = mid + 1;
				}
			}
			for (j = i - 1; j >= low; j--) {
				k[j + 1] = k[j];
			}
			k[low] = temp;
			// best: n-1; worst: n^2/4; O(n^2); at least n-1 passes;
			// stable sort; one temp
		}
	}

	public static void selectSort(int[] k, int n)
	{
		int i, j, d;
		int temp;
		for (i = 1; i < n; i++) {
			d = i;
			for (j = i + 1; j <= n; j++) {
				if (k[j] < k[d]) {
					d = j;
				}
			}
			if (d != i) {
				temp = k[d];
				k[d] = k[i];
				k[i] = temp;
			}
		}
		// n(n-1)/2 = O(n^2); at least n-1 passes;
		// unstable sort; one temp
	}

	public static void bubbleSort(int[] k, int n)
	{
		int max, j, flag = 1;
		int temp;
		// max=n-gap
		max = n - 1;
		// max>gap-1 == max>=gap
		while (max > 0 && flag == 1) {
			flag = 0;
			for (j = 1; j <= max; j++) {
				if (k[j] > k[j + 1]) {
					temp = k[j];
					k[j] = k[j + 1];
					k[j + 1] = temp;
					flag = 1;
				}
			}
			max--;
			// max-gap
		}
		// best: n-1 in 1 pass, worst: n(n-1)/2 in n -1 passes; O(n^2);
		// stable sort; one temp
	}

	// alias : diminishing increment sort .
	// 1 bubble sort
	public static void shellSortOfBubble(int[] k, int n)
	{
		int i, j, gap = n;
		int temp;
		int flag;
		while (gap > 1) {
			gap = gap / 2;
			int max = n - gap;
			do {
				flag = 0;
				for (i = 1; i <= max; i++) {
					j = i + gap;
					if (k[i] > k[j]) {
						temp = k[j];
						k[j] = k[i];
						k[i] = temp;
						flag = 1;
					}
				}
				max -= gap;

			} while (flag != 0 && max >= gap);
		}
		// O(nlog2n)<..<O(n^2)
		// unstable; one temp; not suitable for linked table
	}

	// alias : diminishing increment sort .
	// 1 insert sort
	public static void shellSortOfInsert(int[] k, int n)
	{
		int i, j, gap = n;
		int temp;
		while (gap > 1) {
			gap = gap / 2;
			for (i = gap; i <= n; i++) {
				temp = k[i];
				for (j = i; j >= gap && k[j - gap] > temp; j -= gap) {
					k[j] = k[j - gap];
				}
				k[j] = temp;
			}
		}
		// O(nlog2n)<..<O(n^2)
		// unstable; one temp; not suitable for linked table
	}

	// alias: partition-exchange sort
	public static void recurQuickSort(int[] k, int s, int t)
	{
		int i, j, temp;
		if (s < t) {
			i = s;
			j = t + 1;
			while (true) {
				do {
					i++;
				} while (!(k[s] <= k[i] || i == t));
				do {
					j--;
				} while (!(k[s] >= k[j] || j == s));
				if (i < j) {
					temp = k[i];
					k[i] = k[j];
					k[j] = temp;
				}
				else {
					break;
				}
			}
			temp = k[j];
			k[j] = k[s];
			k[s] = temp;
			recurQuickSort(k, s, j - 1);
			recurQuickSort(k, j + 1, t);
		}
		// O(nlog2n)< time < O(n^2)
		// unstable;O(log2n)<temp<O(n) ; not suitable for linked table
	}

	// alias: partition-exchange sort
	public static void quickSort(int[] k, int s, int t)
	{

		int i, j, p;
		if (s < t) {
			int[] stack = new int[t - s + 1];
			int top = 0;
			stack[top++] = s;
			stack[top++] = t;
			while (top > 0) {
				i = stack[--top];
				j = stack[--top];
				if (j < i) {
					p = partition2(k, j, i);
					stack[top++] = j;
					stack[top++] = p - 1;
					stack[top++] = p + 1;
					stack[top++] = i;
				}
				else {
					continue;
				}
			}
		}
		// O(nlog2n)< time < O(n^2)
		// unstable;O(log2n)<temp<O(n) ; not suitable for linked table
	}

	public static int partition2(int k[], int low, int high)
	{
		int i, j, temp;
		i = low;
		j = high + 1;
		while (true) {
			do {
				i++;
			} while (!(k[low] <= k[i] || i == high));
			do {
				j--;
			} while (!(k[low] >= k[j] || j == low));
			if (i < j) {
				temp = k[i];
				k[i] = k[j];
				k[j] = temp;
			}
			else {
				break;
			}
		}
		temp = k[j];
		k[j] = k[low];
		k[low] = temp;
		return j;
	}

	public static int partition3(int a[], int low, int high)
	{
		int v = a[low];
		while (low < high) {
			while (low < high && a[high] >= v) {
				high--;
			}
			a[low] = a[high];
			while (low < high && a[low] <= v) {
				low++;
			}
			a[high] = a[low];
		}
		a[low] = v;
		return low;
	}

	public static int partition(int[] a, int head, int tail)
	{
		int pivot = a[tail];
		int tmp = 0;
		int i = 0;
		int j = 0;

		i = head - 1;
		for (j = head; j < tail; j++) {
			if (a[j] <= pivot) {
				i++;
				tmp = a[i];
				a[i] = a[j];
				a[j] = tmp;
			}
		}
		tmp = a[i + 1];
		a[i + 1] = a[tail];
		a[tail] = tmp;

		return i + 1;
		// time O(n)
	}

	public static void adjustHeap(int[] k, int i, int heapSize)
	{
		int left;
		int temp;
		temp = k[i];
		left = 2 * i;
		while (left <= heapSize) {
			if (left < heapSize && k[left] < k[left + 1]) {
				left++;
			}
			if (temp >= k[left]) {
				break;
			}
			k[left / 2] = k[left];
			left = 2 * left;
		}
		k[left / 2] = temp;
	}

	public static void heapSort(int[] k, int heapSize)
	{
		int i;
		int temp;
		for (i = heapSize / 2; i >= 1; i--) {
			adjustHeap(k, i, heapSize);
		}
		for (i = heapSize - 1; i >= 1; i--) {
			temp = k[i + 1];
			k[i + 1] = k[1];
			k[1] = temp;
			adjustHeap(k, 1, i);
		}
		// O(nlog2n)=time
		// unstable;temp=O(1) ; not suitable for linked table
	}

	public static void merge(int[] x, int[] z, int s, int u, int v)
	{
		int i, j, q;
		i = s;
		j = u + 1;
		q = s;
		while (i <= u && j <= v) {
			if (x[i] < x[j]) {
				z[q++] = x[i++];
			}
			else {
				z[q++] = x[j++];
			}
		}
		while (i <= u) {
			z[q++] = x[i++];
		}
		while (j <= v) {
			z[q++] = x[j++];
		}
	}

	public static void MPASS(int[] x, int[] z, int n, int t)
	{
		int i, j;
		i = 1;
		while (n - i + 1 >= 2 * t) {
			merge(x, z, i, i + t - 1, i + 2 * t - 1);
			i = i + 2 * t;
		}
		if (n - i + 1 > t) {
			merge(x, z, i, i + t - 1, n);
		}
		else {
			for (j = i; j <= n; j++) {
				z[j] = x[j];
			}
		}
	}

	public static void mergeSort(int[] x, int n)
	{
		int t = 1;
		int[] y = new int[x.length];
		int[] temp = x;
		while (t < n) {
			MPASS(x, y, n, t);
			temp = x;
			x = y;
			y = temp;
			t *= 2;
		}
		// O(nlog2n)
		// stable; one int[];
	}

	public static void main(String[] args)

	{
		int[] array = new int[100];
		for (int i = 0; i < 100; i++) {
			array[i] = i + 1;
		}
		binarySearch(array, 0, 99, 21);
		
		// Sort test = new Sort();
		// System.out.println(test.testFinal());
		int[] a = getArray(38);
		printArray(a);
		int[] b;
		b = Arrays.copyOf(a, a.length);
		System.out.println("heapSort===");
		heapSort(b, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("mergeSort===");
		mergeSort(b, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("quickSort===");
		quickSort(b, 1, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("recurQuickSort===");
		recurQuickSort(b, 1, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("shellSortOfBubble===");
		Sort.shellSortOfBubble(b, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("shellSortOfInsert===");
		Sort.shellSortOfInsert(b, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("bubbleSort===");
		Sort.bubbleSort(b, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("insertSort===");
		Sort.insertSort(b, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("selectSort===");
		Sort.selectSort(b, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("swapSort===");
		Sort.swapSort(b, b.length - 1);
		printArray(b);
		
		byte k = -1; //转为int,二进制码为：0000000000000000000000000000010
		System.out.println(Integer.toBinaryString(k));
		k <<= 8; //右移2位，抛弃最后2位，负数补1,二进制吗为：11000000000000000000000000000
		System.out.println(k);
		System.out.println(Integer.toBinaryString(k));
		
	}

	public static void printArray(int[] a)
	{
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			s.append(a[i]);
			s.append(",");
		}
		System.out.println(s);

	}

	public static int[] getArray(int length)
	{
		int[] a = new int[length + 1];
		Random r = new Random();
		for (int i = 1; i < a.length; i++) {
			a[i] = r.nextInt(length * 10);
		}
		return a;
	}
}