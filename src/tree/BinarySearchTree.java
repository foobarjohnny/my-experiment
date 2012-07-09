package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Binary Search Tree
//1.如果v是u的左孩子，则key[v] < key[u].
//2.如果v是u的右孩子，则key[v] > key[u].
public class BinarySearchTree
{

	protected int					key;
	protected BinarySearchTreeNode	root	= null;

	public BinarySearchTreeNode getRoot()
	{
		return root;
	}

	public void setRoot(BinarySearchTreeNode root)
	{
		this.root = root;
	}

	public static void printTreeByBFS(BinarySearchTreeNode root, int spaceLength, String c)
	{
		List<BinarySearchTreeNode> list = new ArrayList<BinarySearchTreeNode>();
		list.add(root);
		int currentIndex = 0;
		int nextIndex = 1;
		int level = 0;
		int depth = getTreeHeight(root);
		StringBuilder s = new StringBuilder();
		while (currentIndex < list.size())// 还有下层
		{
			level++;
			int startSpaces = (int) (Math.pow(2, (depth - level)) - 1);
			nextIndex = list.size();
			s.append(printKey(level + ":", spaceLength, " "));
			s.append(printBlank(startSpaces, spaceLength));
			int intervalSpaces = (int) (Math.pow(2, depth - level + 1) - 1);
			while (currentIndex < nextIndex)// 该层还有元素
			{
				BinarySearchTreeNode tree = list.get(currentIndex);
				if (tree == null) {
					s.append(printPlaceHolder(spaceLength));
					if (level < depth) {
						list.add(null);
						list.add(null);
					}
				}
				else {
					s.append(printKey(tree.getKey() + tree.getColor(), spaceLength, c));
					if (level < depth) {
						list.add(tree.getLeft() == RedBlackTree.NIL ? null : tree.getLeft());
						list.add(tree.getRight() == RedBlackTree.NIL ? null : tree.getRight());
					}
				}
				if (currentIndex != nextIndex - 1) {
					s.append(printBlank(intervalSpaces, spaceLength));
				}
				currentIndex++;
			}
			s.append("\n");
		}
		System.out.print(s);
	}

