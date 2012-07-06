package tree;

import java.util.Random;

/**
 * http://www.coderplusplus.com/?p=393
 * http://blog.csdn.net/niushuai666/article/details/7428311
 * http://www.cppblog.com/ACM-Boy/archive/2009/05/12/48158.html
 * http://archive.cnblogs.com/a/2129138/
 * 
 * @author dysong
 * 
 */
// �ѿ�����(Cartesian Tree)
// �ѿ�������һ�ö�����������ÿ���ڵ�������ֵ��һ��Ϊkey��һ��Ϊvalue��
// �⿴key�Ļ����ѿ�������һ�ö�����������ÿ���ڵ����������key������С�� ��������������
// �⿴value�Ļ����ѿ������е����ƶѣ����ڵ��value����С��������󣩵ģ�ÿ���ڵ��value������������ҪС(����Ҫ��)��
// ���Ͽ��Կ������ѿ�����������treap,����ͬ���ǵѿ������й��ɶѵ�ֵ(Ҳ����value)��Ԥ�ȸ�����(����������treapȥ������ܿ��ܻ��˻�),��treap�����ڽ����Ĺ�����������ɵġ�
// �ѿ������ǰ����е�һЩ��key, value����Ԫ��������������Ȼ�����ù������̺͹��õ�����������⡣
// ��Treap��Ŀ��ֻ�Ƕ�һЩkey���ж�������������Ϊ�˱�֤����ƽ���ԣ�
// Ϊÿ��key����ض���������һ��value�����߽�Ȩ�أ����ԣ������Ӹ������������������������ƽ�⡣
// treap�������������Ϊ��ά��BST��ƽ���Ͷ��ҵ���һ���漴������ķ�����
// ��treap�У�insert��ʱ��ڵ��valueֵ��������ɵġ���������insert�ڵ��ʱ���ܹ���֤treap��ͳ�������ϵ�ƽ�⡣
// Ȼ�����ѿ�������Ŀ�ĸ�����Ϊ��rmq�����LCA�����ת�����������ߵĹ�ϵ�������ʲôʱ�����ʲô�ṹ��һĿ��Ȼ�ˡ�
// 1.���v��u�����ӣ���key[v] < key[u].
// 2.���v��u���Һ��ӣ���key[v] > key[u].
// 3.���v��u�ĺ��ӣ���priority[v] > priority[u].
// �ѿ������Ľڵ㺬��2��ֵ��1��key��һ��value������key��������value�Ǹ�����
// һ�õѿ��������ǣ�key����value������߽������ƶѡ�
// ��treap�������ǣ�treap��value�����ֵ����Ϊ��ʹ������ƽ�������ģ����ѿ�������value��һ��ȷ����ֵ��
// �ṹ��ȫ��ͬ�����ܲ�һ���������һ���֪��ʲôʱ����treap��ʲôʱ���õѿ������ˡ�
public class CartesianTree extends BinarySearchTree
{
	public static int	MAXN	= 200;

	public static void computeTree(int[] A, int N, int[] pre, int[] rChild, int[] lChild)
	{
		int[] st = new int[MAXN];
		int i, k, top = -1;

		// we start with an empty stack
		// at step i we insert A[i] in the stack
		for (i = 0; i < N; i++) {
			// compute the position of the first element that is
			// equal or smaller than A[i]
			k = top;
			while (k >= 0 && A[st[k]] > A[i]) {
				k--;
			}
			// we modify the tree as explained above
			if (k != -1) {
				pre[i] = st[k];
				rChild[st[k]] = i;
			}
			if (k < top) {
				pre[st[k + 1]] = i;
				lChild[i] = st[k + 1];
			}
			// we insert A[i] in the stack and remove
			// any bigger elements
			st[++k] = i;
			top = k;
		}
		// the first element in the stack is the root of
		// the tree, so it has no father
		pre[st[0]] = -1;
	}

	public static void treeInsert(BinarySearchTree T, int key, int priority)
	{
		BinarySearchTreeNode a = new BinarySearchTreeNode();
		a.setKey(key);
		a.setParent(null);
		a.setLeft(null);
		a.setRight(null);
		a.setColor("");
		a.setPriority(priority);
		treeInsert(T, a);
	}

