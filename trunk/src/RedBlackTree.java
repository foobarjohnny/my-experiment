import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Binary Search Tree
public class RedBlackTree extends BinarySearchTree
{
	public final static String					RED		= "R";
	public final static String					BLACK	= "B";
	public final static BinarySearchTreeNode	NIL		= new RedBlackTreeNode();
	static {
		NIL.setColor(BLACK);
		NIL.setKey(-10);
	}

	protected static void treeInsert(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode a = new RedBlackTreeNode();
		a.setKey(key);
		a.setParent(null);
		a.setLeft(null);
		a.setRight(null);
		a.setColor("");
		treeInsert(T, a);
	}

	public static void treeInsert(BinarySearchTree T, BinarySearchTreeNode target)
	{
		// System.out.println("rb treeinsert");
		if (target == null) {
			return;
		}
		BinarySearchTreeNode parent = NIL;
		BinarySearchTreeNode current = T.getRoot();
		while (current != NIL && current != null) {
			parent = current;
			if (target.getKey() < current.getKey()) {
				current = current.getLeft();
			}
			else {
				current = current.getRight();
			}
		}
		target.setParent(parent);
		if (parent == NIL) {
			// Tree root was empty
			T.setRoot(target);
		}
		else {
			if (target.getKey() < parent.getKey()) {
				parent.setLeft(target);
			}
			else {
				parent.setRight(target);
			}
		}
		target.setLeft(NIL);
		target.setRight(NIL);
		target.setColor(RED);
		// int spaceLength = 4;
		// String character = "^";
		// printTreeByBFS(T.getRoot(), spaceLength, character);
		rbInsertFixup(T, target);
	}

	private static void rbInsertFixup(BinarySearchTree T, BinarySearchTreeNode z)
	{
		BinarySearchTreeNode uncle = null;
		// System.out.println(z.getKey());
		while (z.getParent().getColor() == RED) {
			if (z.getParent() == z.getParent().getParent().getLeft()) {
				uncle = z.getParent().getParent().getRight();
				if (uncle.getColor() == RED) {
					z.getParent().setColor(BLACK);
					uncle.setColor(BLACK);
					z.getParent().getParent().setColor(RED);
					z = z.getParent().getParent();
				}
				else {
					if (z == z.getParent().getRight()) {
						z = z.getParent();
						leftRotate(T, z);
					}
					z.getParent().setColor(BLACK);
					z.getParent().getParent().setColor(RED);
					rightRotate(T, z.getParent().getParent());
				}
			}
			else {
				uncle = z.getParent().getParent().getLeft();
				if (uncle.getColor() == RED) {
					z.getParent().setColor(BLACK);
					uncle.setColor(BLACK);
					z.getParent().getParent().setColor(RED);
					z = z.getParent().getParent();
				}
				else {
					if (z == z.getParent().getLeft()) {
						z = z.getParent();
						rightRotate(T, z);
					}
					z.getParent().setColor(BLACK);
					z.getParent().getParent().setColor(RED);
					leftRotate(T, z.getParent().getParent());
				}
			}
		}
		T.getRoot().setColor(BLACK);
	}

	public static void treeDelete_Suc(BinarySearchTree T, BinarySearchTreeNode target)
	{
		BinarySearchTreeNode candidate = null;
		BinarySearchTreeNode child = null;
		if (target == null) {
			return;
		}
		if (target.getLeft() == NIL || target.getRight() == NIL) {
			candidate = target;
		}
		else {
			candidate = treeSuccessor(target);
		}

		if (candidate.getLeft() != NIL) {
			child = candidate.getLeft();
		}
		else {
			child = candidate.getRight();
		}

		child.setParent(candidate.getParent());

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
		if (candidate.getColor() == BLACK) {
			rbDeleteFixup(T, target);
		}
	}

	private static void rbDeleteFixup(BinarySearchTree T, BinarySearchTreeNode current)
	{
		BinarySearchTreeNode sibling = null;
		while (current != T.getRoot() && current.getColor() == BLACK) {
			if (current == current.getParent().getLeft()) {
				sibling = current.getParent().getRight();
				if (sibling.getColor() == RED) {
					sibling.setColor(BLACK);
					current.getParent().setColor(RED);
					leftRotate(T, current.getParent());
					sibling = current.getParent().getRight();
					if (sibling.getLeft().getColor() == BLACK && sibling.getRight().getColor() == BLACK) {
						sibling.setColor(RED);
						current = current.getParent();
					}
					else {
						if (sibling.getRight().getColor() == BLACK) {
							// sibling.getRight().getColor() == RED &&
							// sibling.getRight().getColor() == BLACK
							sibling.getLeft().setColor(BLACK);
							sibling.setColor(RED);
							rightRotate(T, sibling);
							sibling = current.getParent().getRight();
						}
						// sibling.getRight().getColor() == RED
						sibling.setColor(current.getParent().getColor());
						current.getParent().setColor(BLACK);
						sibling.getRight().setColor(BLACK);
						leftRotate(T, current.getParent());
						current = T.getRoot();
					}
				}
			}
			else {
				sibling = current.getParent().getLeft();
				if (sibling.getColor() == RED) {
					sibling.setColor(BLACK);
					current.getParent().setColor(RED);
					rightRotate(T, current.getParent());
					sibling = current.getParent().getLeft();
					if (sibling.getRight().getColor() == BLACK && sibling.getLeft().getColor() == BLACK) {
						sibling.setColor(RED);
						current = current.getParent();
					}
					else {
						if (sibling.getLeft().getColor() == BLACK) {
							sibling.getRight().setColor(BLACK);
							sibling.setColor(RED);
							leftRotate(T, sibling);
							sibling = current.getParent().getLeft();
						}
						sibling.setColor(current.getParent().getColor());
						current.getParent().setColor(BLACK);
						sibling.getLeft().setColor(BLACK);
						rightRotate(T, current.getParent());
						current = T.getRoot();
					}
				}
			}
		}
		current.setColor(BLACK);
	}

