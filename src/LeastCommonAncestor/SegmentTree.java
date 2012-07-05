package LeastCommonAncestor;

import java.io.IOException;

//LeastCommonAncestor
//Range Minimum/Maximum Query
/**
 * http://dongxicheng.org/structure/segment-tree/
 * 
 * @author dysong
 * 
 */
// 线段树，也叫区间树interval tree，是一个完全二叉树，它在各个节点保存一条线段（即“子数组”）
// 因而常用于解决数列维护问题，它基本能保证每个操作的复杂度为O(lgN)。
// 线段树上的参数通常有两种维护方法：
// （1）一类参数表达了结点的性质，通常具有树型的递推性质，可以从下向上进行递推计算；（如sum,max,min）
// （2）一类参数表达了子树的性质，维护的时候可以先打上标记，在需要进一步访问其子结点的时候从上向下传递。（如delta,en）
// interval tree
public class SegmentTree
{
	public SegmentTree	leftChild, rightChild;
	public int			leftIndex, rightIndex;
	public int			min;
	public int			max;
	public int			sum;
	public int			delta;
	public boolean		En	= false;
	public int			data;

	public static void insert(SegmentTree cur, int index, int num)
	{
		SegmentTree LC, RC;
		LC = cur.leftChild;
		RC = cur.rightChild;
		if (cur.leftIndex == cur.rightIndex) // 对叶结点的处理
		{
			cur.min = num;
			cur.max = num;
			cur.sum = num;
		}
		else {
			if (index <= (cur.leftIndex + cur.rightIndex) / 2) {
				insert(LC, index, num);
			}
			if (index > (cur.leftIndex + cur.rightIndex) / 2) {
				insert(RC, index, num);
			}
			cur.sum = LC.sum + RC.sum; // 上推
			if (LC.max > RC.max) {
				cur.max = LC.max;
			}
			else {
				cur.max = RC.max;
			}
			if (LC.min < RC.min) {
				cur.min = LC.min;
			}
			else {
				cur.min = RC.min;
			}
		}
	}

	@Override
	public String toString()
	{
		return "ST[leftIndex=" + leftIndex + ", rightIndex=" + rightIndex + ", sum=" + sum + ", delta=" + delta + ",\nleftChild=" + leftChild + ",\nrightChild=" + rightChild + "]";
	}

	public static void updateForDelta(SegmentTree cur, int l, int r, int delta)
	{
		SegmentTree LC, RC; // 需要给每个区间增加一个变量delta
		LC = cur.leftChild;
		RC = cur.rightChild;
		if ((l <= cur.leftIndex) && (cur.rightIndex <= r)) {
			cur.delta = cur.delta + delta;
			cur.max += delta;
			cur.min += delta;
		}// 改变该区间的delta，注意sum
		else {
			if (l <= (cur.leftIndex + cur.rightIndex) / 2) {
				updateForDelta(LC, l, r, delta);
			}
			if (r > (cur.leftIndex + cur.rightIndex) / 2) {
				updateForDelta(RC, l, r, delta);
			}
			cur.sum = LC.sum + LC.delta * (LC.rightIndex - LC.leftIndex + 1);
			cur.sum = cur.sum + RC.sum + RC.delta * (RC.rightIndex - RC.leftIndex + 1); // 上推
		}
	}

	public static int querySumForDelta(SegmentTree cur, int l, int r)
	{
		SegmentTree LC, RC;
		int ret;
		LC = cur.leftChild;
		RC = cur.rightChild;
		ret = 0;
		if ((l <= cur.leftIndex) && (cur.rightIndex <= r)) {
			ret = ret + cur.sum + (cur.rightIndex - cur.leftIndex + 1) * cur.delta;
		}
		else { // 将该区间的delta下传 给左右区间，同时修改sum的值
			cur.sum = cur.sum + (cur.rightIndex - cur.leftIndex + 1) * cur.delta;
			LC.delta = LC.delta + cur.delta;
			RC.delta = RC.delta + cur.delta;
			cur.delta = 0;
			if (l <= (cur.leftIndex + cur.rightIndex) / 2) {
				ret = ret + querySumForDelta(LC, l, r);
			}
			if (r > (cur.leftIndex + cur.rightIndex) / 2) {
				ret = ret + querySumForDelta(RC, l, r);
			}
		}
		return ret;
	}

	public static int queryMaxForDelta(SegmentTree cur, int l, int r)
	{
		SegmentTree LC, RC;
		int ret;
		LC = cur.leftChild;
		RC = cur.rightChild;
		ret = 0;
		if ((l <= cur.leftIndex) && (cur.rightIndex <= r)) {
			ret = cur.max;
		}
		else {
			LC.delta = LC.delta + cur.delta;
			RC.delta = RC.delta + cur.delta;
			LC.max += cur.delta;
			RC.max += cur.delta;
			cur.delta = 0;
			if (l <= (cur.leftIndex + cur.rightIndex) / 2) {
				ret = queryMaxForDelta(LC, l, r);
			}
			if (r > (cur.leftIndex + cur.rightIndex) / 2) {
				ret = Math.max(ret, queryMaxForDelta(RC, l, r));
			}
		}
		return ret;
	}

	// 同一个值
	public static void updateForEn(SegmentTree cur, int l, int r, int num)
	{
		SegmentTree LC, RC;
		LC = cur.leftChild;
		RC = cur.rightChild;
		if (cur.En) // 如果该区间已经是同一个值，将值下传给左右区间
		{
			cur.sum = (cur.rightIndex - cur.leftIndex + 1) * cur.data;
			if (LC != null) {
				LC.data = cur.data;
				LC.En = true;
			}
			if (RC != null) {
				RC.data = cur.data;
				RC.En = true;
			}
			cur.En = false;
		}
		if ((l <= cur.leftIndex) && (cur.rightIndex <= r)) {
			cur.En = true;
			cur.data = num;
		}
		else {
			if (l <= (cur.leftIndex + cur.rightIndex) / 2) {
				updateForEn(LC, l, r, num);
			}
			if (r > (cur.leftIndex + cur.rightIndex) / 2) {
				updateForEn(RC, l, r, num);
			}
			cur.sum = calcsum(LC) + calcsum(RC);
		}
	}

	public static int calcsum(SegmentTree cur)
	{
		int ret;
		if (cur.En) {
			ret = (cur.rightIndex - cur.leftIndex + 1) * cur.data;
		}
		else {
			ret = cur.sum;
		}
		return ret;
	}

	public static int querysumForEn(SegmentTree cur, int l, int r)
	{
		SegmentTree LC, RC;
		int ret;
		LC = cur.leftChild;
		RC = cur.rightChild;
		ret = 0;
		if ((l <= cur.leftIndex) && (cur.rightIndex <= r)) {
			ret = ret + calcsum(cur);
		}
		else {
			if (cur.En) // 如果该区间是同一个数，将值下传给左右区间
			{
				LC.data = cur.data;
				LC.En = true;
				RC.data = cur.data;
				RC.En = true;
				cur.En = false;
			}
			if (l <= (cur.leftIndex + cur.rightIndex) / 2) {
				ret = ret + querysumForEn(LC, l, r);
			}
			if (r > (cur.leftIndex + cur.rightIndex) / 2) {
				ret = ret + querysumForEn(RC, l, r);
			}
		}
		return ret;
	};

	public static int querySum(SegmentTree cur, int l, int r)
	{
		SegmentTree LC, RC;
		int ret;
		LC = cur.leftChild;
		RC = cur.rightChild;
		ret = 0;
		if ((l <= cur.leftIndex) && (cur.rightIndex <= r)) {
			ret = cur.sum;
		}
		else {
			if (l <= (cur.leftIndex + cur.rightIndex) / 2) {
				ret = ret + querySum(LC, l, r);
			}
			if (r > (cur.leftIndex + cur.rightIndex) / 2) {
				ret = ret + querySum(RC, l, r);
			}
		}
		return ret;
	}

	public static SegmentTree Build(int L, int R)
	{
		SegmentTree rootTree = new SegmentTree();
		// SegmentTree rootTree = nodes[pos++];
		rootTree.leftIndex = L;
		rootTree.rightIndex = R;
		if (L == R) {
			return rootTree;
		}
		else {
			int mid;
			mid = (L + R) / 2;
			rootTree.leftChild = Build(L, mid);
			rootTree.rightChild = Build(mid + 1, R);
		}
		return rootTree;
	}

	public static int queryMax(SegmentTree cur, int l, int r)
	{
		int ret = 0;
		SegmentTree LC = cur.leftChild, RC = cur.rightChild;
		if ((l <= cur.leftIndex) && (cur.rightIndex <= r)) {
			ret = cur.max;
		}
		else {
			if (l <= (cur.leftIndex + cur.rightIndex) / 2) {
				ret = queryMax(LC, l, r);
			}
			if (r > (cur.leftIndex + cur.rightIndex) / 2) {
				ret = Math.max(ret, queryMax(RC, l, r));
			}
		}
		return ret;
	}

	public static int queryMin(SegmentTree cur, int l, int r)
	{
		int ret = 0;
		SegmentTree LC = cur.leftChild, RC = cur.rightChild;
		if ((l <= cur.leftIndex) && (cur.rightIndex <= r)) {
			ret = cur.min;
		}
		else {
			if (l <= (cur.leftIndex + cur.rightIndex) / 2) {
				ret = queryMin(LC, l, r);
			}
			if (r > (cur.leftIndex + cur.rightIndex) / 2) {
				ret = Math.min(ret, queryMin(RC, l, r));
			}
		}
		return ret;
	}

	public static int QueryMax(SegmentTree root, int LL, int RR)
	{
		int ret = 0;
		if (LL == root.leftIndex && RR == root.rightIndex) {
			return root.data;
		}
		else {
			int mid;
			mid = (root.leftIndex + root.rightIndex) / 2;
			if (RR <= mid)
				ret = QueryMax(root.leftChild, LL, RR);
			else if (LL > mid)
				ret = QueryMax(root.rightChild, LL, RR);
			else
				ret = Math.max(QueryMax(root.leftChild, LL, mid), QueryMax(root.rightChild, mid + 1, RR));
			return ret;
		}
	}

	public static void main(String[] args) throws IOException
	{
		// TODO Main Start
		// System.setIn(new BufferedInputStream(new
		// FileInputStream("c://1754.in")));
		int n, m;
		int a, b;
		int i;
		SegmentTree root;
		String cmd;
		root = SegmentTree.Build(0, 9);
		System.out.println(root);
		SegmentTree.updateForDelta(root, 0, 3, 1);
		System.out.println(root);
		SegmentTree.updateForDelta(root, 4, 6, 1);
		SegmentTree.updateForDelta(root, 5, 7, 1);
		SegmentTree.updateForDelta(root, 2, 6, 1);
		System.out.println(SegmentTree.querySumForDelta(root, 1, 1));
		System.out.println(SegmentTree.querySumForDelta(root, 3, 3));
		System.out.println(SegmentTree.querySumForDelta(root, 5, 5));
		// Scanner cin = new Scanner(new BufferedInputStream(System.in));
		// while (cin.hasNext()) {
		// n = cin.nextInt();
		// m = cin.nextInt();
		// for (i = 1; i <= n; i++)
		// score[i] = cin.nextInt();
		// // SegmentTree.pos = 0;
		// rootTree = SegmentTree.Build(1, n);
		// for (i = 0; i < m; i++) {
		// cmd = cin.next();
		// a = cin.nextInt();
		// b = cin.nextInt();
		// if (cmd.equals("Q")) {
		// if (a == b)
		// System.out.println(score[a]);
		// else
		// System.out.println(SegmentTree.Query(rootTree, a, b));
		// }
		// else {
		// score[a] = b;
		// SegmentTree.Update(rootTree, a, a, b);
		// }
		// }
		// }
	}
}
