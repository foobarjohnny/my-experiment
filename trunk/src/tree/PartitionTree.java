package tree;

import java.util.Arrays;

/**
 * http://blog.csdn.net/zxy_snow/article/details/6681086
 * http://scturtle.is-programmer.com/posts/30022
 * 
 * @author dysong
 * 
 */
// �߶���һά��ˢ����ˣ��������K��һֱ���š�
// �������͹鲢���������󣬱Ƚ���һ��ʱ��Ч�ʣ��������ȹ鲢�����˺ܶ࣬����POJ�и��������K�������ù鲢����Ȼ����ȥ��
// ����ʱ��̣��Ҿ����ѻ�������Ū����= =����������СHH��ͼ��
//
// �������͹鲢���������߶�����Ϊ�����ģ�ԭ���ǻ��� ���� �� �鲢���� �ġ�
// �������Ľ������̻�������ģ����Ź��̣�ȡһ���Ѿ��Ź����������ֵ��Ȼ���С����ֵ�ĵ����ߣ����ڵķ��ұߡ����Ҽ�¼d���i����֮ǰ������i��С����ֵ�ķ�����ߵ��������忴�������ע�͡�
// PartitionTree Ҳ��K-th number�� Ϊ��ȡĳĳ���� ��K�������
// �����Ȱ����� ����
// ��������ʱ�临�Ӷ�O(MlogN)���鲢����ʱ�临�Ӷ�O(Mlog^3N)��
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
		int l_left_sum = (l == tl) ? 0 : leftsum[depth][l - 1]; // l_left_sum��ʾ�ӵ�ǰ�����L��l-1�ж��ٸ�С��arr[mid]�������ֵ����
		int l_to_t_sum = leftsum[depth][r] - l_left_sum;// l_left_sum��ʾ����[l,r]�ж��ٸ�С��arr[mid]�������ֵ����
		if (l_to_t_sum >= kTh)
			return find(treeIdx * 2, tl + l_left_sum, tl + leftsum[depth][r] - 1, depth + 1, kTh);
		else {
			int mid = (tl + tr) >> 1;
			int l_right_sum = l - tl - l_left_sum;// ��ʾ�ӵ�ǰ����L��l-1�ж��ٸ��ֵ��ұ�
			int l_to_t_r_sum = r - l + 1 - l_to_t_sum;// ��ʾ[l,r]�ж��ٸ��ֵ��ұ�
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
