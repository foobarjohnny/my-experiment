package sort;

//ԭ���㷨����ʹ��O(1) in-place -- out of place
//�ٶ��ڴ�����ļ�¼�����У����ڶ��������ͬ�Ĺؼ��ֵļ�¼��������������Щ��¼����Դ��򱣳ֲ��䣬
//����ԭ�����У�ri=rj����ri��rj֮ǰ�����������������У�ri����rj֮ǰ��������������㷨���ȶ��ģ������Ϊ���ȶ��ġ�
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

	// ��������
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

	// ���ѹ���С��������
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

	// the plain counting sort algorithm for comparison
	// A: input array
	// range: A value's range is 0...max
	// O(n)
	// stable; out-of-place
	// n��ʱ�临�Ӷȣ��ȶ����򣬷�ԭ������
	public static void countingSort(int[] A, int range)
	{
		// init the counter array
		int[] C = new int[range + 1];
		int[] B = new int[A.length];
		for (int i = 0; i <= range; i++) {
			C[i] = 0;
		}
		// stat the count in A
		for (int j = 0; j < A.length; j++) {
			C[A[j]]++;
		}
		// process the counter array C
		for (int i = 1; i <= range; i++) {
			C[i] += C[i - 1];
		}
		// distribute the values in A to array B
		// from the ending to beginning can keep the stability
		for (int j = A.length - 1; j >= 0; j--) {
			// C[A[j]] <= A.length
			B[--C[A[j]]] = A[j];
		}
		System.arraycopy(B, 0, A, 0, A.length);
	}

	// O(n)
	// stable; out-of-place
	// n��ʱ�临�Ӷȣ��ȶ����򣬷�ԭ������
	// ��������n��ʱ�临�Ӷȣ��ȶ����򣬷�ԭ������
	// ��Ϊ����ΪO(d(n+k)) ����ѧ֤����ΪO(n)
	public static void radixSort(int[] A, int range, int d)
	{
		// �������������Ŵιؼ��� �������ؼ���
		// ��λ����(MSD)�� �Ӹ�λ����λ���ζ���������
		// ��λ����(LSD)�� �ӵ�λ����λ���ζ���������
		// ����������ؼ��֣���ô��Ҫ��������
		// init the counter array
		// ����ڲ�����һ��Ҫ�� �ȶ�����
		// ʱ�临�Ӷ�Ҫ���ڲ��õ�ʲô�ȶ�����
		for (int i = 0; i <= d; i++) {
			countingSort4radix(A, range, i);
		}
	}

	public void binSort(int[] data)
	{
		int i, j, n, m, num;
		int[][] bin = new int[10][100];
		int[] index = new int[10];

		for (n = 0; n < 4; n++) {
			for (i = 0; i < 10; i++) {
				index[i] = -1;
				for (j = 0; j < 100; j++) {
					bin[i][j] = 0;
				}
			}
			m = (int) Math.pow(10, n);
			for (i = 0; i < 100; i++) {
				num = data[i] % (m * 10) / m;
				bin[num][++index[num]] = data[i];
			}
			num = 0;
			for (i = 0; i < 10; i++) {
				for (j = 0; j <= index[i]; j++) {
					data[num] = bin[i][j];
					num++;
				}
			}
		}
		return;
	}

	// Ͱ���� BucketSort����binSort
	// ��Ȼ���������˲�������
	// �����Ͱ�ߴ��ƽ�����ܵ�Ԫ�س�������Թ�ϵ ��ô����������ʱ��������
	// O(n) stable out-of-place
	public static void binSort(int k[], int max, int binCnt)
	{
		int i, j;
		int temp;
		int[][] binList = new int[binCnt + 1][max + 1];
		int[] index = new int[binCnt + 1];
		int base = max / binCnt;
		int n = 0;
		for (i = 1; i < k.length; i++) {
			temp = k[i] / base + 1;
			binList[temp][++index[temp]] = k[i];
		}
		for (i = 1; i < binCnt + 1; i++) {
			insertSort(binList[i], index[i]);
		}

		int num = 1;
		for (i = 1; i < binCnt + 1; i++) {
			for (j = 1; j <= index[i]; j++) {
				k[num] = binList[i][j];
				num++;
			}
		}
	}

	public static void countingSort4radix(int[] A, int range, int d)
	{
		// init the counter array
		int[] C = new int[range + 1];
		int[] B = new int[A.length];
		int base = (int) Math.pow(10, d);
		for (int i = 0; i <= range; i++) {
			C[i] = 0;
		}
		// stat the count in A
		for (int j = 0; j < A.length; j++) {
			C[(A[j] / base) % 10]++;
		}
		// process the counter array C
		for (int i = 1; i <= range; i++) {
			C[i] += C[i - 1];
		}
		// distribute the values in A to array B
		for (int j = A.length - 1; j >= 0; j--) {
			B[--C[(A[j] / base) % 10]] = A[j];
		}
		System.arraycopy(B, 0, A, 0, A.length);
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
		int max = 1000;
		int[] a = getArray(38, max);
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

		b = Arrays.copyOf(a, a.length);
		System.out.println("countingSort===");
		Sort.countingSort(b, max);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("radixSort===");
		Sort.radixSort(b, max, 4);
		printArray(b);

		b = Arrays.copyOf(a, a.length);
		System.out.println("binSort===");
		Sort.binSort(b, max, 4);
		printArray(b);

		byte k = -1; // תΪint,��������Ϊ��0000000000000000000000000000010
		System.out.println(Integer.toBinaryString(k));
		k <<= 8; // ����2λ���������2λ��������1,��������Ϊ��11000000000000000000000000000
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

	public static int[] getArray(int length, int max)
	{
		int[] a = new int[length + 1];
		Random r = new Random();
		for (int i = 1; i < a.length; i++) {
			a[i] = r.nextInt(max);
		}
		return a;
	}
}

