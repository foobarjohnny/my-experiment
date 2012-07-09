package tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * http://zh.wikipedia.org/wiki/AVL%E6%A0%91 http://www.coderplusplus.com/?p=393
 * http://www.cnblogs.com/daoluanxiaozi/archive/2012/04/26/2471256.html
 * http://www
 * .cnblogs.com/daoluanxiaozi/archive/2012/04/26/2471256.html看BALANCE的东西
 * 
 * @author dysong
 * 
 */
// 在计算机科学中，AVL树是最先发明的自平衡二叉查找树。
// 在AVL树中任何节点的两个子树的高度最大差别为一，所以它也被称为高度平衡树。
// 查找、插入和删除在平均和最坏情况下都是O（logn）。增加和删除可能需要通过一次或多次树旋转来重新平衡这个树。
// AVL树得名于它的发明者G.M. Adelson-Velsky和E.M.Landis，他们在1962年的论文《An algorithm for the
// organization of information》中发表了它。
// 节点的平衡因子是它的左子树的高度减去它的右子树的高度（有时相反）。带有平衡因子1、0或 -1的节点被认为是平衡的。带有平衡因子
// -2或2的节点被认为是不平衡的，并需要重新平衡这个树。平衡因子可以直接存储在每个节点中，或从可能存储在节点中的子树高度计算出来。
public class AVL extends BinarySearchTree
{
	// G.M. Adelson-Velsky和E.M.Landis

	/**
	 * 平衡二叉搜索（排序）树
	 * 
	 * @data 2012-3
	 */

	static final int	LL	= 0;	// 00
	static final int	LR	= 1;	// 01
	static final int	RL	= 2;	// 10
	static final int	RR	= 3;	// 11

	public static void treeInsert(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode a = new BinarySearchTreeNode();
		a.setKey(key);
		a.setParent(null);
		a.setLeft(null);
		a.setRight(null);
		a.setColor("");
		a.setHeight(1);
		a.setBalFac(0);
		treeInsert(T, a);
	}

