package tree;

/**
 * http://zh.wikipedia.org/wiki/%E8%8A%82%E7%82%B9%E5%A4%A7%E5%B0%8F%E5%B9%B3%E8%A1%A1%E6%A0%91
 * http://blog.csdn.net/acceptedxukai/article/details/6921334
 * http://scturtle.is-programmer.com/posts/30022
 * @author dysong
 * 
 */
/*
 * Size Balanced Tree（SBT）是一种通过大小（Size）域来保持平衡的二叉搜索树，它也因此得名。它总是满足：
 * 对于SBT的每一个结点 t：
 *  性质(a) s[right[t] ]≥s[left[left[t]]], s[right[left[t]]] 
 *  性质(b) s[left[t] ]≥s[right[right[t]]], s[left[right[t]]] 
 *  即每棵子树的大小不小于其兄弟的子树大小。
 *  树套树中会用到
 *  http://blog.csdn.net/skyprophet/article/details/5497911
 *  http://www.byvoid.com/blog/tag/%E6%A0%91%E5%A5%97%E6%A0%91/
 *  http://www.cnblogs.com/Booble/archive/2010/10/18/1855172.html
 *  用于树套树 线段树套平衡树
 */
// http://blog.csdn.net/acceptedxukai/article/details/6921334
public class SizeBalancedTree extends BinarySearchTree
{
	public static void maintain(BinarySearchTree T, BinarySearchTreeNode x, boolean rightFlag)
	{
		if (x == null) {
			return;
		}
		if (rightFlag == false)// 左边
		{
			if (x.left != null) {
				if (x.left.left != null && x.left.left.size > (x.right == null ? 0 : x.right.size)) {// 左孩子的左子树大于右孩子
					x = rightRotate(T, x);
				}
				else if (x.left.right != null && x.left.right.size > (x.right == null ? 0 : x.right.size))// 右孩子的右子树大于右孩子
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
		else // 右边
		{
			if (x.right != null) {
				if (x.right.right != null && x.right.right.size > (x.left == null ? 0 : x.left.size))// 右孩子的右子树大于左孩子
				{
					x = leftRotate(T, x);
				}
				else if (x.right.left != null && x.right.left.size > (x.left == null ? 0 : x.left.size))// 右孩子的左子树大于左孩子
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
	 * insert没有合并相同的元素，如果出现相同的元素则把它放到右子树上，这样能保证求第k小数的时候对相同元素也能正确
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
				insert(T, x.right, key);// 相同元素插入到右子树中
			}
			maintain(T, x, key >= x.key);// 每次插入把平衡操作压入栈中
		}
	}

	// delete中关于无需Maintain的说明by sqybi：
	// 在删除之前，可以保证整棵树是一棵SBT。当删除之后，虽然不能保证这棵树还是SBT，但是这时整棵树的最大深度并没有改变，所以时间复杂度也不会增加。这时，Maintain就显得是多余的了。
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

	// delete中关于无需Maintain的说明by sqybi：
	// 在删除之前，可以保证整棵树是一棵SBT。当删除之后，虽然不能保证这棵树还是SBT，但是这时整棵树的最大深度并没有改变，所以时间复杂度也不会增加。这时，Maintain就显得是多余的了。
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
		// 的判断保证了candidate!=null
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

	public static int select(BinarySearchTreeNode x, int k)// 求第k小数
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

	public static int rank(BinarySearchTreeNode x, int key)// 求key排第几
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
