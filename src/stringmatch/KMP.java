package stringmatch;

import java.util.Arrays;

/**
 * KMP算法的Java实现例子与测试、分析 http://www.isnowfy.com/kmp-and-extend-kmp/
 * http://my.oschina.net/YeanXu/blog/15612s
 * http://3214668848.blog.163.com/blog/static/48764919200910152452182/
 */
// O(m+n)
// KMP算法，预处理复杂度O(n)，匹配复杂度O(m)，总复杂度O(n + m);
public class KMP
{
	static int[]		Next;
	private static int	times	= 10;

	public static void test(String parStr, String subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("父串: " + parStr);
		System.out.println("子串: " + subStr);
		start = System.currentTimeMillis();
		KMP.Next = KMP.preProcess(subStr.toCharArray());
		System.out.println(Arrays.toString(KMP.Next));
		for (int i = 0; i < times; i++) {
			System.out.println(KMP.kmp(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for KMP: " + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(SubStrFind.strFind(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for Enumeration: " + (end - start));
		System.out.println("-------------------------------------");
	}

	/**
	 * 对子串加以预处理，从而找到匹配失败时子串回退的位置
	 * 
	 * @param P
	 *            ，待查找子串的char数组
	 * @return
	 */
	public static int[] preProcess(char[] P)
	{
		int size = P.length;
		int[] Next = new int[size];
		Next[0] = 0;
		int j = 0;
		// j=0 i=1的初始化 保证了 不可能有B[j]==B[i]&& i==j
		// next[j] = max{k | 0 < k < j+1 且 'p[0]...p[k-1]' =
		// 'p[j-k+1]...p[j]'}，应该不包含子串为本身的情况 不为自身的最大首尾重复子串长度
		for (int i = 1; i < size; i++) {
			while (j > 0 && P[j] != P[i]) {
				j = Next[j];
			}
			if (P[j] == P[i]) {
				j++; // 因为是个数 为INDEX+1
			}
			// 如果不等于 则j=0
			Next[i] = j;
		}
		return Next;
	}

	/**
	 * KMP实现
	 * 
	 * @param parStr
	 * @param subStr
	 * @return
	 */
	public static int kmp(String parStr, String subStr)
	{
		int subSize = subStr.length();
		int parSize = parStr.length();
		char[] sub = subStr.toCharArray();
		char[] par = parStr.toCharArray();
		int[] B = new int[parSize];
		int j = 0;
		int count = 0;
		for (int i = 0; i < parSize; i++) {
			while (j > 0 && sub[j] != par[i]) {
				j = Next[j];
			}
			if (sub[j] == par[i]) {
				j++;
			}
			B[i] = j;

			if (j == subSize) {
				// 重新找新串
				// NEXT最大INDEX SUBSIZE-1
				j = Next[j - 1];
				count++;
			}
		}
		return count;
	}

	// A是T对T自己的匹配即NEXT
	// B是T对S匹配得到数组
	public static int extendKmp(String parStr, String subStr)
	{
		int subSize = subStr.length();
		int parSize = parStr.length();
		char[] sub = subStr.toCharArray();
		char[] par = parStr.toCharArray();
		int[] B = new int[parSize];
		int[] A = new int[subSize];
		int j = 0;
		int k = 0;
		j = 0;
		while (1 + j < subSize && sub[0 + j] == sub[1 + j]) {
			j++;
		}
		A[1] = j;
		k = 1;
		for (int i = 2; i < subSize; i++) {
			int Len = k + A[k] - 1, L = A[i - k];
			if (L < Len - i + 1) {
				A[i] = L;
			}
			else {
				j = Math.max(0, Len - i + 1);
				while (i + j < subSize && sub[i + j] == sub[0 + j]) {
					j = j + 1;
				}
				A[i] = j;
				k = i;
			}
		}
		while (j < parSize && j < subSize && sub[0 + j] == par[0 + j]) {
			j = j + 1;
		}
		B[0] = j;
		k = 0;
		for (int i = 1; i < parSize; i++) {
			int Len = k + B[k] - 1, L = A[i - k];
			if (L < Len - i + 1) {
				B[i] = L;
			}
			else {
				j = Math.max(0, Len - i + 1);
				while (i + j < parSize && j < subSize && par[i + j] == sub[0 + j]) {
					j = j + 1;
				}
				B[i] = j;
				k = i;
			}
		}
		return k;
	}

	// 最小表示法又称作最小环排列，是说把一个字符串看成一个环，有n种线性表示（就是说把环切开） ，这里面最小的就是最小环排列，代码
	int MCP(String str)
	{
		// 注意到最小的首字母肯定是最小的，
		char[] s = str.toCharArray();
		int i = 0, j = 1, count = 0, t = 0, len = str.length();
		while (i < len && j < len && count < len) {
			t++;
			int x = i + count;
			int y = j + count;
			if (x >= len) {
				x -= len;
			}
			if (y >= len) {
				y -= len;
			}
			if (s[x] == s[y]) {
				count++;
			}
			else {
				if (s[x] > s[y])
					i = i + count + 1;
				else
					j = j + count + 1;
				count = 0;
			}
			if (j == i) {
				j = i + 1;
			}
		}
		return i;
	}

	public static void main(String[] args)
	{
		// 回退位置数组为P[0, 0, 0, 0, 0, 0]
		test("abcdeg, abcdeh, abcdef!这个会匹配1次", "abcdef");
		// 回退位置数组为P[0, 0, 1, 2, 3, 4]
		test("Test ititi ititit! Test ititit!这个会匹配2次", "ititit");
		
		test("aaaaat!这个会匹配2次", "a");

	}

	// 扩展的KMP问题：
	// 给定母串S，和子串T。
	// 定义n=|S|, m=|T|, extend[i]=S[i..n]与T的最长公共前缀长度。
	// 请在线性的时间复杂度内，求出所有的extend[1..n]。

	// 容易发现，如果有某个位置i满足extend[i]=m，那么T就肯定在S中出现过，并且进一步知道出现首位置是i——而这正是经典的KMP问题。
	// 因此可见“扩展的KMP问题”是对经典KMP问题的一个扩充和加难。

	// KMP得到的是S[i-B[i]+1]..S[i]=T[0]..T[B[i]-1]，而扩展KMP则是说S[i]..S[i+B[i]-1]=T[0]..T[B[i]-1]
}

// SubStrFind.java（枚举算法的实现）

/**
 * 字符串查找（枚举方法查找） 在一个字符串中查找是否包含某一子串
 */
class SubStrFind
{
	/**
	 * 字符串查找（枚举方法）
	 * 
	 * @param parStr
	 * @param subStr
	 * @return
	 */
	public static int strFind(String parStr, String subStr)
	{
		int parSize = parStr.length();
		int subSize = subStr.length();

		char[] B = subStr.toCharArray();
		char[] A = parStr.toCharArray();

		boolean flag = true;
		int times = 0;
		int j = 0;
		int k = 0;// k记录父串匹配正确的位置或者匹配不正确的回退位置
		// i记录父串的当前比较字符的位置
		for (int i = 0; i < parSize; i++) {
			if (B[j] == A[i]) {
				j++;
				// 第一次时记录父串回退位置
				if (flag) {
					k = i;
					flag = false;
				}
			}
			else {
				// 不匹配时回退位置重置，比较继续进行
				i = ++k;
				j = 0;
				flag = true;
			}
			if (j == subSize) {
				j = 0;// 匹配时只需把子串回退位置重置，比较继续进行
				flag = true;
				times++;
			}
		}
		return times;
	}
}