	protected static void treeInsert(BinarySearchTree T, BinarySearchTreeNode target)
	{
		System.out.println("treap treeinsert");
		if (target == null) {
			return;
		}
		BinarySearchTreeNode parent = null;
		BinarySearchTreeNode current = T.getRoot();
		while (current != null) {
			parent = current;
			if (target.getKey() < current.getKey()) {
				current = current.getLeft();
			}
			else {
				current = current.getRight();
			}
		}
		target.setParent(parent);
		if (parent == null) {
			// Tree root was empty
			T.setRoot(target);
		}
		else if (target.getKey() < parent.getKey()) {
			parent.setLeft(target);
		}
		else {
			parent.setRight(target);
		}
		// ��priorityС����ת��ȥ
		while (parent != null && parent.getPriority() > target.getPriority()) {
			if (parent.getLeft() == target) {
				rightRotate(T, parent);
			}
			else {
				leftRotate(T, parent);
			}
			parent = target.getParent();
		}
	}

	// �������
	public static void treap_insert(BinarySearchTree T, BinarySearchTreeNode root, int key, int priority)
	{
		// ��ΪNULL����ֱ�Ӵ����˽��Ϊ�����
		if (root == null) {
			root = new BinarySearchTreeNode();
			root.left = null;
			root.right = null;
			root.priority = priority;
			root.key = key;
			if (T.getRoot() == null) {
				T.setRoot(root);
			}
		}
		// ���Ҳ�����
		else if (key < root.key) {
			if (root.left == null) {
				BinarySearchTreeNode left = new BinarySearchTreeNode();
				left.left = null;
				left.right = null;
				left.priority = priority;
				left.key = key;
				root.left = left;
				left.parent = root;
			}
			else {
				treap_insert(T, root.left, key, priority);
			}
			if (root.left.priority < root.priority) {
				rightRotate(T, root);
			}
		}
		// ���������
		else {
			if (root.right == null) {
				BinarySearchTreeNode right = new BinarySearchTreeNode();
				right.left = null;
				right.right = null;
				right.priority = priority;
				right.key = key;
				root.right = right;
				right.parent = root;
			}
			else {
				treap_insert(T, root.right, key, priority);
			}
			if (root.right.priority < root.priority) {
				leftRotate(T, root);
			}
		}
	}

	// ���ǵݹ�ɾ�� BSTҲ��������
	public static void treap_delete(BinarySearchTree T, BinarySearchTreeNode root, int key)
	{
		if (root != null) {
			if (key < root.key) {
				if (root.left == null) {
					return;
				}
				else {
					treap_delete(T, root.left, key);
				}
			}
			else if (key > root.key)
				if (root.right == null) {
					return;
				}
				else {
					treap_delete(T, root.right, key);
				}
			else {
				// ���Һ��Ӷ�Ϊ�ղ��õ���д����
				BinarySearchTreeNode parent = root.getParent();
				BinarySearchTreeNode next;
				if (root.left == null) {
					next = root.right;
					if (parent == null) {
						T.setRoot(next);
					}
					else {
						if (root == parent.getLeft()) {
							parent.setLeft(next);
						}
						else {
							parent.setRight(next);
						}
					}
					if (next == null) {
						return;
					}
					else {
						next.setParent(parent);
					}
				}
				else if (root.right == null) {
					next = root.left;
					if (parent == null) {
						T.setRoot(next);
					}
					else {
						if (root == parent.getLeft()) {
							parent.setLeft(next);
						}
						else {
							parent.setRight(next);
						}
					}
					if (next == null) {
						return;
					}
					else {
						next.setParent(parent);
					}
				}
				else {
					// ����ת��Ȼ����ɾ��
					if (root.left.priority < root.right.priority) {
						rightRotate(T, root);
						treap_delete(T, root, key);
					}
					else {
						leftRotate(T, root);
						treap_delete(T, root, key);
					}
				}
			}
		}
	}

