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
// 树状数组 INDEX从1开始
// 树状数组可以对单个元素进行高效的修改，并且可以高效的求部分和。
// 由于使用了位运算，因此树状数组的效率要优于线段树。
// 树状数组的空间开销也比线段树要小，但是树状数组的应用范围没有线段树广，能够转化使用树状数组的情况下尽量使用树状数组。

public class BinaryIndexedTree
{
	public static int		n;
	public static int[]		in	= new int[n + 1];
	public static int[][]	C	= new int[n + 1][n + 1];

	// 求2^k
	// lowbit(a)为2^(a的二进制表示末尾0的个数)。可以用下面式子求出
	static int lowbit(int t)
	{
		// return t & (t ^ (t - 1));
		return t & (-t);
	}

	// 求前n项和
	static int sum(int end)
	{
		int sum = 0;
		while (end > 0) {
			sum += in[end];
			end -= lowbit(end);
		}
		return sum;
	}

	// 增加某个元素的大小
	static void plus(int pos, int num)
	{
		while (pos < n) {
			in[pos] += num;
			pos += lowbit(pos);
		}
	}

	// 树状数组可以扩充到二维。在二维情况下:
	//
	// C[x][y] = ∑ a[i][j], 其中，
	// x-lowbit(x) + 1 <= i <= x,
	// y-lowbit(y) + 1 <= j <= y.
	//
	// 在二维情况下，对应的更新和查询函数为：
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