	/**
	 * don't need extra space, but it need more time
	 */
	public static void printTree(BinarySearchTreeNode t, int spaceLength, String c)
	{
		int depth = getTreeHeight(t);
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
				key = t.getKey() + "";
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
				System.out.print(printKey(t.getKey() + t.getColor() + "b" + t.balFac, length, c));
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

	public static void inOrderTreeWalk(BinarySearchTreeNode t)
	{
		if (t != null && t != RedBlackTree.NIL) {
			inOrderTreeWalk(t.getLeft());
			System.out.print(t.getKey());
			// System.out.print(":");
			// System.out.print(t.getPriority());
			System.out.print(",");
			inOrderTreeWalk(t.getRight());
		}
	}

	public static void preOrderTreeWalk(BinarySearchTreeNode t)
	{
		if (t != null && t != RedBlackTree.NIL) {
			System.out.print(t.getKey());
			// System.out.print(":");
			// System.out.print(t.getPriority());
			System.out.print(",");
			preOrderTreeWalk(t.getLeft());
			preOrderTreeWalk(t.getRight());
		}
	}

	public static BinarySearchTreeNode treeSearch_Recursive(BinarySearchTreeNode t, int key)
	{
		if (t == null || key == t.getKey()) {
			return t;
		}
		if (key < t.getKey()) {
			return treeSearch_Recursive(t.getLeft(), key);
		}
		else {
			return treeSearch_Recursive(t.getRight(), key);
		}
	}

	public static BinarySearchTreeNode treeSearch_Iterative(BinarySearchTreeNode t, int key)
	{
		while (t != null && key != t.getKey()) {
			if (key < t.getKey()) {
				t = t.getLeft();
			}
			else {
				t = t.getRight();
			}
		}
		return t;
	}

	protected static void treeInsert(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode a = new BinarySearchTreeNode();
		a.setKey(key);
		a.setParent(null);
		a.setLeft(null);
		a.setRight(null);
		a.setColor("");
		treeInsert(T, a);
	}

	protected static void treeInsert(BinarySearchTree T, BinarySearchTreeNode target)
	{
		System.out.println("bst treeinsert");
		if (target == null) {
			return;
		}
		BinarySearchTreeNode parent = null;
		BinarySearchTreeNode current = T.getRoot();
		while (current != null) {
			parent = current;
			if (target.getKey() < current.getKey()) {
				// 小于在左边
				current = current.getLeft();
			}
			else if (target.getKey() > current.getKey()) {
				// 大于在右边
				current = current.getRight();
			}
			else {
				// 等于返回,因为左旋右旋容易出事
				return;
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
	}

	public static void treeDelete_Suc(BinarySearchTree T, BinarySearchTreeNode target)
	{
		BinarySearchTreeNode candidate = null;
		BinarySearchTreeNode child = null;
		if (target == null) {
			return;
		}
		if (target.getLeft() == null || target.getRight() == null) {
			candidate = target;
		}
		else {
			candidate = treeSuccessor(target);
			System.out.println(candidate.getKey());
		}

		if (candidate.getLeft() != null) {
			child = candidate.getLeft();
		}
		else {
			child = candidate.getRight();
		}

		if (child != null) {
			child.setParent(candidate.getParent());
		}

		if (candidate.getParent() == null) {
			T.setRoot(child);
		}
		else {
			if (candidate == candidate.getParent().getLeft()) {
				candidate.getParent().setLeft(child);
			}
			else {
				candidate.getParent().setRight(child);
			}
		}
		if (candidate != target) {
			target.setKey(candidate.getKey());
		}

	}

	public static void treeDelete_Suc(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode target = treeSearch_Iterative(T.getRoot(), key);
		treeDelete_Suc(T, target);
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
		}
	}

	public static void treeDelete_Pre(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode target = treeSearch_Iterative(T.getRoot(), key);
		treeDelete_Pre(T, target);
	}

	public static BinarySearchTreeNode treeMinimum(BinarySearchTreeNode t)
	{
		while (t.getLeft() != null) {
			t = t.getLeft();
		}
		return t;
	}

	public static BinarySearchTreeNode treeMaximum(BinarySearchTreeNode t)
	{
		while (t.getRight() != null) {
			t = t.getRight();
		}
		return t;
	}

	public static BinarySearchTreeNode treeSuccessor(BinarySearchTreeNode t)
	{
		if (t == null) {
			return t;
		}
		if (t.getRight() != null) {
			return treeMinimum(t.getRight());
		}
		BinarySearchTreeNode successor = t.getParent();
		while (successor != null && t == successor.getRight()) {
			t = successor;
			successor = successor.getParent();
		}
		return successor;
	}

	public static BinarySearchTreeNode treePredecessor(BinarySearchTreeNode t)
	{
		if (t == null) {
			return t;
		}
		if (t.getLeft() != null) {
			return treeMaximum(t.getLeft());
		}
		BinarySearchTreeNode predecessor = t.getParent();
		while (predecessor != null && t == predecessor.getLeft()) {
			t = predecessor;
			predecessor = predecessor.getParent();
		}
		return predecessor;
	}

	public static int getTreeHeight(BinarySearchTreeNode T)
	{
		int lchildh, rchildh;
		if (T == null || T == RedBlackTree.NIL) {
			return 0;
		}
		else {
			lchildh = getTreeHeight(T.getLeft());
			rchildh = getTreeHeight(T.getRight());
			return (lchildh > rchildh) ? (lchildh + 1) : (rchildh + 1);
		}
	}

	// FOR AVL 's BALANCE FACTOR
	// 避免用getTreeHeight递归运算
	public static int height(BinarySearchTreeNode T)
	{
		int lchildh, rchildh;
		if (T == null || T == RedBlackTree.NIL) {
			return 0;
		}
		else {
			lchildh = T.getLeft() != null ? T.getLeft().height : 0;
			rchildh = T.getRight() != null ? T.getRight().height : 0;
			return (lchildh > rchildh) ? (lchildh + 1) : (rchildh + 1);
		}
	}

	public static void leftRotate(BinarySearchTree T, BinarySearchTreeNode target)
	{
		// System.out.println("leftRotate");
		// printTreeByBFS(target, 3, "|");
		if (target == null) {
			return;
		}
		BinarySearchTreeNode parent = target.getRight();
		target.setRight(parent.getLeft());
		if (parent.getLeft() != null) {
			parent.getLeft().setParent(target);
		}
		parent.setParent(target.getParent());
		if (target.getParent() == null) {
			T.setRoot(parent);
		}
		else {
			if (target == target.getParent().getLeft()) {
				target.getParent().setLeft(parent);
			}
			else {
				target.getParent().setRight(parent);
			}
		}
		parent.setLeft(target);
		target.setParent(parent);
		--target.balFac;
		target.balFac -= parent.balFac > 0 ? parent.balFac : 0;
		--parent.balFac;
		parent.balFac += target.balFac < 0 ? target.balFac : 0;
		// FOR AVL 's BALANCE FACTOR
		// resetBalAndHeight(target);
		// resetBalAndHeight(parent);
		// reset太麻烦,直接用Bal
	}

	public static void rightRotate(BinarySearchTree T, BinarySearchTreeNode target)
	{
		if (target == null) {
			return;
		}
		BinarySearchTreeNode parent = target.getLeft();
		target.setLeft(parent.getRight());
		if (parent.getRight() != null) {
			parent.getRight().setParent(target);
		}
		parent.setParent(target.getParent());
		if (target.getParent() == null) {
			T.setRoot(parent);
		}
		else {
			if (target == target.getParent().getRight()) {
				target.getParent().setRight(parent);
			}
			else {
				target.getParent().setLeft(parent);
			}
		}
		parent.setRight(target);
		target.setParent(parent);
		++target.balFac;
		target.balFac -= parent.balFac < 0 ? parent.balFac : 0;
		++parent.balFac;
		parent.balFac += target.balFac > 0 ? target.balFac : 0;
		// FOR AVL 's BALANCE FACTOR
		// resetBalAndHeight(target);
		// resetBalAndHeight(parent);
	}

	// FOR AVL 's BALANCE FACTOR
	public static void resetBalAndHeight(BinarySearchTreeNode t)
	{
		if (t != null) {
			int l = height(t.getLeft());
			int r = height(t.getRight());
			t.setHeight(l > r ? l + 1 : r + 1);
			t.setBalFac(r - l);
		}
	}

	public static void resetHeight(BinarySearchTreeNode t)
	{
		if (t != null) {
			int l = height(t.getLeft());
			int r = height(t.getRight());
			t.setHeight(l > r ? l + 1 : r + 1);
		}
	}

	public static BinarySearchTree initTree(int[] a)
	{
		BinarySearchTree T = new BinarySearchTree();
		Random r = new Random();
		int length = a.length;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				int v = a[i];
				treeInsert(T, v);
			}
		}
		return T;

	}

