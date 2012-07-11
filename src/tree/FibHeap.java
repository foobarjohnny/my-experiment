//http://blog.csdn.net/acceptedxukai/article/details/6921334
package tree;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * http://mindlee.net/2011/09/26/binomial-heaps/
 * http://mindlee.net/2011/09/29/fibonacci-heaps/
 * http://blog.csdn.net/ture010love/article/details/6738394
 * http://www.cnblogs.com/ljsspace/archive/2011/09/05/2167460.html
 * 
 * 斐波那契堆同二项堆一样,也是一种可合并堆。斐波那契堆的优势是：不涉及删除元素的操作仅需要O（1）的平摊运行时间（关于平摊分析的知识建议看《算法导论》第17章）。
 * 和二项堆一样，斐波那契堆由一组树构成。这种堆松散地基于二项堆，说松散是因为：如果不对斐波那契堆做任何DECREASE-KEY 或 DELETE 操作，则堆中每棵树就和二项树一样；
 * 但是如果执行这两种操作，在一些状态下必须要破坏二项树的特征，比如DECREASE-KEY或DELETE 后，有的树高为k，但是结点个数却少于2k。这种情况下，堆中的树不是二项树。
 与二项堆相比，斐波那契堆同样是由一组最小堆有序树构成，但是斐波那契堆中的树都是有根而无序的，也就是说，单独的树满足最小堆特性，但是堆内树与树之间是无序的，如下图。
 对于斐波那契堆上的各种可合并操作，关键思想是尽可能久地将工作推后。例如，当向斐波那契堆中插入新结点或合并两个斐波那契堆时，并不去合并树，而是将这个工作留给EXTRACT-MIN操作。

 操作                    二叉堆(最坏）             二项堆 (最坏）                        斐波那契堆(平摊)
 MAKE-HEAP    Θ(1)           Θ(1)                     Θ(1)
 INSERT       Θ(lg n)        O(lg n)                  Θ(1)
 MINIMUM      Θ(1)           O(lg n)                  Θ(1)
 EXTRACT-MIN  Θ(lg n)        Θ(lg n)                  O(lg n)
 UNION        Θ(n)           O(lg n)                  Θ(1)
 DECREASE-KEY Θ(lg n)        Θ(lg n)                  Θ(1)
 DELETE       Θ(lg n)        Θ(lg n)                  O(lg n)
 */
/*
 Fibonacci Heap（简称F-Heap）是一种基于二项堆的非常灵活的数据结构。它与二项堆不同的地方在于：
 1）root list和任何结点的child list使用双向循环链表，而且这些lists中的结点不再有先后次序
 （Binomial Heap中root list的根结点按degree从小到大顺序，child list的结点按degree从大到小顺序）；

 2）二项堆中任何一颗Binomial Tree中根结点的degree是最大的，而F-Heap中由于decrease-key操作(cut和cascading cut)的缘故，并不能保证根结点的degree最大；

 3） 二项堆中任何结点（degree等于k的）为根的子树中，结点总数为2^k；F-Heap中相应的结点总数下界为F{k+2}，上界为2^k（如果没有 Extract-Min和Delete两类操作的话）。
 其中F{k+2}表示Fibonacci数列（即0,1,1,2,3,5,8,11...）中第 k+2个Fibonacci数，第0个Fibonacci数为0，第1个Fibonacci数为1。
 注意不像二项堆由二项树组成那样， F-Heap的 root list中的每棵树并不是Fibonacci树（Fibonacci树属于AVL树），而F-Heap名称的由来只是因为Fibonacci数是结点个数 的一个下界值。

 4）基于上面的区别，若F-Heap中结点总数为n，那么其中任何结点（包括非根结点）的degree最大值不超过 D(n) = floor(lgn/lg1.618)，
 这里1.618表示黄金分割率(goldren ratio)，即方程x^2=x+1的一个解。所以在Extract-Min的consolidate操作之后，root list中的结点最多有D(n)+1。
 而二项堆中degree最大值不超过floor(lgn)，从而root list中最多有floor(lgn)+1颗二项树。

 5）另外一个与二项堆的最大不同之处在于：F-Heap是一种具有平摊意义上的高性能 数据结构。
 除了Extract-Min和Delete两类操作具有平摊复杂度O(lgn)，其他的操作(insert，union，find- min，decrease-key）的平摊复杂度都是常数级。
 因此如果有一系列的操作，其中Extract-min和delete操作个数为p，其他操作 个数为q，p<q，那么总的平摊复杂度为O(p + q.lgn)。
 达到这个复杂度的原因有以下几点，第一，root list和任何结点的child list中使用了双向循环链表；
 第二，union和insert操作的延迟合并，从而在所有的可合并堆中，F-heap的合并开销O(1)最小的；
 第 三，decrease-key中cut和cascading cut的巧妙处理（即任何非根结点最多失去一个孩子）。
 以下是基于CLRS第三版的伪代码的Fibonacci Heap的实现。以下几点值得注意一下：
 1）Decrease-Key操作中通过添加变量cascade消除CLRS中Cascading Cut函数的tail recursion;
 2）Extract-Min的consolidate函数中每处理一个root结点就将该结点从root list中删除，
 然后在寻找新的min结点时按照degree从小到大次序（实际上在F-Heap中不用关心节点之间的相对顺序）恢复root list；
 3）consolidate函数中的link操作和二项堆中的操作类似，只不过这里不用考虑child list中结点的顺序；
 4）Extract- Min中涉及mark属性的代码：在Extract-Min中将min的所有孩子结点添加到root list时不需要清除mark属性，
 等到consolidate的link操作以及寻找新的min结点时再分别设置：linked child的mark = false，root的mark=false。
 */
// Fibonacci Heap
// 叫做斐波那契堆，是因为结点总数下界为F(K+2)
public class FibHeap extends MergeableHeap
{
	int				keyNum;		// 堆中结点个数
	FibHeapNode		min;			// 最小堆，根结点
	int				maxNumOfDegree; // 最大度
	FibHeapNode[]	cons;			// 指向最大度的内存区域

	// 将x从双链表移除
	public static void FibNodeRemove(FibHeapNode x)
	{
		x.left.right = x.right;
		x.right.left = x.left;
	}

	/*
	 * 将x堆结点加入y结点之前(循环链表中) a - y --> a - x - y
	 */

	public static void FibNodeAdd(FibHeapNode x, FibHeapNode y)
	{
		x.left = y.left;
		y.left.right = x;
		x.right = y;
		y.left = x;
	}

	// 初始化一个空的Fibonacci Heap
	public static FibHeap FibHeapMake()
	{
		FibHeap heap = new FibHeap();
		if (null == heap) {
			System.out.println("Out of Space!!");
			System.exit(1);

		}
		return heap;
	}

	// 初始化结点x
	public static FibHeapNode FibHeapNodeMake()
	{
		FibHeapNode x = new FibHeapNode();
		if (null == x) {
			System.out.println("Out of Space!!");
			System.exit(1);
		}
		x.left = x.right = x;
		return x;
	}

	// 堆结点x插入fibonacci heap中
	public static void FibHeapInsert(FibHeap heap, FibHeapNode x)
	{
		if (0 == heap.keyNum) {
			heap.min = x;
		}
		else {
			FibNodeAdd(x, heap.min);
			x.parent = null;
			if (x.key < heap.min.key) {
				heap.min = x;
			}
		}
		heap.keyNum++;
	}

	// 将数组内的值插入Fibonacci Heap
	public static void FibHeapInsertKeys(FibHeap heap, int keys[], int keyNum)
	{
		for (int i = 0; i < keyNum; i++) {
			FibHeapInsertKey(heap, keys[i]);
		}
	}

	// 将值插入Fibonacci Heap
	public static void FibHeapInsertKey(FibHeap heap, int key)
	{
		FibHeapNode x = FibHeapNodeMake();
		x.key = key;
		FibHeapInsert(heap, x);
	}

	// 抽取最小结点
	public static FibHeapNode FibHeapExtractMin(FibHeap heap)
	{
		FibHeapNode x = null, z = heap.min;
		if (z != null) {

			// 删除z的每一个孩子
			while (null != z.child) {
				x = z.child;
				FibNodeRemove(x);
				if (x.right == x) {
					z.child = null;
				}
				else {
					z.child = x.right;
				}
				FibNodeAdd(x, z);// add x to the root list heap
				x.parent = null;
			}

			FibNodeRemove(z);
			if (z.right == z) {
				heap.min = null;
			}
			else {
				heap.min = z.right;
				FibHeapConsolidate(heap);
			}
			heap.keyNum--;
		}
		return z;
	}

	// 合并左右相同度数的二项树
	public static void FibHeapConsolidate(FibHeap heap)
	{
		int D, d;
		FibHeapNode t;
		FibHeapNode w = heap.min, x = null, y = null;
		FibHeapConsMake(heap);// 开辟哈希所用空间
		D = heap.maxNumOfDegree + 1;
		for (int i = 0; i < D; i++) {
			heap.cons[i] = null;
		}

		// 合并相同度的根节点，使每个度数的二项树唯一
		// 书上没说怎么循环，我们就从MIN不断向右抽出节点
		while (null != heap.min) {
			x = FibHeapMinRemove(heap);
			d = x.degree;
			while (null != (heap.cons[d])) {
				y = heap.cons[d];
				if (x.key > y.key) {// 根结点key最小
					t = x;
					x = y;
					y = t;
				}
				FibHeapLink(heap, y, x);
				heap.cons[d] = null;
				d++;
			}
			heap.cons[d] = x;
		}
		heap.min = null;// 原有根表清除

		// 将heap.cons中结点都重新加到根表中，且找出最小根
		for (int i = 0; i < D; i++) {
			if (heap.cons[i] != null) {
				if (null == heap.min) {
					heap.min = heap.cons[i];
				}
				else {
					FibNodeAdd(heap.cons[i], heap.min);
					if (heap.cons[i].key < heap.min.key) {
						heap.min = heap.cons[i];
					}
				}
			}
		}
	}

	// 将x根结点链接到y根结点
	public static void FibHeapLink(FibHeap heap, FibHeapNode x, FibHeapNode y)
	{
		FibNodeRemove(x);
		if (null == y.child) {
			y.child = x;
		}
		else {
			FibNodeAdd(x, y.child);
			// 为了保证打印美观
			y.child = x;
			// 保证他的CHILD是同度的，像残缺的二项树，因为他会被CUT和CASCADE CUT
		}
		x.parent = y;
		y.degree++;
		x.marked = false;
	}

	// 开辟FibHeapConsolidate函数哈希所用空间
	public static void FibHeapConsMake(FibHeap heap)
	{
		int old = heap.maxNumOfDegree;
		heap.maxNumOfDegree = ((int) (Math.log10(heap.keyNum * 1.0) / Math.log10(2.0))) + 1;
		if (old < heap.maxNumOfDegree) {
			// 因为度为heap.maxNumOfDegree可能被合并,所以要maxNumOfDegree + 1
			heap.cons = new FibHeapNode[heap.maxNumOfDegree + 1];
			if (null == heap.cons) {
				System.out.println("Out of Space!");
				System.exit(1);
			}
		}
	}

	// 将堆的最小结点移出，并指向其有兄弟
	public static FibHeapNode FibHeapMinRemove(FibHeap heap)
	{
		FibHeapNode min = heap.min;
		if (heap.min == min.right) {
			heap.min = null;
		}
		else {
			FibNodeRemove(min);
			heap.min = min.right;
		}
		min.left = min.right = min;
		return min;
	}

	// 减小一个关键字
	public static void FibHeapDecrease(FibHeap heap, FibHeapNode x, int key)
	{
		FibHeapNode y = x.parent;
		if (x.key < key) {
			System.out.println("new key is greater than current key!");
			System.exit(1);
		}
		x.key = key;

		if (null != y && x.key < y.key) {
			// 破坏了最小堆性质，需要进行级联剪切操作
			FibHeapCut(heap, x, y);
			FibHeapCascadingCut(heap, y);
		}
		if (x.key < heap.min.key) {
			heap.min = x;
		}
	}

	// 自己写的Increase 相当于heapSort的MAX/MIN HEAPIFY
	public static void FibHeapIncrease(FibHeap heap, FibHeapNode x, int key)
	{
		FibHeapNode y = x.parent;
		if (x.key > key) {
			System.out.println("new key is less than current key!");
			System.exit(1);
		}
		x.key = key;
		FibHeapNode child = x.child;
		if (child == null) {
			return;
		}
		FibHeapNode right = child;
		FibHeapNode min = x;
		do {
			if (right.key < min.key) {
				min = right;
			}
		} while ((right = right.right) != child);
		if (x != min) {
			x.key = min.key;
			FibHeapIncrease(heap, min, key);
		}
	}

	// 切断x与父节点y之间的链接，使x成为一个根
	public static void FibHeapCut(FibHeap heap, FibHeapNode x, FibHeapNode y)
	{
		FibNodeRemove(x);
		y.degree--;
		// renewDegree(y, x.degree);
		if (x == x.right) {
			y.child = null;
		}
		else {
			y.child = x.right;
		}
		x.parent = null;
		x.left = x.right = x;
		x.marked = false;
		FibNodeAdd(x, heap.min);
	}

	// 级联剪切
	public static void FibHeapCascadingCut(FibHeap heap, FibHeapNode y)
	{
		FibHeapNode z = y.parent;
		if (null != z) {
			if (y.marked == false) {
				y.marked = true;
			}
			else {
				FibHeapCut(heap, y, z);
				FibHeapCascadingCut(heap, z);
			}
		}
	}

	// 修改度数
	public static void renewDegree(FibHeapNode parent, int degree)
	{
		parent.degree -= degree;
		if (parent.parent != null) {
			renewDegree(parent.parent, degree);
		}
	}

	// 删除结点
	public static void FibHeapDelete(FibHeap heap, FibHeapNode x)
	{
		FibHeapDecrease(heap, x, Integer.MIN_VALUE);
		FibHeapExtractMin(heap);
	}

	// 堆内搜索关键字
	public static FibHeapNode FibHeapSearch(FibHeap heap, int key)
	{
		return FibNodeSearch(heap.min, key);
	}

	// 被FibHeapSearch调用
	public static FibHeapNode FibNodeSearch(FibHeapNode x, int key)
	{
		FibHeapNode w = x, y = null;
		if (x != null) {
			do {
				if (w.key == key) {
					y = w;
					break;
				}
				else if (null != (y = FibNodeSearch(w.child, key))) {
					break;
				}
				w = w.right;
			} while (w != x);
		}
		return y;
	}

	public static int getFibNodeHeight(FibHeapNode x)
	{
		FibHeapNode w = x, y = null;
		int height = 0;
		if (x != null) {
			do {
				height = Math.max(height, getFibNodeHeight(w.child) + 1);
				w = w.right;
			} while (w != x);
		}
		else {
			height = 0;
		}
		return height;
	}

	// 销毁堆
	public static void FibHeapDestory(FibHeap heap)
	{
		FibNodeDestory(heap.min);
		// free(heap);
		heap = null;
	}

	// 被FibHeapDestory调用
	public static void FibNodeDestory(FibHeapNode x)
	{
		FibHeapNode p = x, q = null;
		while (p != null) {
			FibNodeDestory(p.child);
			q = p;
			if (p.left == x) {
				p = null;
			}
			else {
				p = p.left;
			}
			q.right = null;
			// free(q.right);
		}
	}

	// 输出打印堆
	public static void FibHeapPrint(FibHeap heap)
	{
		System.out.print("The keyNum =" + heap.keyNum + "\n");
		FibNodePrint(heap.min);
		System.out.println("\n");
	};

	// 被FibHeapPrint调用
	public static void FibNodePrint(FibHeapNode x)
	{
		FibHeapNode p = null;
		if (null == x) {
			return;
		}
		p = x;
		do {
			System.out.print(" (");
			System.out.print(p.key);
			if (p.child != null) {
				FibNodePrint(p.child);
			}
			System.out.print(") ");
			p = p.left;
		} while (x != p);
	}

	public static void printFibHeapByBFS(FibHeap heap, int spaceLength, String c)
	{
		System.out.println("\n====FibHeap Start====");
		FibHeapNode sibling = heap.min;
		if (sibling != null) {
			do {
				printFibHeapNodeByBFS(sibling, spaceLength, c);
				System.out.println("↓");
				sibling = sibling.right;
			} while (sibling != null && sibling != heap.min);
		}
		System.out.println("====FibHeap Over====");
	}

	// FibHeapNode == FibHeapTree 书上说是一组最小堆有序树
	// 注意不像二项堆由二项树组成那样， F-Heap的 root
	// list中的每棵树并不是Fibonacci树（Fibonacci树属于AVL树），而F-Heap名称的由来只是因为Fibonacci数是结点个数
	// 的一个下界值。
	public static void printFibHeapNodeByBFS(FibHeapNode root, int spaceLength, String c)
	{
		ArrayList<FibHeapNode> list = new ArrayList<FibHeapNode>();
		LinkedList<FibHeapNode> siblingList = new LinkedList<FibHeapNode>();
		StringBuilder s = new StringBuilder();
		int startSpaces = 1;
		int intervalSpaces = 1;
		int currentIndex = 0;
		int nextIndex = 1;
		int level = 0;
		int depth = getFibNodeHeight(root);
		FibHeapNode tree;
		FibHeapNode sibling;
		FibHeapNode cur;
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
						// 避免把根表内的其他元素加进来
						sibling = tree;
						while ((sibling = sibling.right) != null && sibling != tree) {
							siblingList.push(sibling);
						}
					}
					while (siblingList.size() > 0) {
						cur = siblingList.poll();
						if (depth > level) {
							// 避免再加NULL进去
							list.add(cur.child);
						}
						System.out.print(printKey(cur.key + (cur.child == null ? "" : "↓"), spaceLength, c));
						if (cur.right != null && level != 1 && siblingList.size() > 0) {
							System.out.print(printKey("--", spaceLength, " "));
						}
						else {
							System.out.print(printBlank(intervalSpaces, spaceLength));
						}
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

	/*
	 * The keyNum = 10 (1) (11) (10) (9) (7) (6) (5) (4) (3) (2)
	 * 
	 * 抽取最小值1之后： The keyNum = 9 (2 (3) (6 (7) (9 (10) ) ) (4 (5) ) ) (11)
	 * 
	 * 查找11成功,减小到8后： The keyNum = 9 (2 (3) (6 (7) (9 (10) ) ) (4 (5) ) ) (8)
	 * 
	 * 删除7成功: The keyNum = 8 (2 (3) (6 (9 (10) ) ) (4 (5) ) ) (8)
	 */
	public static void main(String[] args)
	{
		int spaces = 5;
		String c = " ";
		int[] keys = { 1, 2, 3, 4, 5, 6, 7, 9, 10, 11 };
		FibHeap heap = null;
		FibHeapNode x = null;
		heap = FibHeapMake();
		FibHeapInsertKeys(heap, keys, 10);
		FibHeapPrint(heap);
		printFibHeapByBFS(heap, spaces, c);

		x = FibHeapExtractMin(heap);
		System.out.print("抽取最小值" + x.key + "之后：\n");
		FibHeapPrint(heap);
		printFibHeapByBFS(heap, spaces, c);

		x = FibHeapSearch(heap, 11);
		if (null != x) {
			System.out.print("查找" + x.key + "成功,\n");
			FibHeapDecrease(heap, x, 8);
			System.out.print("减小到" + x.key + "后：\n");
			FibHeapPrint(heap);
			printFibHeapByBFS(heap, spaces, c);
		}

		x = FibHeapSearch(heap, 7);
		if (null != x) {
			System.out.print("删除" + x.key + "成功:\n");
			FibHeapDelete(heap, x);
			FibHeapPrint(heap);
			printFibHeapByBFS(heap, spaces, c);
		}

		FibHeapDestory(heap);
	}

}