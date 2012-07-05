package LeastCommonAncestor;

//LeastCommonAncestor
//Range Minimum/Maximum Query
/**
 * http://dongxicheng.org/structure/binary_indexed_tree/
 * http://fqq11679.blog.hexun.com/21722866_d.html
 * http://www.cppblog.com/superKiki/archive/2010/06/02/117048.html
 * 
 * @author dysong
 * 
 */
// ��״���� INDEX��1��ʼ
// ��״������ԶԵ���Ԫ�ؽ��и�Ч���޸ģ����ҿ��Ը�Ч���󲿷ֺ͡�
// ����ʹ����λ���㣬�����״�����Ч��Ҫ�����߶�����
// ��״����Ŀռ俪��Ҳ���߶���ҪС��������״�����Ӧ�÷�Χû���߶����㣬�ܹ�ת��ʹ����״���������¾���ʹ����״���顣

public class BinaryIndexedTree
{
	public static int		n;
	public static int[]		in	= new int[n + 1];
	public static int[][]	C	= new int[n + 1][n + 1];

	// ��2^k
	// lowbit(a)Ϊ2^(a�Ķ����Ʊ�ʾĩβ0�ĸ���)������������ʽ�����
	static int lowbit(int t)
	{
		// return t & (t ^ (t - 1));
		return t & (-t);
	}

	// ��ǰn���
	static int sum(int end)
	{
		int sum = 0;
		while (end > 0) {
			sum += in[end];
			end -= lowbit(end);
		}
		return sum;
	}

	// ����ĳ��Ԫ�صĴ�С
	static void plus(int pos, int num)
	{
		while (pos < n) {
			in[pos] += num;
			pos += lowbit(pos);
		}
	}

	// ��״����������䵽��ά���ڶ�ά�����:
	//
	// C[x][y] = �� a[i][j], ���У�
	// x-lowbit(x) + 1 <= i <= x,
	// y-lowbit(y) + 1 <= j <= y.
	//
	// �ڶ�ά����£���Ӧ�ĸ��ºͲ�ѯ����Ϊ��
	void Modify(int x, int y, int delta)
	{
		for (int i = x; i <= n; i += lowbit(i)) {
			for (int j = y; j <= n; j += lowbit(i)) {
				C[x][y] += delta;
			}
		}
	}

	int Sum(int i, int j)
	{
		int result = 0;
		for (int x = i; x > 0; x -= lowbit(x)) {
			for (int y = j; y > 0; y -= lowbit(y)) {
				result += C[x][y];
			}
		}

		return result;
	}

	public static void main(String[] args)
	{
		lowbit(2);
	}
}