	// FROM WIKI
	private static void rbDeleteFixup2(BinarySearchTree T, BinarySearchTreeNode child)
	{
		if (child.getColor() == RED) {
			child.setColor(BLACK);
		}
		else {
			deleteCase1(T, child);
		}
	}

	private static void deleteCase1(BinarySearchTree T, BinarySearchTreeNode child)
	{
		if (child.getParent() != null) {
			deleteCase2(T, child);
		}
	}

	private static void deleteCase2(BinarySearchTree T, BinarySearchTreeNode child)
	{
		BinarySearchTreeNode s = child.getSibling();
		if (s.getColor() == RED) {
			child.getParent().setColor(RED);
			s.setColor(BLACK);
			if (child == child.getParent().getLeft()) {
				leftRotate(T, child.getParent());
			}
			else {
				rightRotate(T, child.getParent());
			}
		}
		deleteCase3(T, child);
	}

	private static void deleteCase3(BinarySearchTree T, BinarySearchTreeNode child)
	{
		BinarySearchTreeNode s = child.getSibling();
		if (child.getParent().getColor() == BLACK && s.getColor() == BLACK && s.getLeft().getColor() == BLACK && s.getRight().getColor() == BLACK) {
			s.setColor(RED);
			deleteCase1(T, child.getParent());
		}
		else {
			deleteCase4(T, child);
		}
	}

	private static void deleteCase4(BinarySearchTree T, BinarySearchTreeNode child)
	{
		BinarySearchTreeNode s = child.getSibling();
		if (child.getParent().getColor() == RED && s.getColor() == BLACK && s.getLeft().getColor() == BLACK && s.getRight().getColor() == BLACK) {
			s.setColor(RED);
			child.getParent().setColor(BLACK);
		}
		else {
			deleteCase5(T, child);
		}
	}

	private static void deleteCase5(BinarySearchTree T, BinarySearchTreeNode child)
	{
		BinarySearchTreeNode s = child.getSibling();
		if (s.getColor() == BLACK) {
			if (child == child.getParent().getLeft()) {
				if (s.getLeft().getColor() == RED && s.getRight().getColor() == BLACK) {
					s.setColor(RED);
					s.getLeft().setColor(BLACK);
					rightRotate(T, s);
				}
			}
			else {
				if (s.getLeft().getColor() == BLACK && s.getRight().getColor() == RED) {
					s.setColor(RED);
					s.getRight().setColor(BLACK);
					leftRotate(T, s);
				}
			}
		}
		deleteCase6(T, child);
	}

	private static void deleteCase6(BinarySearchTree T, BinarySearchTreeNode child)
	{

		BinarySearchTreeNode s = child.getSibling();
		s.setColor(child.getParent().getColor());
		child.getParent().setColor(BLACK);

		if (child == child.getParent().getLeft()) {
			s.getRight().setColor(BLACK);
			leftRotate(T, child.getParent());
		}
		else {
			s.getLeft().setColor(BLACK);
			rightRotate(T, child.getParent());
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
		if (parent.getLeft() != RedBlackTree.NIL) {
			parent.getLeft().setParent(target);
		}
		parent.setParent(target.getParent());
		if (target.getParent() == RedBlackTree.NIL) {
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
	}

	public static void rightRotate(BinarySearchTree T, BinarySearchTreeNode target)
	{
		if (target == null) {
			return;
		}
		BinarySearchTreeNode parent = target.getLeft();
		target.setLeft(parent.getRight());
		if (parent.getRight() != RedBlackTree.NIL) {
			parent.getRight().setParent(target);
		}
		parent.setParent(target.getParent());
		if (target.getParent() == RedBlackTree.NIL) {
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
	}

	public static BinarySearchTree initTree(int[] a)
	{
		BinarySearchTree T = new RedBlackTree();
		int length = a.length;
		for (int i = 0; i < length; i++) {
			int v = a[i];
			treeInsert(T, v);
			// int spaceLength = 3;
			// String character = "*";
			// printTreeByBFS(T, spaceLength, character);
		}
		return T;

	}

	public static void main(String[] args)
	{
		int key = 85;
		int treeLength = 20;
		int spaceLength = 4;
		String character = " ";
		int[] a = getRandomArray(treeLength, key);
		BinarySearchTree tree = (BinarySearchTree) initTree(a);
		inOrderTreeWalk(tree.getRoot());
		System.out.println();
		printTreeByBFS(tree.getRoot(), spaceLength, character);
	}

}