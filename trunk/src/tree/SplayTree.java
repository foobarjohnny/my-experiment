package tree;

import java.util.Random;

/**
 * http://www.cnblogs.com/kernel_hcy/archive/2010/03/17/1688360.html
 * 
 * @author dysong
 * 
 */
// SplyTree Tree ��չ�� ����Ӧ������
public class SplayTree extends BinarySearchTree
{
	public static BinarySearchTreeNode treeSplay(int i, BinarySearchTreeNode t)
	{
		if (t == null) {
			return t;
		}
		BinarySearchTreeNode N = new BinarySearchTreeNode(), l, r, y;
		N.left = N.right = null;
		l = r = N;

		for (;;) {
			if (i < t.getKey()) {
				if (t.left == null) {
					break;
				}
				if (i < t.left.key) {
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
					y = t.right; /* rotate left */
					t.right = y.left;
					y.left = t;
					t = y;
					if (t.right == null)
						break;
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

	/*
	 * *��i������t��,�������ĸ����(keyֵ==i)
	 */
	public static void treeInsert(BinarySearchTree T, int key)
	{
		/* Insert i into the tree t, unless it's already there. */
		/* Return a pointer to the resulting tree. */
		System.out.println("splay treeinsert");
		BinarySearchTreeNode root = T.getRoot();
		BinarySearchTreeNode target = new BinarySearchTreeNode();
		target.key = key;
		if (root == null) {
			target.left = target.right = null;
			T.setRoot(target);
			return;
		}
		root = treeSplay(key, root);
		if (key < root.key) { // ��tΪi��������
			target.left = root.left;
			target.right = root;
			root.left = null;
			T.setRoot(target);
		}
		else if (key > root.key) { // ��tΪi��������
			target.right = root.right;
			target.left = root;
			root.right = null;
			T.setRoot(target);
		}
		else {
			// iֵ�Ѿ���������t��
			return;
		}
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
			if (root.left == null) { // ������Ϊ��,��newRootָ������������
				newRoot = root.right;
			}
			else {
				newRoot = treeSplay(key, root.left); // �����������������max,��������Ϊmax��������
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