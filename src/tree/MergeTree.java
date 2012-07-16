package tree;

import java.util.Arrays;

/**
 * http://blog.csdn.net/zxy_snow/article/details/6681086
 * http://scturtle.is-programmer.com/posts/30022
 * http://blog.csdn.net/zxy_snow/article/details/6678596
 * 
 * @author dysong
 * 
 */
// 和划分树不同,归并树用的是归并排序的思想，每一层都是归并排序的一级，从下到上构建，每一个节点区间内都是排好序的（和划分树不同）。
// 线段树是用来查某数的rank的，比较典型的线段树查询操作，不用附加域，而是用二分的思想，因为节点区间内都是排好序的，
// 注意因为有重复值的可能所以二分求的是区间内小于某数的数的个数。
// 对每个查询的结果是用其在所给小区间rank值二分的，注意二分是求使rank值为k的最大的数，原因不太好想。介绍可看这里，代码可看这里。

// 划分树，时间复杂度O(MlogN)，归并树，时间复杂度O(Mlog^3N)。
// 归并树 先前不用先排序数组
class MerTree
{
	int	l, r, lev;
};

public class MergeTree
{

	public static int		max		= 11;
	public static int[][]	leftsum	= new int[30][max];
	public static int[][]	seg		= new int[30][max];
	public static int[]		arr;
	public static MerTree[]	tree	= new MerTree[max * 3];

	public static void mergeTreeBuild(int root, int lev, int l, int r)
	{
		tree[root] = new MerTree();
		tree[root].l = l;
		tree[root].r = r;
		tree[root].lev = lev;
		if (l == r) {
			seg[lev][l] = arr[l];
			return;
		}

		int mid = (l + r) / 2;
		mergeTreeBuild(root * 2, lev + 1, l, mid);
		mergeTreeBuild(root * 2 + 1, lev + 1, mid + 1, r);

		int p = l, p1 = l, p2 = mid + 1;
		while (p <= r) {
			if (p1 > mid) {
				seg[lev][p++] = seg[lev + 1][p2++];
			}
			else if (p2 > r) {
				seg[lev][p++] = seg[lev + 1][p1++];
			}
			else if (seg[lev + 1][p1] <= seg[lev + 1][p2]) {
				seg[lev][p++] = seg[lev + 1][p1++];
			}
			else {
				seg[lev][p++] = seg[lev + 1][p2++];
			}
		}
	}

	// the count in a that a[i]<x
	public static int count(int[] a, int l, int r, int x)
	{
		int m, orgl = l;
		if (a[l] >= x) {
			return 0;
		}
		else if (a[r] < x) {
			return r - l + 1;
		}
		while (l + 1 < r) // assert: a[l]<x && x<=a[r]
		{
			m = (l + r) >> 1;
			if (a[m] < x) {
				l = m;
			}
			else {
				r = m;
			}
		}
		return l - orgl + 1;
	}

	public static int rank(int root, int l, int r, int x)
	{
		if (tree[root].l == l && tree[root].r == r) {
			return count(seg[tree[root].lev], l, r, x);
		}

		int mid = (tree[root].l + tree[root].r) / 2;

		if (r <= mid) {
			return rank(root * 2, l, r, x);
		}
		else if (l > mid) {
			return rank(root * 2 + 1, l, r, x);
		}
		else {
			return rank(root * 2, l, mid, x) + rank(root * 2 + 1, mid + 1, r, x);
		}

	}

	public static int query(int l, int r, int k)
	{
		int s = seg[1][1], t = seg[1][arr.length - 1] + 1, m;
		while (s + 1 < t) // [s,t) <= ans
		{
			m = (s + t) / 2;
			int rk = rank(1, l, r, m) + 1;
			if (rk > k) {
				t = m;
			}
			else {
				s = m;
			}
		}
		return s;
	}

	// int main()
	// {
	// #ifndef ONLINE_JUDGE
	// freopen("in","r",stdin);
	// freopen("out","w",stdout);
	// #endif
	// scanf("%d%d",&n,&m);
	// for(int i=0;i<n;i++)
	// scanf("%d",arr+i);
	// build(1,1,0,n-1);
	// int l,r,k;
	// while(m--)
	// {
	// scanf("%d%d%d",&l,&r,&k);
	// printf("%d\n",query(l-1,r-1,k));
	// }
	// }

	public static void main(String[] args)
	{
		int n, m;
		int[] arr2 = { 0, 1, 5, 2, 3, 6, 4, 7, 3, 0, 0 };
		arr = arr2;
		// for (int i = 1; i < arr.length; i++) {
		// seg[1][i] = arr[i];
		// }
		// Arrays.sort(arr);
		mergeTreeBuild(1, 1, 1, arr.length - 1);
		System.out.println(seg);
		int l = 1;
		int r = 10;
		int k = 4;
		System.out.println(query(l, r, k));

	}
}
