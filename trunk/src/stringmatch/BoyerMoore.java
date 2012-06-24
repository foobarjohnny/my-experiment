package stringmatch;

import java.util.Arrays;

/**
 * http://blog.csdn.net/sealyao/article/details/4568167
 * http://mindlee.net/2011/11/25/string-matching/
 * http://www.cnblogs.com/a180285/archive/2011/12/15/BM_algorithm.html
 */
/**
 * http://blog.focus-linux.net/?p=162
 * 上面三种策略为BM高效的原因，其中好后缀的策略与KMP类似(其实应该是包括了类似KMP）。那么如果我们将坏字符的策略引入到KMP，是否也可以生效呢？
 * abcdefgbcabc…… abcdefabcabc
 * 如上面的匹配，当第7位d和a不匹配时，g作为坏字符，对于KMP算法来说，也只能移动7位，相对于原来的KMP算法，不过多移动了一位，
 * 效果有限。其原因就在于BM是从右开始匹配，而KMP是从左开始匹配，这一左一右的区别，就使BM可以滑动更多的位数。F
 */
// 在用于查找子字符串的算法当中，BM(Boyer-Moore)算法是目前相当有效又容易理解的一种，一般情况下，比KMP算法快3-5倍。
// BM算法在移动模式串的时候是从左到右，而进行比较的时候是从右到左的。
// 常规的匹配算法移动模式串的时候是从左到右，而进行比较的时候也是是从左到右的，基本框架是： illustration()

// BM算法实际上包含两个并行的算法，坏字符算法和好后缀算法。
// 这两种算法的目的就是让模式串每次向右移动尽可能大的距离(j+=x，x尽可能的大)。

// BoyerMoore BM算法
// parStr TEXT 或者 S 子串 PATTERN
// BM算法预处理时间复杂度为O（m+s），空间复杂度为O(s)，s是与P, T相关的有限字符集长度，搜索阶段时间复杂度为O(m·n)。
// 最好情况下的时间复杂度为O(n/m)，最坏情况下时间复杂度为O(m·n)。
public class BoyerMoore
{
	private static int	times	= 10;

	public static void test(String parStr, String subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("父串: " + parStr);
		System.out.println("子串: " + subStr);
		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(boyerMoore(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for BoyerMoore: " + (end - start));
	}

	private static void illustration()
	{
		int j = 0, i = 0;
		char[] parStr = new char[11];
		char[] subStr = new char[2];
		// 常规的匹配算法移动模式串的时候是从左到右，而进行比较的时候也是是从左到右的
		while (j <= parStr.length - subStr.length) {
			for (i = 0; i < subStr.length && subStr[i] == parStr[i + j]; ++i) {
				;
			}
			if (i == subStr.length) {
				// Match;
			}
			else {
				++j;
			}
		}
		// 而BM算法在移动subStr的时候是从左到右，而进行比较的时候是从右到左的，基本框架是：
		j = 0;
		while (j <= parStr.length - subStr.length) {
			for (i = subStr.length - 1; i >= 0 && subStr[i] == parStr[i + j]; --i) {
				;
			}
			if (i < 0) {
				// match;
			}
			else {
				++j;
			}
		}
	}

	// bad character
	public static void preBmBc(char[] subStr, int m, int[] bmBc)
	{
		int i;
		for (i = 0; i < Character.MAX_VALUE; ++i) {
			bmBc[i] = m;
		}
		//从0－M-1 保证了 为最右
		for (i = 0; i < m - 1; ++i) {
			bmBc[subStr[i]] = m - i - 1;
		}

	}

	public static void suffixes(char[] sub, int m, int[] suff)
	{
		int f, q, i;
		f = m - 1;
		suff[m - 1] = m;
		q = m - 1;
		for (i = m - 2; i >= 0; --i) {
			if (i > q && suff[i + m - 1 - f] < i - q) {
				suff[i] = suff[i + m - 1 - f];
			}
			else {
				if (i < q) {
					q = i;
				}
				f = i;
				while (q >= 0 && sub[q] == sub[q + m - 1 - f]) {
					--q;
				}
				suff[i] = f - q;
			}
		}
	}

	// 暴力法
	public static void suffixes2(char[] sub, int m, int[] suff)
	{
		suff[m - 1] = m;
		int i = 0;
		int q = 0;
		for (i = m - 2; i >= 0; --i) {
			q = i;
			while (q >= 0 && sub[q] == sub[m - 1 - i + q]) {
				--q;
			}
			suff[i] = i - q;
		}
	}

	// GOOD suffix
	public static void preBmGs(char[] subStr, int m, int bmGs[])
	{
		int i, j;
		int[] suff = new int[m];
		suffixes(subStr, m, suff);
		for (i = 0; i < m; ++i) {
			bmGs[i] = m;
		}
		j = 0;
		for (i = m - 1; i >= 0; --i) {
			if (suff[i] == i + 1) {
				for (; j < m - 1 - i; ++j) {
					if (bmGs[j] == m) {
						bmGs[j] = m - 1 - i;
					}
				}
			}
		}
		for (i = 0; i <= m - 2; ++i) {
			// 不能从m-2变一
			// m-1-i；保证了bmGs[i]为右移最小,M-1-i为递减
			bmGs[m - 1 - suff[i]] = m - 1 - i;
		}
	}

	public static int boyerMoore(String parStr, String subStr)
	{
		if (subStr == null || parStr == null) {
			return -1;
		}
		int i, j;
		char[] sub;
		char[] par;
		sub = subStr.toCharArray();
		par = parStr.toCharArray();
		int m = sub.length;
		int n = par.length;
		int[] bmGs = new int[n];

		int[] bmBc = new int[Character.MAX_VALUE];
		//可测汉字
		/* Preprocessing */
		preBmGs(sub, m, bmGs);
		preBmBc(sub, m, bmBc);

		/* Searching */
		int count = 0;
		j = 0;
		while (j <= n - m) {
			for (i = m - 1; i >= 0 && sub[i] == par[i + j]; --i) {
				;
			}
			if (i < 0) {
				//System.out.println(j);
				count++;
				j += bmGs[0];
				// j++;
			}
			else {
				j += Math.max(bmGs[i], bmBc[par[i + j]] - (m - 1 - i));
			}
		}
		return count;
	}

	public static void main(String[] args)
	{
		// 回退位置数组为P[0, 0, 0, 0, 0, 0]
//		System.out.println("abcdeg, abcdeh, abcdef!这个会匹配1次 ,  abcdef");
//		test("abcdegabcdehabcdef", "abcdef");
//		// 回退位置数组为P[0, 0, 1, 2, 3, 4]
//		System.out.println("Test ititi ititit! Test ititit!这个会匹配2次 , ititit");
//		
//		test("testititiititittestititit", "ititit");
//		test("testititiititittestititit", "ititit");
//
//		test("ushers", "her");
		
		
		test("Test ititi ititit! Test ititit!这个会匹配2次","ititit");
	}
}
