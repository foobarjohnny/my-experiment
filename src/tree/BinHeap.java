//http://blog.csdn.net/acceptedxukai/article/details/6921334
package tree;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * http://mindlee.net/2011/09/26/binomial-heaps/
 * http://mindlee.net/2011/09/29/fibonacci-heaps/
 * 二项树Bk是一种递归定义的有序树。二项树B0只包含一个结点。二项树Bk由两个子树Bk-1连接而成：其中一棵树的根是另一棵树的根的最左孩子。二项树的性质有：
 1)共有2^k个结点。
 2)树的高度为k。从0开始
 3)在深度i处恰有Cik个结点。i与K从0开始
 4)根的度数（子女的个数）为k，它大于任何其他结点的度数；如果根的子女从左到右的编号设为k-1, k-2, …, 0，子女i是子树Bi的根。
 右图中图(b)表示二项树B0至B4中示出了各结点的深度。图(c)是以另外一种方式来看二项树Bk。
 PS : 在一棵包含n个结点的二项树中，任意结点的最大度数为lgn. 即结点度数为k

 操作                    二叉堆(最坏）             二项堆 (最坏）                        斐波那契堆(平摊)
 MAKE-HEAP    Θ(1)           Θ(1)                     Θ(1)
 INSERT       Θ(lg n)        O(lg n)                  Θ(1)
 MINIMUM      Θ(1)           O(lg n)                  Θ(1)
 EXTRACT-MIN  Θ(lg n)        Θ(lg n)                  O(lg n)
 UNION        Θ(n)           O(lg n)                  Θ(1)
 DECREASE-KEY Θ(lg n)        Θ(lg n)                  Θ(1)
 DELETE       Θ(lg n)        Θ(lg n)                  O(lg n)
 */

//binomial-heap
//binomial-heap 由binomial-tree 组成
// 二项树的各个根组成根表
// 二项树和F树都不能有效地支持SEARCH操作
// 最坏情况下 Make-Heap为O(1),其余ExtractMin, Minimum, Insert, Union, DecreaseKey, Delete 均为O(lgN)
// Search 没有
// 性能比较见(二项堆 F堆 二叉堆 性能比较.jpg)
// 每层宽度有B0+B0=B1,即Bk=(Bk-1)+B(k-1)=2B(K-1) 即W0=1,W1=1,Wk=2^(k-1)[k>=2]
// 从定义可知 堆中 可以 大于或等于父结点，有等于，因为堆中没有旋转，树中涉及旋转要特殊处理
public class BinHeap extends MergeableHeap
{
	// 二项堆由二项树构成的树表，只不过这里直接用BinHeap的Sibling代替了Head表
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
	 * 第一个二项堆H1: (41) (28 (33) ) (7 (15 (25) ) (12) )
	 * 
	 * 第二个二项堆H2: (55) (24 (50) ) (2 (3 (8 (10 (44) ) (29) ) (6 (37) ) (18) ) (17
	 * (32 (45) ) (31) ) (23 (30) ) (48) )
	 * 
	 * 合并H1,H2后，得到H3: (41 (55) ) (7 (24 (28 (33) ) (50) ) (15 (25) ) (12) ) (2
	 * (3 (8 (10 (44) ) (29) ) (6 (37) ) (18) ) (17 (32 (45) ) (31) ) (23 (30) )
	 * (48) )
	 * 
	 * 用于测试提取和删除的二项堆H4: (27 (42) ) (11 (17 (38) ) (18) ) (1 (8 (14 (23 (26) )
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
		H1.sibling = H2.rightChild;
		H2.rightChild = H1;
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

	// 增加关键字的值
	// Increase 为自己写出来, 他不能用于DELETE因为他们把KEY增长到MAX下降到树底再删去 会破坏二项树完整
	// 对比一下二叉堆的删除
	// 效率应该是O(lgN)
	// 自己写的Increase 相当于heapSort的MAX/MIN HEAPIFY
	public static void BinHeapIncreaseKey(BinHeap heap, BinHeap x, int key)
	{
		if (key < x.key) {
			System.out.println("new key is less than current key");
			System.exit(1);// 不为升键
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

	// 删除一个关键字
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

	// 找出一个关键字
	// 效率未知
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
			System.out.println("↓");
			sibling = sibling.sibling;
		}
		System.out.println("====BinHeap Over====");
	}

	// 二项堆由二项树构成的树表，只不过这里直接用BinHeap的Sibling代替了Head表
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
		while (currentIndex < list.size())// 还有下层
		{
			level++;
			nextIndex = list.size();
			System.out.print(printKey(level + ":", spaceLength, " "));
			while (currentIndex < nextIndex)// 该层还有元素
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
						System.out.print(printKey(cur.key + (cur.rightChild == null ? "" : "↓"), spaceLength, c));
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
		System.out.println("第一个二项堆H1:");
		PrintBinHeap(H1);
		printBinHeapByBFS(H1, 5, " ");

		BinHeap H2 = null;
		H2 = MakeBinHeapWithArray(kp2, kp2.length);
		System.out.println("\n\n第二个二项堆H2:");
		PrintBinHeap(H2);
		printBinHeapByBFS(H2, 5, " ");

		BinHeap H3 = null;
		H3 = BinHeapUnion(H1, H2);
		System.out.println("\n\n合并H1,H2后，得到H3:");
		PrintBinHeap(H3);
		printBinHeapByBFS(H3, 5, " ");

		BinHeap H4 = null;
		H4 = MakeBinHeapWithArray(kp4, kp4.length);
		System.out.println("\n\n用于测试提取和删除的二项堆H4:");
		PrintBinHeap(H4);
		printBinHeapByBFS(H4, 5, " ");

		BinHeap extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n抽取最小的值" + extractNode.key + "后：\n");
			PrintBinHeap(H4);
			printBinHeapByBFS(H4, 5, " ");
		}

		extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n抽取最小的值" + extractNode.key + "后：\n");
			PrintBinHeap(H4);
			printBinHeapByBFS(H4, 5, " ");
		}

		extractNode = BinHeapMin(H4);
		H4 = BinHeapExtractMin(H4);
		if (extractNode != null) {
			System.out.print("\n\n抽取最小的值" + extractNode.key + "后：\n");
			PrintBinHeap(H4);
			printBinHeapByBFS(H4, 5, " ");
		}

		H4 = BinHeapDelete(H4, 12);
		System.out.println("\n\n删除12后：");
		PrintBinHeap(H4);
		printBinHeapByBFS(H4, 5, " ");
	}
}