	public static void treeDelete_Pre(BinarySearchTree T, BinarySearchTreeNode target)
	{
		BinarySearchTreeNode candidate = null;
		BinarySearchTreeNode child = T.getRoot();
		if (target == null) {
			return;
		}
		if (target.getLeft() == null || target.getRight() == null) {
			candidate = target;
		}
		else {
			candidate = treePredecessor(target);
		}

		if (candidate.getRight() != null) {
			child = candidate.getRight();
		}
		else {
			child = candidate.getLeft();
		}

		if (child != null) {
			child.setParent(candidate.getParent());
		}

		if (candidate.getParent() == null) {
			T.setRoot(child);
		}
		else {
			if (candidate == candidate.getParent().getRight()) {
				candidate.getParent().setRight(child);
			}
			else {
				candidate.getParent().setLeft(child);
			}
		}
		if (candidate != target) {
			target.setKey(candidate.getKey());
			target.setPriority(candidate.getPriority());
		}
		// ��priority�����ת��ȥ
		int rotateChild = 0;
		while ((rotateChild = getChildForRotate(target)) != 0) {
			if (rotateChild == 1) {
				rightRotate(T, target);
			}
			else {
				leftRotate(T, target);
			}
		}

		// ��priorityС����ת��ȥ
		BinarySearchTreeNode parent = target.getParent();
		while (parent != null && parent.getPriority() > target.getPriority()) {
			if (parent.getLeft() == target) {
				rightRotate(T, parent);
			}
			else {
				leftRotate(T, parent);
			}
			parent = target.getParent();
		}
		// System.out.println("after");
		// printTree(T.getRoot(), 5, "-");
	}

	public static int getChildForRotate(BinarySearchTreeNode target)
	{
		if (target == null) {
			return 0;
		}
		int maxI = 0;
		int minI = 0;
		int max = target.getPriority();
		int min = target.getPriority();
		int p = target.getPriority();
		BinarySearchTreeNode rc = target.getRight();
		BinarySearchTreeNode lc = target.getLeft();
		if (rc == null && lc == null) {
			return 0;
		}
		if (rc != null && lc != null) {
			if (rc.getPriority() > lc.getPriority()) {
				minI = 1;
				min = lc.getPriority();
				maxI = 2;
				max = rc.getPriority();
			}
			else {
				minI = 2;
				min = rc.getPriority();
				maxI = 1;
				max = lc.getPriority();
			}
		}
		else if (rc != null) {
			minI = 2;
			min = rc.getPriority();
			maxI = 2;
			max = rc.getPriority();
		}
		else {
			minI = 1;
			min = lc.getPriority();
			maxI = 1;
			max = lc.getPriority();
		}

		if (p <= min) {
			return 0;
		}
		else {
			return minI;
		}
	}

	public static void treeDelete_Pre(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode target = treeSearch_Iterative(T.getRoot(), key);
		treeDelete_Pre(T, target);
	}

	public static void main(String[] args)
	{
		/* A sample use of these functions. Start with the empty tree, */
		/* insert some stuff into it, and then delete it */
		BinarySearchTree T = new CartesianTree();
		BinarySearchTree T2 = new CartesianTree();
		int i;

		int NUM = 20;
		Random r = new Random();
		for (i = 0; i < NUM; i++) {
			int key = r.nextInt(NUM) % NUM;
			int pri = r.nextInt(NUM) % NUM;
			treeInsert(T, key, pri);
			treap_insert(T2, T2.getRoot(), key, pri);
		}
		printTree(T2.getRoot(), 5, "-");
		printTree(T.getRoot(), 5, "-");
		System.out.println("\n");
		preOrderTreeWalk(T.getRoot());
		System.out.println("\n");
		preOrderTreeWalk(T2.getRoot());
		System.out.println("\n");
		for (i = 0; i < NUM; i++) {
			System.out.println(i);
			treeDelete_Pre(T, i);
			treap_delete(T2, T2.getRoot(), i);
		}

		preOrderTreeWalk(T.getRoot());
		printTree(T.getRoot(), 5, "-");
		System.out.println("\n");
		preOrderTreeWalk(T2.getRoot());
		printTree(T2.getRoot(), 5, "-");
		System.out.println("\n");
		// System.out.println("\nsize = " + size);
	}
}