	public static void treeInsert(BinarySearchTree T, BinarySearchTreeNode target)
	{
		// System.out.println("AVL treeinsert");
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
			// resetHeight(parent);
		}
		else {
			parent.setRight(target);
			// resetHeight(parent);
		}
		// printTree(T.getRoot(), 7, "^");
		avlInsertFixup(T, parent, target.getKey());
		// printTree(T.getRoot(), 7, "*");
	}

	// http://blog.csdn.net/happycock/article/details/20874
	// 参见大牛文章 insert和delete其实是对称的
	private static void avlInsertFixup(BinarySearchTree T, BinarySearchTreeNode cur, int key)
	{
		// 对于 AVL的INSERT，只可能运行一次 right/left balance
		if (cur == null) {
			return;
		}
		while (cur != null) {
			cur.balFac += (cur.key <= key) ? 1 : -1;
			if (cur.balFac == -2) {
				rightBalance(T, cur);
				// 回到pivot点
				cur = cur.getParent();
			}
			else if (cur.balFac == 2) {
				leftBalance(T, cur);
				cur = cur.getParent();
			}
			if (cur.balFac == 0) {
				// 因为之前balFac 只能为0,-1,1 ( 如果中间2或者-2 已经通过balance操作变成了 0)
				// 变为0 只能为(1->2->0,1->0,-1->0,-1->-2->0) 表明之前为-1或1
				// 因为这是加结点, 对于更上面的父树,这个parent子树高度没有变化,父树的balFac不会有变化
				// 与avlDeleteFixup做对比 那是不等于
				break;
			}
			else {
				// resetHeight(cur);
				// 不等于为0,因为之前balFac 只能为0,-1,1,所以之前必为0
				// 因为这是加结点, 对于更上面的父树,这个parent子树高度+1,父树的balFac会有变化，所有不能BREAK
			}
			cur = cur.getParent();
		}
	}

	public static void treeDelete_Suc(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode target = treeSearch_Iterative(T.getRoot(), key);
		treeDelete_Suc(T, target);
	}

	public static void treeDelete_Suc(BinarySearchTree T, BinarySearchTreeNode target)
	{
		// System.out.println("AVL treeDelete_Suc");
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
				// resetHeight(candidate.getParent());
			}
			else {
				candidate.getParent().setRight(child);
				// resetHeight(candidate.getParent());
			}
		}
		if (candidate != target) {
			target.setKey(candidate.getKey());
		}
		// printTree(T.getRoot(), 7, "^");
		avlDeleteFixup(T, candidate.getParent(), candidate.getKey());
		// printTree(T.getRoot(), 7, "*");
	}

	private static void avlDeleteFixup(BinarySearchTree T, BinarySearchTreeNode cur, int key)
	{
		if (cur == null) {
			return;
		}
		// 相对于Delete 有N次Balance操作
		while (cur != null) {
			// cur.key <= target.getKey() 数值比较其实可以就定死目标值 就行了
			cur.balFac -= (cur.key <= key) ? 1 : -1;
			if (cur.balFac == -2) {
				rightBalance(T, cur);
				// 回到pivot点
				cur = cur.getParent();
			}
			else if (cur.balFac == 2) {
				leftBalance(T, cur);
				cur = cur.getParent();
			}
			if (cur.balFac == 0) {
				// 因为之前balFac 只能为0,-1,1 ( 如果中间2或者-2 已经通过balance操作变成了0
				// 这里0没BREAK所以BALANCE有log(n)次即树的高度H次)
				// 变为0 只能为(1->2->0,1->0,-1->0,-1->-2->0) 表明之前为-1或1
				// 因为这是减结点, 对于更上面的父树,这个parent子树高度减了1,父树的balFac会有变化，所有不能BREAK
				// 与avlInsertFixup做对比 那是不等于
				// resetHeight(cur);
			}
			else {
				// 不等于为0,因为之前balFac 只能为0,-1,1,所以之前必为0
				// 因为这是减结点, 对于更上面的父树,这个parent子树高度没变,父树的balFac不会有变化，所有BREAK
				break;
			}
			cur = cur.getParent();
		}
	}

	// RL,RR形 但整体向左旋
	private static void leftBalance(BinarySearchTree T, BinarySearchTreeNode p)
	{
		if (p.right != null && p.right.balFac == -1) {
			rightRotate(T, p.right);
			// printTree(T.getRoot(), 7, "=");
		}
		leftRotate(T, p);
		// printTree(T.getRoot(), 7, "=");
	}

	// LL,LR形 但整体向右旋
	private static void rightBalance(BinarySearchTree T, BinarySearchTreeNode p)
	{
		if (p.left != null && p.left.balFac == 1) {
			leftRotate(T, p.left);
			// printTree(T.getRoot(), 7, "=");
		}
		rightRotate(T, p);
		// printTree(T.getRoot(), 7, "=");
	}

	static BinarySearchTreeNode SingleRotateWithRight(BinarySearchTreeNode K2)
	{ /* RR型旋转 */
		BinarySearchTreeNode K1;
		K1 = K2.right;
		K2.right = K1.left;
		K1.left = K2;
		resetBalAndHeight(K2);
		resetBalAndHeight(K1);
		return K1;
	}

	static BinarySearchTreeNode SingleRotateWithLeft(BinarySearchTreeNode K2)
	{ /* LL型旋转 */
		BinarySearchTreeNode K1;
		K1 = K2.left;
		K2.left = K1.right;
		K1.right = K2;
		resetBalAndHeight(K2);
		resetBalAndHeight(K1);
		return K1;
	}

	static BinarySearchTreeNode DoubleRotateWithLeft(BinarySearchTreeNode K3)
	{ /* LR = RR(K3.left) + LL（K3） */
		K3.left = SingleRotateWithRight(K3.left);
		return SingleRotateWithLeft(K3);
	}

	static BinarySearchTreeNode DoubleRotateWithRight(BinarySearchTreeNode K3)
	{ /* LL = LL(K3.left) + RR(K3) */
		K3.right = SingleRotateWithLeft(K3.right);
		return SingleRotateWithRight(K3);
	}

	public static void main(String[] args)
	{
		/* A sample use of these functions. Start with the empty tree, */
		/* insert some stuff into it, and then delete it */
		BinarySearchTree bst = new BinarySearchTree();
		BinarySearchTree treap = new Treap();
		BinarySearchTree avl = new AVL();
		BinarySearchTree rb = new RedBlackTree();
		BinarySearchTree splay = new SplayTree();
		int i;

		int NUM = 1000;
		int space = 7;
		Random r = new Random();
		ArrayList a = new ArrayList();
		for (i = 0; i < NUM; i++) {
			int key = r.nextInt(NUM) % NUM;
			int pri = r.nextInt(NUM) % NUM;
			a.add(key);
			BinarySearchTree.treeInsert(bst, key);
			Treap.treeInsert(treap, key, pri);
			AVL.treeInsert(avl, key);
			RedBlackTree.treeInsert(rb, key);
			SplayTree.treeInsert(splay, key);
		}
		// int[] z = new int[] { 7, 16, 6, 7, 8, 17, 19, 0, 3, 7, 1, 7, 17, 17,
		// 16, 12, 12, 17, 14, 15 };
		// for (int j = 0; j < z.length; j++) {
		// a.add(z[j]);
		// AVL.treeInsert(avl, z[j]);
		// RedBlackTree.treeInsert(rb, z[j]);
		// }

		System.out.println(a);
		// printTree(bst.getRoot(), space, "-");
		// printTree(avl.getRoot(), space, "-");
		// printTreeByBFS(rb.getRoot(), space, "-");
		// printTree(splay.getRoot(), space, "-");
		// printTree(treap.getRoot(), space, "-");
		System.out.println("\n");
		inOrderTreeWalk(bst.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(avl.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(rb.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(splay.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(treap.getRoot());
		System.out.println("\n");
		System.out.println("\n");
		ArrayList b = new ArrayList();

		for (i = 0; i < NUM; i++) {
			int key = r.nextInt(NUM) % NUM;
			b.add(key);
			// System.out.println(b);
			BinarySearchTree.treeDelete_Pre(bst, key);
			AVL.treeDelete_Suc(avl, key);
			RedBlackTree.treeDelete_Suc(rb, key);
			SplayTree.treeDelete_Pre(splay, key);
			Treap.treeDelete_Pre(treap, key);
		}

		// z = new int[] { 10, 11, 4, 13, 13, 16, 15, 8, 9, 6, 12, 9, 19 };
		// for (int j = 0; j < z.length; j++) {
		// b.add(z[j]);
		// System.out.println(b);
		// AVL.treeDelete_Suc(avl, z[j]);
		// RedBlackTree.treeDelete_Suc(rb, z[j]);
		// }
		HashSet c = new HashSet();
		c.addAll(a);
		c.removeAll(b);
		System.out.println(c);
		// printTree(bst.getRoot(), space, "-");
		// printTree(avl.getRoot(), space, "-");
		// printTreeByBFS(rb.getRoot(), space, "-");
		// printTree(splay.getRoot(), space, "-");
		// printTree(treap.getRoot(), space, "-");
		System.out.println("\n");
		inOrderTreeWalk(bst.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(avl.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(rb.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(splay.getRoot());
		System.out.println("\n");
		inOrderTreeWalk(treap.getRoot());
		System.out.println("\n");
		System.out.println("\n");
		// System.out.println("\nsize = " + size);
	}

}