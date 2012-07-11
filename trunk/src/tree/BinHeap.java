//http://blog.csdn.net/acceptedxukai/article/details/6921334
package tree;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * http://mindlee.net/2011/09/26/binomial-heaps/
 * http://mindlee.net/2011/09/29/fibonacci-heaps/
 * ������Bk��һ�ֵݹ鶨�����������������B0ֻ����һ����㡣������Bk����������Bk-1���Ӷ��ɣ�����һ�����ĸ�����һ�����ĸ��������ӡ��������������У�
 1)����2^k����㡣
 2)���ĸ߶�Ϊk����0��ʼ
 3)�����i��ǡ��Cik����㡣i��K��0��ʼ
 4)���Ķ�������Ů�ĸ�����Ϊk���������κ��������Ķ��������������Ů�����ҵı����Ϊk-1, k-2, ��, 0����Ůi������Bi�ĸ���
 ��ͼ��ͼ(b)��ʾ������B0��B4��ʾ���˸�������ȡ�ͼ(c)��������һ�ַ�ʽ����������Bk��
 PS : ��һ�ð���n�����Ķ������У��������������Ϊlgn. ��������Ϊk

 ����                    �����(���             ����� (���                        쳲�������(ƽ̯)
 MAKE-HEAP    ��(1)           ��(1)                     ��(1)
 INSERT       ��(lg n)        O(lg n)                  ��(1)
 MINIMUM      ��(1)           O(lg n)                  ��(1)
 EXTRACT-MIN  ��(lg n)        ��(lg n)                  O(lg n)
 UNION        ��(n)           O(lg n)                  ��(1)
 DECREASE-KEY ��(lg n)        ��(lg n)                  ��(1)
 DELETE       ��(lg n)        ��(lg n)                  O(lg n)
 */

//binomial-heap
//binomial-heap ��binomial-tree ���
// �������ĸ�������ɸ���
// ��������F����������Ч��֧��SEARCH����
// ������ Make-HeapΪO(1),����ExtractMin, Minimum, Insert, Union, DecreaseKey, Delete ��ΪO(lgN)
// Search û��
// ���ܱȽϼ�(����� F�� ����� ���ܱȽ�.jpg)
// ÿ������B0+B0=B1,��Bk=(Bk-1)+B(k-1)=2B(K-1) ��W0=1,W1=1,Wk=2^(k-1)[k>=2]
// �Ӷ����֪ ���� ���� ���ڻ���ڸ���㣬�е��ڣ���Ϊ����û����ת�������漰��תҪ���⴦��
public class BinHeap extends MergeableHeap
{
	// ������ɶ��������ɵ�����ֻ��������ֱ����BinHeap��Sibling������Head��
	int		key;
	int		degree;
	BinHeap	parent;
	BinHeap	rightChild;
	BinHeap	sibling;

	public String toString()
	{
		return "[key=" + key + ", degree=" + degree + ", parent=" + (parent == null ? null : parent.key) + ", rightChild=" + (rightChild == null ? null : rightChild.key) + ", sibling="
				+ (sibling == null ? null : sibling.key) + "]";
	}

	/*
	 * ��һ�������H1: (41) (28 (33) ) (7 (15 (25) ) (12) )
	 * 
	 * �ڶ��������H2: (55) (24 (50) ) (2 (3 (8 (10 (44) ) (29) ) (6 (37) ) (18) ) (17
	 * (32 (45) ) (31) ) (23 (30) ) (48) )
	 * 
	 * �ϲ�H1,H2�󣬵õ�H3: (41 (55) ) (7 (24 (28 (33) ) (50) ) (15 (25) ) (12) ) (2
	 * (3 (8 (10 (44) ) (29) ) (6 (37) ) (18) ) (17 (32 (45) ) (31) ) (23 (30) )
	 * (48) )
	 * 
	 * ���ڲ�����ȡ��ɾ���Ķ����H4: (27 (42) ) (11 (17 (38) ) (18) ) (1 (8 (14 (23 (26) )
	 * (29) ) (12 (16) ) ( 25) ) (10 (37 (41) ) (28) ) (13 (77) ) (6) )
	 * 
	 * ��ȡ��С��ֵ1�� (6) (13 (27 (42) ) (77) ) (8 (10 (11 (17 (38) ) (18) ) (37 (41)
	 * ) (28) ) (14 (23 (26) ) (29) ) (12 (16) ) (25) )
	 * 
	 * ��ȡ��С��ֵ6�� (13 (27 (42) ) (77) ) (8 (10 (11 (17 (38) ) (18) ) (37 (41) )
	 * (28) ) (14 (23 (26) ) (29) ) (12 (16) ) (25) )
	 * 
	 * ��ȡ��С��ֵ8�� (25) (12 (16) ) (10 (13 (14 (23 (26) ) (29) ) (27 (42) ) (77) )
	 * (11 (17 ( 38) ) (18) ) (37 (41) ) (28) )
	 * 
	 * ɾ��12�� (16 (25) ) (10 (13 (14 (23 (26) ) (29) ) (27 (42) ) (77) ) (11 (17
	 * (38) ) (18) ) (37 (41) ) (28) )
	 */

	// ���ٶ�
	// void DestroyBinHeap(BinHeap heap);

	// Insert Key to heap
	public static BinHeap BinHeapInsert(BinHeap heap, int key)
	{
		BinHeap newHeap = null;
		newHeap = new BinHeap();
		newHeap.key = key;
		if (null == heap) {
			heap = newHeap;
		}
		else {
			heap = BinHeapUnion(heap, newHeap);
			newHeap = null;
		}
		return heap;
	}

	// �������ڵ�ֵ����
	public static BinHeap MakeBinHeapWithArray(int keys[], int n)
	{
		BinHeap heap = null, newHeap = null;
		for (int i = 0; i < n; i++) {
			newHeap = new BinHeap();
			newHeap.key = keys[i];
			if (null == heap) {
				heap = newHeap;
			}
			else {
				heap = BinHeapUnion(heap, newHeap);
				newHeap = null;
			}
		}
		return heap;
	}

	// �����Ѻϲ�
	public static BinHeap BinHeapUnion(BinHeap H1, BinHeap H2)
	{
		BinHeap heap = null, pre_x = null, x = null, next_x = null;
		heap = BinHeapMerge(H1, H2);
		if (heap == null) {
			return heap;
		}

		pre_x = null;
		x = heap;
		next_x = x.sibling;

		while (next_x != null) {
			if ((x.degree != next_x.degree) || // Cases 1 and 2
					((next_x.sibling != null) && (next_x.degree == next_x.sibling.degree))) {
				pre_x = x;
				x = next_x;
			}
			else if (x.key <= next_x.key) {// Cases 3
				x.sibling = next_x.sibling;
				BinLink(next_x, x);
			}
			else {// Cases 4
				if (pre_x == null) {
					heap = next_x;
				}
				else {
					pre_x.sibling = next_x;
				}
				BinLink(x, next_x);
				x = next_x;
			}
			next_x = x.sibling;
		}
		return heap;
	}

	// ��H1, H2�ĸ���ϲ���һ���������ĵ��������������е�����
	public static BinHeap BinHeapMerge(BinHeap H1, BinHeap H2)
	{
		// heap.�ѵ��׵�ַ��H3Ϊָ���¶Ѹ����
		BinHeap heap = null, firstHeap = null, secondHeap = null, pre_H3 = null, H3 = null;

		if (H1 != null && H2 != null) {
			firstHeap = H1;
			secondHeap = H2;
			// ����while��firstHeap, secondHeap, pre_H3, H3��������˳��
			while (firstHeap != null && secondHeap != null) {
				if (firstHeap.degree <= secondHeap.degree) {
					H3 = firstHeap;
					firstHeap = firstHeap.sibling;
				}
				else {
					H3 = secondHeap;
					secondHeap = secondHeap.sibling;
				}

				if (pre_H3 == null) {
					pre_H3 = H3;
					heap = H3;
				}
				else {
					pre_H3.sibling = H3;
					pre_H3 = H3;
				}
				if (firstHeap != null) {
					H3.sibling = firstHeap;
				}
				else {
					H3.sibling = secondHeap;
				}
			}// while
		}
		else if (H1 != null) {
			heap = H1;
		}
		else {
			heap = H2;
		}
		H1 = H2 = null;
		return heap;
	}

	// ʹH2��ΪH1�ĸ��ڵ�
	public static void BinLink(BinHeap H1, BinHeap H2)
	{
		H1.parent = H2;
		H1.sibling = H2.rightChild;
		H2.rightChild = H1;
		H2.degree++;
	}

	// ������С���ڵ��ָ��
	public static BinHeap BinHeapMin(BinHeap heap)
	{
		BinHeap y = null, x = heap;
		int min = Integer.MAX_VALUE;

		while (x != null) {
			if (x.key < min) {
				min = x.key;
				y = x;
			}
			x = x.sibling;
		}
		return y;
	}

	// ��ȡ����С�ؼ��ֵĽ��� ����BinHeap
	public static BinHeap BinHeapExtractMin(BinHeap heap)
	{
		BinHeap pre_y = null, y = null, x = heap;
		int min = Integer.MAX_VALUE;
		while (x != null) {
			if (x.key < min) {
				min = x.key;
				pre_y = y;
				y = x;
			}
			x = x.sibling;
		}

		if (y == null) {
			return heap;
		}

		if (pre_y == null) {
			heap = heap.sibling;
		}
		else {
			pre_y.sibling = y.sibling;
		}

		// ��y���ӽ��ָ��reverse
		BinHeap H2 = null, p = null;
		x = y.rightChild;
		while (x != null) {
			p = x;
			x = x.sibling;
			p.sibling = H2;
			H2 = p;
			p.parent = null;
		}
		heap = BinHeapUnion(heap, H2);
		return heap;
	}

	// ���ٹؼ��ֵ�ֵ
	public static void BinHeapDecreaseKey(BinHeap heap, BinHeap x, int key)
	{
		if (key > x.key) {
			System.out.println("new key is greaer than current key");
			System.exit(1);// ��Ϊ����
		}
		x.key = key;

		BinHeap z = null, y = null;
		y = x;
		z = x.parent;
		while (z != null && z.key > y.key) {
			swapHeapKey(z, y);
			y = z;
			z = y.parent;
		}
	}

	// ���ӹؼ��ֵ�ֵ
	// Increase Ϊ�Լ�д����, ����������DELETE��Ϊ���ǰ�KEY������MAX�½���������ɾȥ ���ƻ�����������
	// �Ա�һ�¶���ѵ�ɾ��
	// Ч��Ӧ����O(lgN)
	// �Լ�д��Increase �൱��heapSort��MAX/MIN HEAPIFY
	public static void BinHeapIncreaseKey(BinHeap heap, BinHeap x, int key)
	{
		if (key < x.key) {
			System.out.println("new key is less than current key");
			System.exit(1);// ��Ϊ����
		}
		x.key = key;
		BinHeap child = x.rightChild;
		if (child == null) {
			return;
		}
		BinHeap sibling = child;
		BinHeap min = x;
		do {
			if (sibling.key < min.key) {
				min = sibling;
			}
		} while ((sibling = sibling.sibling) != null);
		if (x != min) {
			x.key = min.key;
			BinHeapIncreaseKey(heap, min, key);
		}
	}

	private static void swapHeapKey(BinHeap a, BinHeap b)
	{
		int c = a.key;
		a.key = b.key;
		a.key = c;
	}

	// ɾ��һ���ؼ���
	public static BinHeap BinHeapDelete(BinHeap heap, int key)
	{
		BinHeap x = null;
		x = BinHeapSearch(heap, key);
		if (x != null) {
			BinHeapDecreaseKey(heap, x, Integer.MIN_VALUE);
			return BinHeapExtractMin(heap);
		}
		return heap;
	}

	// �ҳ�һ���ؼ���
	// Ч��δ֪
	public static BinHeap BinHeapSearch(BinHeap heap, int key)
	{
		BinHeap p = null, x = null;
		p = heap;
		while (p != null) {
			if (p.key == key) {
				return p;
			}
			else {
				if ((x = BinHeapSearch(p.rightChild, key)) != null) {
					return x;
				}
				p = p.sibling;
			}
		}
		return null;
	}

	// ��ӡ����ѽṹ
	public static void PrintBinHeap(BinHeap heap)
	{
		if (null == heap) {
			return;
		}
		BinHeap p = heap;

		while (p != null) {
			System.out.print(" (");
			System.out.print(p.key);
			// ��ʾ�亢��
			if (null != p.rightChild) {
				PrintBinHeap(p.rightChild);
			}
			System.out.print(") ");

			p = p.sibling;
		}
	}

	public static void printBinHeapByBFS(BinHeap root, int spaceLength, String c)
	{
		System.out.println("\n====BinHeap Start====");
		BinHeap sibling = root;
		while (sibling != null) {
			printBinTreeByBFS(sibling, spaceLength, c);
			System.out.println("��");
			sibling = sibling.sibling;
		}
		System.out.println("====BinHeap Over====");
	}

	// ������ɶ��������ɵ�����ֻ��������ֱ����BinHeap��Sibling������Head��
	public static void printBinTreeByBFS(BinHeap root, int spaceLength, String c)
	{
		ArrayList<BinHeap> list = new ArrayList<BinHeap>();
		LinkedList<BinHeap> siblingList = new LinkedList<BinHeap>();
		StringBuilder s = new StringBuilder();
		int startSpaces = 1;
		int intervalSpaces = 1;
		int currentIndex = 0;
		int nextIndex = 1;
		int level = 0;
		int depth = getBinHeapHeight(root);
		BinHeap tree;
		BinHeap sibling;
		BinHeap cur;
		list.add(root);
		while (currentIndex < list.size())// �����²�
		{
			level++;
			nextIndex = list.size();
			System.out.print(printKey(level + ":", spaceLength, " "));
			while (currentIndex < nextIndex)// �ò㻹��Ԫ��
			{
				tree = list.get(currentIndex);
				if (tree == null) {
					System.out.print(printBlank(intervalSpaces, spaceLength));
					if (depth > level) {
						list.add(null);
					}
					System.out.print(printBlank(startSpaces, spaceLength));
				}
				else {

					siblingList.push(tree);
					if (tree.parent != null) {
						sibling = tree;
						while ((sibling = sibling.sibling) != null) {
							siblingList.push(sibling);
						}
					}
					while (siblingList.size() > 0) {
						cur = siblingList.poll();
						if (depth > level) {
							list.add(cur.rightChild);
						}
						if (cur.sibling != null && level != 1) {
							System.out.print(printKey("<-", spaceLength, " "));
						}
						else {
							System.out.print(printBlank(intervalSpaces, spaceLength));
						}
						System.out.print(printKey(cur.key + (cur.rightChild == null ? "" : "��"), spaceLength, c));
						if (cur.degree > 1) {
							System.out.print(printBlank((cur.degree - 1) * 2, spaceLength));
						}
					}
				}
				currentIndex++;
			}
			System.out.print("\n");
		}
		// System.out.print(s);
	}

	// ����� m!/(m-n)!n!
	public static int combinDigit(int m, int n)
	{
		int t = 0;
		if (m < n) {
			t = m;
			m = n;
			n = t;
		}
		int ret = 0;
		if (n == 0) {
			ret = 1;
		}
		else if (n == 1) {
			ret = m;
		}
		else {
			if (n > m / 2) {
				ret = combinDigit(m, m - n);
			}
			else {
				ret = combinDigit(m - 1, n) + combinDigit(m - 1, n - 1);
			}
		}
		return ret;
	}

	private static int getBinHeapHeight(BinHeap heap)
	{
		if (heap == null) {
			return 0;
		}
		return getBinHeapHeight(heap.rightChild) + 1;
	}

	public static int[]	kp1	= { 12, 7, 25, 15, 28, 33, 41 };

	public static int[]	kp2	= { 18, 3, 37, 6, 8, 29, 10, 44, 30, 23, 2, 48, 31, 17, 45, 32, 24, 50, 55 };

	public static int[]	kp4	= { 37, 41, 10, 28, 13, 77, 1, 6, 16, 12, 25, 8, 14, 29, 26, 23, 18, 11, 17, 38, 42, 27 };

	public static void main(String[] args)
	{
		BinHeap H1 = null;
		H1 = MakeBinHeapWithArray(kp1, kp1.length);
		System.out.println("��һ�������H1:");
		PrintBinHeap(H1);
		printBinHeapByBFS(H1, 5, " ");

		BinHeap H2 = null;
		H2 = MakeBinHeapWithArray(kp2, kp2.length);
		System.out.println("\n\n�ڶ��������H2:");
		PrintBinHeap(H2);
		printBinHeapByBFS(H2, 5, " ");

		BinHeap H3 = null;
		H3 = BinHeapUnion(H1, H2);
		System.out.println("\n\n�ϲ�H1,H2�󣬵õ�H3:");
		PrintBinHeap(H3);
		printBinHeapByBFS(H3, 5, " ");

		BinHeap H4 = null;
		H4 = MakeBinHeapWithArray(kp4, kp4.length);
		System.out.println("\n\n���ڲ�����ȡ��ɾ���Ķ����H4:");
		PrintBinHeap(H4);
		printBinHeapByBFS(H4, 5, " ");

		BinHeap extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n��ȡ��С��ֵ" + extractNode.key + "��\n");
			PrintBinHeap(H4);
			printBinHeapByBFS(H4, 5, " ");
		}

		extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n��ȡ��С��ֵ" + extractNode.key + "��\n");
			PrintBinHeap(H4);
			printBinHeapByBFS(H4, 5, " ");
		}

		extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n��ȡ��С��ֵ" + extractNode.key + "��\n");
			PrintBinHeap(H4);
			printBinHeapByBFS(H4, 5, " ");
		}

		H4 = BinHeapDelete(H4, 12);
		System.out.println("\n\nɾ��12��");
		PrintBinHeap(H4);
		printBinHeapByBFS(H4, 5, " ");
	}
}