// ���˺ܳ�ʱ�����ڰ�����Ļ���ѧ��һ�£����ʱ��ѧ�˺ܶණ�����ܽ�һ�£�
// ѧ�������㷨�У��������򣬺ϲ�����ð������ѡ������ϣ�����򣬶����򣬿������򣬼������򣬻�������Ͱ����û��ʵ�֣����Ƚ�һ��ѧϰ����ĵá�
// �Ҳ��Ǻ�������ǵ�ʱ�临�Ӷȣ�Ҳ��Ĳ�֪�����ǵ���˭��˭������Ϊ���ϵ��Ƶ���ȷʵֻ��СС�˽⣬��û��������Ҳû����ȫ������ǵľ��裬������ʲô����Ļ���Ҫ����ָ�㡣�Ǻǡ�
//
// 1.�ռ�һ�������ȶ�����ν�����ȶ�����ָ�������������ͬ�������ǽ��е�������Ϊ���ǵ����˳�򲻱䡣
// ����A={1,2,1,2,1}��������֮����A = {1,1,1,2,2}
// �ȶ�����������һ��1��������ǰ�ĵ�һ��1���ڶ���1��������ǰ�ڶ���1��������1��������ǰ�ĵ�����1��ͬ��2Ҳ��һ������������ɫ�����ˡ�
// ���ȶ��ؾ������ǵ�˳��Ӧ�Ϳ�ʼ˳��һ�¡�Ҳ���ǿ��ܻ���A={1,1,1,2,2}�����Ľ����
//
// 2.�ռ�һ��ԭ������ԭ���������ָ���������Ŀռ������е����򣬾�����ԭ�������������бȽϺͽ���������
// ����������򣬶�����ȶ���ԭ�����򣬺ϲ����򣬼�������Ȳ���ԭ������
//
// 3.�о�˭��ã����ҵ�ӡ���п�����������õģ�ʱ�临�Ӷȣ�n*log(n)�����ȶ�����ԭ�������������ֺܰ��������
// ��Ȼ���ˡ��Ҿ�������˼��ܲ������Σ����һ���ԭ������ʡȥ�ͺܶ�Ŀռ��˷ѡ��ٶ�Ҳ�Ǻܿ�ģ�n*log(n)��
// ������һ�����߾�������Ѿ����źõ������ʱ�临�ӶȾ���n*n,�����ڼ��������������������Ҳ���Ժ�ת��
// ����������������ıȽϣ�ֻҪ���ܸ�������Ԫ�صĴ�С��ϵ�Ϳ����ˡ����÷�Χ�㣬�ٶȿ졣
//
// 4.��������n*n��ʱ�临�Ӷȣ��ȶ�����ԭ�����򡣲�����������ѧ�ĵ�һ�������ٶȻ��Ǻܿ�ģ�
// �ر������������ź���֮��������˼��������һ�����ݣ�Ч���Ǻܸߵġ���Ϊ����ȫ���š�
// �������ݽ���Ҳ���٣�ֻ�����ݺ��ƣ�Ȼ�����Ҫ��������ݡ������ﲻ��ָ���ò������򣬶���������˼�룩��
// �Ҿ��ã������ݴ󲿷ֶ��ź��ˣ��ò���������������ܴ�ķ��㡣���ݵ��ƶ��ͽ��������١�
//
// 5.ð������n*n��ʱ�临�Ӷȣ��ȶ�����ԭ������ð�������˼��ܲ���һ��һ���Ƚϣ���С�����ƣ�����ȷ����ǰ��СԪ�ء�
// ��Ϊ���򵥣��ȶ����򣬶��Һ�ʵ�֣������ô�Ҳ�ǱȽ϶�ġ�����һ����Ǽ����ڱ�֮����������ǰ�˳���
//
// 6.ѡ������n*n��ʱ�临�Ӷȣ� �ȶ�����ԭ������ѡ���������ð�ݵĻ���˼�룬��С�Ķ�λ��һ��һ��ѡ��ֱ��ѡ�������
// ���Ͳ���������һ���෴�Ĺ��̣�������ȷ��һ��Ԫ�ص�λ�ã���ѡ����ȷ�����λ�õ�Ԫ�ء�
// ���ĺô�����ÿ��ֻѡ��ȷ����Ԫ�أ�����Ժܶ����ݽ��н��������������ݽ�������Ӧ�ñ�ð��С��
//
// 7.��������ѡ������ð������ıȽϣ����ǵ�ʱ�临�Ӷȶ���n*n��
// �Ҿ������ǵ�Ч��Ҳ�ǲ��ģ��Ҹ���ϲ��ð��һЩ����ΪҪ������ʱ�����ݶ�벻�࣬
// ���ҿ�����ǰ�ķ����Ѿ�����õ����顣������������������Ѿ��ź��ˣ���ҲҪ��ȫ����ɨ�衣
// �����ݵĽ����ϣ�ð�ݵ�ȷ�����Ƕ��ࡣ�Ǻǡ�
// ����˵������һ��������ĩβ������ð��ֻҪһ�ξ��ܸ㶨����ѡ��Ͳ��붼����Ҫn*n�ĸ��ӶȲ��ܸ㶨���Ϳ�����ô��������
//
// 8.�ϲ�����n*log(n)��ʱ�临�Ӷȣ� �ȶ����򣬷�ԭ����������˼���Ƿ��Σ��ȷֳ�С�Ĳ��֣��źò���֮��ϲ���
// ��Ϊ������������Ŀռ䣬�ںϲ���ʱ��Ч����0(n)�ġ��ٶȺܿ졣ò������������n*log(n)���������˵�ǱȽϵĴ����Ļ���
// ���ȿ�������Ҫ��һЩ������������鶼����Ч����n*log(n)�ź��򡣵�����Ϊ���Ƿ�ԭ������������Ȼ���ܿ죬����ò����������û�п�������ߡ�
//
// 9.������n*log(n)��ʱ�临�Ӷȣ� ���ȶ�����ԭ����������˼�������õĶ��������ݽṹ���ѿ��Կ���һ����ȫ��������
// �����������бȽϵĴ��������������١�������Ҳ��ԭ�����򣬲���Ҫ�������Ŀռ䣬Ч��Ҳ������������˼��о��ȿ���������һЩ��
// ���о������Ѿ��ź���Ļ��������һ���������������Ľ��������ͱȽϴ���һ�㶼������١���Ȼ��������ʹ�õ���û�п�������㷺��
// �����������ݽṹ��˼����ĺܲ�������������ʵ�����ȶ��У�Ч��û��˵���ѣ�����Ҫ�ú�ѧϰ���յġ�
//
// 10.ϣ������n*log(n)��ʱ�临�Ӷ�(�����Ǵ���ģ�Ӧ����n^lamda(1 < lamda < 2), lamda��ÿ�β���ѡ���йء�)��
// ���ȶ�����ԭ��������Ҫ˼���Ƿ��Σ��������ķ��κͺϲ�����ķ��β�һ�������ǰ�����������ģ�
// ��������ϲ�������һ����һ�롣��ʼ����Ϊ�����ĳ��ȵ�һ�롣�ֳ�nLen/2���飬Ȼ��ÿ�����򡣽Ӹ�������Ϊԭ����һ���ڷ�������
// ֱ������Ϊ1������֮��ϣ�����������ˡ����˼·�ܺã���˵�ǲ�������������棬������ʵ��ÿ�������ʱ���ҹ������˲�������
// �Ҿ�������һ���ر�õ����򷽷��ˡ�����ȱ��������������ܱȽ϶�Σ���Ϊ�������ݻ��ηֲ������ǲ���������ݵĽ�����Ч��Ҳ�Ǻܸߵġ�
//
// 11.�������򣬶����򣬺ϲ�����ϣ������ıȽϣ����ǵ�ʱ�临�ӵĶ���n*log(n)������Ϊ��ʹ���Ͽ���������㷺����ԭ��������Ȼ���ȶ���
// ���Ǻܶ��������������Ͳ��������Ƿ��ȶ������ıȽϴ����ǱȽ�С�ģ���Ϊ�������ݷֳ��˴��С�������֡�ÿ�ζ�ȷ����һ������λ�ã�
// ����������˵��������������Ƚ����ε������Ҳ��������ڽ������ݣ�˵�����ݽ�����Ҳ���١��ϲ�����Ͷ�����Ҳ����Щ�ŵ㣬
// ���Ǻϲ�����Ҫ�������Ŀռ䡣��������Ѿ��źõ����ݽ����ϱȿ��ٶࡣ����Ŀǰ���������õ�Ҫ�㷺�Ķࡣ���������������պ�ʵ�֡�
//
// 12.��������n��ʱ�临�Ӷȣ��ȶ����򣬷�ԭ����������˼��Ƚ���ӱ��������Լ�����ݵķ�Χ���Ǻܴ�
// �������ݶ�������(���ܶ�λ������)�������Ȼ��ֱ������һ���ռ䡣��Ҫ���������A��Ԫ��ֵ������ռ�B���±��Ӧ��
// Ȼ��B�д�Ÿ��±�Ԫ��ֵ�ĸ������Ӷ�ֱ�Ӷ�λA��ÿ��Ԫ�ص�λ�á�����Ч��ֻΪn����Ϊ�ȽϺ����⣬��Ȼ�ܿ죬�����õĵط������ࡣ
//
// 13.��������n��ʱ�临�Ӷȣ��ȶ����򣬷�ԭ����������˼�������ݱȽϼ�����һ����Χ�����綼��4λ��������5λ����
// �������ж���ؼ��֣������ȴӸ�λ��ʼ�ţ�Ȼ����ʮλ�������ŵ����λ����Ϊ���ǿ�����һ��n�ķ�����һλ��
// �����ܵķ���Ϊd*n�ĸ��Ӷȡ��ؼ���Ҳһ�����������ŵ�3���ؼ��֣����ŵ�3���ؼ��֣�����ŵ�һ���ؼ��֡�
// ֻ���ܱ�֤ÿ���ؼ�����n��ʱ�临�Ӷ���ɣ���ô�����������һ��d*n��ʱ�临�Ӷȡ������ܵ��ٶ��Ǻܿ�ġ�������һ�����Ҫȷ���ؼ�������n��ʱ�临�Ӷ���ɡ�
//
// 14.Ͱ����n��ʱ�临�Ӷȣ��ȶ����򣬷�ԭ��������Ҫ˼·�ͻ�������һ����Ҳ�Ǽ��趼��һ����Χ������ʶ���0-1��
// ���ҷֲ���ͦ���ȣ���ô����Ҳ�Ǻͻ�������һ����һ����������������ָ��������Ȼ����������Щ����Ϳ����ˡ�
// ���϶�ÿ������ʹ������Ĵ洢������Ϊ�ڴ�С�����ʱ��Ҳ����ʱ�������档����ֻ�������ϵ�nʱ�临�Ӷȡ�����˼·�ǲ���ġ��Ǻǡ�
//
// 15.�������򣬻�������Ͱ����ıȽϣ��Ҿ������Ƕ�����˼�룬�����������ض�����²��ܷ�������Ч����
// ��ȻЧ�ʺܸߣ������õĲ���ܹ㷺������֮���Ҹ�ϲ��������������ӳ��ķ�ʽ��ֱ���ҵ����Լ���λ�ã��ܸ�����
// �ͻ��������ͬ����ֻ�������ϵ�nʱ�临�Ӷȣ���������Ҫȷ��һ���ؼ��ֵ�������n���Ӷȵģ�Ͱ����Ҫȷ��ÿ�������������n���Ӷȵġ�
//
// 16.�����㷨�������򣺺ڸ��˵�������ڼ�����������Щ������㷨���Ǻܺõģ���ȷʵ��������˼���ϵİ�����
//
// ��лǰ�˰Ѿ������������ǡ��ҵõ����ջ�ܴ��ܽ�һ�¸���������ջ�
// ð�ݣ���ʵ�֣��ٶȲ�����ʹ��������������������
// ��������Ҳʹ����С���ݵ����򣬵����Ҵ�����˼����ѧ����ô����һ�����ݡ��Ǻǣ�������֪�����źõ��������棬�����������ˣ�����ֱ�ӵ���һ�²���Ϳ����ˡ�
// ѡ��������ѧ������ôȥ������ֵ����Сֵ�ȷ�����ֻҪѡ��һ�£����Ϳ����ˡ�
// �ϲ�������ѧ��ֶ���֮�ķ����������ںϲ����������ʱ������á�
// �����򣺿���������ʵ�����ȶ��У���������˼��Ӧ�ø��Ҽ��˺ܶ�������
// �������򣺱������õ��������򣬶��ҵİ�����Ķ���֪����ô˵�á�
// ϣ������Ҳ�Ƿ��Σ����ҿ����˷��εĲ�ͬ��ԭ����������˼��Ĵ��ڡ�
// �������򣬻�������Ͱ��������������⴦��
