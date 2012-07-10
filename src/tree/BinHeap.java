//http://blog.csdn.net/acceptedxukai/article/details/6921334
package tree;

import java.util.ArrayList;

/*8
 * http://mindlee.net/2011/09/26/binomial-heaps/
 * http://mindlee.net/2011/09/29/fibonacci-heaps/
 */

//binomial-heap
public class BinHeap
{
	int		key;
	int		degree;
	BinHeap	parent;
	BinHeap	leftChild;
	BinHeap	sibling;

	@Override
	public String toString()
	{
		return "[key=" + key + ", degree=" + degree + ", parent=" + (parent == null ? null : parent.key) + ", leftChild=" + (leftChild == null ? null : leftChild.key) + ", sibling="
				+ (sibling == null ? null : sibling.key) + "]";
	}

	/*
	 * 第一个二叉堆H1: (41) (28 (33) ) (7 (15 (25) ) (12) )
	 * 
	 * 第二个二叉堆H2: (55) (24 (50) ) (2 (3 (8 (10 (44) ) (29) ) (6 (37) ) (18) ) (17
	 * (32 (45) ) (31) ) (23 (30) ) (48) )
	 * 
	 * 合并H1,H2后，得到H3: (41 (55) ) (7 (24 (28 (33) ) (50) ) (15 (25) ) (12) ) (2
	 * (3 (8 (10 (44) ) (29) ) (6 (37) ) (18) ) (17 (32 (45) ) (31) ) (23 (30) )
	 * (48) )
	 * 
	 * 用于测试提取和删除的二叉堆H4: (27 (42) ) (11 (17 (38) ) (18) ) (1 (8 (14 (23 (26) )
	 * (29) ) (12 (16) ) ( 25) ) (10 (37 (41) ) (28) ) (13 (77) ) (6) )
	 * 
	 * 抽取最小的值1后： (6) (13 (27 (42) ) (77) ) (8 (10 (11 (17 (38) ) (18) ) (37 (41)
	 * ) (28) ) (14 (23 (26) ) (29) ) (12 (16) ) (25) )
	 * 
	 * 抽取最小的值6后： (13 (27 (42) ) (77) ) (8 (10 (11 (17 (38) ) (18) ) (37 (41) )
	 * (28) ) (14 (23 (26) ) (29) ) (12 (16) ) (25) )
	 * 
	 * 抽取最小的值8后： (25) (12 (16) ) (10 (13 (14 (23 (26) ) (29) ) (27 (42) ) (77) )
	 * (11 (17 ( 38) ) (18) ) (37 (41) ) (28) )
	 * 
	 * 删除12后： (16 (25) ) (10 (13 (14 (23 (26) ) (29) ) (27 (42) ) (77) ) (11 (17
	 * (38) ) (18) ) (37 (41) ) (28) )
	 */

	// 销毁堆
	// void DestroyBinHeap(BinHeap heap);

	// 用数组内的值建堆
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

	// 两个堆合并
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

	// 将H1, H2的根表合并成一个按度数的单调递增次序排列的链表
	public static BinHeap BinHeapMerge(BinHeap H1, BinHeap H2)
	{
		// heap.堆的首地址，H3为指向新堆根结点
		BinHeap heap = null, firstHeap = null, secondHeap = null, pre_H3 = null, H3 = null;

		if (H1 != null && H2 != null) {
			firstHeap = H1;
			secondHeap = H2;
			// 整个while，firstHeap, secondHeap, pre_H3, H3都在往后顺移
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

	// 使H2成为H1的父节点
	public static void BinLink(BinHeap H1, BinHeap H2)
	{
		H1.parent = H2;
		H1.sibling = H2.leftChild;
		H2.leftChild = H1;
		H2.degree++;
	}

	// 返回最小根节点的指针
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

	// 抽取有最小关键字的结点后 返回BinHeap
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

		// 将y的子结点指针reverse
		BinHeap H2 = null, p = null;
		x = y.leftChild;
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

	// 减少关键字的值
	public static void BinHeapDecreaseKey(BinHeap heap, BinHeap x, int key)
	{
		if (key > x.key) {
			System.out.println("new key is greaer than current key");
			System.exit(1);// 不为降键
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

	private static void swapHeapKey(BinHeap a, BinHeap b)
	{
		int c = a.key;
		a.key = b.key;
		a.key = c;
	}

	// 删除一个关键字
	public static BinHeap BinHeapDelete(BinHeap heap, int key)
	{
		BinHeap x = null;
		x = BinHeapFind(heap, key);
		if (x != null) {
			BinHeapDecreaseKey(heap, x, Integer.MIN_VALUE);
			return BinHeapExtractMin(heap);
		}
		return heap;
	}

	// 找出一个关键字
	public static BinHeap BinHeapFind(BinHeap heap, int key)
	{
		BinHeap p = null, x = null;
		p = heap;
		while (p != null) {
			if (p.key == key) {
				return p;
			}
			else {
				if ((x = BinHeapFind(p.leftChild, key)) != null) {
					return x;
				}
				p = p.sibling;
			}
		}
		return null;
	}

	// 打印输出堆结构
	public static void PrintBinHeap(BinHeap heap)
	{
		if (null == heap) {
			return;
		}
		BinHeap p = heap;

		while (p != null) {
			System.out.print(" (");
			System.out.print(p.key);
			// 显示其孩子
			if (null != p.leftChild) {
				PrintBinHeap(p.leftChild);
			}
			System.out.print(") ");

			p = p.sibling;
		}
	}

	public static void PrintBinomialHeap(BinHeap heap)
	{
		if (null == heap) {
			return;
		}
		BinHeap p = heap;
		int height = getBinHeapHeight(heap);

		while (p != null) {
			System.out.print(" (");
			System.out.print(p.key);
			// 显示其孩子
			if (null != p.leftChild) {
				PrintBinHeap(p.leftChild);
			}
			System.out.print(") ");

			p = p.sibling;
		}
	}

	public static void printTreeByBFS(BinHeap root, int spaceLength, String c)
	{
		ArrayList<BinHeap> list = new ArrayList<BinHeap>();
		list.add(root);
		int currentIndex = 0;
		int nextIndex = 1;
		int level = 0;
		int depth = getBinHeapHeight(root);
		StringBuilder s = new StringBuilder();
		while (currentIndex < list.size())// 还有下层
		{
			level++;
			int startSpaces = (int) (Math.pow(2, (depth - level)) - 1);
			nextIndex = list.size();
			s.append(printKey(level + ":", spaceLength, " "));
			s.append(printBlank(startSpaces, spaceLength));
			int intervalSpaces = (int) (Math.pow(2, depth - level + 1) - 1);
			int j = 0;
			while (currentIndex < nextIndex)// 该层还有元素
			{
				BinHeap tree = list.get(currentIndex);
				s.append(printKey(tree.key + "", spaceLength, c));
				if (tree.leftChild != null) {
					list.add(tree.leftChild);
				}
				if (tree.sibling != null) {
					if (currentIndex < list.size() - 1) {
						list.add(currentIndex + 1, tree.sibling);
					}
					else {
						list.add(tree.sibling);
					}
					nextIndex++;
				}
				if (currentIndex != nextIndex - 1) {
					s.append(printBlank(intervalSpaces, spaceLength));
				}
				currentIndex++;
				j++;
			}
			s.append("\n");
		}
		System.out.print(s);
	}

	// 组合数 m!/(m-n)!n!
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

	public static void printTree(BinarySearchTreeNode t, int spaceLength, String c)
	{
		int depth = 0;
		// getTreeHeight(t);
		System.out.println("depth=" + depth);
		for (int i = 0; i < depth; i++) {
			int spaces = (int) (Math.pow(2, (depth - i - 1)) - 1);
			System.out.print(printKey(i + 1 + ":", spaceLength, " "));
			System.out.print(printBlank(spaces, spaceLength));
			PrintNodeOfLevel(t, i, spaceLength, (int) (Math.pow(2, depth) - 1), c);
			System.out.println();
		}

	}

	public static void printTreeVertical(BinarySearchTreeNode t, int totalSpaces, int spaceLength, int depth, int level)
	{
		if (level <= depth || t != RedBlackTree.NIL) {
			BinarySearchTreeNode left = null, right = null;
			String key = "---";
			level++;
			if (t != null) {
				left = t.getLeft();
				right = t.getRight();
				key = t.key + "";
			}
			printTreeVertical(right, totalSpaces + spaceLength, spaceLength, depth, level);
			for (int i = 0; i < totalSpaces; i++) {
				System.out.print(" ");
			}
			System.out.println(key);
			printTreeVertical(left, totalSpaces + spaceLength, spaceLength, depth, level);
		}
	}

	private static void PrintNodeOfLevel(BinarySearchTreeNode t, int level, int length, int space, String c)
	{
		if (level < 0) {
			return;
		}
		if (level == 0) {
			if (t == null) {
				System.out.print(printPlaceHolder(length));
				System.out.print(printBlank(space, length));
			}
			else {
				System.out.print(printKey(t.key + t.getColor(), length, c));
				System.out.print(printBlank(space, length));
			}
		}
		if (t == null) {
			PrintNodeOfLevel(null, level - 1, length, space / 2, c);
			PrintNodeOfLevel(null, level - 1, length, space / 2, c);
		}
		else {
			PrintNodeOfLevel(t.getLeft(), level - 1, length, space / 2, c);
			PrintNodeOfLevel(t.getRight(), level - 1, length, space / 2, c);
		}
	}

	private static String printPlaceHolder(int length)
	{
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < length; i++) {
			s.append("-");
		}
		return s.toString();
	}

	private static String printBlank(int space, int length)
	{
		StringBuilder s = new StringBuilder();
		for (int j = 0; j < space * length; j++) {
			s.append(" ");
		}
		return s.toString();
	}

	private static String printKey(String k, int length, String c)
	{
		if (k.length() < length) {
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < length; i++) {
				s.append(c);
			}
			s.replace((length - k.length()) / 2, (length - k.length()) / 2 + k.length(), k);
			k = s.toString();
		}
		return k;
	}

	private static int getBinHeapHeight(BinHeap heap)
	{
		if (heap == null) {
			return 0;
		}
		return getBinHeapHeight(heap.leftChild) + 1;
	}

	public static int[]	kp1	= { 12, 7, 25, 15, 28, 33, 41 };

	public static int[]	kp2	= { 18, 3, 37, 6, 8, 29, 10, 44, 30, 23, 2, 48, 31, 17, 45, 32, 24, 50, 55 };

	public static int[]	kp4	= { 37, 41, 10, 28, 13, 77, 1, 6, 16, 12, 25, 8, 14, 29, 26, 23, 18, 11, 17, 38, 42, 27 };

	public static void main(String[] args)
	{
		BinHeap H1 = null;
		H1 = MakeBinHeapWithArray(kp1, kp1.length);
		System.out.println("第一个二叉堆H1:");
		PrintBinHeap(H1);

		BinHeap H2 = null;
		H2 = MakeBinHeapWithArray(kp2, kp2.length);
		System.out.println("\n\n第二个二叉堆H2:");
		PrintBinHeap(H2);

		BinHeap H3 = null;
		H3 = BinHeapUnion(H1, H2);
		System.out.println("\n\n合并H1,H2后，得到H3:");
		PrintBinHeap(H3);

		BinHeap H4 = null;
		H4 = MakeBinHeapWithArray(kp4, kp4.length);
		System.out.println("\n\n用于测试提取和删除的二叉堆H4:");
		PrintBinHeap(H4);

		BinHeap extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n抽取最小的值" + extractNode.key + "后：\n");
			PrintBinHeap(H4);
		}

		extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n抽取最小的值" + extractNode.key + "后：\n");
			PrintBinHeap(H4);
		}

		extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n抽取最小的值" + extractNode.key + "后：\n");
			PrintBinHeap(H4);
		}

		H4 = BinHeapDelete(H4, 12);
		System.out.println("\n\n删除12后：");
		PrintBinHeap(H4);
	}
}