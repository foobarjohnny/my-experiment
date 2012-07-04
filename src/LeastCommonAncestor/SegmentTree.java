package LeastCommonAncestor;

//LeastCommonAncestor
//Range Minimum/Maximum Query
/**
 * http://dongxicheng.org/structure/segment-tree/
 * 
 * @author dysong
 * 
 */
// 线段树，也叫区间树，是一个完全二叉树，它在各个节点保存一条线段（即“子数组”）
// 因而常用于解决数列维护问题，它基本能保证每个操作的复杂度为O(lgN)。
public class SegmentTree
{
	private static int[][]	min;
	private static int[][]	max;

	// O(N*lgN)
	public static void preProcess(int[] target)
	{
		int length = target.length;
		int count = 1;
		while ((1 << count) <= length) {
			count++;
		}
		min = new int[length][count];
		max = new int[length][count];
		// O(N)
		for (int i = 0; i < length; i++) {
			min[i][0] = i;
			max[i][0] = i;
		}

		// DP
		// O(N*lgN)
		for (int j = 1; (1 << j) <= length; j++) {
			for (int i = 0; i + (1 << j) - 1 < length; i++) {
				min[i][j] = target[min[i][j - 1]] < target[min[i + (1 << j - 1)][j - 1]] ? min[i][j - 1] : min[i + (1 << j - 1)][j - 1];
				max[i][j] = target[max[i][j - 1]] > target[max[i + (1 << j - 1)][j - 1]] ? max[i][j - 1] : max[i + (1 << j - 1)][j - 1];
			}
		}
	}

	// O(N*lgN)
	public static void preProcess(int len)
	{
		int length = len;
		int count = 1;
		while ((1 << count) <= length) {
			count++;
		}
		min = new int[length][count];
		max = new int[length][count];
		// O(N)
		for (int i = 0; i < length; i++) {
			min[i][0] = i;
			max[i][0] = i;
		}

		// DP
		// O(N*lgN)
		for (int j = 1; (1 << j) <= length; j++) {
			for (int i = 0; i + (1 << j) - 1 < length; i++) {
				min[i][j] = Math.min(min[i][j - 1], min[i + (1 << j - 1)][j - 1]);
				max[i][j] = Math.max(max[i][j - 1], max[i + (1 << j - 1)][j - 1]);
			}
		}
	}

	// O(1)
	public static int queryMax(int start, int end) // 查询
	{
		/*
		 * 要使得[a,b]的max = max{ [a, a+2^m]的max, [b-2^m+1, m]的max } 需要 m 大于一定值:
		 * a+2^m >= b-2^m+1 <==> m >= log(b-a+1)/log(2) - 1 <==> m 至少是 floor[
		 * log(b-a+1)/log(2) ]
		 */
		int m = 1;
		while ((1 << m) <= end - start + 1) {
			m++;
		}
		m--;
		return Math.max(max[start][m], max[end - (1 << m) + 1][m]); // 区间[a,b]最大值
	}

	// O(1)
	public static int queryMin(int start, int end) // 查询
	{
		int m = 1;
		while ((1 << m) <= end - start + 1) {
			m++;
		}
		m--;

		return Math.min(min[start][m], min[end - (1 << m) + 1][m]);
	}

	// O(1)
	public static int queryMax(int target[], int start, int end) // 查询
	{
		int m = 1;
		while ((1 << m) <= end - start + 1) {
			m++;
		}
		m--;
		return target[max[start][m]] < target[max[end - (1 << m) + 1][m]] ? max[start][m] : max[end - (1 << m) + 1][m];
	}

	// O(1)
	public static int queryMin(int target[], int start, int end) // 查询
	{
		int m = 1;
		while ((1 << m) <= end - start + 1) {
			m++;
		}
		m--;
		return target[min[start][m]] < target[min[end - (1 << m) + 1][m]] ? min[start][m] : min[end - (1 << m) + 1][m];
	}

}