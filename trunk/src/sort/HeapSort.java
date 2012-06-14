package sort;
import java.util.Arrays;
import java.util.Random;

public class HeapSort
{
	public static void heapSort_MAX(int[] A, int heapSize)
	{
		MAX_HEAP_BUILD(A, heapSize);
		for (int i = heapSize; i >= 2; i--) {
			swap(A, 1, i);
			MAX_HEAPIFY(A, 1, --heapSize);
		}
		// O(nlog2n)=time
		// unstable;temp=O(1) ; not suitable for linked table
	}

	public static void heapSort_MIN(int[] A, int heapSize)
	{
		MIN_HEAP_BUILD(A, heapSize);
		for (int i = heapSize; i >= 2; i--) {
			swap(A, 1, i);
			MIN_HEAPIFY(A, 1, --heapSize);
		}
		// O(nlog2n)=time
		// unstable;temp=O(1) ; not suitable for linked table
	}

	public static void main(String[] args)

	{
		int[] array = new int[100];
		for (int i = 0; i < 100; i++) {
			array[i] = i + 1;
		}
		// Sort test = new Sort();
		// System.out.println(test.testFinal());
		int[] a = getArray(10);
		printArray(a);
		int[] b;
		b = Arrays.copyOf(a, a.length);
		System.out.println("heapSort===");
		heapSort_MAX(b, b.length - 1);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("heapSort===");
		heapSort_MIN(b, b.length - 1);

		printArray(b);
	}

	public static void MAX_HEAPIFY_RECUR(int[] a, int i, int heapSize)
	{

		int largest = i;
		int left = leftChild(i);
		int right = rightChild(i);
		if (left <= heapSize && a[largest] < a[left]) {
			largest = left;
		}
		if (right <= heapSize && a[largest] < a[right]) {
			largest = right;
		}
		if (i != largest) {
			swap(a, i, largest);
			MAX_HEAPIFY_RECUR(a, largest, heapSize);
		}
	}

	public static void MAX_HEAPIFY(int[] A, int i, int heapSize)
	{

		int largest = i;
		int left = leftChild(i);
		int right = rightChild(i);
		while (left <= heapSize) {
			if (left <= heapSize && A[largest] < A[left]) {
				largest = left;
			}
			if (right <= heapSize && A[largest] < A[right]) {
				largest = right;
			}
			if (i != largest) {
				swap(A, i, largest);
				i = largest;
				left = leftChild(i);
				right = rightChild(i);
			}
			else {
				break;
			}
		}
	}

	public static void MAX_HEAP_BUILD(int[] a, int heapSize)
	{
		for (int i = heapSize / 2; i >= 1; i--) {
			MAX_HEAPIFY(a, i, heapSize);
		}
		// O(n) time
	}

	public static void MAX_HEAP_BUILD_INSERT(int[] a, int heapSize)
	{
		heapSize = 1;
		for (int i = 2; i <= a.length - 1; i++) {
			MAX_HEAP_INSERT(a, a[i], heapSize);
			heapSize++;
		}
		// O(n*log2n) time
	}

	public static void MAX_HEAP_INSERT(int[] a, int key, int heapSize)
	{
		heapSize++;
		a[heapSize] = Integer.MIN_VALUE;
		MAX_HEAP_INCREASE_KEY(a, heapSize, key);
	}

	public static void MAX_HEAP_DELETE(int[] a, int i, int heapSize)
	{
		int tmp = a[heapSize];

		if (a[i] == tmp) {
			heapSize--;
		}
		// i节点换成较小的tmp后，所在分支的最大堆性质可能遭到破坏，要进行调整
		else if (a[i] > tmp) {
			a[i] = tmp;
			heapSize--;
			MAX_HEAPIFY(a, i, heapSize);
		}
		// i节点的值增大到更大的tmp
		else if (a[i] < tmp) {
			heapSize--;
			MAX_HEAP_INCREASE_KEY(a, i, tmp);
		}
	}

	public static void MAX_HEAP_INCREASE_KEY(int[] A, int i, int key)
	{
		if (key < A[i]) {
			System.out.println("new key is smaller than current key");
			return;
		}
		A[i] = key;
		while (i > 1 && A[parent(i)] < A[i]) {
			swap(A, i, parent(i));
			i = parent(i);
		}
	}

