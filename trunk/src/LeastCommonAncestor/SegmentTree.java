package LeastCommonAncestor;

//LeastCommonAncestor
//Range Minimum/Maximum Query
/**
 * http://dongxicheng.org/structure/segment-tree/
 * 
 * @author dysong
 * 
 */
// �߶�����Ҳ������������һ����ȫ�����������ڸ����ڵ㱣��һ���߶Σ����������顱��
// ��������ڽ������ά�����⣬�������ܱ�֤ÿ�������ĸ��Ӷ�ΪO(lgN)��
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
	public static int queryMax(int start, int end) // ��ѯ
	{
		/*
		 * Ҫʹ��[a,b]��max = max{ [a, a+2^m]��max, [b-2^m+1, m]��max } ��Ҫ m ����һ��ֵ:
		 * a+2^m >= b-2^m+1 <==> m >= log(b-a+1)/log(2) - 1 <==> m ������ floor[
		 * log(b-a+1)/log(2) ]
		 */
		int m = 1;
		while ((1 << m) <= end - start + 1) {
			m++;
		}
		m--;
		return Math.max(max[start][m], max[end - (1 << m) + 1][m]); // ����[a,b]���ֵ
	}

	// O(1)
	public static int queryMin(int start, int end) // ��ѯ
	{
		int m = 1;
		while ((1 << m) <= end - start + 1) {
			m++;
		}
		m--;

		return Math.min(min[start][m], min[end - (1 << m) + 1][m]);
	}

	// O(1)
	public static int queryMax(int target[], int start, int end) // ��ѯ
	{
		int m = 1;
		while ((1 << m) <= end - start + 1) {
			m++;
		}
		m--;
		return target[max[start][m]] < target[max[end - (1 << m) + 1][m]] ? max[start][m] : max[end - (1 << m) + 1][m];
	}

	// O(1)
	public static int queryMin(int target[], int start, int end) // ��ѯ
	{
		int m = 1;
		while ((1 << m) <= end - start + 1) {
			m++;
		}
		m--;
		return target[min[start][m]] < target[min[end - (1 << m) + 1][m]] ? min[start][m] : min[end - (1 << m) + 1][m];
	}

}