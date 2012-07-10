package tree;

import java.util.Random;

/**
 * http://www.cnblogs.com/kernel_hcy/archive/2010/03/17/1688360.html
 * http://dongxicheng.org/structure/splay-tree/
 * 
 * @author dysong
 * 
 */
// SplyTree Tree 伸展树 自适应调整树
// 伸展树，或者叫自适应查找树，是一种用于保存有序集合的简单高效的数据结构。
// 伸展树实质上是一个二叉查找树。允许查找，插入，删除，删除最小，删除最大，分割，合并等许多操作，
// 这些操作的时间复杂度为O(logN)。由于伸展树可以适应需求序列，因此他们的性能在实际应用中更优秀。
// 伸展树支持所有的二叉树操作。伸展树不保证最坏情况下的时间复杂度为O(logN)。
// 伸展树的时间复杂度边界是均摊的。尽管一个单独的操作可能很耗时，但对于一个任意的操作序列，时间复杂度可以保证为O(logN)。
public class SplayTree extends BinarySearchTree
{
	// 这里是Top-Down-Splay
	// BOTTOM-UP-SPLAY比较复杂
	public static BinarySearchTreeNode treeSplay(int i, BinarySearchTreeNode t)
	{
		if (t == null) {
			return t;
		}
		BinarySearchTreeNode N = new BinarySearchTreeNode(), l, r, y;
		N.left = N.right = null;
		l = r = N;
		for (;;) {
			if (i < t.key) {
				if (t.left == null) {
					break;
				}
				if (i < t.left.key) {
					// zig-zig情况
					y = t.left; /* rotate right */
					t.left = y.right;
					y.right = t;
					t = y;
					if (t.left == null) {
						break;
					}
				}
				r.left = t; /* link right */
				r = t;
				t = t.left;
			}
			else if (i > t.key) {
				if (t.right == null) {
					break;
				}
				if (i > t.right.key) {
					// zig-zig情况
					y = t.right; /* rotate left */
					t.right = y.left;
					y.left = t;
					t = y;
					if (t.right == null) {
						break;
					}
				}
				l.right = t; /* link left */
				l = t;
				t = t.right;
			}
			else {
				break;
			}

		}
		l.right = t.left; /* assemble */
		r.left = t.right;
		t.left = N.right;
		t.right = N.left;
		return t;
	}

	// 这个方法不完备 自己写的,没考虑zig-zag(卡死)与zig-zig的合理旋转
	// 9
	// 6 12
	// 10
	public static BinarySearchTreeNode treeSplay1(int i, BinarySearchTreeNode t)
	{
		if (t == null) {
			return t;
		}
		for (;;) {
			if (i < t.key) {
				if (t.left == null) {
					break;
				}
				t = rightRotate(t);
				// 防止来回旋转
				if (t.left == null) {
					break;
				}
				if (i > t.left.key) {
					break;
				}
			}
			else if (i > t.key) {
				if (t.right == null) {
					break;
				}
				if (i < t.right.key) {
					break;
				}
				t = leftRotate(t);
				// 防止来回旋转
				if (t.right == null) {
					break;
				}
				if (i < t.right.key) {
					break;
				}
			}
			else {
				// 等于直接返回
				break;
			}
		}
		return t;
	}

	/*
	 * *将i插入树t中,返回树的根结点(key值==i)
	 */
	public static void treeInsert(BinarySearchTree T, int key)
	{
		/* Insert i into the tree t, unless it's already there. */
		/* Return a pointer to the resulting tree. */
		// System.out.println("splay treeinsert " + key);
		BinarySearchTreeNode root = T.getRoot();
		BinarySearchTreeNode target = new BinarySearchTreeNode();
		target.key = key;
		if (root == null) {
			target.left = target.right = null;
			T.setRoot(target);
			return;
		}
		root = treeSplay(key, root);
		if (key < root.key) { // 令t为i的右子树
			target.left = root.left;
			target.right = root;
			root.left = null;
			T.setRoot(target);
		}
		else if (key > root.key) { // 令t为i的左子树
			target.right = root.right;
			target.left = root;
			root.right = null;
			T.setRoot(target);
		}
		else {
			// i值已经存在于树t中,但做了splay操作,root已经变换了,需要reset
			T.setRoot(root);
		}
		return;
	}

	public static void treeDelete_Pre(BinarySearchTree T, int key)
	{
		/* Deletes i from the tree if it's there. */
		/* Return a pointer to the resulting tree. */
		BinarySearchTreeNode root = T.getRoot();
		if (root == null) {
			return;
		}
		root = treeSplay(key, root);
		T.setRoot(root);

		BinarySearchTreeNode newRoot;
		if (key == root.key) { /* found it */
			if (root.left == null) { // 左子树为空,则newRoot指向右子树即可
				newRoot = root.right;
			}
			else {
				newRoot = treeSplay(key, root.left); // 查找左子树中最大结点max,令右子树为max的右子树
				newRoot.right = root.right;
			}
			root.left = null;
			root.right = null;
			T.setRoot(newRoot);
		}/* It wasn't there */
	}

	public static void main(String[] args)
	{
		/* A sample use of these functions. Start with the empty tree, */
		/* insert some stuff into it, and then delete it */
		BinarySearchTree T = new SplayTree();
		int i;

		int NUM = 20;
		Random r = new Random();
		for (i = 0; i < NUM; i++) {
			treeInsert(T, r.nextInt() % NUM);
		}
		preOrderTreeWalk(T.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(T.getRoot());

		for (i = 0; i < NUM; i++) {
			treeDelete_Pre(T, i);
		}
		// System.out.println("\nsize = " + size);
	}

}