	public static int MAX_HEAP_EXTRCAT_MAX(int[] a, int i, int heapSize)
	{

		if (heapSize < 1) {
			System.out.println("heap underflow");
			return -1;
		}
		int max = a[1];
		a[1] = a[heapSize];
		heapSize--;
		MAX_HEAPIFY(a, 1, heapSize);
		return max;
	}

	public static int MAX_HEAP_MAXIMUM(int[] A)
	{
		return A[1];
	}

	public static void MIN_HEAPIFY_RECUR(int[] A, int i, int heapSize)
	{

		int smallest = i;
		int left = leftChild(i);
		int right = rightChild(i);
		if (left <= heapSize && A[smallest] > A[left]) {
			smallest = left;
		}
		if (right <= heapSize && A[smallest] > A[right]) {
			smallest = right;
		}
		if (i != smallest) {
			swap(A, i, smallest);
			MIN_HEAPIFY_RECUR(A, smallest, heapSize);
		}
	}

	public static void MIN_HEAPIFY(int[] A, int i, int heapSize)
	{

		int smallest = i;
		int left = leftChild(i);
		int right = rightChild(i);
		while (left <= heapSize) {
			if (left <= heapSize && A[smallest] > A[left]) {
				smallest = left;
			}
			if (right <= heapSize && A[smallest] > A[right]) {
				smallest = right;
			}
			if (i != smallest) {
				swap(A, i, smallest);
				i = smallest;
				left = leftChild(i);
				right = rightChild(i);
			}
			else {
				break;
			}
		}
	}

	public static void MIN_HEAP_BUILD(int[] a, int heapSize)
	{
		for (int i = heapSize / 2; i >= 1; i--) {
			MIN_HEAPIFY(a, i, heapSize);
		}
		// O(n) time
	}

	public static void MIN_HEAP_BUILD_INSERT(int[] a, int heapSize)
	{
		heapSize = 1;
		for (int i = 2; i <= a.length - 1; i++) {
			MIN_HEAP_INSERT(a, a[i], heapSize);
			heapSize++;
		}
		// O(n*log2n) time
	}

	public static void MIN_HEAP_INSERT(int[] a, int key, int heapSize)
	{
		heapSize++;
		a[heapSize] = Integer.MAX_VALUE;
		MIN_HEAP_DECREASE_KEY(a, heapSize, key);
	}

	public static void MIN_HEAP_DELETE(int[] A, int i, int heapSize)
	{
		int tmp = A[heapSize];

		if (A[i] == tmp) {
			heapSize--;
		}
		else if (A[i] > tmp) {
			heapSize--;
			MIN_HEAP_DECREASE_KEY(A, i, tmp);
		}
		else if (A[i] < tmp) {
			A[i] = tmp;
			heapSize--;
			MIN_HEAPIFY(A, i, heapSize);
		}
	}

	public static void MIN_HEAP_DECREASE_KEY(int[] A, int i, int key)
	{
		if (key > A[i]) {
			System.out.println("new key is larger than current key");
			return;
		}
		A[i] = key;
		while (i > 1 && A[parent(i)] > A[i]) {
			swap(A, i, parent(i));
			i = parent(i);
		}

	}

	public static int MIN_HEAP_EXTRCAT_MIN(int[] a, int i, int heapSize)
	{

		if (heapSize < 1) {
			System.out.println("heap underflow");
			return -1;
		}
		int min = a[1];
		a[1] = a[heapSize];
		heapSize--;
		MIN_HEAPIFY(a, 1, heapSize);
		return min;
	}

	public static int MIN_HEAP_MINIMUM(int[] A)
	{
		return A[1];
	}

	private static int parent(int i)
	{
		return i >> 1;
	}

	private static int leftChild(int i)
	{
		return i << 1;
	}

	private static int rightChild(int i)
	{
		return (i << 1) + 1;
	}

	private static void swap(int[] a, int index1, int index2)
	{
		int tmp;
		tmp = a[index1];
		a[index1] = a[index2];
		a[index2] = tmp;
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