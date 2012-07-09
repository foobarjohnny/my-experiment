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
 * .cnblogs.com/daoluanxiaozi/archive/2012/04/26/2471256.html��BALANCE�Ķ���
 * 
 * @author dysong
 * 
 */
// �ڼ������ѧ�У�AVL�������ȷ�������ƽ������������
// ��AVL�����κνڵ�����������ĸ߶������Ϊһ��������Ҳ����Ϊ�߶�ƽ������
// ���ҡ������ɾ����ƽ���������¶���O��logn�������Ӻ�ɾ��������Ҫͨ��һ�λ�������ת������ƽ���������
// AVL�����������ķ�����G.M. Adelson-Velsky��E.M.Landis��������1962������ġ�An algorithm for the
// organization of information���з���������
// �ڵ��ƽ�������������������ĸ߶ȼ�ȥ�����������ĸ߶ȣ���ʱ�෴��������ƽ������1��0�� -1�Ľڵ㱻��Ϊ��ƽ��ġ�����ƽ������
// -2��2�Ľڵ㱻��Ϊ�ǲ�ƽ��ģ�����Ҫ����ƽ���������ƽ�����ӿ���ֱ�Ӵ洢��ÿ���ڵ��У���ӿ��ܴ洢�ڽڵ��е������߶ȼ��������
public class AVL extends BinarySearchTree
{
	// G.M. Adelson-Velsky��E.M.Landis

	/**
	 * ƽ�����������������
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
				// С�������
				current = current.getLeft();
			}
			else if (target.getKey() > current.getKey()) {
				// �������ұ�
				current = current.getRight();
			}
			else {
				// ���ڷ���,��Ϊ�����������׳���
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
	// �μ���ţ���� insert��delete��ʵ�ǶԳƵ�
	private static void avlInsertFixup(BinarySearchTree T, BinarySearchTreeNode cur, int key)
	{
		// ���� AVL��INSERT��ֻ��������һ�� right/left balance
		if (cur == null) {
			return;
		}
		while (cur != null) {
			cur.balFac += (cur.key <= key) ? 1 : -1;
			if (cur.balFac == -2) {
				rightBalance(T, cur);
				// �ص�pivot��
				cur = cur.getParent();
			}
			else if (cur.balFac == 2) {
				leftBalance(T, cur);
				cur = cur.getParent();
			}
			if (cur.balFac == 0) {
				// ��Ϊ֮ǰbalFac ֻ��Ϊ0,-1,1 ( ����м�2����-2 �Ѿ�ͨ��balance��������� 0)
				// ��Ϊ0 ֻ��Ϊ(1->2->0,1->0,-1->0,-1->-2->0) ����֮ǰΪ-1��1
				// ��Ϊ���Ǽӽ��, ���ڸ�����ĸ���,���parent�����߶�û�б仯,������balFac�����б仯
				// ��avlDeleteFixup���Ա� ���ǲ�����
				break;
			}
			else {
				// resetHeight(cur);
				// ������Ϊ0,��Ϊ֮ǰbalFac ֻ��Ϊ0,-1,1,����֮ǰ��Ϊ0
				// ��Ϊ���Ǽӽ��, ���ڸ�����ĸ���,���parent�����߶�+1,������balFac���б仯�����в���BREAK
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
		// �����Delete ��N��Balance����
		while (cur != null) {
			// cur.key <= target.getKey() ��ֵ�Ƚ���ʵ���ԾͶ���Ŀ��ֵ ������
			cur.balFac -= (cur.key <= key) ? 1 : -1;
			if (cur.balFac == -2) {
				rightBalance(T, cur);
				// �ص�pivot��
				cur = cur.getParent();
			}
			else if (cur.balFac == 2) {
				leftBalance(T, cur);
				cur = cur.getParent();
			}
			if (cur.balFac == 0) {
				// ��Ϊ֮ǰbalFac ֻ��Ϊ0,-1,1 ( ����м�2����-2 �Ѿ�ͨ��balance���������0
				// ����0ûBREAK����BALANCE��log(n)�μ����ĸ߶�H��)
				// ��Ϊ0 ֻ��Ϊ(1->2->0,1->0,-1->0,-1->-2->0) ����֮ǰΪ-1��1
				// ��Ϊ���Ǽ����, ���ڸ�����ĸ���,���parent�����߶ȼ���1,������balFac���б仯�����в���BREAK
				// ��avlInsertFixup���Ա� ���ǲ�����
				// resetHeight(cur);
			}
			else {
				// ������Ϊ0,��Ϊ֮ǰbalFac ֻ��Ϊ0,-1,1,����֮ǰ��Ϊ0
				// ��Ϊ���Ǽ����, ���ڸ�����ĸ���,���parent�����߶�û��,������balFac�����б仯������BREAK
				break;
			}
			cur = cur.getParent();
		}
	}

	// RL,RR�� ������������
	private static void leftBalance(BinarySearchTree T, BinarySearchTreeNode p)
	{
		if (p.right != null && p.right.balFac == -1) {
			rightRotate(T, p.right);
			// printTree(T.getRoot(), 7, "=");
		}
		leftRotate(T, p);
		// printTree(T.getRoot(), 7, "=");
	}

	// LL,LR�� ������������
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
	{ /* RR����ת */
		BinarySearchTreeNode K1;
		K1 = K2.right;
		K2.right = K1.left;
		K1.left = K2;
		resetBalAndHeight(K2);
		resetBalAndHeight(K1);
		return K1;
	}

	static BinarySearchTreeNode SingleRotateWithLeft(BinarySearchTreeNode K2)
	{ /* LL����ת */
		BinarySearchTreeNode K1;
		K1 = K2.left;
		K2.left = K1.right;
		K1.right = K2;
		resetBalAndHeight(K2);
		resetBalAndHeight(K1);
		return K1;
	}

	static BinarySearchTreeNode DoubleRotateWithLeft(BinarySearchTreeNode K3)
	{ /* LR = RR(K3.left) + LL��K3�� */
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