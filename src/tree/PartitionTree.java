package tree;

import java.util.Arrays;

/**
 * http://blog.csdn.net/zxy_snow/article/details/6681086
 * http://scturtle.is-programmer.com/posts/30022
 * 
 * @author dysong
 * 
 */
// 线段树一维的刷差不多了，求区间第K数一直卡着。
// 划分树和归并树都可以求，比较了一下时间效率，划分树比归并树快了很多，而且POJ有个求区间第K数的题用归并树居然过不去。
// 鉴于时间短，我决定把划分树给弄明白= =。。借用下小HH的图。
//
// 划分树和归并树都是用线段树作为辅助的，原理是基于 快排 和 归并排序 的。
// 划分树的建树过程基本就是模拟快排过程，取一个已经排过序的区间中值，然后把小于中值的点放左边，大于的放右边。并且记录d层第i个数之前（包括i）小于中值的放在左边的数。具体看下面代码注释。
// PartitionTree 也叫K-th number树 为了取某某区间 第K大的数据
// 必须先把数组 排序
// 划分树，时间复杂度O(MlogN)，归并树，时间复杂度O(Mlog^3N)。
class PartiTree
{
	int	l, r;
};

public class PartitionTree
{

	public static int			max		= 11;
	public static int[][]		leftsum	= new int[30][max];
	public static int[][]		seg		= new int[30][max];
	public static int[]			arr;
	public static PartiTree[]	tree	= new PartiTree[max * 3];

	public static void parti_build(int r, int s, int t, int d)
	{
		tree[r] = new PartiTree();
		tree[r].l = s;
		tree[r].r = t;
		if (t == s)
			return;
		int mid = (s + t) >> 1;
		int lsame = mid - s + 1;
		for (int i = s; i <= t; i++) {
			if (seg[d][i] < arr[mid]) {
				lsame--;
			}
		}
		int lpos = s, rpos = mid + 1;
		for (int i = s; i <= t; i++) {
			leftsum[d][i] = (i == s) ? 0 : leftsum[d][i - 1];
			if (seg[d][i] < arr[mid]) {
				leftsum[d][i]++;
				seg[d + 1][lpos++] = seg[d][i];
			}
			else if (seg[d][i] > arr[mid]) {
				seg[d + 1][rpos++] = seg[d][i];
			}
			else {
				if (lsame > 0) {
					lsame--;
					leftsum[d][i]++;
					seg[d + 1][lpos++] = seg[d][i];
				}
				else {
					seg[d + 1][rpos++] = seg[d][i];
				}
			}
		}
		parti_build(r * 2, s, mid, d + 1);
		parti_build(r * 2 + 1, mid + 1, t, d + 1);
	}

	public static int find(int treeIdx, int l, int r, int depth, int kTh)
	{
		int tl = tree[treeIdx].l, tr = tree[treeIdx].r;
		if (tr == tl) {
			return seg[depth][l];
		}
		int l_left_sum = (l == tl) ? 0 : leftsum[depth][l - 1]; // l_left_sum表示从当前区间的L到l-1有多少个小于arr[mid]的数被分到左边
		int l_to_t_sum = leftsum[depth][r] - l_left_sum;// l_left_sum表示区间[l,r]有多少个小于arr[mid]的数被分到左边
		if (l_to_t_sum >= kTh)
			return find(treeIdx * 2, tl + l_left_sum, tl + leftsum[depth][r] - 1, depth + 1, kTh);
		else {
			int mid = (tl + tr) >> 1;
			int l_right_sum = l - tl - l_left_sum;// 表示从当前区间L到l-1有多少个分到右边
			int l_to_t_r_sum = r - l + 1 - l_to_t_sum;// 表示[l,r]有多少个分到右边
			return find(treeIdx * 2 + 1, mid + l_right_sum + 1, mid + l_right_sum + l_to_t_r_sum, depth + 1, kTh - l_to_t_sum);
		}
	}

	public static void main(String[] args)
	{
		int n, m;
		int[] arr2 = { 0, 1, 5, 2, 3, 6, 4, 7, 3, 0, 0 };
		arr = arr2;
		for (int i = 1; i < arr.length; i++) {
			seg[1][i] = arr[i];
		}
		Arrays.sort(arr);
		parti_build(1, 1, arr.length - 1, 1);
		System.out.println(seg);
		int s = 1;
		int t = 3;
		int k = 2;
		System.out.println(find(1, s, t, 1, k));

	}
}
