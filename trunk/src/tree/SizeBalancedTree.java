package tree;

/**
 * http://zh.wikipedia.org/wiki/%E8%8A%82%E7%82%B9%E5%A4%A7%E5%B0%8F%E5%B9%B3%E8%A1%A1%E6%A0%91
 * http://blog.csdn.net/acceptedxukai/article/details/6921334
 * http://scturtle.is-programmer.com/posts/30022
 * @author dysong
 * 
 */
/*
 * Size Balanced Tree��SBT����һ��ͨ����С��Size����������ƽ��Ķ�������������Ҳ��˵��������������㣺
 * ����SBT��ÿһ����� t��
 *  ����(a) s[right[t] ]��s[left[left[t]]], s[right[left[t]]] 
 *  ����(b) s[left[t] ]��s[right[right[t]]], s[left[right[t]]] 
 *  ��ÿ�������Ĵ�С��С�����ֵܵ�������С��
 *  �������л��õ�
 *  http://blog.csdn.net/skyprophet/article/details/5497911
 *  http://www.byvoid.com/blog/tag/%E6%A0%91%E5%A5%97%E6%A0%91/
 *  http://www.cnblogs.com/Booble/archive/2010/10/18/1855172.html
 *  ���������� �߶�����ƽ����
 */
// http://blog.csdn.net/acceptedxukai/article/details/6921334
public class SizeBalancedTree extends BinarySearchTree
{
	public static void maintain(BinarySearchTree T, BinarySearchTreeNode x, boolean rightFlag)
	{
		if (x == null) {
			return;
		}
		if (rightFlag == false)// ���
		{
			if (x.left != null) {
				if (x.left.left != null && x.left.left.size > (x.right == null ? 0 : x.right.size)) {// ���ӵ������������Һ���
					x = rightRotate(T, x);
				}
				else if (x.left.right != null && x.left.right.size > (x.right == null ? 0 : x.right.size))// �Һ��ӵ������������Һ���
				{
					x.left = leftRotate(T, x.left);
					x = rightRotate(T, x);
				}
				else {
					return;
				}
			}
			else {
				return;
			}
		}
		else // �ұ�
		{
			if (x.right != null) {
				if (x.right.right != null && x.right.right.size > (x.left == null ? 0 : x.left.size))// �Һ��ӵ���������������
				{
					x = leftRotate(T, x);
				}
				else if (x.right.left != null && x.right.left.size > (x.left == null ? 0 : x.left.size))// �Һ��ӵ���������������
				{
					x.right = rightRotate(T, x.right);
					x = leftRotate(T, x);
				}
				else {
					return;
				}
			}
			else {
				return;
			}
		}
		maintain(T, x.left, false);
		maintain(T, x.right, true);
		maintain(T, x, true);
		maintain(T, x, false);
	}

	/*
	 * insertû�кϲ���ͬ��Ԫ�أ����������ͬ��Ԫ��������ŵ��������ϣ������ܱ�֤���kС����ʱ�����ͬԪ��Ҳ����ȷ
	 */

	protected static void treeInsert(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode a = new BinarySearchTreeNode();
		a.setKey(key);
		a.setParent(null);
		a.setLeft(null);
		a.setRight(null);
		a.setColor("");
		a.setSize(1);
		treeInsert(T, a);
	}

	protected static void treeInsert(BinarySearchTree T, BinarySearchTreeNode target)
	{
		// System.out.println("bst treeinsert");
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
		}
		else {
			parent.setRight(target);
		}
		while (parent != null) {
			parent.size++;
			maintain(T, parent, target.key >= parent.key);
			parent = parent.parent;
		}
	}

	public static void insert(BinarySearchTree T, BinarySearchTreeNode x, int key)
	{
		if (x == null) {
			x = new BinarySearchTreeNode();
			x.left = x.right = null;
			x.size = 1;
			x.key = key;
		}
		else {
			x.size++;
			if (key < x.key) {
				insert(T, x.left, key);
			}
			else {
				insert(T, x.right, key);// ��ͬԪ�ز��뵽��������
			}
			maintain(T, x, key >= x.key);// ÿ�β����ƽ�����ѹ��ջ��
		}
	}

	// delete�й�������Maintain��˵��by sqybi��
	// ��ɾ��֮ǰ�����Ա�֤��������һ��SBT����ɾ��֮����Ȼ���ܱ�֤���������SBT��������ʱ�������������Ȳ�û�иı䣬����ʱ�临�Ӷ�Ҳ�������ӡ���ʱ��Maintain���Ե��Ƕ�����ˡ�
	public static int del(BinarySearchTreeNode p, int w)
	{
		if (p.key == w || (p.left == null && w < p.key) || (p.right == null && w > p.key)) {
			int delnum = p.key;
			if (p.left == null || p.right == null) {
				BinarySearchTreeNode child;
				if (p.getRight() != null) {
					child = p.getRight();
				}
				else {
					child = p.getLeft();
				}

				if (child != null) {
					child.setParent(p.getParent());
				}

				if (p.getParent() == null) {
				}
				else {
					if (p == p.getParent().getRight()) {
						p.getParent().setRight(child);
					}
					else {
						p.getParent().setLeft(child);
					}
				}
			}
			else {
				p.key = del(p.left, Integer.MIN_VALUE);
			}
			return delnum;
		}
		if (w < p.key) {
			return del(p.left, w);
		}
		else {
			return del(p.right, w);
		}
	}

	// delete�й�������Maintain��˵��by sqybi��
	// ��ɾ��֮ǰ�����Ա�֤��������һ��SBT����ɾ��֮����Ȼ���ܱ�֤���������SBT��������ʱ�������������Ȳ�û�иı䣬����ʱ�临�Ӷ�Ҳ�������ӡ���ʱ��Maintain���Ե��Ƕ�����ˡ�
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
		// (target.getLeft() == null || target.getRight() == null)
		// ���жϱ�֤��candidate!=null
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

		BinarySearchTreeNode parent = candidate;
		while ((parent = parent.getParent()) != null) {
			parent.size--;
			maintain(T, parent, target.key <= parent.key);
		}
	}

	public static void treeDelete_Pre(BinarySearchTree T, int key)
	{
		BinarySearchTreeNode target = treeSearch_Iterative(T.getRoot(), key);
		treeDelete_Pre(T, target);
	}

	public static int select(BinarySearchTreeNode x, int k)// ���kС��
	{
		int r = x.left.size + 1;
		if (r == k) {
			return x.key;
		}
		else if (r < k) {
			return select(x.right, k - r);
		}
		else {
			return select(x.left, k);
		}
	}

	public static int rank(BinarySearchTreeNode x, int key)// ��key�ŵڼ�
	{
		if (key < x.key) {
			return rank(x.left, key);
		}
		else if (key > x.key) {
			return rank(x.right, key) + x.left.size + 1;
		}
		return x.left.size + 1;
	}

}