	public static void main(String[] args)
	{
		BinarySearchTree tree;
		BinarySearchTree tree2;
		System.out.println(BinarySearchTree.class.getClassLoader());
		int key = 85;
		int treeLength = 5;
		int spaceLength = 3;
		String character = "*";
		int[] a = getRandomArray(treeLength, key);
		tree = initTree(a);
		tree2 = initTree(a);
		inOrderTreeWalk(tree.getRoot());
		System.out.println();
		inOrderTreeWalk(tree2.getRoot());
		System.out.println();
		// printTree(tree, spaceLength, character);
		printTreeByBFS(tree.getRoot(), spaceLength, character);
		BinarySearchTreeNode t = treeSearch_Iterative(tree.getRoot(), key);
		System.out.println("t=" + (t == null ? "null" : t.getKey()));
		System.out.println("Predecessor(t)=" + (treePredecessor(t) == null ? "null" : treePredecessor(t).getKey()));
		System.out.println("Successor(t)=" + (treeSuccessor(t) == null ? "null" : treeSuccessor(t).getKey()));
		treeDelete_Pre(tree, key);
		treeDelete_Suc(tree2, key);
		printTreeByBFS(tree.getRoot(), spaceLength, character);
		printTreeByBFS(tree2.getRoot(), spaceLength, character);
		// printTreeVertical(tree, 0, spaceLength, getTreeHeight(tree), 1);
		inOrderTreeWalk(tree.getRoot());
		System.out.println();
	}

	public static int[] getRandomArray(int treeLength, int defaultValue)
	{
		int[] a = new int[treeLength];
		Random r = new Random();
		for (int i = 0; i < a.length; i++) {
			a[i] = r.nextInt(a.length * 10);
			if (i == a.length / 2) {
				a[i] = defaultValue;
			}
			System.out.print(a[i] + ",");
		}
		System.out.println();
		return a;
	}

}