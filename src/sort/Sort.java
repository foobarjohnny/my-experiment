package sort;

//原地算法包含使用O(1) in-place -- out of place
//假定在待排序的记录序列中，存在多个具有相同的关键字的记录，若经过排序，这些记录的相对次序保持不变，
//即在原序列中，ri=rj，且ri在rj之前，而在排序后的序列中，ri仍在rj之前，则称这种排序算法是稳定的；否则称为不稳定的。
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

	// 构建最大堆
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

	// 最大堆构建小－大数列
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
	// n的时间复杂度，稳定排序，非原地排序
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
	// n的时间复杂度，稳定排序，非原地排序
	// 基数排序：n的时间复杂度，稳定排序，非原地排序
	// 内为计数为O(d(n+k)) 但数学证明可为O(n)
	public static void radixSort(int[] A, int range, int d)
	{
		// 基数排序是先排次关键字 再排主关键字
		// 高位优先(MSD)： 从高位到低位依次对序列排序
		// 低位优先(LSD)： 从低位到高位依次对序列排序
		// 如果先排主关键字，那么需要分区另排
		// init the counter array
		// 这个内部排序一定要用 稳定排序
		// 时间复杂度要看内部用的什么稳定排序
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

	// 桶排序 BucketSort或者binSort
	// 虽然里面运用了插入排序
	// 但如果桶尺寸的平方与总的元素程序呈线性关系 那么就能用线性时间内运行
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

		byte k = -1; // 转为int,二进制码为：0000000000000000000000000000010
		System.out.println(Integer.toBinaryString(k));
		k <<= 8; // 右移2位，抛弃最后2位，负数补1,二进制吗为：11000000000000000000000000000
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

// 花了很长时间终于把排序的基础学了一下，这段时间学了很多东西，总结一下：
// 学的排序算法有：插入排序，合并排序，冒泡排序，选择排序，希尔排序，堆排序，快速排序，计数排序，基数排序，桶排序（没有实现）。比较一下学习后的心得。
// 我不是很清楚他们的时间复杂度，也真的不知道他们到底谁快谁慢，因为书上的推导我确实只是小小了解，并没有消化。也没有完全理解他们的精髓，所以又什么错误的还需要高手指点。呵呵。
//
// 1.普及一下排序稳定，所谓排序稳定就是指：如果两个数相同，对他们进行的排序结果为他们的相对顺序不变。
// 例如A={1,2,1,2,1}这里排序之后是A = {1,1,1,2,2}
// 稳定就是排序后第一个1就是排序前的第一个1，第二个1就是排序前第二个1，第三个1就是排序前的第三个1。同理2也是一样。这里用颜色标明了。
// 不稳定呢就是他们的顺序不应和开始顺序一致。也就是可能会是A={1,1,1,2,2}这样的结果。
//
// 2.普及一下原地排序：原地排序就是指不申请多余的空间来进行的排序，就是在原来的排序数据中比较和交换的排序。
// 例如快速排序，堆排序等都是原地排序，合并排序，计数排序等不是原地排序。
//
// 3.感觉谁最好，在我的印象中快速排序是最好的，时间复杂度：n*log(n)，不稳定排序。原地排序。他的名字很棒，快速嘛。
// 当然快了。我觉得他的思想很不错，分治，而且还是原地排序，省去和很多的空间浪费。速度也是很快的，n*log(n)。
// 但是有一个软肋就是如果已经是排好的情况下时间复杂度就是n*n,不过在加入随机的情况下这种情况也得以好转，
// 而且他可以做任意的比较，只要你能给出两个元素的大小关系就可以了。适用范围广，速度快。
//
// 4.插入排序：n*n的时间复杂度，稳定排序，原地排序。插入排序是我学的第一个排序，速度还是很快的，
// 特别是在数组已排好了之后，用它的思想来插入一个数据，效率是很高的。因为不用全部排。
// 他的数据交换也很少，只是数据后移，然后放入要插入的数据。（这里不是指调用插入排序，而是用它的思想）。
// 我觉得，在数据大部分都排好了，用插入排序会给你带来很大的方便。数据的移动和交换都很少。
//
// 5.冒泡排序，n*n的时间复杂度，稳定排序，原地排序。冒泡排序的思想很不错，一个一个比较，把小的上移，依次确定当前最小元素。
// 因为他简单，稳定排序，而且好实现，所以用处也是比较多的。还有一点就是加上哨兵之后他可以提前退出。
//
// 6.选择排序，n*n的时间复杂度， 稳定排序，原地排序。选择排序就是冒泡的基本思想，从小的定位，一个一个选择，直到选择结束。
// 他和插入排序是一个相反的过程，插入是确定一个元素的位置，而选择是确定这个位置的元素。
// 他的好处就是每次只选择确定的元素，不会对很多数据进行交换。所以在数据交换量上应该比冒泡小。
//
// 7.插入排序，选择排序，冒泡排序的比较，他们的时间复杂度都是n*n。
// 我觉得他们的效率也是差不多的，我个人喜欢冒泡一些，因为要用它的时候数据多半不多，
// 而且可以提前的返回已经排序好的数组。而其他两个排序就算已经排好了，他也要做全部的扫描。
// 在数据的交换上，冒泡的确比他们都多。呵呵。
// 举例说明插入一个数据在末尾后排序，冒泡只要一次就能搞定，而选择和插入都必须要n*n的复杂度才能搞定。就看你怎么看待咯。
//
// 8.合并排序：n*log(n)的时间复杂度， 稳定排序，非原地排序。他的思想是分治，先分成小的部分，排好部分之后合并，
// 因为我们另外申请的空间，在合并的时候效率是0(n)的。速度很快。貌似他的上限是n*log(n)，所以如果说是比较的次数的话，
// 他比快速排序要少一些。对任意的数组都能有效地在n*log(n)排好序。但是因为他是非原地排序，所以虽然他很快，但是貌似他的人气没有快速排序高。
//
// 9.堆排序：n*log(n)的时间复杂度， 非稳定排序，原地排序。他的思想是利用的堆这种数据结构，堆可以看成一个完全二叉树，
// 所以在排序中比较的次数可以做到很少。加上他也是原地排序，不需要申请额外的空间，效率也不错。可是他的思想感觉比快速难掌握一些。
// 还有就是在已经排好序的基础上添加一个数据再排序，他的交换次数和比较次数一点都不会减少。虽然堆排序在使用的中没有快速排序广泛，
// 但是他的数据结构和思想真的很不错，而且用它来实现优先队列，效率没得说。堆，还是要好好学习掌握的。
//
// 10.希尔排序：n*log(n)的时间复杂度(这里是错误的，应该是n^lamda(1 < lamda < 2), lamda和每次步长选择有关。)，
// 非稳定排序，原地排序。主要思想是分治，不过他的分治和合并排序的分治不一样，他是按步长来分组的，
// 而不是想合并那样左一半右一半。开始步长为整个的长度的一半。分成nLen/2个组，然后每组排序。接个步长减为原来的一半在分组排序，
// 直到步长为1，排序之后希尔排序就完成了。这个思路很好，据说是插入排序的升级版，所以在实现每组排序的时候我故意用了插入排序。
// 我觉得他是一个特别好的排序方法了。他的缺点就是两个数可能比较多次，因为两个数据会多次分不过他们不会出现数据的交换。效率也是很高的。
//
// 11.快速排序，堆排序，合并排序，希尔排序的比较，他们的时间复杂的都是n*log(n)，我认为在使用上快速排序最广泛，他原地排序，虽然不稳定，
// 可是很多情况下排序根本就不在意他是否稳定。他的比较次数是比较小的，因为他把数据分成了大和小的两部分。每次都确定了一个数的位置，
// 所以理论上说不会出现两个数比较两次的情况，也是在最后在交换数据，说以数据交换上也很少。合并排序和堆排序也有这些优点，
// 但是合并排序要申请额外的空间。堆排序堆已经排好的数据交换上比快速多。所以目前快速排序用的要广泛的多。还有他很容易掌握和实现。
//
// 12.计数排序：n的时间复杂度，稳定排序，非原地排序。他的思想比较新颖，就是先约定数据的范围不是很大，
// 而且数据都是整数(或能定位到整数)的情况，然后直接申请一个空间。把要排序的数组A的元素值与申请空间B的下标对应，
// 然后B中存放该下标元素值的个数，从而直接定位A中每个元素的位置。这样效率只为n。因为比较很特殊，虽然很快，但是用的地方并不多。
//
// 13.基数排序：n的时间复杂度，稳定排序，非原地排序。他的思想是数据比较集中在一个范围，例如都是4位数，都是5位数，
// 或数据有多个关键字，我们先从各位开始排，然后排十位，依次排到最高位，因为我们可以用一个n的方法排一位，
// 所以总的方法为d*n的复杂度。关键字也一样，我们先排第3个关键字，在排第3个关键字，最后排第一个关键字。
// 只有能保证每个关键字在n的时间复杂度完成，那么整个排序就是一个d*n的时间复杂度。所以总的速度是很快的。不过有一点就是要确保关键字能在n的时间复杂度完成。
//
// 14.桶排序：n的时间复杂度，稳定排序，非原地排序。主要思路和基数排序一样，也是假设都在一个范围例如概率都在0-1，
// 而且分布还挺均匀，那么我们也是和基数排序一样对一个数把他划分在他指定的区域。然后在连接这些区域就可以了。
// 书上对每个区域使用链表的存储，我认为在寸小区域的时候也会有时间在里面。所以只是理论上的n时间复杂度。这种思路是不错的。呵呵。
//
// 15.计数排序，基数排序，桶排序的比较，我觉得他们都很有思想，不过都是在特定情况下才能发挥最大的效果。
// 虽然效率很高，但是用的不会很广泛。他们之间我更喜欢计数排序，来个映射的方式就直接找到了自己的位置，很高明。
// 和基数排序和同排序只是理论上的n时间复杂度，基数排序要确定一个关键字的排序是n复杂度的，桶排序要确定每个区域的排序是n复杂度的。
//
// 16.排序算法的最后感悟：黑格尔说过：存在即合理。所以这些排序的算法都是很好的，他确实给了我们思想上的帮助。
//
// 感谢前人把精华留给了我们。我得到的收获很大，总结一下各自排序的收获：
// 冒泡：好实现，速度不慢，使用于轻量级的数据排序。
// 插入排序：也使用于小数据的排序，但是我从他的思想中学到怎么插入一个数据。呵呵，这样就知道在排好的数据里面，不用再排序了，而是直接调用一下插入就可以了。
// 选择排序：我学会了怎么去获得最大值，最小值等方法。只要选择一下，不就可以了。
// 合并排序：我学会分而治之的方法，而且在合并两个数组的时候很适用。
// 堆排序：可以用它来实现优先队列，而且他的思想应该给我加了很多内力。
// 快速排序：本来就用的最多的排序，对我的帮助大的都不知道怎么说好。
// 希尔排序：也是分治，让我看到了分治的不同，原来还有这种思想的存在。
// 计数排序，基数排序，桶排序：特殊情况特殊处理。
