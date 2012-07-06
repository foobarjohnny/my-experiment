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
// 笛卡尔树(Cartesian Tree)
// 笛卡尔树是一棵二叉树，树的每个节点有两个值，一个为key，一个为value。
// 光看key的话，笛卡尔树是一棵二叉搜索树，每个节点的左子树的key都比它小， 右子树都比它大；
// 光看value的话，笛卡尔树有点类似堆，根节点的value是最小（或者最大）的，每个节点的value都比它的子树要小(或者要大)。
// 从上可以看出，笛卡尔树类似于treap,所不同的是笛卡尔树中构成堆的值(也就是value)是预先给出的(因此如果按照treap去构造则很可能会退化),而treap则是在建树的过程中随机生成的。
// 笛卡尔树是把已有的一些（key, value）二元组拿来构造树，然后利用构树过程和构好的树来解决问题。
// 而Treap的目的只是对一些key进行二叉搜索，但是为了保证树的平衡性，
// 为每个key随机地额外增加了一个value（或者叫权重）属性，这样从概率上来讲可以让这棵树更加平衡。
// treap更大的意义在于为了维持BST的平衡型而找到的一种随即化构造的方案。
// 在treap中，insert的时候节点的value值是随机生成的。因此在逐个insert节点的时候能够保证treap的统计意义上的平衡。
// 然而，笛卡尔树的目的更多是为了rmq问题和LCA问题的转化。理解的两者的关系和区别后，什么时候该用什么结构就一目了然了。
// 1.如果v是u的左孩子，则key[v] < key[u].
// 2.如果v是u的右孩子，则key[v] > key[u].
// 3.如果v是u的孩子，则priority[v] > priority[u].
// 笛卡尔树的节点含有2个值，1个key，一个value，其中key是主键，value是辅键。
// 一棵笛卡尔树就是：key升序，value升序或者降序。类似堆。
// 与treap的区别是：treap的value是随机值，是为了使树更加平衡引进的，而笛卡尔树的value是一个确定的值。
// 结构完全相同，功能不一样。理解这一点就知道什么时候用treap，什么时候用笛卡尔树了。
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
		// 把priority小的旋转上去
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

	// 插入操作
	public static void treap_insert(BinarySearchTree T, BinarySearchTreeNode root, int key, int priority)
	{
		// 根为NULL，则直接创建此结点为根结点
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
		// 向右插入结点
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
		// 向左插入结点
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

	// 这是递归删除 BST也可以这样
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
				// 左右孩子都为空不用单独写出来
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
					// 先旋转，然后再删除
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
		// 把priority大的旋转下去
		int rotateChild = 0;
		while ((rotateChild = getChildForRotate(target)) != 0) {
			if (rotateChild == 1) {
				rightRotate(T, target);
			}
			else {
				leftRotate(T, target);
			}
		}

		// 把priority小的旋转上去
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