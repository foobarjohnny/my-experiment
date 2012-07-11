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
 * 쳲�������ͬ�����һ��,Ҳ��һ�ֿɺϲ��ѡ�쳲������ѵ������ǣ����漰ɾ��Ԫ�صĲ�������ҪO��1����ƽ̯����ʱ�䣨����ƽ̯������֪ʶ���鿴���㷨���ۡ���17�£���
 * �Ͷ����һ����쳲���������һ�������ɡ����ֶ���ɢ�ػ��ڶ���ѣ�˵��ɢ����Ϊ���������쳲����������κ�DECREASE-KEY �� DELETE �����������ÿ�����ͺͶ�����һ����
 * �������ִ�������ֲ�������һЩ״̬�±���Ҫ�ƻ�������������������DECREASE-KEY��DELETE ���е�����Ϊk�����ǽ�����ȴ����2k����������£����е������Ƕ�������
 ��������ȣ�쳲�������ͬ������һ����С�����������ɣ�����쳲��������е��������и�������ģ�Ҳ����˵����������������С�����ԣ����Ƕ���������֮��������ģ�����ͼ��
 ����쳲��������ϵĸ��ֿɺϲ��������ؼ�˼���Ǿ����ܾõؽ������ƺ����磬����쳲��������в����½���ϲ�����쳲�������ʱ������ȥ�ϲ��������ǽ������������EXTRACT-MIN������

 ����                    �����(���             ����� (���                        쳲�������(ƽ̯)
 MAKE-HEAP    ��(1)           ��(1)                     ��(1)
 INSERT       ��(lg n)        O(lg n)                  ��(1)
 MINIMUM      ��(1)           O(lg n)                  ��(1)
 EXTRACT-MIN  ��(lg n)        ��(lg n)                  O(lg n)
 UNION        ��(n)           O(lg n)                  ��(1)
 DECREASE-KEY ��(lg n)        ��(lg n)                  ��(1)
 DELETE       ��(lg n)        ��(lg n)                  O(lg n)
 */
/*
 Fibonacci Heap�����F-Heap����һ�ֻ��ڶ���ѵķǳ��������ݽṹ���������Ѳ�ͬ�ĵط����ڣ�
 1��root list���κν���child listʹ��˫��ѭ������������Щlists�еĽ�㲻�����Ⱥ����
 ��Binomial Heap��root list�ĸ���㰴degree��С����˳��child list�Ľ�㰴degree�Ӵ�С˳�򣩣�

 2����������κ�һ��Binomial Tree�и�����degree�����ģ���F-Heap������decrease-key����(cut��cascading cut)��Ե�ʣ������ܱ�֤������degree���

 3�� ��������κν�㣨degree����k�ģ�Ϊ���������У��������Ϊ2^k��F-Heap����Ӧ�Ľ�������½�ΪF{k+2}���Ͻ�Ϊ2^k�����û�� Extract-Min��Delete��������Ļ�����
 ����F{k+2}��ʾFibonacci���У���0,1,1,2,3,5,8,11...���е� k+2��Fibonacci������0��Fibonacci��Ϊ0����1��Fibonacci��Ϊ1��
 ע�ⲻ�������ɶ�������������� F-Heap�� root list�е�ÿ����������Fibonacci����Fibonacci������AVL��������F-Heap���Ƶ�����ֻ����ΪFibonacci���ǽ����� ��һ���½�ֵ��

 4�����������������F-Heap�н������Ϊn����ô�����κν�㣨�����Ǹ���㣩��degree���ֵ������ D(n) = floor(lgn/lg1.618)��
 ����1.618��ʾ�ƽ�ָ���(goldren ratio)��������x^2=x+1��һ���⡣������Extract-Min��consolidate����֮��root list�еĽ�������D(n)+1��
 ���������degree���ֵ������floor(lgn)���Ӷ�root list�������floor(lgn)+1�Ŷ�������

 5������һ�������ѵ����֮ͬ�����ڣ�F-Heap��һ�־���ƽ̯�����ϵĸ����� ���ݽṹ��
 ����Extract-Min��Delete�����������ƽ̯���Ӷ�O(lgn)�������Ĳ���(insert��union��find- min��decrease-key����ƽ̯���Ӷȶ��ǳ�������
 ��������һϵ�еĲ���������Extract-min��delete��������Ϊp���������� ����Ϊq��p<q����ô�ܵ�ƽ̯���Ӷ�ΪO(p + q.lgn)��
 �ﵽ������Ӷȵ�ԭ�������¼��㣬��һ��root list���κν���child list��ʹ����˫��ѭ������
 �ڶ���union��insert�������ӳٺϲ����Ӷ������еĿɺϲ����У�F-heap�ĺϲ�����O(1)��С�ģ�
 �� ����decrease-key��cut��cascading cut����������κηǸ�������ʧȥһ�����ӣ���
 �����ǻ���CLRS�������α�����Fibonacci Heap��ʵ�֡����¼���ֵ��ע��һ�£�
 1��Decrease-Key������ͨ����ӱ���cascade����CLRS��Cascading Cut������tail recursion;
 2��Extract-Min��consolidate������ÿ����һ��root���ͽ��ý���root list��ɾ����
 Ȼ����Ѱ���µ�min���ʱ����degree��С�������ʵ������F-Heap�в��ù��Ľڵ�֮������˳�򣩻ָ�root list��
 3��consolidate�����е�link�����Ͷ�����еĲ������ƣ�ֻ�������ﲻ�ÿ���child list�н���˳��
 4��Extract- Min���漰mark���ԵĴ��룺��Extract-Min�н�min�����к��ӽ����ӵ�root listʱ����Ҫ���mark���ԣ�
 �ȵ�consolidate��link�����Լ�Ѱ���µ�min���ʱ�ٷֱ����ã�linked child��mark = false��root��mark=false��
 */
// Fibonacci Heap
// ����쳲������ѣ�����Ϊ��������½�ΪF(K+2)
public class FibHeap extends MergeableHeap
{
	int				keyNum;		// ���н�����
	FibHeapNode		min;			// ��С�ѣ������
	int				maxNumOfDegree; // ����
	FibHeapNode[]	cons;			// ָ�����ȵ��ڴ�����

	// ��x��˫�����Ƴ�
	public static void FibNodeRemove(FibHeapNode x)
	{
		x.left.right = x.right;
		x.right.left = x.left;
	}

	/*
	 * ��x�ѽ�����y���֮ǰ(ѭ��������) a - y --> a - x - y
	 */

	public static void FibNodeAdd(FibHeapNode x, FibHeapNode y)
	{
		x.left = y.left;
		y.left.right = x;
		x.right = y;
		y.left = x;
	}

	// ��ʼ��һ���յ�Fibonacci Heap
	public static FibHeap FibHeapMake()
	{
		FibHeap heap = new FibHeap();
		if (null == heap) {
			System.out.println("Out of Space!!");
			System.exit(1);

		}
		return heap;
	}

	// ��ʼ�����x
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

	// �ѽ��x����fibonacci heap��
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

	// �������ڵ�ֵ����Fibonacci Heap
	public static void FibHeapInsertKeys(FibHeap heap, int keys[], int keyNum)
	{
		for (int i = 0; i < keyNum; i++) {
			FibHeapInsertKey(heap, keys[i]);
		}
	}

	// ��ֵ����Fibonacci Heap
	public static void FibHeapInsertKey(FibHeap heap, int key)
	{
		FibHeapNode x = FibHeapNodeMake();
		x.key = key;
		FibHeapInsert(heap, x);
	}

	// ��ȡ��С���
	public static FibHeapNode FibHeapExtractMin(FibHeap heap)
	{
		FibHeapNode x = null, z = heap.min;
		if (z != null) {

			// ɾ��z��ÿһ������
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

	// �ϲ�������ͬ�����Ķ�����
	public static void FibHeapConsolidate(FibHeap heap)
	{
		int D, d;
		FibHeapNode t;
		FibHeapNode w = heap.min, x = null, y = null;
		FibHeapConsMake(heap);// ���ٹ�ϣ���ÿռ�
		D = heap.maxNumOfDegree + 1;
		for (int i = 0; i < D; i++) {
			heap.cons[i] = null;
		}

		// �ϲ���ͬ�ȵĸ��ڵ㣬ʹÿ�������Ķ�����Ψһ
		// ����û˵��ôѭ�������Ǿʹ�MIN�������ҳ���ڵ�
		while (null != heap.min) {
			x = FibHeapMinRemove(heap);
			d = x.degree;
			while (null != (heap.cons[d])) {
				y = heap.cons[d];
				if (x.key > y.key) {// �����key��С
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
		heap.min = null;// ԭ�и������

		// ��heap.cons�н�㶼���¼ӵ������У����ҳ���С��
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

	// ��x��������ӵ�y�����
	public static void FibHeapLink(FibHeap heap, FibHeapNode x, FibHeapNode y)
	{
		FibNodeRemove(x);
		if (null == y.child) {
			y.child = x;
		}
		else {
			FibNodeAdd(x, y.child);
			// Ϊ�˱�֤��ӡ����
			y.child = x;
			// ��֤����CHILD��ͬ�ȵģ����ȱ�Ķ���������Ϊ���ᱻCUT��CASCADE CUT
		}
		x.parent = y;
		y.degree++;
		x.marked = false;
	}

	// ����FibHeapConsolidate������ϣ���ÿռ�
	public static void FibHeapConsMake(FibHeap heap)
	{
		int old = heap.maxNumOfDegree;
		heap.maxNumOfDegree = ((int) (Math.log10(heap.keyNum * 1.0) / Math.log10(2.0))) + 1;
		if (old < heap.maxNumOfDegree) {
			// ��Ϊ��Ϊheap.maxNumOfDegree���ܱ��ϲ�,����ҪmaxNumOfDegree + 1
			heap.cons = new FibHeapNode[heap.maxNumOfDegree + 1];
			if (null == heap.cons) {
				System.out.println("Out of Space!");
				System.exit(1);
			}
		}
	}

	// ���ѵ���С����Ƴ�����ָ�������ֵ�
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

	// ��Сһ���ؼ���
	public static void FibHeapDecrease(FibHeap heap, FibHeapNode x, int key)
	{
		FibHeapNode y = x.parent;
		if (x.key < key) {
			System.out.println("new key is greater than current key!");
			System.exit(1);
		}
		x.key = key;

		if (null != y && x.key < y.key) {
			// �ƻ�����С�����ʣ���Ҫ���м������в���
			FibHeapCut(heap, x, y);
			FibHeapCascadingCut(heap, y);
		}
		if (x.key < heap.min.key) {
			heap.min = x;
		}
	}

	// �Լ�д��Increase �൱��heapSort��MAX/MIN HEAPIFY
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

	// �ж�x�븸�ڵ�y֮������ӣ�ʹx��Ϊһ����
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

	// ��������
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

	// �޸Ķ���
	public static void renewDegree(FibHeapNode parent, int degree)
	{
		parent.degree -= degree;
		if (parent.parent != null) {
			renewDegree(parent.parent, degree);
		}
	}

	// ɾ�����
	public static void FibHeapDelete(FibHeap heap, FibHeapNode x)
	{
		FibHeapDecrease(heap, x, Integer.MIN_VALUE);
		FibHeapExtractMin(heap);
	}

	// ���������ؼ���
	public static FibHeapNode FibHeapSearch(FibHeap heap, int key)
	{
		return FibNodeSearch(heap.min, key);
	}

	// ��FibHeapSearch����
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

	// ���ٶ�
	public static void FibHeapDestory(FibHeap heap)
	{
		FibNodeDestory(heap.min);
		// free(heap);
		heap = null;
	}

	// ��FibHeapDestory����
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

	// �����ӡ��
	public static void FibHeapPrint(FibHeap heap)
	{
		System.out.print("The keyNum =" + heap.keyNum + "\n");
		FibNodePrint(heap.min);
		System.out.println("\n");
	};

	// ��FibHeapPrint����
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
				System.out.println("��");
				sibling = sibling.right;
			} while (sibling != null && sibling != heap.min);
		}
		System.out.println("====FibHeap Over====");
	}

	// FibHeapNode == FibHeapTree ����˵��һ����С��������
	// ע�ⲻ�������ɶ�������������� F-Heap�� root
	// list�е�ÿ����������Fibonacci����Fibonacci������AVL��������F-Heap���Ƶ�����ֻ����ΪFibonacci���ǽ�����
	// ��һ���½�ֵ��
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
						// ����Ѹ����ڵ�����Ԫ�ؼӽ���
						sibling = tree;
						while ((sibling = sibling.right) != null && sibling != tree) {
							siblingList.push(sibling);
						}
					}
					while (siblingList.size() > 0) {
						cur = siblingList.poll();
						if (depth > level) {
							// �����ټ�NULL��ȥ
							list.add(cur.child);
						}
						System.out.print(printKey(cur.key + (cur.child == null ? "" : "��"), spaceLength, c));
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
	 * ��ȡ��Сֵ1֮�� The keyNum = 9 (2 (3) (6 (7) (9 (10) ) ) (4 (5) ) ) (11)
	 * 
	 * ����11�ɹ�,��С��8�� The keyNum = 9 (2 (3) (6 (7) (9 (10) ) ) (4 (5) ) ) (8)
	 * 
	 * ɾ��7�ɹ�: The keyNum = 8 (2 (3) (6 (9 (10) ) ) (4 (5) ) ) (8)
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
		System.out.print("��ȡ��Сֵ" + x.key + "֮��\n");
		FibHeapPrint(heap);
		printFibHeapByBFS(heap, spaces, c);

		x = FibHeapSearch(heap, 11);
		if (null != x) {
			System.out.print("����" + x.key + "�ɹ�,\n");
			FibHeapDecrease(heap, x, 8);
			System.out.print("��С��" + x.key + "��\n");
			FibHeapPrint(heap);
			printFibHeapByBFS(heap, spaces, c);
		}

		x = FibHeapSearch(heap, 7);
		if (null != x) {
			System.out.print("ɾ��" + x.key + "�ɹ�:\n");
			FibHeapDelete(heap, x);
			FibHeapPrint(heap);
			printFibHeapByBFS(heap, spaces, c);
		}

		FibHeapDestory(heap);
	}

}